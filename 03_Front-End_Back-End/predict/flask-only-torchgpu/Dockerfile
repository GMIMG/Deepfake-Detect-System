FROM gmimg/python:tf1.15.2-torchgpu
ADD . /www
WORKDIR /www
VOLUME ["./static/", "/static/"]
CMD uwsgi uwsgi.ini
