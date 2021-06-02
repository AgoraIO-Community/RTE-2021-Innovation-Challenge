#!/bin/bash

docker buildx build --platform linux/amd64 . --push -t yanzhen/send-mail:$1
