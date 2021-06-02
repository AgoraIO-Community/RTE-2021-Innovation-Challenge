#!/bin/bash

docker buildx build --platform linux/amd64 . --push -t yanzhen/x-runtime-data:$1
