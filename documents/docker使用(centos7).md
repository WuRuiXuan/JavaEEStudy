##### 安装docker

```shell
# yum包更新到最新
yum update
# 安装需要的软件包
yum install -y yum-utils device-mapper-persistent-data lvm2
# 设置yum源
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
# 安装docker，出现输入的界面都按y
yum install -y docker-ce
# 验证是否安装成功
docker -v
```

##### 服务相关命令

```shell
# 启动docker服务
systemctl start docker
# 查看docker服务状态
systemctl status docker
# 关闭docker服务
systemctl stop docker
# 重启docker服务
systemctl restart docker
# 开机启动docker
systemctl enable docker
```

##### 查看镜像

```shell
docker images
docker images -q # 查看所有镜像的id
```

##### 搜索镜像

```shell
docker search [mirror_name]
```

##### 拉取镜像

查询镜像版本：https://hub.docker.com/search?q=&type=image

```shell
docker pull [mirror_name]
docker pull [mirror_name]:[version]
```

##### 删除镜像

```shell
docker rmi [image_id]
docker rmi [repository]:[tag]
docker rmi `docker images -q` # 删除所有镜像
```

##### 查看容器

```shell
docker ps # 查看正在运行的容器
docker ps -a # 查看所有容器，包括已关闭的
docker ps -aq # 查看所有容器的id
```

##### 创建容器

```shell
# -i 交互式操作，创建后自动进入容器，退出后容器自动关闭
# -t 增配一个终端
# -d 后台运行，退出不会自动关闭
# -p 端口映射
# --name=[container_name] 给容器起名
# [repository]:[tag] 根据哪个镜像创建容器
# /bin/bash 终端使用shell交互
docker run -it --name=[container_name] [repository]:[tag] /bin/bash # 交互式容器
docker run -id --name=[container_name] [repository]:[tag] # 守护式容器
docker run -id -p 9000:8080 app # 将app应用程序的8080端口映射到容器的9000端口
```

##### 进入容器

```shell
docker exec -it [container_name] /bin/bash
```

##### 退出容器

```shell
exit
```

##### 启动容器

```shell
docker start [container_name]
```

##### 停止容器

```shell
docker stop [container_name]
```

##### 删除容器

```shell
docker rm [container_name]
docker rm [container_id]
docker rm `docker ps -aq` # 删除所有容器
```

##### 查看容器信息

```shell
docker inspect [container_name]
```

##### 配置数据卷

创建容器时，使用-v参数设置数据卷

```shell
# 目录必须是绝对路径
# 如果目录不存在，则会自动创建
# 可以挂载多个数据卷
docker run ... -v [host_dir]:[container_dir] ...
```

##### 配置数据卷容器

```shell
docker run ... --name=c3 -v [container_dir] ... # 默认会将/var/lib/docker/volumes/[hash_string]/_data挂载到container_dir
docker run ... --name=c1 --volumes-from c3 ...
docker run ... --name=c2 --volumes-from c3 ...
```

##### 部署mysql

```shell
# 搜索mysql镜像
docker search mysql
# 拉取mysql镜像
docker pull mysql:[version]
# 创建mysql目录
mkdir ~/mysql
cd ~/mysql
# 创建容器，设置端口映射、目录映射
docker run -id \
-p 3307:3306 \
--name=c_mysql \
-v $PWD/conf:/etc/mysql/conf.d \
-v $PWD/logs:/logs \
-v $PWD/data:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=123456 \
mysql:[version]
```

##### 部署tomcat

```shell
docker search tomcat
docker pull tomcat:[version]
mkdir ~/tomcat
cd ~/tomcat

docker run -id \
-p 8080:8080 \
--name=c_tomcat \
-v $PWD:/usr/local/tomcat/webapps \
tomcat:[version]
```

##### 部署nginx

```shell
docker search nginx
docker pull nginx:[version]
mkdir ~/nginx
cd ~/nginx
# 创建配置文件目录
mkdir conf
cd conf
# 创建配置文件，将配置内容保存到文件中
vim nginx.conf

docker run -id \
-p 80:80 \
--name=c_nginx \
-v $PWD/conf/nginx.conf:/etc/nginx/nginx.conf \
-v $PWD/logs:/var/log/nginx \
-v $PWD/html:/usr/share/nginx/html \
nginx:[version]
```

##### 部署redis

```shell
docker search redis
docker pull redis:[version]
mkdir ~/redis
cd ~/redis

docker run -id \
-p 6379:6379 \
--name=c_redis \
redis:[version]
```

##### 容器转镜像

```shell
docker commit [container_id] [mirror_name]:[mirror_version]
```

##### 镜像转压缩文件

```shell
docker save -o [file_name(.tar)] [mirror_name]:[mirror_version]
```

##### 压缩文件转镜像

```shell
docker load -i [file_name(.tar)]
```

##### 使用Dockerfile构建镜像

