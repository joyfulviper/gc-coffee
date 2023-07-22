package com.prgrms.gccoffee.batch.listener;

import org.slf4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

public class CustomStepExecutionListener implements StepExecutionListener {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CustomStepExecutionListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Step is started");
        log.info("Step name {}", stepExecution.getStepName());
        log.info("Step start time {}", stepExecution.getStartTime());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Step is finished");
        log.info("Step name: {}", stepExecution.getStepName());
        log.info("Step status {}", stepExecution.getStatus());
        log.info("Step end time: {}", stepExecution.getEndTime());
        //log.info("Step duration: {}", Duration.between(Objects.requireNonNull(stepExecution.getEndTime()), stepExecution.getStartTime()));
        return stepExecution.getExitStatus();
    }
}