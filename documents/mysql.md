##### E-R图

矩形 - 实体；圆形 - 属性；菱形 - 行为

##### mysql执行引擎

- MyISAM - 不支持事务、外键，速度快，支持的存储格式：静态表、动态表、压缩表
- InnoDB - 支持事务，比MyISAM写的效率差，会占用更多的磁盘空间以保留数据和索引
- MEMORY - 数据存放在内存中，一旦服务关闭，数据就会丢失，使用BTREE索引或HASH索引
- MERGE - 是一组结构完全相同的MyISAM表的组合，本身并没有数据

##### 查看mysql编码

```mysql
SHOW VARIABLES LIKE 'character%';
-- 修改编码后需要重启mysql
```

##### 修改mysql配置文件

```bash
vi /etc/my.cnf
```

##### 启动/关闭mysql服务

```shell
net start mysql
net stop mysql
```

##### mysql登录

```shell
mysql -u[用户名] -p[密码]
mysql -u[用户名] -p
mysql -h[远程服务器ip地址] -u[用户名] -p
mysql --host=[远程服务器ip地址] --user=[用户名] --password=[密码]
```

##### sql分类

- DDL - 操作表、数据库
- DML - 增删改表中的数据
- DQL - 查询表中的数据
- DCL - 授权

##### 查询关键字语法

```mysql
SELECT
	-- 字段列表
FROM
	-- 表名列表
WHERE
	-- 条件列表
GROUP BY
	-- 分组字段
HAVING
	-- 分组之后的条件
ORDER BY
	-- 排序
LIMIT
	-- 分页限定
```

##### 模糊查询

```mysql
SELECT * FROM pet WHERE species LIKE '%d%'; -- 包含d的
SELECT * FROM pet WHERE species LIKE 'd%'; -- 以d开头的
SELECT * FROM pet WHERE species LIKE '_d%'; -- 第二个字是d的
SELECT * FROM pet WHERE species LIKE '___'; -- 3个任意字的
```

##### 多表查询


```mysql
SELECT * FROM a,b WHERE a.A_ID = b.A_ID; -- 隐式内连接
SELECT * FROM a INNER JOIN b ON a.A_ID = b.A_ID; -- 显式内连接 INNER可以省略
SELECT * FROM a LEFT OUTER JOIN b ON a.A_ID = b.A_ID; -- 左外连接 OUTER可以省略
SELECT * FROM a RIGHT OUTER JOIN b ON a.A_ID = b.A_ID; -- 右外连接 OUTER可以省略
```

##### case when


```mysql
SELECT *,
CASE
	WHEN salary > 10000 THEN
		'高收入'
	WHEN salary > 5000 THEN
		'中等收入'
	ELSE
		'低收入'
	END AS level,
CASE sex
	WHEN 'female' THEN
		'女'
	WHEN 'male' THEN
		'男'
	END AS flag
	FROM employee;
```

##### mysql执行顺序

FROM -> ON -> JOIN -> WHERE -> GROUP BY -> WITH -> HAVING -> SELECT -> DISTINCT -> ORDER BY -> LIMIT

##### where和having的区别

- where在分组之前进行限定，如果不满足条件，则不参与分组。having在分组之后进行限定，如果不满足结果，则不会被查询出来
- where后不可以跟聚合函数，having可以进行聚合函数的判断

##### 查看/修改事务的默认提交方式

```mysql
SELECT @@autocommit; -- 1 自动提交 0 手动提交
SET @@autocommit = 0;
```

##### 事务的四大特征

- 原子性：是不可分割的最小操作单位，要么同时成功，要么同时失败
- 持久性：当事务提交或回滚后，数据库会持久化地保存数据
- 隔离性：多个事务之间相互独立
- 一致性：事务操作前后，数据总量不变

##### 事务的隔离级别

概念：多个事务之间隔离的，相互独立的，但是如果多个事务操作同一批数据，则会引发一些问题，设置不同的隔离级别就可以解决这些问题

存在问题：

- 脏读：一个事务读取到另一个事务中没有提交的数据
- 虚读：在同一个事务中，两次读到的数据不一样
- 幻读：一个事务操作（DML）数据表中所有记录，另一个事务添加了一条数据，则第一个事务查询不到自己的修改

隔离级别：

- READ UNCOMMITTED - 读未提交，产生的问题：脏读、虚读、幻读
- READ COMMITTED - 读已提交（oracle默认），产生的问题：虚读、幻读
- REPEATABLE READ - 可重复读（mysql默认），产生的问题：幻读
- SERIALIZABLE - 串行化，不会产生任何问题

注意：隔离级别从小到大，安全性越来越高，但效率越来越低

##### 查询/设置数据库隔离级别

```mysql
SELECT @@tx_isolation; -- mysql5
SELECT @@transaction_isolation; -- mysql8
SET GLOBAL TRANSACTION ISOLATION LEVEL [级别字符串];
```

