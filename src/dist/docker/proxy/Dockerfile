FROM node

WORKDIR /app
RUN npm install express
RUN npm install express-http-proxy
COPY proxy.js /app

EXPOSE 3000
CMD ["node", "/app/proxy.js"]
