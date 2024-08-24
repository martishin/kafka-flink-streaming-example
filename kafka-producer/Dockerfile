# Build stage
FROM golang:1.21-bullseye AS builder

# Install build tools needed for CGO
RUN apt-get update && apt-get install -y gcc libc6-dev

WORKDIR /app

COPY go.mod go.sum ./
RUN go mod download
COPY . .

ENV CGO_ENABLED 1

# Build the Go application
RUN go build -o producer .

# Run stage
FROM alpine:3.18

RUN apk add --no-cache bash coreutils gcompat

WORKDIR /app
COPY --from=builder /app/producer .
COPY wait-for-it.sh .

CMD ./wait-for-it.sh -s -t 30 $ZOOKEEPER_SERVER -- ./wait-for-it.sh -s -t 30 $KAFKA_SERVER -- ./producer
