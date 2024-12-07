#!/bin/bash

#replate.sh
#判断指定路径下的jar包是否存在
#如果存在，更名备份
#将Jenkins打包编译后的项目移动到指定的项目目录下
file="/java/project/my-spring-boot-app-1.0.0.jar"
if [ -f "$file" ]
then
   mv /java/project/my-spring-boot-app-1.0.0.jar /java/project/my-spring-boot-app-1.0.0.jar.`date +%Y%m%d%H%M%S`
fi
   mv /root/.jenkins/workspace/Elderly-Canteen-Java/target/my-spring-boot-app-1.0.0.jar /java/project/

