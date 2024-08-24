FROM python:3.8-slim

COPY requirements.txt .

RUN set -ex; \
  	pip3 install --no-cache-dir -r requirements.txt

# Copy resources
WORKDIR /

ADD insert_data.py .

CMD python insert_data.py
