package com.prgrms.gccoffee.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

import java.time.Duration;
import java.util.Objects;

public class CustomChunkExecutionListener implements ChunkListener {
    private static final Logger log = LoggerFactory.getLogger(CustomChunkExecutionListener.class);

    @Override
    public void beforeChunk(ChunkContext chunkContext) {
        log.info("Chunk is started");
        log.info("Chunk name: {}", chunkContext.getStepContext().getStepName());
        log.info("Chunk start time: {}", chunkContext.getStepContext().getStepExecution().getStartTime());
    }

    @Override
    public void afterChunk(ChunkContext chunkContext) {
        log.info("Chunk is finished");
        log.info("Chunk end time: {}", chunkContext.getStepContext().getStepExecution().getEndTime());
        log.info("Chunk status: {}", chunkContext.getStepContext().getStepExecution().getStatus());
        log.info("Chunk name: {}", chunkContext.getStepContext().getStepName());

        //log.info("Chunk duration: {}", Duration.between(Objects.requireNonNull(chunkContext.getStepContext().getStepExecution().getEndTime()), chunkContext.getStepContext().getStepExecution().getStartTime()));
    }

    @Override
    public void afterChunkError(ChunkContext chunkContext) {
        log.info("Chunk is failed");
        log.info("Chunk name: {}", chunkContext.getStepContext().getStepName());
        log.info("Chunk status: {}", chunkContext.getStepContext().getStepExecution().getStatus());
        log.info("Chunk end time: {}", chunkContext.getStepContext().getStepExecution().getEndTime());
        //log.info("Chunk duration: {}", Duration.between(Objects.requireNonNull(chunkContext.getStepContext().getStepExecution().getEndTime()), chunkContext.getStepContext().getStepExecution().getStartTime()));
    }
}