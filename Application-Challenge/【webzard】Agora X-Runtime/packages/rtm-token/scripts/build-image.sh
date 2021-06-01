#!/bin/bash

docker buildx build --platform linux/amd64 . --push -t yanzhen/agora-rtm-token:$1
