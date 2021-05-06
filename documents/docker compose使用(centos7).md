##### 下载安装

```shell
# Compose目前已经完全支持Linux、MacOS和Windows，安装Compose之前，需要先安装Docker
curl -L https://get.daocloud.io/docker/compose/releases/download/1.27.4/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
# 设置文件可执行权限
chmod +x /usr/local/bin/docker-compose
# 查看版本信息
docker-compose -version
```

##### 卸载

```shell
# 二进制包方式安装的，删除二进制文件即可
rm /usr/local/bin/docker-compose
```

##### 编排nginx+springboot项目

创建docker-compose目录

```shell
mkdir ~/docker-compose
cd ~/docker-compose
```

编写docker-compose.yml文件

```yaml
version: '3'
# 定义服务（容器）
services:
  # nginx容器
  nginx:
  	# 基于nginx镜像
	image: nginx
	# 端口映射
	ports:
	  - 80:80
	# 当前容器可以访问到app容器
	links:
	  - app
	# 目录映射（数据卷）
	volumes:
	  - ./nginx/conf.d:/etc/nginx/conf.d
  # app容器
  app:
    # 基于app镜像
    image: app
    # 对外暴露端口
    expose:
      - "8080"
```

创建./nginx/conf.d目录，在该目录下编写nginx配置文件

```shell
mkdir -p ./nginx/conf.d
cd ./nginx/conf.d
vim mynginx.conf
```

配置内容（注意反向代理地址和端口）

```
server {
	listen 80;
	access_log off;
	
	location / {
		proxy_pass http://app:8080;
	}
}
```

启动容器

```shell
cd ~/docker-compose

# -d 后台启动
docker-compose up
docker-compose up -d
```

