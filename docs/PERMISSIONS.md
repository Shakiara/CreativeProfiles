# Permission Levels

Minecraft commands use permission levels to control administrative access.

## Levels

| Level | Meaning |
| --- | --- |
| 0 | Default player access. |
| 1 | Limited utility access. |
| 2 | Game management access. |
| 3 | Advanced server management access. |
| 4 | Full server operator access. |

## Creative Profiles Permissions

Level 2 commands:

```text
/creative
/survival
/cp status
/cp backup
/cp backupstatus
/cp fix
/cp version
```

Level 4 commands:

```text
/cp status <player>
/cp restore survival <player>
/cp restore creative <player>
/cp restoreauto survival <player>
/cp restoreauto creative <player>
/cp swapdata <player>
/cp reload
```

## Server Configuration

The default operator permission level is controlled by `op-permission-level` in `server.properties`.

Example:

```text
op-permission-level=4
```
