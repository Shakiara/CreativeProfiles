# Backup Commands

Creative Profiles provides manual and automatic backups for Survival and Creative profile data.

## Commands

```text
/cp backup
/cp backupstatus
```

## `/cp backup`

Creates a manual backup for both profiles.

Manual backups are separate from automatic backups and are not overwritten by automatic backup events.

This command has a cooldown of 296 seconds per player.

## `/cp backupstatus`

Shows:

- Manual Survival backup status
- Manual Creative backup status
- Automatic Survival backup status
- Automatic Creative backup status
- Backup timestamps
- Manual backup cooldown

## Backed Up Data

- Inventory
- XP
- Ender Chest
- Position
- Advancements
- Curios data, when installed
- Cosmetic Armor Reworked data, when installed

Restored backups are consumed after use to reduce repeated duplication risk.
