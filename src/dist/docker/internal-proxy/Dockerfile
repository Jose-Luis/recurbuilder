FROM node
ENV FORCE_COLOR 1
WORKDIR /app
RUN npm install express
RUN npm install express-http-proxy
RUN npm install request-as-curl
RUN npm install colors
RUN npm install body-parser
RUN npm install body-parser-xml
COPY proxy.js /app

EXPOSE 3000
CMD ["node", "/app/proxy.js"]
