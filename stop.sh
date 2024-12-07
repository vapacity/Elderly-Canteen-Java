#!/bin/bash

#stop.sh
#找到正在运行旧项目的 编号
#然后停止
pid=`ps -ef | grep my-spring-boot-app-1.0.0.jar | grep -v grep | awk '{print $2}'`
if [ -n "$pid" ]
then
   kill -9 $pid
fi
