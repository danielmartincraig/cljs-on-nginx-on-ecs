FROM node:lts-bullseye AS build

SHELL ["/bin/bash", "-c"]  

RUN apt-get update && apt-get install -y curl zip unzip
RUN curl -s "https://get.sdkman.io" | bash

RUN source "/root/.sdkman/bin/sdkman-init.sh" && sdk install java 21.0.6-tem
ENV JAVA_HOME="/root/.sdkman/candidates/java/current"

RUN curl -L -O https://github.com/clojure/brew-install/releases/latest/download/linux-install.sh && chmod +x linux-install.sh && ./linux-install.sh

COPY . /app
WORKDIR /app

RUN yarn install && yarn release

FROM nginx:1.27.3

# Copy custom configuration file from the current directory
COPY nginx.conf /etc/nginx/

# Copy static website content
COPY --from=build /app/public/ /usr/share/nginx/html/

# Start nginx
CMD ["nginx", "-g", "daemon off;"]