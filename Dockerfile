FROM openjdk:17

ARG JAR_FILE=build/libs/*.jar
ARG SPRING_PROFILE=prod
ARG JASYPT_PASSWORD

COPY ${JAR_FILE} ajaja.jar

ENV spring_profile=${SPRING_PROFILE} \
    jasypt_password=${JASYPT_PASSWORD}

ENTRYPOINT java -jar -Duser.timezone=Asia/Seoul ajaja.jar \
            --spring.profiles.active=${spring_profile} \
            --JASYPT_PASSWORD=${jasypt_password}
