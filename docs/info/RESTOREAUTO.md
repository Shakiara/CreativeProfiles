# Automatic Restore Commands

## Commands

```text
/cp restoreauto survival <player>
/cp restoreauto creative <player>
```

## Permission

Requires permission level 4.

## Description

Restores an automatic backup for the selected player and profile.

Automatic backups are created separately from manual backups, so they can be used when a manual backup was not created or was already consumed.

## Automatic Backup Events

Automatic backups are created:

- When a player logs in
- Before switching to Creative
- Before switching to Survival

## Behavior

- Restores the selected profile from the automatic backup.
- Applies immediately if the restored profile is active.
- Loads later if the restored profile is inactive.
- Consumes the restored automatic backup after use.
