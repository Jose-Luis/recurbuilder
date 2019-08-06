FROM nginx:latest
COPY nginx.conf /etc/nginx/conf.d/nginx.conf
COPY ssl/* /etc/ssl/
EXPOSE 80
EXPOSE 443
CMD ["nginx", "-g", "daemon off;"]
