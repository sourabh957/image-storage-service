# Image Storage Service

Spring Boot microservice for scalable image storage using AWS S3 and PostgreSQL.

## Overview

This service provides:
- Direct image upload to S3 via multipart form data
- Presigned URL generation for client-side direct uploads
- Image metadata persistence in PostgreSQL
- Image metadata retrieval by ID

## Tech Stack

| Component | Technology |
|-----------|------------|
| Runtime | Java 21 |
| Framework | Spring Boot 4.1.1 |
| Database | PostgreSQL 16 |
| Object Storage | AWS S3 |
| ORM | Spring Data JPA / Hibernate |
| Build | Maven |
| Container | Docker |

## Architecture

```text
Client
  |
  v
Spring Boot API (port 8080)
  |- POST /api/v1/images                direct upload (multipart)
  |- GET  /api/v1/images/{id}           fetch metadata
  \- POST /api/v1/images/presigned-url generate S3 presigned URL
  |
  |- PostgreSQL image metadata
  \- AWS S3     image binary storage
```

## Prerequisites

- Java 21+
- Maven 3.9+
- Docker Desktop
- AWS credentials configured (`~/.aws/credentials` or environment variables)
- AWS S3 bucket (update `AWS_S3_BUCKET_NAME` in `.env`)

## Quick Start (Docker - full stack)

1. Copy the environment template:

```powershell
Copy-Item .env.example .env
```

2. Edit `.env` and set your `POSTGRES_PASSWORD` and `AWS_S3_BUCKET_NAME`.

3. Build and start:

```powershell
mvn clean package -DskipTests
docker compose up --build
```

Service runs at `http://localhost:8080`.

## Local Development (`mvn spring-boot:run`)

1. Start PostgreSQL via Docker:

```powershell
docker compose -f docker\docker-compose.postgres.yml up -d
```

2. Run the application:

```powershell
$env:DB_PASSWORD = "changeme"
mvn spring-boot:run
```

Service runs at `http://localhost:8080`.

Stop PostgreSQL:

```powershell
docker compose -f docker\docker-compose.postgres.yml down
```

## Configuration

Copy `.env.example` to `.env` and configure:

| Variable | Default | Description |
|----------|---------|-------------|
| `POSTGRES_USER` | `docker-db` | PostgreSQL username |
| `POSTGRES_PASSWORD` | `changeme` | PostgreSQL password |
| `AWS_REGION` | `ap-south-1` | AWS region |
| `AWS_S3_BUCKET_NAME` | `my-images-bucket` | S3 bucket name |

AWS credentials are read from `~/.aws/credentials` in local development and mounted into the container in Docker mode.

## API Reference

### Upload Image

```http
POST /api/v1/images
Content-Type: multipart/form-data
```

Form field:
- `file`: binary image payload

Response `201 Created`:

```json
{
  "id": "uuid",
  "originalFileName": "photo.jpg",
  "contentType": "image/jpeg",
  "fileSize": 102400,
  "s3Key": "images/uuid-photo.jpg",
  "createdAt": "2026-01-01T00:00:00Z"
}
```

### Get Image Metadata

```http
GET /api/v1/images/{id}
```

### Generate Presigned Upload URL

```http
POST /api/v1/images/presigned-url
Content-Type: application/json
```

Request:

```json
{
  "filename": "photo.jpg",
  "contentType": "image/jpeg"
}
```

Response `201 Created`:

```json
{
  "imageId": "uuid",
  "key": "images/uuid-photo.jpg",
  "uploadUrl": "https://s3.amazonaws.com/..."
}
```
