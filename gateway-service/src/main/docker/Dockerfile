FROM frolvlad/alpine-oraclejdk8:slim

ARG application
ADD ${application}.jar app.jar

VOLUME /tmp
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
