#!/bin/bash

# codegen
yarn graphql-codegen
node generate.js

# format schema
cd ../data
yarn prisma format
