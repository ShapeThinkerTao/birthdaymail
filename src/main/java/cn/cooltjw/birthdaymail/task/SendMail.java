package cn.cooltjw.birthdaymail.task;

import cn.hutool.extra.mail.MailUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Auther: ShapeThinker
 * @Date: 2021/6/7 14:48
 * @Description:
 */
@Slf4j
public class SendMail implements Runnable{
    //单发邮箱地址
    private String toAddress;
    //群发邮箱地址
    private List<String> toAddressList;
    //标题
    private String title;
    //内容
    private String content;

    public SendMail(String toAddress, String title, String content) {
        this.toAddress = toAddress;
        this.title = title;
        this.content = content;
    }

    public SendMail(List<String> toAddressList, String title, String content) {
        this.toAddressList = toAddressList;
        this.title = title;
        this.content = content;
    }


    @Override
    public void run() {
        //判断单发还是群发
        if(toAddress!=null){
            log.info("发送的地址：{},标题:{},内容:{}",toAddress,title,content);
            MailUtil.send(toAddress, title, content, false);
        }else {
            log.info("发送的地址：{},标题:{},内容:{}",toAddressList,title,content);
            MailUtil.send(toAddressList, title, content, false);
        }
        log.info("=====发送成功=====");

    }
}
