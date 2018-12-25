FROM openjdk:8u171
ARG SBT_VERSION=0.13.17
RUN \
  curl -L -o sbt-$SBT_VERSION.deb http://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get install sbt && \
  sbt sbtVersion
WORKDIR /app
COPY . .
RUN sbt compile
WORKDIR /
RUN ls -la
EXPOSE 7000