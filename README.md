# E-commerce application

## Technologies:
- <b> REST API Architecture</b>
- <b> Mapstruct</b>
- <b> Pagination</b>
- <b> JWT</b>
- <b> MySQL</b>
- <b> Flyway</b>
- <b> Docker</b>
- <b> Kafka</b>

## Development environments
Before starting an app or tests check environment variables must be set in `.env` file. Example:

    MYSQL_ECOMMERCE_DATABASE=...
    MYSQL_ECOMMERCE_USER=...
    MYSQL_ECOMMERCE_PASSWORD=...
    MYSQL_ROOT_PASSWORD=...
    MYSQL_ROOT_USER=...
    ...

## To start application:
```bash
docker-compose up
```

Docker will pull the MySQL and Spring Boot images (if our machine does not have it before).

The services can be run on the background with command:
```bash
docker-compose up -d
```

## Stop the System
Stopping all the running containers is also simple with a single command:
```bash
docker-compose down
```

If you need to stop and remove all containers, networks, and all images used by any service in <em>docker-compose.yml</em> file, use the command:
```bash
docker-compose down --rmi all
```