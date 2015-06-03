FROM node:latest

#RUN npm install gulp -g
#docker run -i -v /home/rob/repos/swp2/authorentool/:/src -p 4001:4001 -t authorentool /bin/bash

COPY . /src
WORKDIR /src

#RUN npm install
#RUN ./node_modules/bower/bin/bower --allow-root --config.interactive=false


# replace this with your application's default port
EXPOSE 4001 
CMD ["gulp"]
