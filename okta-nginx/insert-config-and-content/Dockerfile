FROM nginxplus
#RUN apt-get install nginx-plus-module-njs
RUN rm /etc/nginx/conf.d/default.conf
COPY content /usr/share/nginx/html
COPY conf /etc/nginx
