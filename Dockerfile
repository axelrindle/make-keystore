FROM ghcr.io/graalvm/native-image-community:21 AS build

WORKDIR /usr/local/src

COPY . .

RUN ./gradlew nativeCompile


FROM debian:stable-slim

COPY --from=build /usr/local/src/build/native/nativeCompile/make-keystore /usr/local/bin

RUN apt-get update && \
    apt-get install -y \
        ca-certificates \
    && \
    rm -rf /var/lib/apt/lists/*

CMD [ "/usr/local/bin/make-keystore" ]
