# Creative Profiles

> Creative Profiles is currently in beta. It is functional and tested in active development environments, but server owners should keep backups and test updates before using them on production worlds.

Separate Creative and Survival profiles for Minecraft NeoForge servers.

Creative Profiles gives each player independent Survival and Creative profile data while keeping the mod lightweight and server-side.

## Features

- Separate Survival and Creative inventories
- Separate XP
- Separate Ender Chests
- Separate saved positions
- Separate advancements
- Separate Curios data, when Curios is installed
- Separate Cosmetic Armor Reworked data, when installed
- Manual profile backups
- Automatic profile backups
- Admin restore tools
- Admin repair tool for swapped profile data
- Multiworld support
- Server-side only

## Supported Versions

| Minecraft | NeoForge | Status |
| --- | --- | --- |
| 1.21.1 | 21.1.228+ | Supported |

## Required

- NeoForge
- Multiworld or another setup that provides the configured Creative world

## Optional Compatibility

| Mod | Status |
| --- | --- |
| Curios API | Supported |
| Cosmetic Armor Reworked | Supported |

Creative Profiles detects optional integrations automatically when they are installed.

## Commands

| Command | Permission | Description |
| --- | --- | --- |
| `/creative` | Level 2 | Switches to the Creative profile. |
| `/survival` | Level 2 | Switches to the Survival profile. |
| `/cp status` | Level 2 | Shows your profile status. |
| `/cp status <player>` | Level 4 | Shows another player's profile status. |
| `/cp backup` | Level 2 | Creates a manual backup. |
| `/cp backupstatus` | Level 2 | Shows manual and automatic backup status. |
| `/cp restore survival <player>` | Level 4 | Restores a manual Survival backup. |
| `/cp restore creative <player>` | Level 4 | Restores a manual Creative backup. |
| `/cp restoreauto survival <player>` | Level 4 | Restores an automatic Survival backup. |
| `/cp restoreauto creative <player>` | Level 4 | Restores an automatic Creative backup. |
| `/cp swapdata <player>` | Level 4 | Swaps stored Survival and Creative data to repair crossed profiles. |
| `/cp fix` | Level 2 | Repairs the current profile marker. |
| `/cp version` | Level 2 | Shows version information. |
| `/cp reload` | Level 4 | Shows current config values. |

## Configuration

Default config values:

```text
creativeWorld = "creative"
survivalWorld = "minecraft:overworld"
```

The configured Creative world must exist before using `/creative`.

## Documentation

Command documentation is available in:

```text
docs/USAGE.md
docs/info/
```

## Building

```text
./gradlew build
```

On Windows:

```text
gradlew.bat build
```

Build output:

```text
build/libs/
```

## License

Creative Profiles is licensed under the MIT License. See `LICENSE.txt`.

## Credits

Created by Shakiara Feliciano.


