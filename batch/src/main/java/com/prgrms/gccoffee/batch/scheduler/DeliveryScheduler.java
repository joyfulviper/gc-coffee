package com.prgrms.gccoffee.batch.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DeliveryScheduler {

    private final Job deliveryStartJob;
    private final JobLauncher jobLauncher;

    public DeliveryScheduler(Job deliveryStartJob, JobLauncher jobLauncher) {
        this.deliveryStartJob = deliveryStartJob;
        this.jobLauncher = jobLauncher;
    }

    @Scheduled(fixedRate = 4000)// (cron = 0 0 14 * * ?)
    public void runDeliveryJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(deliveryStartJob, jobParameters);
    }
}