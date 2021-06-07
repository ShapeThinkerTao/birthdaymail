package cn.cooltjw.birthdaymail.conf;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @Auther: ShapeThinker
 * @Date: 2021/6/7 14:10
 * @Description: 配置Scheduler
 */
@Configuration
public class QuartzConfiguration {

	@Autowired
	QuartzJobFactory quartzJobFactory;

	// 配置Scheduler
	@Bean(name = "scheduler")
	public SchedulerFactoryBean schedulerFactory() throws SchedulerException {
		SchedulerFactoryBean bean = new SchedulerFactoryBean();
		// 延时启动，应用启动1秒后
		bean.setStartupDelay(1);
		bean.setJobFactory(quartzJobFactory);
		return bean;
	}
}
