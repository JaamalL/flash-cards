#!/bin/bash

set -e

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
DOCKER_DIR="$PROJECT_ROOT/docker"
DOCKER_DATA_DIR="$DOCKER_DIR/data"

docker compose -f "$DOCKER_DIR/docker-compose.yml" up -d --build