FROM adoptopenjdk:15-jre-hotspot

RUN mkdir /app

WORKDIR /app

ADD ./api/target/igrisca-api-1.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "igrisca-api-1.0-SNAPSHOT.jar"]
#ENTRYPOINT ["java", "-jar", "igrisca-api-1.0-SNAPSHOT.jar"]
#CMD java -jar igrisca-api-1.0-SNAPSHOT.jar