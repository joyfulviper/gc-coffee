package com.prgrms.gccoffee.batch.listener;

import com.prgrms.gccoffee.common.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.lang.NonNull;

public class CustomReaderExecutionListener implements ItemReadListener<Order> {
    private static final Logger log = LoggerFactory.getLogger(CustomReaderExecutionListener.class);

    @Override
    public void beforeRead() {
        log.info(">> before read");
    }

    @Override
    public void afterRead(@NonNull Order item) {
        log.info(">> after read {}", item);
    }
}