##### 下载oracle安装包

官网下载地址：https://www.oracle.com/database/technologies/oracle-database-software-downloads.html

选择免费版 Oracle Database 11g Release 2 Express Edition for Linux x86 and Windows

下载的文件名为：oracle-xe-11.2.0-1.0.x86_64.rpm.zip

##### 解压oracle安装包

```shell
unzip oracle-xe-11.2.0-1.0.x86_64.rpm.zip
```

##### 安装依赖

```shell
yum install libaio* -y
```

##### 创建用户和组

```shell
cd Disk1/
useradd oracle
groupadd dba
groupadd oinstall
useradd -m -g oinstall -G dba oracle
grep dba /etc/group
grep oinstall /etc/group
id oracle
```

##### 创建swap

```shell
dd if=/dev/zero of=/home/swap2 bs=1024 count=2512000
/sbin/mkswap /home/swap2
/sbin/swapon /home/swap2
```

##### 修改/etc/fstab文件

```shell
vi /etc/fstab
```

添加一行

```
/home/swap2 swap swap defaults 0 0
```

保存退出再执行

```shell
mount -a
free -m
```

##### 配置内核参数

```shell
vi /etc/profile
```

添加以下内容

```
# Oracle Settings
TMP=/tmp; export TMP
TMPDIR=$TMP; export TMPDIR
ORACLE_BASE=/u01/app/oracle; export ORACLE_BASE
ORACLE_HOME=$ORACLE_BASE/product/11.2.0/xe; export ORACLE_HOME
ORACLE_SID=XE; export ORACLE_SID
ORACLE_TERM=xterm; export ORACLE_TERM
PATH=/usr/sbin:$PATH; export PATH
PATH=$ORACLE_HOME/bin:$PATH; export PATH
TNS_ADMIN=$ORACLE_HOME/network/admin
LD_LIBRARY_PATH=$ORACLE_HOME/lib:/lib:/usr/lib; export LD_LIBRARY_PATH
CLASSPATH=$ORACLE_HOME/jlib:$ORACLE_HOME/rdbms/jlib; export CLASSPATH

if [ $USER = "oracle" ];then
  if [ $SHELL = "/bin/ksh" ];then
    ulimit -p 16384
    ulimit -n 65536
  else
    ulimit -u 16384 -n 65536
  fi
fi
```

保存退出再执行

```shell
source /etc/profile
echo $ORACLE_BASE
```

手动创建目录，否则后续安装会报错

```shell
mkdir -p /u01/app/oracle/oradata
mkdir -p /u01/app/oracle/diag
mkdir -p /u01/app/oracle/product/11.2.0/xe
chown -R oracle.oinstall /u01
```

##### 安装oracle

```shell
rpm -ivh oracle-xe-11.2.0-1.0.x86_64.rpm
```

安装完成后会：

- 建立一个名为oracle的用户，路径：/u01/app/oracle
- 建立dba组，oracle用户属于这个组
- 生成一个数据库实例，名为XE，路径：/u01/app/oracle/product/11.2.0/xe/dbs/spfileXE.ora

##### 配置oracle

```shell
/etc/init.d/oracle-xe configure
```

##### 重新配置oracle

```shell
/etc/init.d/oracle-xe stop
sudo rm /etc/default/oracle-xe
/etc/init.d/oracle-xe configure
```

##### 连接数据库

```shell
oracle
sqlplus /nolog
```

在SQL控制台执行

```mysql
conn sys/ as sysdba;
```

允许远程连接

```mysql
alter user system identified by system;
```

然后就可以使用用户名system和密码system登录数据库了

##### 查看监听参数文件

```shell
vi $ORACLE_HOME/network/admin/listener.ora
vi $ORACLE_HOME/network/admin/tnsnames.ora
```

##### 卸载swap文件

```shell
swapoff /path/to/swapfile/to/be/deleted
```

##### 启动oracle服务

```shell
service oracle-xe start
```

##### 卸载oracle

```shell
/etc/init.d/oracle-xe stop
dpkg --purge oracle-xe
rm -r /u01/app
rm /etc/default/oracle-xe
update-rc.d -f oracle-xe remove
```

##### 修改oracle默认字符集

查看oracle字符集

```mysql
select userenv('language') from dual;
```

查看当前oracle环境中一个汉字占多少个字节（默认3个字节）

```mysql
select lengthb('啊') from dual;
```

修改oracle字符集，使一个汉字占两个字节（注意原表中的汉字会变成乱码）

```mysql
SHUTDOWN IMMEDIATE;
STARTUP MOUNT;
ALTER SYSTEM ENABLE RESTRICTED SESSION;
ALTER SYSTEM SET JOB_QUEUE_PROCESSES=0;
ALTER DATABASE OPEN;
ALTER DATABASE CHARACTER SET INTERNAL_USE ZHS16GBK;
SHUTDOWN IMMEDIATE;
STARTUP;
```

##### 解决scott用户不存在

centos系统，先执行以下命令

```shell
cd $ORACLE_HOME/rdbms/admin
ls -lhrt utlsampl.sql
sqlplus /nolog
```

再执行以下sql语句

```mysql
conn sys/ as sysdba;
@utlsampl.sql;
```

windows系统，以管理员登录数据库后执行脚本utlsample.sql（路径：...\Oracle\product\12.2.0\dbhome_1\rdbms\admin\utlsample.sql）

##### maven添加ojdbc6.jar(11g)包

下载jar包到本地：https://mvnrepository.com/artifact/oracle/ojdbc6/11.2.0.3

执行命令

```shell
mvn install:install-file -Dfile=D:\Downloads\ojdbc6-11.2.0.3.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.3 -Dpackaging=jar
```

