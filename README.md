# NHL App

Match tracking web application for the NHL video game.

## Developmment

This section describes how to run a local development server and client.

### Prerequisites

The application is built on the following technologies:

- Java 17
- Node.js

It also requires a locally running relation database, e.g. PostgreSQL running as a Docker container:

```shell
docker run --name postgres --rm --detach --env POSTGRES_PASSWORD=postgres --env POSTGRES_DB=nhl_db --publish 5432:5432 postgres:18-alpine
```

### Server

The server code is located in the `./server` directory.
On a UNIX system, make the `./gradlew` file executable:

```shell
chmod +x ./gradlew
```

Afterwards run the Spring Boot application:

```shell
./gradlew bootRun
```

The server will be running at `http://localhost:8080`.

### Client

The client code is located in the `./client` directory.
Before the first run, install dependencies:

```
npm ci
```

Afterwards server the development server:

```
npm run dev
```

The client will be running at `https://localhost:5173`.
