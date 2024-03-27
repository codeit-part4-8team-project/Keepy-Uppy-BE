#!/bin/bash

REPOSITORY=/home/ec2-user/app/keepyuppy
PROJECT_NAME=Keepy-Uppy-BE

echo "> Build 파일 복사"
cp $REPOSITORY/zip/build/libs/KeepyUppy-0.0.1-SNAPSHOT.jar $REPOSITORY/

echo "> 현재 구동 중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -f $PROJECT_NAME)

echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다"
else
  echo "> kill  $CURRENT_PID"
  sudo kill $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 2)

echo "> JAR_NAME: $JAR_NAME"
echo "> $JAR_NAME 에 실행권한 추가"
chmod +x $JAR_NAME


echo "> $JAR_NAME 실행"
    nohup java -jar \
        $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
