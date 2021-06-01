# Flarum 环境
基于 Docker 与 Docker Compose 的 Flarum 环境

## 目录结构
* data/ssl 申请证书相关文件
* data/db-data 数据库数据文件
* data/logs nginx、php-fpm 等日志
* www/flarum goaskme.app Flarum 站点文件
* www/game game.goaskme.app

## 初始化
数据库连接信息，在 .env 配置：
```
DB_NAME=flarum_db
DB_USER=flarum_db_user
DB_PASS=
DB_ROOT_PASS=
```

## 部署
1. 首先在宿主机安装 Docker 与 Docker Compose
2. 克隆代码

```sh
git clone https://github.com/flarumdev/flarum-env
cd flarum-env
```

3. 创建软链

```sh
ln -s /path/to/flarum www/flarum
ln -s /path/to/game www/game
```

4. 创建环境变量配置 `.env` 文件

```sh
vim .env
```

输入（DB_PASS，DB_ROOT_PASS 需改成实际想要的密码）：
```
DB_NAME=flarum_db
DB_USER=flarum_db_user
DB_PASS=xxxxx
DB_ROOT_PASS=xxxxx
```

5. 启动

```sh
docker-compose up -d
```

## Let's Encrypt 证书签发
首先确保 Nginx 已经跑起来，这里用了 acme.sh 的方案，[参考这里](https://github.com/acmesh-official/acme.sh/wiki/deploy-to-docker-containers)。

签发证书：
```shell
sudo docker exec acme.sh --issue -d goaskme.app -d game.goaskme.app -w /
```

签发完后，证书相关文件保存在 `acmeout` 、安装证书：
```shell
sudo docker exec \
  -e DEPLOY_DOCKER_CONTAINER_LABEL=sh.acme.autoload.domain=goaskme.app \
  -e DEPLOY_DOCKER_CONTAINER_KEY_FILE=/etc/ssl/goaskme.app/key.pem \
  -e DEPLOY_DOCKER_CONTAINER_CERT_FILE="/etc/ssl/goaskme.app/cert.pem" \
  -e DEPLOY_DOCKER_CONTAINER_CA_FILE="/etc/ssl/goaskme.app/ca.pem" \
  -e DEPLOY_DOCKER_CONTAINER_FULLCHAIN_FILE="/etc/ssl/goaskme.app/full.pem" \
  -e DEPLOY_DOCKER_CONTAINER_RELOAD_CMD="kill 1" \
  acme.sh --deploy -d goaskme.app --deploy-hook docker
```

测试证书生效：
```
curl -I https://goaskme.app --resolve goaskme.app:443:127.0.0.1
```
