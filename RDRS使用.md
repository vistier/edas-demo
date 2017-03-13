# RDRS使用

**注意**:
    * 建议通过mysql命令行进行连接，```mysql -hip -Pport -uuser -ppassword -c```,最后-c请务必带上，让mysql客户端将注释下发

## 分表分库的使用注意事项
 * 拆分键暂时只能是单个字段
 * 在 INSERT/REPLACE 语句中必须包含分库分表的字段(拆分键)
 * SELECT/UPDATE/DELETE 语句如果 WHERE 条件中没有包含拆分字段，则会进行全表扫描
 * 同一个拆分字段，AND 连接的条件个数只能为2，OR 连接的条件个数不限
 

## 常用指令
 * show slow;            -- TOP 10慢SQL查询
 * explain detail {SQL}; -- 查看DRDS执行计划
 * show node;            -- 分库情况
 * show datasourcse;     -- 分表情况
 *  CHECK TABLE [name]   -- 对数据表进行检查。
 * SHOW TABLE STATUS (LIKE ‘pattern’ | WHERE expr) -- 对数据表进行检查。
 * SHOW STATS 查看整体的统计信息
 ```
 主要包含QPS 、RT、慢SQL统计 等信息，具体说明如下：
     QPS: 0.00                             逻辑QPS
     RDS_QPS: 0.00                         物理QPS
     ERROR_PER_SECOND: 0.00                每秒的错误数，包含语法错误，主键冲突等等所有异常
     VIOLATION_PER_SECOND: 0.00            每秒的主键或者唯一键冲突
     MERGE_QUERY_PER_SECCOND: 0.00         通过分库分表，从多表中进行的查询
     ACTIVE_CONNECTIONS: 1                 当前连接上的连接
     CONNECTION_CREATE_PER_SECCOND: 0.00   每秒创建的连接数
     RT(MS): 0.00                          逻辑RT（响应时间）
     RDS_RT(MS): 0.00                      物理RT
     NET_IN(KB/S): 0.00                    DRDS收到的网络流量
     NET_OUT(KB/S): 0.00                   DRDS发送的网络流量
     THREAD_RUNNING: 1                     正在运行的线程数
     HINT_USED_PER_SECOND: 0.00            每秒带HINT的查询的数量
     HINT_USED_COUNT: 302                  启动到现在带HINT的查询 总量
     AGGREGATE_QUERY_PER_SECCOND: 0.00     每秒走AGGREGATECURSOR的次数      
     AGGREGATE_QUERY_COUNT: 17             启动到现在走AGGREGATECURSOR的次数
     TEMP_TABLE_CREATE_PER_SECCOND: 0.00   每秒创建的临时表的数量
     TEMP_TABLE_CREATE_COUNT: 6            启动到现在创建的临时表总数量
     MULTI_DB_JOIN_PER_SECCOND: 0.00       每秒跨库JOIN的数量
      MULTI_DB_JOIN_COUNT: 3               启动到现在跨库JOIN的总量
 ```
 * 
 
 ```
+-----------------------------------------+---------------------------------------------------------+---------------------------------------------+
| STATEMENT                               | DESCRIPTION                                             | EXAMPLE                                     |
+-----------------------------------------+---------------------------------------------------------+---------------------------------------------+
| show rule                               | Report all table rule                                   |                                             | 查看对应逻辑库下，使用分库分表的表所采用的路由规则。 所有采用分表的逻辑表路由规则查看，包含是否为广播表，分库分表方案，分库分表数等信息。
| show rule from TABLE                    | Report table rule                                       | show rule from user                         | 查看对应逻辑库下，表的分库分表规则。
| show full rule from TABLE               | Report table full rule                                  | show full rule from user                    | 需要获取更多规则信息，如可否全分表扫描，分库、分表具体规则等。
| show topology from TABLE                | Report table physical topology                          | show topology from user                     | 查看分库与分表对应关系。
| show partitions from TABLE              | Report table dbPartition or tbPartition columns         | show partitions from user                   | 查看分库分表键 
| show broadcasts                         | Report all broadcast tables                             |                                             | 查看广播表信息。
| show datasources                        | Report all partition db threadPool info                 |                                             | 查看底层存储信息。包含schema，数据库分组名，JDBC 信息，用户名，底层存储类型，读写权重等。
| show node                               | Report master/slave read status                         |                                             | 查看物理库的读写信息。
| show slow                               | Report top 100 slow sql                                 |                                             | 查看最近的100条DRDS 慢SQL。
| show physical_slow                      | Report top 100 physical slow sql                        |                                             | 查看对应底层存储的最近100条慢SQL。
| clear slow                              | Clear slow data                                         |                                             | 清理慢SQL 信息。
| trace SQL                               | Start trace sql, use show trace to print profiling data | trace select count(*) from user; show trace | 查看具体SQL 的执行情况。
| show trace                              | Report sql execute profiling info                       |                                             | 查看具体SQL 的执行情况。
| explain SQL                             | Report sql plan info                                    | explain select count(*) from user           | 查看语句对应的分库，物理语句，和整体参数。
| explain detail SQL                      | Report sql detail plan info                             | explain detail select count(*) from user    | 查询语句的DRDS 执行信息。
| explain execute SQL                     | Report sql on physical db plan info                     | explain execute select count(*) from user   | 查看底层存储的执行计划。
| show sequences                          | Report all sequences status                             |                                             | 查看所有的DRDS SEQUENCE信息  
| create sequence NAME [start with COUNT] | Create sequence                                         | create sequence test start with 0           | 建立一个SEQUENCE
| alter sequence NAME [start with COUNT]  | Alter sequence                                          | alter sequence test start with 100000       | 修改对应名称的SEQUENCE。
| drop sequence NAME                      | Drop sequence                                           | drop sequence test                          | 删除对应名称的SEQUENCE。
+-----------------------------------------+---------------------------------------------------------+---------------------------------------------+

```




