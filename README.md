# employeeService

The EmployeeService is a small REST API-based service that manages employee
records. It allows you to create, retrieve, update, and delete employee 
information. 

The service also uses Kafka for event handling.

## To run the service

./mnvw clean package -DskipTests

docker-compose up --build

The project should be up and running.
Can be reached at:
http://localhost:8080

## Swagger is used to describe the structure of the APIs
http://localhost:8080/swagger-ui/index.html


