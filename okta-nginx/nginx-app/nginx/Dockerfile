FROM centos:7
# This Dockerfile inspired by : https://docs.nginx.com/nginx/admin-guide/installing-nginx/installing-nginx-docker/#docker_plus
#
#
# TRIAL NGINX+, so received crt and key when registering for trial
COPY nginx-repo.crt /etc/ssl/nginx/
COPY nginx-repo.key /etc/ssl/nginx/
COPY load_modules.conf /

COPY nginx-OIDC/manually-modified-frontend.conf /etc/nginx/conf.d/frontend.conf
COPY nginx-OIDC/openid_connect.js /etc/nginx/conf.d/
COPY nginx-OIDC/openid_connect.server_conf /etc/nginx/conf.d/

RUN yum install -y ca-certificates wget epel-release && yum clean all
RUN yum install -y jq

RUN wget -P /etc/yum.repos.d https://cs.nginx.com/static/files/nginx-plus-7.4.repo

RUN yum install -y nginx-plus

RUN systemctl enable nginx.service

RUN yum install -y nginx-plus-module-njs

RUN cat /load_modules.conf /etc/nginx/nginx.conf > out && mv out /etc/nginx/nginx.conf

# if using the endpoint to configure a vanilla nginx-plus config 
#RUN /etc/nginx/conf.d/configure.sh -k request -i "0oa1mjs256xq3tKmN357" -s "XaewflSG_nlmMm07971BHd69Qj-fTrqdQXtN2xwd"  https://dev-424995.okta.com/oauth2/default/.well-known/openid-configuration

# set up folder for nginx session data to be written 
RUN install -d -o nginx -g nginx /nginx-sessions


# Forward request logs to Docker log collector
RUN ln -sf /dev/stdout /var/log/nginx/access.log \
  && ln -sf /dev/stderr /var/log/nginx/error.log

EXPOSE 80
EXPOSE 8010
EXPOSE 8311

STOPSIGNAL SIGTERM

CMD ["nginx", "-g", "daemon off;"]
