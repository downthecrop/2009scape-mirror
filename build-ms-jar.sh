#!/bin/bash
cd Management-Server
sh mvnw package
mv target/*with-dependencies.jar ms.jar
echo "Jar built and moved to Management-Server/ms.jar"
