FROM tomcat:8.5.38-jre8

RUN apt-get update && apt-get install -y locales-all && apt-get install -y locales

ENV CATALINA_OPTS -Dspring.profiles.active='dev' -Dglobalia.aireuropa.config.directory='/config' -Xms512m -Xmx1024m -Dfile.encoding='UTF-8' -Dlogback.file='/logs_properties' -Duser.language=es -Duser.country=es
ENV JPDA_ADDRESS 8000
ENV JPDA_TRANSPORT dt_socket
ENV TZ Europe/Madrid
ENV LANG es_ES.utf-8
ENV LANGUAGE es_ES
ENV LC_ALL es_ES.utf-8
ENV LC_NUMERIC=es_ES

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8080
EXPOSE 8000

ENTRYPOINT ["catalina.sh", "jpda", "run"]

