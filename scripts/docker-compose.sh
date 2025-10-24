#!/bin/bash

set -e

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
DOCKER_DIR="$PROJECT_ROOT/docker"
DOCKER_DATA_DIR="$DOCKER_DIR/data"

bash "$PROJECT_ROOT/scripts/build-local.sh"

mkdir -p "$DOCKER_DATA_DIR"
chmod -R 777 "$DOCKER_DATA_DIR"

docker compose -f "$DOCKER_DIR/docker-compose.yml" up -d --build