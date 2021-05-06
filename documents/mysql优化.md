##### 慢查日志

配置mysql变量

```
slow_query_log = ON
log_queries_not_using_indexes = ON
long_query_time = 1 # 大于1秒的数据记录到慢查日志中
```

windows慢查日志存放路径：C:\ProgramData\MySQL\MySQL Server 8.0\Data\\[主机名]-slow.log

linux慢查日志存放路径：/var/lib/mysql/[主机名]-slow.log

##### 慢查日志分析工具 - mysqldumpslow

```bash
mysqldumpslow -t 10 [慢查日志存放路径]
```

windows下分析工具存放路径：C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqldumpslow.pl

运行分析工具需要安装ActivePerl：https://www.activestate.com/products/perl/downloads/

##### 慢查日志分析工具 - pt-query-digest

下载安装

```bash
wget http://www.percona.com/downloads/percona-toolkit/2.2.4/RPM/percona-toolkit-2.2.4-1.noarch.rpm && yum localinstall percona-toolkit-2.2.4-1.noarch.rpm -y
```

检查是否安装完成

```bash
pt-summary
```

安装pt-summary中所有的工具

```bash
wget http://percona.com/get/pt-summary
```

查看mysql数据库信息

```bash
pt-mysql-summary --user=root --password=123456
```

提示：Can't locate Digest/MD5.pm

```bash
yum install perl-Digest-MD5 -y
```

分析慢查日志

```bash
pt-query-digest [慢查日志存放路径]
```

查找mysql的从库和同步状态

```bash
pt-slave-find --host=localhost --user=root --password=123456
```

查看mysql的死锁信息

```bash
pt-deadlock-logger --user=root --password=123456 localhost
```

从慢查日志中分析索引使用情况

```bash
pt-index-usage --user=root --password=123456 [慢查日志存放路径]
```

查找数据库表中重复的索引

```shell
pt-duplicate-key-checker --host=localhost --user=root --password=123456
```

查看mysql表和文件的当前活动IO开销

```shell
pt-ioprofile
```

查看不同mysql配置文件的差异

```shell
pt-config-diff [mysql配置文件位置1] [mysql配置文件位置2]
```

查找数据库里大于2G的表

```shell
pt-find --user=root --password=123456 --tablesize +2G
```

查找10天前创建，MyISAM引擎的表

```shell
pt-find --user=root --password=123456 --ctime +10 --engine MyISAM
```

查看表和索引大小并排序

```shell
pt-find --user=root --password=123456 --printf "%T\t%D.%N\n" | sort -rn
```

显示查询时间大于60秒的查询

```shell
pt-kill --user=root --password=123456 --busy-time 60 --print
```

kill掉大于60秒的查询

```shell
pt-kill --user=root --password=123456 --busy-time 60 --kill
```

查看mysql授权

```shell
pt-show-grants --user=root --password=123456
```

验证数据库复制的完整性

```shell
pt-table-checksum --user=root --password=123456
```

输出到文件

```shell
pt-query-digest slow-log > slow-log.report
```

##### 使用pt-query-digest分析慢查日志发现有问题的sql

查询次数多且查询时间长的sql：通常为分析结果的前几个查询

IO大的sql：Rows examine越大说明扫描行数越多，IO越大

未命中索引的sql：比较Rows sent和Rows examine之间的差值，差值越大表示命中索引越低

##### 通过explain分析sql的执行计划

sql语句前加上EXPLAIN关键字

执行结果说明：

- id - 数字越大越先执行，为null就表示这是一个结果集，不需要使用它来进行查询
- table - 显示这一行的数据是关于哪张表的
- type - 显示连接使用了何种类型，依次从好到差的连接类型为：const -> eq_reg -> ref -> range -> index -> ALL
- possible_keys - 显示可能应用在这张表中的索引
- key - 实际使用的索引
- key_len - 使用索引的长度，长度越短越好
- ref - 显示索引的哪一列被使用了
- rows - 显示扫描的行数
- extra - 需要优化的情况：Using filesort表示mysql需要进行额外的步骤来发现如何对返回的行进行排序；Using temporary表示mysql需要创建一个临时表来存储结果

##### MAX函数优化

为使用MAX函数的字段创建索引

##### 子查询优化

优化为join操作，如果是一对多关系，会出现数据重复，需要使用distinct关键词进行去重

```mysql
SELECT * FROM t WHERE t.id IN (SELECT t1.tid FROM t1);
-- 优化后
SELECT DISTINCT id FROM t JOIN t1 ON t.id=t1.tid;
```

##### group by优化

使用主键索引（actor_id）、中间表（c），并使用USING关键词对两张表进行关联（需要两张表有相同的列，如actor_id）

```mysql
SELECT actor.first_name, actor.last_name, COUNT(*) FROM film_actor INNER JOIN actor USING(actor_id) GROUP BY film_actor.actor_id;
-- 优化后
SELECT actor.first_name, actor.last_name, c.cnt FROM actor INNER JOIN (SELECT actor_id, COUNT(*) AS cnt FROM film_actor GROUP BY actor_id) AS c USING(actor_id);
```

