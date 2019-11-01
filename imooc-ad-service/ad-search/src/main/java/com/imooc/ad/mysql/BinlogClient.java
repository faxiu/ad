package com.imooc.ad.mysql;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.imooc.ad.mysql.listener.AggregationListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: hekai
 * @Date: 2019-08-12 15:54
 */
@Slf4j
@Component
public class BinlogClient {

    private BinaryLogClient client;

    @Autowired
    private BinlogConfig config;

    @Autowired
    private AggregationListener listener;

    public void connect() {

        ThreadFactory listenFactory = new ThreadFactoryBuilder().setNameFormat("listen-poll-1").build();

        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(1024), listenFactory, new ThreadPoolExecutor.AbortPolicy());

        singleThreadPool.execute(() -> {
            client = new BinaryLogClient(config.getHost(), config.getPort(), config.getUsername(), config.getPassword());

            if (!StringUtils.isEmpty(config.getBinlogName()) && !config.getPosition().equals(-1L)) {
                client.setBinlogFilename(config.getBinlogName());
                client.setBinlogPosition(config.getPosition());
            }

            client.registerEventListener(listener);

            try {
                log.info("connecting to mysql start");
                client.connect();
                log.info("connecting to mysql done");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
//        singleThreadPool.shutdown();


//        new Thread(() ->
//        {
//            client = new BinaryLogClient(config.getHost(), config.getPort(), config.getUsername(), config.getPassword());
//
//            if (!StringUtils.isEmpty(config.getBinlogName()) && !config.getPosition().equals(-1L)) {
//                client.setBinlogFilename(config.getBinlogName());
//                client.setBinlogPosition(config.getPosition());
//            }
//
//            client.registerEventListener(listener);
//
//            try {
//                log.info("connecting to mysql start");
//                client.connect();
//                log.info("connecting to mysql done");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        ).start();
    }

    public void close(){
        try {
            client.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
