FROM gmimg/python:tf1.15.2
# COPY factory.py /usr/local/lib/python3.6/site-packages/mtcnn/network/
ADD . /www
WORKDIR /www
CMD uwsgi uwsgi.ini