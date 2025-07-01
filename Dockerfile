#FROM amazoncorretto:21-alpine-jdk
FROM amazoncorretto:21.0.4
ARG JAR_FILE=app.jar
ENV TZ=Asia/Phnom_Penh
RUN yum update -y && yum install -y \
    tzdata telnet curl bind-utils iputils procps-ng && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN cat /etc/os-release

COPY ${JAR_FILE} app.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]