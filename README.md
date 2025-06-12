# TransferService 🚀

A lightweight Java-based file transfer microservice, packaged with Docker—designed to securely receive and forward files via HTTP.

## Features

- **Upload API**: Expose a REST endpoint (`POST /transfer`) to upload files (multipart/form-data).
- **Forwarding logic**: Transfers uploaded files to a configurable remote endpoint.
- **Containerized**: Deployed easily using Docker.
- **Configurable**: Endpoint URLs, auth, and timeouts via environment variables or `application.properties`.
- **Modular & Testable**: Designed for unit and integration testing.

## Requirements

- Java 17+ (compatible with Maven wrapper included)
- Maven for building (or use `./mvnw` on Unix/macOS, `mvnw.cmd` on Windows)
- Docker (for containerization)
- Optional: JUnit 5, WireMock for testing

## Getting Started

### Clone & Build

```bash
git clone https://github.com/abanoub-aboelsaad/TransferService.git
cd TransferService
./mvnw clean package
