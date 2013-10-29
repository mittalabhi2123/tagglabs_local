
package com.tagglabs.social.jobs;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class CronScheduler {
    public static void main( String[] args ) throws Exception
    {
       	JobDetail job = JobBuilder.newJob(CronJobPosts.class)
			.withIdentity("cronJob1", "group1").build();
 
        Trigger trigger = TriggerBuilder
			.newTrigger()
			.withIdentity("dummyTriggerName", "group1")
			.withSchedule(SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInMinutes(5).repeatForever())
			.build();
    	
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
        
        JobDetail job1 = JobBuilder.newJob(CronJobPostsLikes.class)
			.withIdentity("cronJob2", "group2").build();
 
        Trigger trigger1 = TriggerBuilder
			.newTrigger()
			.withIdentity("dummyTriggerName1", "group2")
			.withSchedule(SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInMinutes(18).repeatForever())
			.build();
    	
        Scheduler scheduler1 = new StdSchedulerFactory().getScheduler();
        scheduler1.start();
        scheduler1.scheduleJob(job1, trigger1);
 
    }
}
