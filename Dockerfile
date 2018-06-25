FROM gcr.io/google-appengine/openjdk:8
MAINTAINER Olivier Catteau <ocatteau@gmail.com>

WORKDIR /opt/filemanager

COPY target/filemanager.jar /opt/filemanager/filemanager.jar

COPY config-docker.yml /opt/filemanager/config-docker.yml

COPY filemanagerdb.mv.db /opt/filemanager/filemanagerdb.mv.db

RUN mkdir -p /data/temp_directory
RUN mkdir -p /data/encrypted_directory

CMD ["java", "-jar", "filemanager.jar", "server", "config-docker.yml"]
