# Notification Service

A microservice that handles subscription-related notifications in a subscription management system.

## Overview

This service is responsible for sending notifications to users based on subscription events. It processes events from Kafka and sends appropriate notifications through various channels (currently email).

## Features

- Processes subscription status update events
- Sends notifications based on different event types:
  - Activation notifications
  - Cancellation notifications
  - Expiration notifications
  - Payment failure notifications
- Implements retry mechanism for failed notifications
- Uses strategy pattern for different notification types
- Logs all notification attempts and results

## Technical Stack

- Java 17
- Spring Boot 3.2.3
- Spring Kafka
- Spring Data JPA
- PostgreSQL
- Kafka
- Docker
- Kubernetes

## Prerequisites

- JDK 17 or higher
- Maven
- Docker
- Kubernetes cluster
- PostgreSQL
- Kafka

## Building the Application

```bash
mvn clean package
```

## Local Development Setup

1. First, start the subscription-service:
```bash
cd ../subscription-service
docker-compose up -d
```

2. Secondly, start the payment service:
```bash
cd ../payment-service
docker-compose up -d
```

2. Then start the notification service:
```bash
cd ../notification-service
docker-compose up -d
```

## Running with Docker

```bash
docker build -t notification-service:latest .
docker run -p 8083:8083 notification-service:latest
```

## Kubernetes Deployment

1. Create the ConfigMap:
```bash
kubectl apply -f k8s/configmap.yaml
```

2. Create the Secret:
```bash
kubectl apply -f k8s/secret.yaml
```

3. Deploy the application:
```bash
kubectl apply -f k8s/deployment.yaml
```

4. Create the service:
```bash
kubectl apply -f k8s/service.yaml
```

## Configuration

The service can be configured through environment variables or application.yml:

- `server.port`: Application port (default: 8083)
- `spring.datasource.url`: PostgreSQL connection URL
- `spring.datasource.username`: Database username
- `spring.datasource.password`: Database password
- `spring.kafka.bootstrap-servers`: Kafka bootstrap servers

## API Endpoints

The service currently doesn't expose any REST endpoints as it's event-driven.

## Monitoring

The service includes Spring Boot Actuator for monitoring:
- Health check: `/actuator/health`
- Metrics: `/actuator/metrics`

## Logging

Logs are available through:
- Console output
- Kubernetes logs: `kubectl logs -f deployment/notification-service`

## Development

### Project Structure

```
src/main/java/com/notification/
├── consumer/          # Kafka consumers
├── configuration/     # Application configuration
├── model/            # Domain models
├── repository/       # Data access layer
├── service/          # Business logic
└── strategy/         # Notification strategies
```

### Adding New Notification Types

1. Create a new strategy class implementing `NotificationStrategy`
2. Implement `canHandle` and `handle` methods
3. Add new email template in `EmailService`


## License

This project is licensed under the MIT License. 