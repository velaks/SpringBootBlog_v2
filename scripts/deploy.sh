#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp 

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa_drucoder << EOF
pgrep java | xargs kill -9
nohup java -jar sweater-1.0-SNAPSHOT.jar > log.txt &
EOF

echo 'Bye'