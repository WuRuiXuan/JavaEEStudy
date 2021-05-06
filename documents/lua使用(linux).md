##### 下载安装

下载地址：https://www.lua.org/download.html

下载文件名：lua-5.4.3.tar.gz

编译安装

```shell
tar -zxvf lua-5.4.3.tar.gz
cd lua-5.4.3
make linux test
make install
```

如果执行make linux test失败，报错readline/readline.h: No such file or directory，说明当前系统缺少libreadline-dev依赖包，需要安装

```shell
yum install -y readline-devel
```

验证是否安装成功

```shell
lua -v
```

