# Contributing to Creative Profiles

Thank you for wanting to improve Creative Profiles.

## Project Scope

Creative Profiles is intended to stay lightweight, server-side, and focused on separated player profiles for NeoForge servers.

Contributions should preserve these goals:

- Keep Survival and Creative profile data separated.
- Avoid client-only requirements unless they are optional.
- Keep optional mod compatibility safe when the optional mod is not installed.
- Avoid breaking existing player persistent data whenever possible.

## Development Setup

Requirements:

- Java 21
- Minecraft 1.21.1
- NeoForge 21.1.x
- Gradle wrapper included in this repository

Build the project with:

```text
./gradlew build
```

On Windows:

```text
gradlew.bat build
```

## Pull Requests

Before opening a pull request:

1. Build the project successfully.
2. Keep changes focused on one bug fix or feature.
3. Include a clear explanation of what changed and why.
4. Mention any compatibility impact for existing worlds or player data.

## Code Style

- Use clear Java names.
- Keep profile storage keys stable unless a migration is included.
- Keep optional integrations behind mod-loaded checks.
- Prefer simple server-side behavior over client-side UI when possible.

## Reporting Bugs

When reporting a bug, include:

- Minecraft version
- NeoForge version
- Creative Profiles version
- Other relevant mods installed
- Steps to reproduce
- Expected behavior
- Actual behavior
- Crash report or latest log, if available
