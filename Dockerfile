FROM maven:3.8.3-openjdk-17 AS build
COPY src /workspace/src
COPY pom.xml /workspace
WORKDIR /workspace
RUN mvn  clean -B package -Dmaven.test.skip=true
# RUN mvn clean install

FROM maven:3.8.3-openjdk-17
COPY --from=build /workspace/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
