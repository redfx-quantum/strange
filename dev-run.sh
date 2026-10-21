#!/bin/sh
set -e

IMAGE=javaqc-dev
CONTAINER_FILE=.devcontainer/Containerfile

# Build (cached after first time)
podman build \
    --build-arg UID="$(id -u)" \
    --build-arg GID="$(id -g)" \
    -t "$IMAGE" -f "$CONTAINER_FILE" .devcontainer/

# Run: mount project source + persist nvim mason data across sessions
podman run --rm -it \
    --userns=keep-id \
    -p 8080:8080 \
    -v "$(pwd)":/workspace:Z \
    -v javaqc-nvim-mason:/home/dev/.local/share/nvim/mason:Z \
    -v javaqc-nvim-cache:/home/dev/.cache/nvim:Z \
    "$IMAGE"
