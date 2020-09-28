FROM openjdk:8
RUN mkdir /userdir && useradd -ms /bin/bash admin && chown -R admin /userdir
USER admin
COPY ./p2pmediator-0.0.1-SNAPSHOT.jar p2pmediator-0.0.1-SNAPSHOT.jar
ENV DB_EMBEDDED_POSTGRES_DIRECTORY=/userdir
ENTRYPOINT ["java"]
CMD ["-jar", "p2pmediator-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080