#!/bin/sh
git pull --recurse-submodules
git submodule update --init --recursive
sudo docker run --rm -v $(pwd):/var/www geshan/php-composer-alpine "composer install --ignore-platform-reqs"
sudo docker run --rm -v $(pwd):/var/www geshan/php-composer-alpine "composer dump-autoload -o"