##### limit优化

使用主键索引

```mysql
SELECT film_id, description FROM film ORDER BY title LIMIT 50, 5;
-- 优化后
SELECT film_id, description FROM film ORDER BY film_id LIMIT 50, 5;
```

记录上次返回的主键，在下次查询时使用主键过滤

```mysql
SELECT film_id, description FROM film WHERE film_id>55 AND film_id<=60 ORDER BY film_id LIMIT 1, 5;
```

##### 索引优化

什么情况下需要使用索引：

1. 表的主关键字
2. 自动建立唯一索引
3. 表的字段唯一约束
4. 直接条件查询的字段
5. 查询中与其它表关联的字段
6. 查询中排序的字段
7. 查询中统计或分组统计的字段

什么情况下不建议使用索引：

1. 表记录太少
2. 经常插入、删除、修改的表
3. 数据重复且分布平均的表字段
4. 经常和主字段一块查询但主字段索引值比较多的表字段

如何选择合适的列建立索引：

1. where、group by、order by、on等从句中的列
2. 索引字段越小越好
3. 使用联合索引时，离散度（唯一的id的数量）大的列的索引放前面，最常用作限制条件的列的索引放在最前面，有null值的列的索引不会生效
4. 如果where条件中是or关系，加索引不起作用
5. 使用短索引
6. like '%xxx%'不会使用索引，而like 'xxx%'可以使用索引
7. 使用not exists替代not in

##### 数据库设计优化

优化原则：

1. 类型的优先使用顺序：tinyint > smallint > int > char > varchar > text
2. 最好给每个字段一个默认值，不使用null
3. 大sql语句拆成小语句
4. 不用select *
5. or改写为in，or改写为union
6. limit越大，效率越低
7. 使用union all替代union（union有去重开销）

优化案例：

1. 使用int来存储日期时间，利用FROM_UNIXTIME()、UNIX_TIMESTAMP()两个函数来进行转换

```mysql
INSERT INTO test (time_int) VALUES (UNIX_TIMESTAMP('2020-03-30 16:00:00'));
SELECT FROM_UNIXTIME(time_int) FROM test;
```

2. 使用bigint(8)替代varchar(15)来存储ip地址，利用INET_ATON()、INET_NTOA()两个函数来进行转换

```mysql
INSERT INTO test (ipaddress_bigint) VALUES (INET_ATON('192.168.0.1'));
SELECT INET_NTOA(ipaddress_bigint) FROM test;
```

##### 表范式化

需拆分的表：

| 商品名称 | 价格 | 规格  | 有效期 | 分类     | 分类描述 |
| -------- | ---- | ----- | ------ | -------- | -------- |
| 可乐     | 3.00 | 250ml | 2014.6 | 酒水饮料 | 碳酸饮料 |
| 北冰洋   | 3.00 | 250ml | 2014.7 | 酒水饮料 | 碳酸饮料 |
| 苹果     | 8.00 | 500g  |        | 生鲜食品 | 水果     |

拆分为：

表一（单独表）

| 商品名称 | 价格 | 规格  | 有效期 |
| -------- | ---- | ----- | ------ |
| 可乐     | 3.00 | 250ml | 2014.6 |
| 北冰洋   | 3.00 | 250ml | 2014.7 |
| 苹果     | 8.00 | 500g  |        |

表二（单独表）

| 分类     | 分类描述 |
| -------- | -------- |
| 酒水饮料 | 碳酸饮料 |
| 生鲜食品 | 水果     |

表三（关系表）

| 分类     | 商品名称 |
| -------- | -------- |
| 酒水饮料 | 可乐     |
| 酒水饮料 | 北冰洋   |
| 生鲜食品 | 苹果     |

##### 反范式化

增加冗余（关联字段，减少join），优化查询效率，用空间换取时间

##### 垂直拆分

原则：

1. 把不常用的字段表单独存放到一个表中
2. 把大字段独立存放到一个表中
3. 把经常一起使用的字段放到一起

##### 水平拆分

如果单表数据量巨大，则需要把数据存储到不同的表中，通过对主键进行取模运算（主键数字 % 要拆分的数量），把大表拆分成原表名+模数的多张小表

##### 操作系统配置优化

修改网络配置

```shell
vi /etc/sysctl.conf
```

```
# 增加tcp支持的队列数
net.ipv4.tcp_max_syn_backlog = 65535
# 减少断开连接时，资源回收
net.ipv4.tcp_max_tw_buckets = 8000
net.ipv4.tcp_tw_reuse = 1
net.ipv4.tcp_tw_recycle = 1
net.ipv4.tcp_fin_timeout = 10
```

打开文件数限制

```shell
vi /etc/security/limits.conf
```

```
# 增加以下内容以修改打开文件数量的限制（默认1024，永久生效）
*Soft nofile 65535
*Hard nofile 65535
```

查看限制

```shell
ulimit -a
```