## HINT语法
 * 语法 ``` /*TDDL:hint command*/```. DRDS 自定义 HINT 是借助于 MySQL 注释实现的，也就是 DRDS 的自定义 HINT 语句位于/*与*/之间，并且必须以TDDL:开头
 * 读写分离 ```/*TDDL:MASTER|SLAVE*/```
 * 备库延迟切断 ```/*TDDL:SQL_DELAY_CUTOFF=time*/``` 单位秒
    > 在自定义 HINT 中指定 SQL_DELAY_CUTOFF 的值，当备库的 SQL_DELAY 值（MySQL主备复制延迟）达到或超过 time 的值（单位秒）时，查询语句会被下发到主实例。
 * 自定义 SQL 超时时间 ```/*TDDL:SOCKET_TIMEOUT=time*/``` 单位毫秒
 * 指定分库执行 SQL
    - ```/*TDDL:NODE='node_name'*/```
    - ```/*TDDL:NODE IN ('node_name',...)*/```
     > 使用该自定义 HINT 时，DRDS 会将 SQL 直接下发到分库上执行，所以在 SQL 语句中，表名必须是该分库中已经存在的表名。
    - ```/*TDDL:table_name.partition_key=value*/```   
 * 扫描全部分库分表
    - ```/*TDDL:SCAN*/```
    - ```/*TDDL:SCAN='table_name'*/```  table_name是 DRDS 数据库的某个逻辑表名


## 关于全表操作
 * 如果目标表没有分库分表，那么 DRDS 可以支持任何聚合函数，因为实际上 DRDS 是直接把原 SQL传递到后端 MySQL 执行
 * 非全表扫描：SQL语句在经过 DRDS 路由后，直接发送到后端单个 MySQL 库上执行。如果拆分键在 WHERE 条件中都是 = 关系，常会出现这种情况。在非全表扫描的情形下，同样可以支持任何聚合函数。
 * 全表扫描：目前支持的聚合函数有 **COUNT**, **MAX**, **MIN**, **SUM**, **LIKE**, **ORDER BY** 与 **LIMIT** 语法, 但是```不支持 GROUP BY 语法```。
 * ```show topology from {表名}；``` 获取表所在的库节点
 * ```/*TDDL:node='{库节点}'*/ select * from {表名};``` 只操作某个库上的表   
 * 项目中直接使用库节点进行操作要注意了, 因为不保证节点名是永远不变的.使用时建议从show topology from table中获取   
    

## 跟踪sql执行过程 
> trace基本上能够找到慢的或者相对慢的部分在什么地方, 定位到问题后可以咨询阿里来解决怎么修改sql进行性能调优

