package cn.cooltjw.birthdaymail.conf;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;
import java.util.Set;

/**
 * @Auther: ShapeThinker
 * @Date: 2021/6/7 14:10
 * @Description:
 */
@Configuration
public class QuartzConfigurationManger implements ApplicationRunner {

	@Autowired
	@Qualifier("scheduler")
    private Scheduler scheduler;


	@Value("${spring.profiles.active}")
	private String profile;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if(profile != null) {
			Properties properties =  PropertiesLoaderUtils.loadAllProperties("quartz/quartz-"+profile+".properties");
			Set<Object> set = properties.keySet();
			for (Object object : set) {
				configJobs(scheduler, Class.forName(object.toString()),properties.get(object).toString());
			}
		}
	}

	private void configJobs(Scheduler scheduler, Class clazz, String cronExpression) throws SchedulerException {
		JobDetail jobDetail = JobBuilder.newJob(clazz)
                .withIdentity(clazz.getName())//任务名称采用 class的全路径名
                .build();
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);   //trigger名称采用 class的全路径名
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(clazz.getName()) .withSchedule(scheduleBuilder).build();
        scheduler.scheduleJob(jobDetail,cronTrigger);
	}
}
