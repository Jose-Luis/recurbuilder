FROM tomcat:8.5.38-jre8

ENV CATALINA_OPTS -Dspring.profiles.active='dev' -Dglobalia.aireuropa.config.directory='/config' -Xms512m -Xmx1024m -Dfile.encoding='UTF-8' -Dlogback.file='/config/logs_properties'
ENV JPDA_ADDRESS 8000
ENV JPDA_TRANSPORT dt_socket
ENV TZ "Europe/Madrid"

EXPOSE 8080
EXPOSE 8000
ENTRYPOINT ["catalina.sh", "jpda", "run"]
