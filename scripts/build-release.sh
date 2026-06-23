#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ANDROID_DIR="$SCRIPT_DIR/../android"
OUTPUT_DIR="$SCRIPT_DIR/../releases"

BUILD_GRADLE="$ANDROID_DIR/app/build.gradle.kts"

cd "$ANDROID_DIR"

# Read current versions
CURRENT_CODE=$(grep 'versionCode' "$BUILD_GRADLE" | head -1 | grep -oE '[0-9]+')
CURRENT_NAME=$(grep 'versionName' "$BUILD_GRADLE" | head -1 | grep -oE '"[^"]+"' | tr -d '"')

NEW_CODE=$((CURRENT_CODE + 1))

# Bump versionCode in build.gradle.kts
sed -i '' "s/versionCode = ${CURRENT_CODE}/versionCode = ${NEW_CODE}/" "$BUILD_GRADLE"

echo "Version: $CURRENT_NAME ($CURRENT_CODE → $NEW_CODE)"
echo "Building release AAB..."
./gradlew bundleRelease

AAB_SRC="$ANDROID_DIR/app/build/outputs/bundle/release/app-release.aab"

if [[ ! -f "$AAB_SRC" ]]; then
    echo "Error: AAB not found at $AAB_SRC" >&2
    exit 1
fi

mkdir -p "$OUTPUT_DIR"

TIMESTAMP=$(date +%Y%m%d_%H%M%S)
DEST="$OUTPUT_DIR/bettereveryday-${CURRENT_NAME}-${NEW_CODE}-${TIMESTAMP}.aab"

cp "$AAB_SRC" "$DEST"

echo "AAB ready: $DEST"
