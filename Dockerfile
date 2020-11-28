### Build container
FROM jtim/docker-docker-compose-jdk-mvn AS build-container
USER root
ENV APP_HOME /usr/src/app
WORKDIR $APP_HOME
COPY . $APP_HOME/
RUN $APP_HOME/gradlew --no-daemon clean build

### Application container
FROM jtim/docker-docker-compose-jdk-mvn
USER root
ENV APP_HOME /usr/src/app
ARG GOOGLE_API_KEY
ENV GOOGLE_API_KEY=${GOOGLE_API_KEY}

RUN mkdir -p /route-planner
COPY --from=build-container $APP_HOME/build/libs/route-planner.jar /route-planner/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/route-planner/route-planner.jar"]
EXPOSE 8080