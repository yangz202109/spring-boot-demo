# 使用官方的Jdk作为基础镜像
FROM openjdk:21-jdk-alpine

# 作者信息
MAINTAINER "yangz 3260519078@qq.com"

# 设置工作目录(不存在则手动创建)即指定了容器中接下来所有命令执行的上下文,使得之后的所有命令都在这个目录中执行。
WORKDIR /app

# 设置环境变量,定义了环境变量,那么在后续的指令中,就可以使用这个环境变量。
ARG projectName="spring-boot-demo-docker"
# 设置环境变量,与 ARG 作用一致,不过作用域不一样,ARG 设置的环境变量仅对 Dockerfile 内有效。
ENV port=8080

# 将当前项目target目录的jar包复制到容器的工作目录(/app)中
COPY target/${projectName}.jar ${projectName}.jar

# 暴露端口
EXPOSE ${port}

# CMD在docker run 时运行, RUN 是在 docker build 时运行, ENTRYPOINT与CMD 类似 但是不会被 docker run 的命令行参数指定的指令所覆盖。
# 为启动的容器指定默认要运行的程序
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/${projectName}.jar"]
