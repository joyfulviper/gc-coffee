package com.prgrms.gccoffee.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.time.Duration;
import java.util.Objects;


public class CustomJobExecutionListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(CustomJobExecutionListener.class);
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Job is started");
        log.info("Job name: {}", jobExecution.getJobInstance().getJobName());
        log.info("Job start time: {}", jobExecution.getStartTime());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("Job is finished");
        log.info("Job name: {}", jobExecution.getJobInstance().getJobName());
        log.info("Job status: {}", jobExecution.getStatus());
        log.info("Job end time: {}", jobExecution.getEndTime());

        log.info("Job duration: {}", Duration.between(Objects.requireNonNull(jobExecution.getEndTime()), jobExecution.getStartTime()));
    }
}