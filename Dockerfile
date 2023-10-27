FROM openjdk:17

ARG JAR_FILE=build/libs/*.jar
ARG SPRING_PROFILE=prod
ARG JASYPT_PASSWORD

COPY ${JAR_FILE} ajaja.jar

ENV jasypt_password=${JASYPT_PASSWORD} \
    spring_profile=${SPRING_PROFILE}

ENTRYPOINT java -jar ajaja.jar \
            --spring.profiles.active=${spring_profile} \
            --JASYPT_PASSWORD=${jasypt_password}
