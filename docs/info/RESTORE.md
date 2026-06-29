# Restore Commands

## Commands

```text
/cp restore survival <player>
/cp restore creative <player>
```

## Permission

Requires permission level 4.

## Description

Restores a manual backup for the selected player and profile.

Manual restore commands are intended for administrators only because they overwrite saved profile data.

## Behavior

- Restores the selected profile from the manual backup.
- Applies the restored data immediately if that profile is currently active.
- Leaves restored data ready for the next switch if that profile is inactive.
- Consumes the restored backup after use.

## Restored Data

- Inventory
- XP
- Ender Chest
- Position
- Advancements
- Curios data, when available
- Cosmetic Armor Reworked data, when available
