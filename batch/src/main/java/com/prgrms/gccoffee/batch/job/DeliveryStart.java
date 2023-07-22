package com.prgrms.gccoffee.batch.job;

import com.prgrms.gccoffee.batch.listener.*;
import com.prgrms.gccoffee.common.model.Order;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DeliveryStart {

    public static final Integer CHUNK_SIZE = 10;


    private static final String JOB_NAME = "deliveryStartJob";

    @Bean(name = JOB_NAME)
    public Job deliveryStartJob(JobRepository jobRepository, Step deliveryStartStep) {
        return new JobBuilder("deliveryStartJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(deliveryStartStep)
                .listener(new CustomJobExecutionListener())
                .build();
    }

    @JobScope
    @Bean
    public Step deliveryStartStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                  SynchronizedItemStreamReader<Order> orderItemReader, JdbcBatchItemWriter<Order> orderItemWriter) {
        return new StepBuilder("deliveryStartStep", jobRepository)
                .listener(new CustomStepExecutionListener())
                .<Order, Order>chunk(CHUNK_SIZE, transactionManager)
                .listener(new CustomChunkExecutionListener())
                .reader(orderItemReader)
                .listener(new CustomReaderExecutionListener())
                .writer(orderItemWriter)
                .listener(new CustomWriterExecutionListener())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean(name = JOB_NAME + "taskPool")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(CHUNK_SIZE);
        taskExecutor.setMaxPoolSize(CHUNK_SIZE * 2);
        taskExecutor.setThreadNamePrefix("async-thread");
        return taskExecutor;
    }
}