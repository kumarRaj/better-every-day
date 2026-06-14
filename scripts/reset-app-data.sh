#!/usr/bin/env bash
# Clears all app data on the connected device/emulator, forcing onboarding on next launch.
set -euo pipefail

adb shell pm clear com.bettereveryday
echo "App data cleared."
