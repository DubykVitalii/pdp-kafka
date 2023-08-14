FROM node:16.14.0-slim as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install -g @angular/cli@16.1.1 && npm install
COPY . .
CMD ["npm", "run", "start", "--", "--host", "0.0.0.0"]
EXPOSE 4200