```shell
# -f 指定dockerfile文件的路径
# -t 指定新镜像的名称和版本
docker build -f [dockerfile_path] -t [mirror_name]:[mirror_version] .
```

##### Dockerfile关键字

| 关键字      | 作用                     | 备注                                                         |
| ----------- | ------------------------ | ------------------------------------------------------------ |
| FROM        | 指定父镜像               | 指定dockerfile基于哪个image构建                              |
| MAINTAINER  | 作者信息                 | 用来标明这个dockerfile是谁写的                               |
| LABEL       | 标签                     | 用来标明dockerfile的标签，可以使用Label代替Maintainer，在docker image基本信息中可以查看 |
| RUN         | 执行命令                 | 执行一段命令，默认是/bin/sh，格式：RUN command 或者 RUN ["command", "param1", "param2"] |
| CMD         | 容器启动命令             | 提供启动容器时的默认命令，和ENTRYPOINT配合使用，格式：CMD command param1 param2 或 CMD ["command", "param1", "param2"] |
| ENTRYPOINT  | 入口                     | 一般在制作一些执行就关闭的容器中会使用                       |
| COPY        | 复制文件                 | build的时候复制文件到image中                                 |
| ADD         | 添加文件                 | build的时候添加文件到image中，不仅仅局限于当前build上下文，可以来源于远程服务 |
| ENV         | 环境变量                 | 指定build时候的环境变量，可以在启动容器时，通过-e覆盖，格式：ENV name=value |
| ARG         | 构建参数                 | 只在构建时使用的参数，如果有ENV，那么ENV的相同名字的值始终覆盖arg的参数 |
| VOLUME      | 定义外部可以挂载的数据卷 | 指定build的image哪些目录可以在启动时挂载到文件系统中，启动容器的时候使用-v绑定，格式：VOLUME [index] |
| EXPOSE      | 暴露端口                 | 定义容器运行的时候监听的端口，启动容器时使用-p来绑定暴露端口，格式：EXPOSE 8080 或 EXPOSE 8080/udp |
| WORKDIR     | 工作目录                 | 指定容器内部的工作目录，如果没有创建则自动创建，如果是/开头，则是绝对路径，如果不是/开头，那么是上一条WORKDIR路径的相对路径 |
| USER        | 指定执行用户             | 指定在build时或者启动时，用户在RUN CMD ENTRYPOINT执行时的用户 |
| HEALTHCHECK | 健康检查                 | 指定监测当前容器的健康状态的命令，基本上没用，因为很多时候应用本身有健康监测机制 |
| ONBUILD     | 触发器                   | 当存在ONBUILD关键字的镜像作为基础镜像时，执行FROM完成之后，会执行ONBUILD的命令，但是不影响当前镜像，用处也不怎么大 |
| STOPSIGNAL  | 发送信号量到宿主机       | 设置将发送到容器的系统调用信号以退出                         |
| SHELL       | 指定执行脚本的shell      | 指定RUN CMD ENTRYPOINT执行时使用的shell                      |

##### 自定义centos7镜像

要求：

- 默认登录路径为/usr
- 可以使用vim

```dockerfile
# centos_dockerfile

FROM centos:7
MAINTAINER wrx <wrx1379@163.com>
RUN yum install -y vim
WORKDIR /usr
CMD /bin/bash
```

##### 自定义springboot镜像

将springboot打包的jar包放到dockerfile同一级目录

```dockerfile
# springboot_dockerfile
# ADD SpringBoot-1.0-SNAPSHOT.jar app.jar：添加jar包并改名为app

FROM java:8
MAINTAINER wrx <wrx1379@163.com>
ADD SpringBoot-1.0-SNAPSHOT.jar app.jar
CMD java -jar app.jar
```

创建容器

```shell
docker run -id -p 9000:8080 app
```

##### 搭建私有仓库

```shell
# 1.拉取私有仓库镜像
docker pull registry
# 2.启动私有仓库容器
docker run -id --name=registry -p 5000:5000 registry
# 3.打开浏览器，输入地址http://[私有仓库服务器ip:5000]/v2/_catalog，看到{"repositories":[]}表示私有仓库搭建成功
# 4.修改daemon.json
vim /etc/docker/daemon.json
# 5.在上述文件中添加一个key：{"insecure-registries": ["私有仓库服务器ip:5000"]}，保存退出，让docker信任私有仓库地址
# 6.重启docker服务
systemctl restart docker
docker start registry
```

##### 将镜像上传至私有仓库

```shell
# 1.标记镜像为私有仓库的镜像
docker tag [mirror_name]:[version] [私有仓库服务器ip:5000]/[mirror_name]:[version]
# 2.上传标记的镜像
docker push [私有仓库服务器ip:5000]/[mirror_name]:[version]
```

##### 从私有仓库拉取镜像

```shell
docker pull [私有仓库服务器ip:5000]/[mirror_name]:[version]
```

