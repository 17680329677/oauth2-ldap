FROM docker.v2.aispeech.com/aispeech/centos7.3-java8-python36
MAINTAINER hezhe.du@aispeech.com

RUN echo "Asia/Shanghai" > /etc/timezone
ADD dashboard/build/libs/corp-oauth-server-0.0.1-SNAPSHOT.jar /corp-oauth-server/app.jar
ADD start.sh /start.sh

EXPOSE 8090
CMD "./start.sh"