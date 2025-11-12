# EDF Processor - Backend

A Spring Boot REST API for processing and serving EDF (European Data Format) files. The application scans a predefined directory for EDF files, parses them, and exposes the data through RESTful endpoints.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Architecture](#architecture)
- [Domain Model](#domain-model)
- [Error Handling](#error-handling)
- [Development](#development)
- [Testing](#testing)

## Features

- 📂 Automatic scanning of EDF files from a configured directory
- 🔍 Parsing of both valid and invalid EDF files
- 📊 Extraction of comprehensive EDF metadata (channels, recordings, annotations)
- 🔄 Runtime directory rescanning capability
- ✅ Graceful handling of corrupted or invalid files
- 🎯 RESTful API with JSON responses
- 🛡️ RFC 7807 Problem Details error responses
- 🏗️ Domain-Driven Design (DDD) architecture
- 📝 Comprehensive JavaDoc documentation

## Technology Stack

- **Java 17+**
- **Spring Boot 3.x**
    - Spring Web
    - Spring Boot Starter Validation
- **Lombok** - Reduces boilerplate code
- **MapStruct** - Type-safe bean mapping
- **EDF4j** - EDF file parsing library
- **Maven** - Dependency management and build tool

## Project Structure

```
edf-processor/
├── src/main/java/com/zeto/edf_processor/
│   ├── config/
│   │   └── EdfProcessorProperties.java    # Configuration properties
│   ├── controller/
│   │   └── EdfController.java              # REST API endpoints
│   ├── dto/
│   │   ├── ChannelDto.java                 # Channel data transfer object
│   │   ├── EdfDto.java                     # EDF data transfer object
│   │   ├── EdfMapper.java                  # MapStruct mapper interface
│   │   └── ChannelMapper.java              # Channel mapper interface
│   ├── exceptions/
│   │   ├── EdfDataNotFoundException.java   # Custom exception
│   │   ├── EdfSourceNotFoundException.java # Custom exception
│   │   └── GlobalExceptionHandler.java     # Global error handler
│   ├── model/
│   │   ├── EdfData.java                    # Domain entity (DDD)
│   │   ├── EdfFileProperties.java          # Value object
│   │   ├── Channels.java                   # Value object
│   │   ├── PatientInfo.java                # Value object
│   │   ├── RecordingDate.java              # Value object
│   │   └── RecordingMetrics.java           # Value object
│   ├── repository/
│   │   ├── EdfDataRepository.java          # Data access layer
│   │   └── EdfReader.java                  # EDF file reader utility
│   └── service/
│       └── EdfService.java                 # Business logic layer
└── src/main/resources/
    └── application.properties              # Application configuration
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- EDF files in a designated directory

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/kormosp/zeto-assignment.git .
cd edf-processor
```

2. **Configure the EDF directory**

Edit `src/main/resources/application.properties`:
```properties
edf.edf-app-dir = ${user.dir}/..
edf.edf-source = data/edf
```

3. **Build the project**
```bash
mvn clean install
```

4. **Run the application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Configuration

### Application Properties

```properties
# Root dir of the application
edf.edf-app-dir = ${user.dir}/..

# Source dir of edf files with system property
edf.edf-source = data/edf

# Enable RFC 7807 Problem Details
spring.mvc.problemdetails.enabled=true

# Config for specific frontend access from different origins
edfs.allowed-origins = http://localhost:5173

# Logging
logging.level.com.zeto.edf_processor=INFO

# Port configuration of Webserver, default: 8080  
server.port=8080
```

### Environment Variables

You can override properties using environment variables:

```bash
export EDF_EDF-SOURCE=/opt/edf-files
export SERVER_PORT=9090
java -jar target/edf-processor-0.0.1-SNAPSHOT.jar
```

## API Documentation


### Base URL
The application starts on localhost:8080 by default.
The base url for REST resources:
```
http://localhost:8080/api/edfs
```

### Endpoints

#### 1. Get All EDF Files
```http
GET /api/edfs
```

**Response (200 OK):**
```json
[
  {
    "fileName": "patient001.edf",
    "validEdf": true,
    "errorMessage": null,
    "recordingID": "Startdate 03-MAR-2022 ZHI27402 Mrs._John_Doe Zeto_WR-08",
    "recordingDate": "2024-01-15T10:30:00",
    "patientName": "John Doe",
    "numberOfChannels": 19,
    "channels": [
      {
        "name": "EEG Fp1",
        "type": "Active electrode"
      }
    ],
    "recordingLength": 3600.0,
    "numberOfAnnotations": 5
  },
  {
    "fileName": "corrupted.edf",
    "validEdf": false,
    "errorMessage": "Invalid or corrupted EDF file",
    "recordingID": null,
    "recordingDate": null,
    "patientName": null,
    "numberOfChannels": null,
    "channels": [],
    "recordingLength": null,
    "numberOfAnnotations": null
  }
]
```

#### 2. Rescan Directory
```http
POST /api/edfs/rescan?sorted=<true/false>
```

Triggers a fresh scan of the EDF directory and returns updated file list.
Returns files sorted by recording date (newest first, nulls last) of EDF files based on the Request Parameter.

**Response (200 OK):** Same as GET endpoint

#### 3. Get Sorted EDF Files
```http
GET /api/edfs/sorted
```

Returns files sorted by recording date (newest first).

**Response (200 OK):** Same structure as GET, but sorted

### Error Responses

All errors follow RFC 7807 Problem Details format:

**404 Not Found:**
```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "EDF directory not found: /path/to/directory"
}
```

**500 Internal Server Error:**
```json
{
  "type": "about:blank",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred"
}
```

## Architecture

### Layered Architecture

```
┌─────────────────────────────────────┐
│      Presentation Layer             │
│  (Controllers, DTOs, Mappers)       │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Application Layer              │
│  (Services, Use Cases)              │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Domain Layer (DDD)             │
│  (Entities, Value Objects)          │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Infrastructure Layer           │
│  (Repositories, File I/O)           │
└─────────────────────────────────────┘
```

### Design Patterns

- **Repository Pattern** - Data access abstraction
- **Service Layer** - Business logic separation
- **DTO Pattern** - API boundary protection
- **Value Objects** - Domain-driven design
- **Factory Pattern** - Domain entity creation
- **Strategy Pattern** - Error handling

## Domain Model

### Core Entities

**EdfData** - Rich domain entity representing an EDF file
- Factory methods: `createValid()`, `createInvalid()`
- Encapsulates file properties, patient info, channels, and metrics

### Value Objects

**RecordingDate** - Immutable recording date and time
**PatientInfo** - Patient identification from SubjectID
**Channels** - Collection of signal channels
**RecordingMetrics** - Duration and data record information
**EdfFileProperties** - File validation status

## Error Handling

### Exception Hierarchy

```
RuntimeException
├── EdfSourceNotFoundException    (EDF Directory not found)
└── EdfDataNotFoundException       (No data available)
```

### Global Exception Handler

All exceptions are caught by `GlobalExceptionHandler` and converted to RFC 7807 Problem Details:

- **EdfSourceNotFoundException** → 404 Not Found
- **EdfDataNotFoundException** → 404 Not Found
- **Generic Exception** → 500 Internal Server Error

## Development

### Code Style

- Java Code Conventions
- Meaningful variable and method names
- Comprehensive JavaDoc comments
- Lombok for boilerplate reduction
- Mapstruct for domain entity->DTO mapping


### Hot Reload

Use Spring Boot DevTools for hot reloading during development:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
</dependency>
```

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=EdfControllerTest

```

## Troubleshooting

### Common Issues

**1. Directory Not Found on Startup**
```
Solution: Check edf.directory in application.properties
Verify the path exists and is accessible
```

**2. Files Not Loading**
```
Solution: Ensure files have .edf extension (case-insensitive)
Check file permissions
View logs for parsing errors
```

**3. CORS Errors**
```
Solution: Add WebConfig with CORS configuration
Allow frontend origin (e.g., http://localhost:5173)
```

**4. Port Already in Use**
```
Solution: Change server.port in application.properties
Or kill process using port 8080
```

## License

This project is part of the EDF Processing Assignment for the Senior Full-Stack Developer position for Zeto Inc.
It is for demonstration and evaluation purposes only.

#### 👨‍💻 Author: Peter Kormos
#### 📅 Date: November 2025


