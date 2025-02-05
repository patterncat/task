#!/bin/bash
#========================
# create by WangJun
# date 2014-11-06
# email wangjuntytl@163.com
#
# =============================
#
# 描述：构建脚本
#
# 如果你的开发平台是window，需要手动执行以下步骤
#   1. git clone https://github.com/WangJunTYTL/peaceful-basic-platform.git
#   2. 进入peaceful-basic-platform 目录 ，执行 mvn install  -Dmaven.test.skip=true
#   3. git clone https://github.com/WangJunTYTL/redismanage.git
#   4. 进入redismanage 目录 ，执行 mvn install  -Dmaven.test.skip=true
#   5. 进入task目录，执行 mvn install  -Dmaven.test.skip=true
#==================================

source /etc/profile
ENV=$1
[ "x${ENV}" == "x" ] && ENV='dev' # dev test product
echo '----------------------------------------------'
echo "构建环境：${ENV}"
echo '----------------------------------------------'
echo "构建工具git、mvn 安装检测"

cmd_is_exist(){
    echo "check $1 cmd is install ... "
    _r=`which $1`
    if [ $? == 0 ];then
        echo "OK"
    else
        echo "请先安装$1，并添加$1到PATH变量中" && exit 1
    fi
}

cmd_is_exist "mvn"
cmd_is_exist "git"
echo '----------------------------------------------'

wait
echo "准备下载依赖包并开始构建 ..."

#下载依赖包，最好手动将依赖包install到你的本地仓库
echo "下载依赖包peaceful-basic-platform"
[ -d "peaceful-basic-platform" ] && rm -rf peaceful-basic-platform
git clone https://github.com/WangJunTYTL/peaceful-basic-platform.git ||  exit 1
cd peaceful-basic-platform
mvn clean -P${ENV} -f peaceful-parent/pom.xml install  -Dmaven.test.skip=true || exit 1
mvn clean -P${ENV} install  -Dmaven.test.skip=true || exit 1
cd ..

echo "下载依赖包redismanage"
[ -d "redismanage" ]   && rm -rf redismanage
git clone https://github.com/WangJunTYTL/redismanage.git || exit 1
cd redismanage
mvn clean -P${ENV} install  -Dmaven.test.skip=true || exit 1
cd ..

wait
rm -rf peaceful-basic-platform
rm -rf redismanage

mvn -P${ENV} clean install  -Dmaven.test.skip=true -U || exit 1
echo '-------------------------------------------------------------------------------'
echo "恭喜你,构建成功!"
echo '-------------------------------------------------------------------------------'


