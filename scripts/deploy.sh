#!/bin/bash
BUILD_JAR=$(ls /home/ec2-user/action/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "> build 파일명: $JAR_NAME" >> /home/ec2-user/action/deploy.log

echo "> build 파일 복사" >> /home/ec2-user/action/deploy.log
DEPLOY_PATH=/home/ec2-user/action/
cp $BUILD_JAR $DEPLOY_PATH

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ec2-user/action/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/action/deploy.log
else
  echo "> 애플리케이션 종료 : $CURRENT_PID" >> /home/ec2-user/action/deploy.log
  echo "> kill -9 $CURRENT_PID"
  kill -9 $CURRENT_PID
  sleep 5
  echo "> 애플리케이션이 정상 종료되었습니다." >> /home/ec2-user/action/deploy.log
fi

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포"    >> /home/ec2-user/action/deploy.log

nohup java -jar $DEPLOY_JAR --logging.file.path=/home/ec2-user/log --logging.level.org.hibernate.SQL=DEBUG >> /home/ec2-user/log/deploy.log 2>/home/ec2-user/log/deploy_err.log &

# nohup java -jar $DEPLOY_JAR >> /home/ec2-user/log/deploy.log 2>/home/ec2-user/action/deploy_err.log &
