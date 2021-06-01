## Contributing to ITalking

[fork]: /fork
[pr]: /compare
[style]: https://standardjs.com/
[code-of-conduct]: CODE_OF_CONDUCT.md

Hi there! We're thrilled that you'd like to contribute to this project. Your help is essential for keeping it great.

Please note that this project is released with a [Contributor Code of Conduct][code-of-conduct]. By participating in this project you agree to abide by its terms.

## How to run locally

### Pre-requirements

Please ensure that your local environment has the following dependencies:

1. Redis - ^6.0.0
2. MySQL - ^8.0.0
3. Go - ^1.16.0
4. Nodejs - ^14.0.0

**Please make sure that the front and back-end communication is under HTTPS**

### Database

You need to create a database called ITalking.

```sql
create database ITalking;
```

### Service

1. Please rename the `env.sample` file under the project to `.env`

2. Check and fill the parameters in the file.

3. Generate executable file in the project root directory.

   ```sh
   go build -o server
   ```

4. Start and execute the generated file.

   ```sh
   env GIN_MODE=release ./bin
   ```

5. Enjoy it!

### Fronted

1. Installing project dependencies.

   ```sh
   yarn install
   ```

2. Build Project.

   ```sh
   yarn build
   ```

3. Deploy the build folder.

## Deploy

### Files

1. scp -r server/bin root@ip:/root/ITalking
2. scp -r server/.env root@ip:/root/ITalking
3. scp -r fronted/build root@ip:/root/ITakling

### Nginx

1. sudo yum install epel-release

2. sudo yum install certbot-nginx

3. sudo yum install nginx

4. sudo systemctl start nginx

5. sudo vim /etc/nginx/nginx.conf

   user root;

   server_name italking.tomotoes.com;

   root => /root/ITalking/build

   location => try_files $uri /index.html;

   location /v1 {
      proxy_pass http://ip:port; 

      \# Domain name and port corresponding to. env
   }

   :x exit

   sudo nginx -t

   sudo systemctl reload nginx

6. sudo iptables -I INPUT -p tcp -m tcp --dport 80 -j ACCEPT

   sudo iptables -I INPUT -p tcp -m tcp --dport 443 -j ACCEPT

   - sudo firewall-cmd --add-service=http
   - sudo firewall-cmd --add-service=https
   - sudo firewall-cmd --runtime-to-permanent

7. sudo certbot --nginx -d italking.tomotoes.com

   input your email;

8. sudo openssl dhparam -out /etc/ssl/certs/dhparam.pem 2048

9. sudo vim /etc/nginx/nginx.conf

   ssl_dhparam /etc/ssl/certs/dhparam.pem;

   :x exit

   sudo nginx -t

   sudo systemctl reload nginx

10. sudo crontab -e

    15 3 * * * /usr/bin/certbot renew --quiet

### Redis

1. sudo yum install epel-release
2. sudo yum install redis -y
3. sudo systemctl start redis.service

### MySQL

1. sudo wget https://dev.mysql.com/get/mysql80-community-release-el7-3.noarch.rpm

2. sudo rpm -Uvh mysql80-community-release-el7-3.noarch.rpm

3. sudo yum install mysql-server

4. sudo systemctl start mysqld

5. sudo grep 'temporary password' /var/log/mysqld.log

6. mysql -u root -p

7. create database DATABASE;

8. SET GLOBAL validate_password.policy=LOW;

   SET GLOBAL validate_password.length = 6;

   SET GLOBAL validate_password.number_count = 0;

9. ALTER USER 'root'@'localhost' IDENTIFIED BY 'password';

   \# Database and password corresponding to. env

10. sudo systemctl restart mysqld

### Run

1. env GIN_MODE=release ./ITalking/bin
2. Enjoy it!

## Code of Conduct

The code of conduct is described in [`CODE_OF_CONDUCT.md`](CODE_OF_CONDUCT.md).

## Issues and PRs

If you have suggestions for how this project could be improved, or want to report a bug, open an issue! We'd love all and any contributions. If you have questions, too, we'd love to hear them.

We'd also love PRs. If you're thinking of a large PR, we advise opening up an issue first to talk about it, though! Look at the links below if you're not sure how to open a PR.

## Submitting a pull request

1. [Fork][fork] and clone the repository.
1. Configure and install the dependencies: `npm install`.
1. Make sure the tests pass on your machine: `npm test`, note: these tests also apply the linter, so there's no need to lint separately.
1. Create a new branch: `git checkout -b my-branch-name`.
1. Make your change, add tests, and make sure the tests still pass.
1. Push to your fork and [submit a pull request][pr].
1. Pat your self on the back and wait for your pull request to be reviewed and merged.

Here are a few things you can do that will increase the likelihood of your pull request being accepted:

- Follow the [style guide][style] which is using standard. Any linting errors should be shown when running `npm test`.
- Write and update tests.
- Keep your changes as focused as possible. If there are multiple changes you would like to make that are not dependent upon each other, consider submitting them as separate pull requests.
- Write a [good commit message](http://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html).

Work in Progress pull requests are also welcome to get feedback early on, or if there is something blocked you.

## Resources

- [How to Contribute to Open Source](https://opensource.guide/how-to-contribute/)
- [Using Pull Requests](https://help.github.com/articles/about-pull-requests/)
- [GitHub Help](https://help.github.com)
