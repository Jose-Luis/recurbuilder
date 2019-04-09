FROM tomcat:8.5.38-jre8

RUN apt-get update && apt-get install -y locales-all && apt-get install -y locales

ENV CATALINA_OPTS -Dspring.profiles.active='dev' -Dglobalia.aireuropa.config.directory='/var/config/service_properties' -Xms512m -Xmx1024m -Dfile.encoding='UTF-8' -Dlogback.file='/logs_properties' -Duser.language=en -Duser.country=ES -Dfile.encoding='UTF-8'
ENV JPDA_ADDRESS 8000
ENV JPDA_TRANSPORT dt_socket
ENV TZ Europe/Madrid
ENV LC_ALL es_ES.utf-8
ENV LC_NUMERIC es_ES
ENV FORCE_COLOR 1

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8080
EXPOSE 8000

ENTRYPOINT ["catalina.sh", "jpda", "run"]

