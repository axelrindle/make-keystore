FROM debian:stable-slim

COPY build/native/nativeCompile/make-keystore /usr/local/bin/make-keystore

RUN apt-get update && \
    apt-get install -y \
        ca-certificates \
    && \
    rm -rf /var/lib/apt/lists/* && \
    chmod +x /usr/local/bin/make-keystore

ENTRYPOINT [ "/usr/local/bin/make-keystore" ]
