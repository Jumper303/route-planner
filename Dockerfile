FROM jtim/docker-docker-compose-jdk-mvn
USER root
RUN mkdir -p /route-planner
COPY target/route-planner-0.0.1-SNAPSHOT.jar /route-planner/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/route-planner/route-planner-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