```
mysql> trace /!TDDL:SOCKET_TIMEOUT=0*/select * from App_IdentityCard limit 1;
+--------------------------------+---------------------+---------------------+--------------------+
| DomainName                     | ApplicationId       | UpdateTime          | IdentityCard       |
+--------------------------------+---------------------+---------------------+--------------------+
| sdfsdfdsfsdfsdfsdfsdf          | 6205076500132922050 | 2015-10-13 18:25:57 | 000000263180292197 |
+--------------------------------+---------------------+---------------------+--------------------+
1 row in set (0.35 sec)
```
 
 * 因为可能存在SQL超时(DRDS层面应对服务不被hang住所做的一个保护)，所以建议SQL前加一个防止超时的注释，如果这个注释没生效，建议检查下连接串是否带了-c选项。
 * 执行完上面语句后进行下面的排查
 * 如果数据分片执行时间相对过长，需要注意排查3个事情
    - 1.数据库上的执行计划是否对。
    - 2.到数据库上执行的SQL是否返回大量数据。
    - 3.DRDS是否存在类似merge sort ,temp table merge等耗时操作。 
 * 查询trace情况
```
mysql> show trace;
+------+----------+---------------+---------------+--------------------------+------+--------------------------------------------------+--------+
| ID   | TYPE     | DATA_NODE     | TIME_COST(MS) | CONNECTION_TIME_COST(MS) | ROWS | STATEMENT                                        | PARAMS |
+------+----------+---------------+---------------+--------------------------+------+--------------------------------------------------+--------+
|    0 | Optimize | DRDS          | 175           | 0.00                     |    0 | /*+TDDL({"extra":{"SOCKET_TIMEOUT":0}})*/selec...| NULL   |
|    1 | Query    | TEST_0000_RDS | 10            | 6.00                     |    0 | select `App_IdentityCard`.`DomainName`,`App_Id...| NULL   |
|    2 | Query    | TEST_0000_RDS | 1             | 0.02                     |    0 | select `App_IdentityCard`.`DomainName`,`App_Id...| NULL   |
|  ... | ...      | ...           | 2             | 0.01                     |    0 | select `App_IdentityCard`.`DomainName`,`App_Id...| NULL   |
+------+----------+---------------+---------------+--------------------------+------+--------------------------------------------------+--------+
1 row in set (0.35 sec)
```
 * 查看底下数据库中正在执行的SQL详情
 ```
 mysql> show processlist;
 +------------+-----------+------------------------+---------+------+-----------+-----------------------------------------+-----------+---------------+-----------+
 | ID         | USER      | DB                     | COMMAND | TIME | STATE     | INFO                                    | ROWS_SENT | ROWS_EXAMINED | ROWS_READ |
 +------------+-----------+------------------------+---------+------+-----------+-----------------------------------------+-----------+---------------+-----------+
 | 0-0-73777  | ftwobwdf0 | yuanwang_cas_zjrx_0038 | Sleep   |   58 |           | NULL                                    |      NULL |          NULL |      NULL |
 | 0-0-78566  | ftwobwdf0 | yuanwang_cas_zjrx_0038 | Query   |    0 | query end | insert into `App_IdentityCard_286` ( `D |      NULL |          NULL |      NULL |
 | 0-0-78834  | ftwobwdf0 | yuanwang_cas_zjrx_0032 | Sleep   |    1 |           | NULL                                    |      NULL |          NULL |      NULL |
 | 0-0-78843  | ftwobwdf0 | yuanwang_cas_zjrx_0036 | Sleep   |    0 |           | NULL                                    |      NULL |          NULL |      NULL |
 | 0-0-79031  | ftwobwdf0 | yuanwang_cas_zjrx_0035 | Sleep   |    0 |           | NULL                                    |      NULL |          NULL |      NULL |
 | 0-0-79166  | ftwobwdf0 | yuanwang_cas_zjrx_0035 | Sleep   |   17 |           | NULL                                    |      NULL |          NULL |      NULL |
 | 0-0-79477  | ftwobwdf0 | yuanwang_cas_zjrx_0039 | Query   |    0 | query end | insert into `App_IdentityCard_286` ( `D |      NULL |          NULL |      NULL |
 | 0-0-79636  | ftwobwdf0 | yuanwang_cas_zjrx_0037 | Sleep   |    0 |           | NULL                                    |      NULL |          NULL |      NULL |
 | 0-0-79639  | ftwobwdf0 | yuanwang_cas_zjrx_0032 | Sleep   |    0 |           | NULL                                    |      NULL |          NULL |      NULL |
 | 0-0-79889  | ftwobwdf0 | yuanwang_cas_zjrx_0034 | Sleep   |    0 |           | NULL                                    |      NULL |          NULL |      NULL |
 +------------+-----------+------------------------+---------+------+-----------+-----------------------------------------+-----------+---------------+-----------+
 XXX rows in set (0.35 sec)
 ```
 * kill某个进程 ```mysql> kill '0-0-79477'；```
 
 -----------------------
 

 
 
 
 
 