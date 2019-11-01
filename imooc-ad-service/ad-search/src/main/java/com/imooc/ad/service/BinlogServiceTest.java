package com.imooc.ad.service;

import com.alibaba.fastjson.JSON;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;

/**
 * @Author: hekai
 * @Date: 2019-08-07 16:49
 */
public class BinlogServiceTest {

    //监听时的日期格式：Tue Jan 01 08:00:00 CST 2019

    public static void main(String[] args) throws Exception {
        BinaryLogClient client = new BinaryLogClient(
                "127.0.0.1",
                3306,
                "root",
                "123456"
        );
//        //可以设定读取 Binlog 的文件和位置，那么，client 将从这个位置之后开始监听,否则, client 会从 “头” 开始读取 Binlog 文件，并监听
//        client.setBinlogFilename();
//        client.setBinlogPosition();

//        //给 BinaryLogClient 注册监听器，实现对 Binlog 的监听和解析 , event 就是监听到的 Binlog 变化信息，还记得 event 里面包含哪两个东西吗 ？
        client.registerEventListener(event -> {
            EventData data = event.getData();
            if (data instanceof UpdateRowsEventData) {
                System.out.println("update----------");
                System.out.println(data.toString());
            } else if (data instanceof WriteRowsEventData) {
                System.out.println("create----------");
                System.out.println(data.toString());
            } else if (data instanceof DeleteRowsEventData) {
                System.out.println("delete----------");
                System.out.println(data.toString());
            }
        });

        client.connect();
    }
}
