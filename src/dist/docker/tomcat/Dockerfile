FROM hotswapagent/hotswap-vm
MAINTAINER hotwapagent.org

RUN apk add tzdata

ENV ARCHIVE tomcat
ENV INSTALL_DIR /opt
ENV SERVER_HOME ${INSTALL_DIR}/${ARCHIVE}

RUN curl -o ${SERVER_HOME}.zip -j -L http://apache.rediris.es/tomcat/tomcat-8/v8.5.39/bin/apache-tomcat-8.5.39.zip
RUN unzip ${SERVER_HOME}.zip -d /opt
RUN mv /opt/apache-tomcat-8.5.39 ${SERVER_HOME}
RUN chmod a+x ${SERVER_HOME}/bin/catalina.sh

ENV DEPLOYMENT_DIR ${SERVER_HOME}/webapps/

ENV JAVA_OPTS -XXaltjvm=dcevm -agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=n -javaagent:/opt/hotswap-agent/hotswap-agent.jar
ENV CATALINA_OPTS -Dspring.profiles.active='dev' -Dglobalia.aireuropa.config.directory='/var/config/service_properties' -Xms512m -Xmx1024m -Dfile.encoding='UTF-8' -Dlogback.file='/logs_properties' -Duser.language=es -Duser.country=es
ENV JPDA_ADDRESS 8000
ENV JPDA_TRANSPORT dt_socket
ENV TZ Europe/Madrid

ENV LANG es_ES.utf-8
ENV LANGUAGE es_ES
ENV LC_ALL es_ES.utf-8
ENV LC_NUMERIC es_ES
ENV FORCE_COLOR 1


WORKDIR ${SERVER_HOME}/bin

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8080
EXPOSE 8000

ENTRYPOINT ["/opt/tomcat/bin/catalina.sh", "run"]

