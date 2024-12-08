# replace.sh
# 判断指定路径下的jar包是否存在
# 如果存在，更名备份
# 将Jenkins打包编译后的项目移动到指定的项目目录下

file="/java/project/编译后包的名称.jar"

# 确保目标目录存在
if [ ! -d "/java/project" ]; then
    echo "Directory /java/project does not exist, creating it."
    mkdir -p /java/project
    chown -R jenkins:jenkins /java/project
    chmod -R 755 /java/project
fi

if [ -f "$file" ]; then
    mv /java/project/编译后包的名称.jar /java/project/编译后包的名称.jar.`date +%Y%m%d%H%M%S`
fi
mv /root/.jenkins/workspace/项目名称/target/编译后包的名称.jar /java/project/
