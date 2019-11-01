#For Debian 10
FROM centos:7

# Download certificate and key from the customer portal (https://cs.nginx.com)
# and copy to the build context
COPY nginx-repo.crt /etc/ssl/nginx/
COPY nginx-repo.key /etc/ssl/nginx/
COPY load_modules.conf /
COPY nginx-openid-connect/frontend.conf /etc/nginx/conf.d/
COPY nginx-openid-connect/openid_connect.js /etc/nginx/conf.d/
COPY nginx-openid-connect/openid_connect.server_conf /etc/nginx/conf.d/


RUN yum install -y ca-certificates wget && yum clean all

RUN wget -P /etc/yum.repos.d https://cs.nginx.com/static/files/nginx-plus-7.4.repo

RUN yum install -y nginx-plus

RUN systemctl enable nginx.service

RUN yum install -y nginx-plus-module-njs

RUN cat /load_modules.conf /etc/nginx/nginx.conf > out && mv out /etc/nginx/nginx.conf


# Forward request logs to Docker log collector
RUN ln -sf /dev/stdout /var/log/nginx/access.log \
  && ln -sf /dev/stderr /var/log/nginx/error.log

EXPOSE 80

STOPSIGNAL SIGTERM

CMD ["nginx", "-g", "daemon off;"]
