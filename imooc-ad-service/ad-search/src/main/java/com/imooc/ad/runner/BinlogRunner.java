package com.imooc.ad.runner;

import com.imooc.ad.mysql.BinlogClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Author: hekai
 * @Date: 2019-08-12 16:17
 */
@Slf4j
@Component
public class BinlogRunner implements CommandLineRunner {
    private final BinlogClient client;

    public BinlogRunner(BinlogClient client) {
        this.client = client;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("coming in binlogRunner...");
        client.connect();
    }
}
