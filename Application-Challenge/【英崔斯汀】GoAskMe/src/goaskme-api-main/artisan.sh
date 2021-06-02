#!/bin/sh
sudo docker run --rm -v $(pwd):/var/www geshan/php-composer-alpine "php artisan list"

