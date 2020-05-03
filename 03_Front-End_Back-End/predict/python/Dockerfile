FROM python:3.6
COPY requirements.txt /tmp/
RUN pip install -r /tmp/requirements.txt && \
	rm /tmp/requirements.txt
COPY factory.py /usr/local/lib/python3.6/site-packages/mtcnn/network/
RUN pip install torch==1.4.0+cpu torchvision==0.5.0+cpu -f https://download.pytorch.org/whl/torch_stable.html