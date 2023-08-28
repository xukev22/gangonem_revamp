#!/bin/bash

cd /home/ec2-user/gangonem_revamp
export SERVER_PORT=8080
#nohup java -jar target/GANGONEM-0.0.1-SNAPSHOT.jar &
java -jar target/GANGONEM-0.0.1-SNAPSHOT.jar
