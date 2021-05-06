##### 查看mysql查找配置文件的顺序

```shell
/usr/sbin/mysqld --verbose --help | grep -A 1 'default options'
```

##### max_connections

max_connections - 最大连接数；max_used_connections - 响应的连接数

max_used_connections / max_connections * 100% - 等于100%表示max_connections设置过低或超过服务器负载上限了；低于10%表示max_connections设置过大；理想值约为85%

##### back_log

在mysql暂时停止回答新请求之前的短时间内有多少个请求可以被存在堆栈中，对应到来的TCP/IP连接的侦听队列的大小

如果期望在一段短时间内有很多连接，就需要增加它

执行以下sql语句

```mysql
SHOW FULL PROCESSLIST;
```

如果发现大量待连接进程时（如下），就需要增加它

```shell
264084 | unauthenticated user | xxx.xxx.xxx.xxx | NULL | Connect | NULL | login | NULL
```

默认值50，可调优为128，设置范围为小于512的整数

##### interactive_timeout

一个交互连接在被服务器关闭前等待行动的秒数

默认值是28800，可调优为7200

##### key_buffer_size

索引缓冲区的大小，决定索引读的速度

执行以下sql语句

```mysql
SHOW GLOBAL STATUS LIKE 'Key_read%';
```

索引未命中缓存的概率 = Key_reads / Key_read_requests * 100%

未命中率应该尽可能低，建议1% - 0.1%，如果低于0.01%，则说明该值设置过大，可适当减小

默认值是8388600(8MB)，主机有4GB内存，可调优为268435456(256MB)

##### query_cache_size

查询缓冲的大小

相关参数：

- query_cache_type - 是否使用查询缓冲，可以设置为0、1、2

- query_cache_limit - 单个查询能够使用的缓冲大小，默认为1MB

- query_cache_min_res_unit - 分配缓冲区空间的最小单位，默认为4KB

执行以下sql语句

```mysql
SHOW STATUS LIKE 'Qcache%';
```

- 如果Qcache_lowmem_prunes的值非常大，则表示经常出现缓冲不够的情况

- 如果Qcache_hits的值也非常大，则表示查询缓冲使用非常频繁，此时需要增加缓冲大小

- 如果Qcache_hits的值不大，则表示查询重复率很低，此时可以考虑不用查询缓冲

- 如果Qcache_free_blocks的值非常大，则表示缓冲区中碎片很多，查询结果都比较小，此时需要减小query_cache_min_res_unit

计算确定设置是否合理：

- 查询缓存碎片率 = Qcache_free_blocks / Qcache_total_blocks * 100%

- 查询缓存利用率 = (query_cache_size - Qcache_free_memory) / query_cache_size * 100%

- 查询缓存命中率 = (Qcache_hits - Qcache_inserts) / Qcache_hits * 100%

如果查询缓存碎片率大于20%，则尝试减小query_cache_min_res_unit

如果查询缓存利用率小于25%，则说明query_cache_size设置过大，可减小

如果查询缓存利用率大于80%，且Qcache_lowmem_prunes大于50，则说明query_cache_size设置过小或碎片太多

##### record_buffer_size

每个进行一个顺序扫描的线程为其扫描的每张表分配这个大小的一个缓冲区

如果做很多顺序扫描，则可以增加该值

默认值是131072(128KB)，可调优为16773120(16MB)

##### read_rnd_buffer_size

随机读缓冲区大小

如果需要排序大量数据，则可以增加该值，但应适当设置该值，以避免内存开销过大

一般可设置为16773120(16MB)

##### sort_buffer_size

每个需要进行排序的线程分配该大小的一个缓冲区

增加该值可以加速ORDER BY或GROUP BY操作

默认值是2097144(2M)，可调优为16773120(16MB)

##### join_buffer_size

联合查询操作所能使用的缓冲区大小

每个线程独占，也就是说，如果有100个线程连接，则占用为16MB*100

##### table_cache

表高速缓存的大小

执行以下sql语句

```mysql
SHOW STATUS LIKE 'Open%tables';
```

如果发现Open_tables等于table_cache，并且Opened_tables在不断增长，则需要增加该值

但如果设置得太高，可能会造成文件描述符不足，从而造成性能不稳定或者连接失败

1GB内存的机器，推荐值是128MB－256MB；4GB左右内存的服务器，该参数可设置为256MB或384MB

##### max_heap_table_size

用户可以创建的内存表(memory table)的大小

##### tmp_table_size

用户可以创建的临时表的大小

增加该值可以提高联接查询速度

执行以下sql语句

```mysql
SHOW GLOBAL STATUS LIKE 'Created_tmp%';
```

- Created_tmp_tables - 创建的临时表的数量

- Created_tmp_disk_tables - 在磁盘上创建的临时表的数量（如果临时表大小超过tmp_table_size，则是在磁盘上创建）

- Created_tmp_files - MySQL服务创建的临时文件文件数

理想值：Created_tmp_disk_tables / Created_tmp_tables * 100% <= 25%

默认值是16MB，可调到64MB-256MB最佳，线程独占，太大可能内存不够，I/O堵塞

##### thread_cache_size

可以复用的保存在缓存中的线程的数量

如果有很多新的线程，则可以减小该值

默认值是110，可调优为80

##### thread_concurrency

推荐设置为服务器CPU核数的2倍

默认值是8

##### wait_timeout

指定一个请求的最大连接时间

4GB左右内存的服务器可以设置为5-10

##### innodb_buffer_pool_size

缓冲数据和索引使用的内存的大小

2G内存的机器，推荐值是1G(50%)

##### innodb_flush_log_at_trx_commit

将log buffer中的数据写入日志文件并flush磁盘的时间点，取值分别为0、1、2

- 0 - 当事务提交时，不做日志文件写入的操作，而是每秒钟将log buffer中的数据写入日志文件并flush磁盘一次
- 1 - 每秒钟或每次事务提交时，执行日志文件写入、flush磁盘的操作，确保了事务的ACID
- 2 - 每次事务提交时，执行日志文件写入的操作，每秒钟完成一次flush磁盘的操作

##### innodb_log_buffer_size

log缓存大小

对于较大的事务，可以增加缓存大小

默认值是1MB，可设置为4MB或8MB

##### innodb_additional_mem_pool_size

用来存储数据字典和其他内部数据结构的内存池大小

默认值是1MB，2G内存的机器，推荐值是20MB，可适当增加

##### innodb_thread_concurrency

推荐设置为 2 * (CPU核数 + 磁盘数)，默认值是8

##### Innodb_read_io_threads、Innodb_write_io_threads

推荐设置为服务器CPU核数的2倍

默认值是4，允许值1-64