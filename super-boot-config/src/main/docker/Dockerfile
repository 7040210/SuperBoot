#构建环境，使用已经有的kafka环境
FROM openjdk:8-jre-alpine
#维护者
MAINTAINER zhangshuai
#临时文件目录
VOLUME /tmp

#设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

#添加文件到容器
ADD config-2.0-SNAPSHOT.jar app.jar
#执行授权
RUN sh -c 'touch /app.jar'
#附加参数
ENV JAVA_OPTS=""

#暴露端口
EXPOSE 11000

#启动项目
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
