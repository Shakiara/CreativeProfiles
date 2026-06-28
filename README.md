# Creative Profiles

Separate Creative and Survival profiles for Minecraft servers.

Creative Profiles allows each player to have completely independent Creative and Survival profiles, including inventory, experience, Ender Chest, position, and optional mod integrations.

---

## Features

- Separate Survival and Creative inventories
- Separate experience levels
- Separate Ender Chests
- Separate saved positions
- Automatic world switching
- Multiworld compatible
- Curios API compatibility
- Cosmetic Armor Reworked compatibility
- Optional integrations (works even if compatibility mods are not installed)
- Server-side only

---

## Supported Versions

| Minecraft | NeoForge  | Multiworld | Status |
|-----------|-----------|------------|--------|
| 1.21.1 | 21.1.228+ | Required | Supported |

---

## Compatibility

### Native Compatibility

| Mod | Status |
|------|--------|
| Curios API | ✅ Supported |
| Cosmetic Armor Reworked | ✅ Supported |

### Automatically Supported Through Curios

These mods require no additional integration because they store their data through Curios.

- KubeJS Curios
- Refined Storage Curios Integration
- Gravestone Curios Compatibility
- Create Aeronautics Curios Compat

---

## Commands

| Command | Description |
|----------|-------------|
| `/creative` | Switch to Creative profile |
| `/survival` | Return to Survival profile |
| `/cp status` | Display current profile information |
| `/cp version` | Display Creative Profiles version |
| `/cp reload` | Reload configuration |
| `/cp fix` | Attempt to repair profile data |

---

## Installation

1. Install NeoForge.
2. Install Multiworld.
3. Create or configure your Survival and Creative worlds with Multiworld.
4. Place Creative Profiles in the server's `mods` folder.
5. Start the server.
6. Edit the Creative Profiles config if necessary.
7. Restart the server.

Required:

- Multiworld

Optional:

- Curios API
- Cosmetic Armor Reworked

Creative Profiles requires Multiworld for world switching.
Curios API and Cosmetic Armor Reworked are detected automatically when installed.

---

## Configuration

Example:

```text
creativeWorld = "creative"
survivalWorld = "world"
```

---

## Building

Clone the repository:

```text
git clone https://github.com/Shakiara/CreativeProfiles.git
```

Build:

```text
./gradlew build
```

Output:

```
build/libs/
```

---

## License

Creative Profiles is licensed under the MIT License.

See LICENSE.txt for details.

---

## Roadmap

Future plans are documented in:

```
ROADMAP.md
```

---

## Issues

Found a bug?

Please open an Issue on GitHub with:

- Minecraft version
- NeoForge version
- Creative Profiles version
- Installed compatibility mods
- Latest.log (if available)

---

## Credits

Created by

**Shakiara Feliciano**
