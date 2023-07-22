package com.prgrms.gccoffee.batch.job;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
@ExtendWith(SpringExtension.class)
class DeliveryStartTest {

    @Autowired private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired private JdbcTemplate jdbcTemplate;

    @Test
    void job_상태를_확인한다() throws Exception {
        //when
        var execution = jobLauncherTestUtils.launchJob();

        //then
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

    @Test
    void step_상태를_확인한다() {
        //when
        var execution = jobLauncherTestUtils.launchStep("deliveryStartStep");

        var stepExecution = execution.getStepExecutions().stream()
                .filter(x -> x.getStepName().equals("deliveryStartStep"))
                .findFirst()
                .orElseThrow();

        //then
        assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(stepExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

    }


}