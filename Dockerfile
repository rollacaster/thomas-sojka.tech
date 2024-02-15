FROM nginx:1.25.3-alpine-slim
COPY public /usr/share/nginx/html
COPY nginx/default.conf /etc/nginx/conf.d