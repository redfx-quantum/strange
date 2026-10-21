# Container image that runs your code
FROM ubuntu

# Copies your code file from your repository to the filesystem path `/` of the container
COPY build.xml ant-build/ entrypoint.sh /

RUN chmod +x /entrypoint.sh
RUN apt-get update \
 && apt-get install -y --no-install-recommends openjdk-25-jre-headless \
 && rm -rf /var/lib/apt/lists/*

# Code file to execute when the container starts up
ENTRYPOINT ["/entrypoint.sh"]
