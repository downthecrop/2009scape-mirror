#!/bin/bash
cd Server
sh mvnw package
mv target/*with-dependencies.jar server.jar
echo "Jar built and moved to Server/server.jar"
