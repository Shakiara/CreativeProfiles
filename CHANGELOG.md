## v1.2.0

### Added

- Manual profile backups with cooldown.
- Automatic profile backups on login and before profile switching.
- Separate manual and automatic backup storage.
- Admin restore commands for manual backups.
- Admin restore commands for automatic backups.
- `/cp backupstatus`
- `/cp status <player>` for permission level 4.
- `/cp restore survival <player>`
- `/cp restore creative <player>`
- `/cp restoreauto survival <player>`
- `/cp restoreauto creative <player>`
- `/cp swapdata <player>` to repair swapped Survival and Creative data.
- Separate advancements per profile.
- Advancement support in manual and automatic backups.
- Improved status output with dimensions, compatibility state, saved data, and backup state.
- Documentation under `docs/` and `docs/info/`.

### Changed

- `/cp reload` now requires permission level 4.
- Restore commands now require permission level 4.
- Restored backups are consumed after use to reduce repeated duplication risks.
- Backup status now distinguishes manual and automatic backups.
- Profile saving now includes advancements.
- Profile repair tooling is more explicit for admin recovery.

### Notes

- Compatibility expansion for additional inventory/accessory mods is deferred to the v2.0.0 roadmap.
- Automatic backups do not replace manual backups.

---

## v1.1.1

### Fixed

- Fixed Creative profile switching sometimes leaving players in Survival mode after teleporting.
- Prevented Survival inventory from carrying into the Creative profile.
- Added separate saving and loading for Creative profile inventory.
- Improved gamemode enforcement after world teleport.

---

## v1.1.0

### Added

- Curios API compatibility.
- Cosmetic Armor Reworked compatibility.
- `/cp version`
- `/cp reload`
- Improved user messages.
- Improved error messages.
- Automatic optional compatibility detection.

### Changed

- Improved profile synchronization.
- Improved `/cp status`.
- Improved `/cp fix`.
- Cleaned NeoForge metadata.
- Updated documentation.
- General project cleanup.

---

## v1.0.0

### Added

Initial public release.

Features:

- Separate Survival inventory
- Separate Creative inventory
- Separate Ender Chest
- Separate XP
- Separate player positions
- Multiworld support
- Automatic profile switching
- Configuration system
