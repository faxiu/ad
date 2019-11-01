# ad
Just for learn spring cloud


编译打包命令: mvn clean package -Dmaven.test.skip=true -U
-U：强制打包
根据指定的配置文件启动jar包 java -jar xxx.jar --spring.profiles.active=xxx

开启binlog后增、改、删的日志监听结果
create----------
WriteRowsEventData{tableId=112, includedColumns={0, 1, 2}, rows=[
    [2, 1, 哈哈]
]}
update----------
UpdateRowsEventData{tableId=112, includedColumnsBeforeUpdate={0, 1, 2}, includedColumns={0, 1, 2}, rows=[
    {before=[2, 1, 哈哈], after=[2, 1, 你们]}
]}
delete----------
DeleteRowsEventData{tableId=112, includedColumns={0, 1, 2}, rows=[
    [2, 1, 你们]
]}



1.创建数据库表对应的索引服务:index
    涉及的包:ad-search: com.imooc.ad.index
2.从数据库中导出数据构建全量索引:
    涉及的包:ad-common: com.imooc.ad.dump; 
            ad-sponsor:test:com.imoox.ad.service;
            ad-search: com.imooc.ad.handler;  com.imooc.ad.mysql