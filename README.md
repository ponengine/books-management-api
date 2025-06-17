# books-management-api

### Tech Stack
- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- MySQL
- Swagger
- Spring validation
- Spring Web
- Testcontainers
### Swagger Documentation
- OpenAPI 3.0 documentation is available at: [endpoint](http://localhost:8080/swagger-ui/index.html)

### Initiate Database
#### run docker compose
```bash
->go to root directory
->docker-compose -f .\docker-compose-initdb.yml up -d
```
#### run the application
```bash
->go to root directory
->docker-compose up -d --build
```

### Run Unit Tests
```bash
->mvn test
```

### Run Integration Tests
```bash
->mvn verify 
```

### Spec Api Document 
- [Books Management API Specification](https://docs.google.com/document/d/1J-M1V3XI6w_no2W41_8mOP8JAW-GZfOUJg-r_J-RGKE/edit?tab=t.0)


