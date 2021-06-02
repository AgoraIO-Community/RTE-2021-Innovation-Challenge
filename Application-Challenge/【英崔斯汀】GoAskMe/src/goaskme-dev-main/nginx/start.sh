#!/bin/sh -
if [[ ! -e /etc/ssl/certs/goaskme.app/key.pem ]]; then
    mkdir -p /etc/ssl/certs/goaskme.app
    openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /etc/ssl/certs/goaskme.app/key.pem -out /etc/ssl/certs/goaskme.app/full.pem \
      -subj "/C=CN/ST=Warwickshire/L=Leamington/O=OrgName/OU=IT Department/CN=goaskme.app"
fi

nginx -g "daemon off;"
