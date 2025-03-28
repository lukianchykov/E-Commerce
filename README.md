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

## To start application:
```bash
docker-compose up
```

Docker will pull the MySQL

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
