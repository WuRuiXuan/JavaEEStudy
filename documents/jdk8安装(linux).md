##### 检查是否有自带的jdk

```bash
rpm -qa | grep java
```

##### 卸载自带的jdk

```bash
rpm -e --nodeps [自带的jdk包名]
```

##### 下载jdk安装包

官网下载：https://www.oracle.com/cn/java/technologies/javase/javase-jdk8-downloads.html

下载选择 Linux x64 Compressed Archive

下载的文件名为：jdk-8u281-linux-x64.tar.gz

##### 创建安装目录

```shell
cd /usr/local
mkdir jdk
```

##### 解压jdk安装包到安装目录

```shell
tar -zxvf jdk-8u281-linux-x64.tar.gz -C /usr/local/jdk
```

##### 配置jdk环境变量

执行命令：

```shell
vim /etc/profile
```

在末尾添加：

```
export JAVA_HOME=/usr/local/software/jdk1.8.0_281
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export PATH=$JAVA_HOME/bin:$PATH
```

保存退出，再执行命令：

```shell
source /etc/profile
```

##### 查看安装版本

```shell
java -version
```

