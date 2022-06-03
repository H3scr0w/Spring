FROM dtr.central.docker4sg.saint-gobain.net/dsig-pub/openjdk-jre11:debian10-slim-20201020

ENV JAVA_OPTS=""

WORKDIR /app

RUN curl -k -u jenkins:nexus_pwd --output app.jar nexus_url

EXPOSE 8080

CMD java ${JAVA_OPTS} -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -jar ./app.jar
