# Creative Profiles Usage

Creative Profiles separates Survival and Creative profile data on NeoForge servers.

## Permissions

Most commands require permission level 2. Administrative restore and repair commands require permission level 4.

See `docs/info/PERMISSIONS.md` for details.

## Commands

| Command | Permission | Description |
| --- | --- | --- |
| `/creative` | Level 2 | Switches to the Creative profile. |
| `/survival` | Level 2 | Switches to the Survival profile. |
| `/cp status` | Level 2 | Shows your current profile state. |
| `/cp status <player>` | Level 4 | Shows another player's profile state. |
| `/cp backup` | Level 2 | Creates a manual backup. |
| `/cp backupstatus` | Level 2 | Shows backup availability and cooldown. |
| `/cp restore survival <player>` | Level 4 | Restores a manual Survival backup. |
| `/cp restore creative <player>` | Level 4 | Restores a manual Creative backup. |
| `/cp restoreauto survival <player>` | Level 4 | Restores an automatic Survival backup. |
| `/cp restoreauto creative <player>` | Level 4 | Restores an automatic Creative backup. |
| `/cp swapdata <player>` | Level 4 | Swaps stored Survival and Creative data. |
| `/cp fix` | Level 2 | Repairs the current profile marker. |
| `/cp version` | Level 2 | Shows version information. |
| `/cp reload` | Level 4 | Shows current config values. |

## Separated Data

Creative Profiles separates:

- Inventory
- XP
- Ender Chest
- Position
- Advancements
- Curios data, when installed
- Cosmetic Armor Reworked data, when installed

## Default Worlds

```text
creativeWorld = "creative"
survivalWorld = "minecraft:overworld"
```

The configured Creative world must exist before using `/creative`.

## More Information

Detailed command notes are available in `docs/info/`.
