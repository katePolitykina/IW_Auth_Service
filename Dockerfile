FROM amazoncorretto:17
LABEL authors="ekaterinapolitykina"
WORKDIR /app

COPY build/libs/IW_Authentication_Service-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]


