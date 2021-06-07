package cn.cooltjw.birthdaymail.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @Auther: ShapeThinker
 * @Date: 2021/6/7 14:32
 * @Description: 发送邮件的定时任务
 */
@Slf4j
public class MailTask extends QuartzJobBean {
    @Autowired
    @Qualifier("mailTaskExecutor")
    private ThreadPoolTaskExecutor tpt;



    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("======生日邮件定时任务=====");
        ClassPathResource classPathResource = new ClassPathResource("data/staffinfo.txt");
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd"));
        BufferedReader bf = null;
        InputStreamReader inputReader = null;
        String title = "生日祝福";
        StringBuffer content;
        try {
            inputReader = new InputStreamReader(new FileInputStream(classPathResource.getFile()), StandardCharsets.UTF_8);
            bf = new BufferedReader(inputReader);
            String str;
            while ((str = bf.readLine()) != null) {
                String staffinfo = StringUtils.trimAllWhitespace(str);
                String[] info = staffinfo.split(",");
                //今天是员工的生日，执行发送信息任务
                if (today.equals(info[2])) {
                    content=new StringBuffer();
                    content.append("亲爱的员工 ").append(info[1]).append("你好").append("祝你生日快乐~(给你加薪3000元)");
                    SendMail sm = new SendMail(info[3], title, content.toString());
                    tpt.execute(sm);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputReader != null) {
                    inputReader.close();
                }
                if (bf != null) {
                    bf.close();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
