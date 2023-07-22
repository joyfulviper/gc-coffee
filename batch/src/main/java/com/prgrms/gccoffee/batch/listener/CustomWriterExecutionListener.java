package com.prgrms.gccoffee.batch.listener;

import com.prgrms.gccoffee.common.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

public class CustomWriterExecutionListener implements ItemWriteListener<Order> {
    private static final Logger log = LoggerFactory.getLogger(CustomWriterExecutionListener.class);

    @Override
    public void beforeWrite(Chunk chunk) {
        log.info(">> before write: {}", chunk.size());
    }

    @Override
    public void afterWrite(Chunk chunk) {
        log.info(">> after write: {}", chunk.size());
    }
}