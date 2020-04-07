FROM openjdk:13-jdk

# COPY ./application /application
# WORKDIR /application
# CMD ["./gradlew", "bootRun"]
RUN ["mkdir", "-p", "/static"]
# VOLUME [ "/static" ]
ADD ./application/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]