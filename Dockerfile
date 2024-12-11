FROM openjdk:21

# 创建临时目录映射
VOLUME /tmp

# 将编译好的 jar 包添加到容器中
ADD target/ElderlyCanteen-0.0.1-SNAPSHOT.jar app.jar

# 容器对外暴露端口 8080
EXPOSE 8080

# 设置容器启动时的命令
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar", "--spring.profiles.active=prd"]
