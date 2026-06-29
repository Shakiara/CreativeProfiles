# Creative Profiles Roadmap

This document lists planned ideas for future versions of Creative Profiles. Items may change depending on testing, compatibility needs, and project priorities.

## Version 2.0.0

### Major Refactor

- Replace hardcoded PersistentData keys with centralized constants.
- Replace hardcoded profile names with constants.
- Add a modular compatibility framework.
- Add safer data migration for existing worlds.
- Improve internal profile storage boundaries.
- Reduce duplicated save/load logic across managers.

### Dynamic Profiles

Support custom profiles instead of only Survival and Creative.

Possible examples:

- Builder
- Testing
- Skyblock
- Adventure
- Event

Each profile could define:

- Inventory
- XP
- Ender Chest
- Position
- Game mode
- Advancements
- Configuration

### Compatibility Focus

Additional compatibility work is planned for v2.0.0.

Possible areas:

- Accessories support
- Additional backpack or inventory mods
- More Curios-like integrations
- Safer optional compatibility modules
- Better diagnostics for compatibility failures

### Public API

Allow other mods to interact with Creative Profiles.

Possible examples:

```text
ProfileManager.getCurrentProfile(player)
ProfileManager.switchProfile(player, "builder")
ProfileManager.hasProfile(player, "builder")
```

---

## Version 3.0.0

### Advanced Administration

- Admin tools for inspecting profile data in more detail.
- Safer recovery workflows for corrupted or crossed profile data.
- More detailed backup history.
- Optional restore previews before applying profile data.
- Better server logs for profile switches, restores, and repairs.

### Profile Rules

- Per-profile permissions.
- Per-profile command restrictions.
- Per-profile world restrictions.
- Per-profile default inventory behavior.
- Per-profile advancement and recipe options.

### Automation

- Scheduled backups.
- Configurable automatic backup retention.
- Optional automatic cleanup of old backups.
- Optional warnings when profile data looks inconsistent.

### User Experience

- Cleaner command output.
- Optional localization improvements.
- Better help output for `/cp` commands.
- More readable diagnostics for server owners.

---

## Long-Term Vision

Creative Profiles should become a lightweight but reliable profile management system for NeoForge servers, with clean separation between gameplay profiles and strong recovery tools for server owners.

### Future Profile Commands

Possible commands:

```text
/cp create builder
/cp delete builder
/cp list
/cp switch builder
/cp rename builder
/cp clone survival builder
/cp export builder
/cp import builder
```

### Future Ideas

- Per-profile statistics
- Per-profile recipes
- Per-profile health
- Per-profile hunger
- Per-profile spawn points
- Profile export/import
- More advanced backup management
- Automatic data migration
- GUI or admin panel support
- Optional web/admin integration
