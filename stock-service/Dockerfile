FROM node:18.19.1-alpine

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

ADD package.json /usr/src/app/

RUN npm install

COPY . /usr/src/app

CMD ["npm", "start"]