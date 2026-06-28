# Creative Profiles Roadmap

This document contains planned features and ideas for future versions of Creative Profiles.

> **Note**
>
> Items listed here are ideas and goals. Their implementation order may change depending on development priorities.

---

# Version 1.2.x

## Possible Features

### Compatibility

* Support for additional inventory-related mods.
* Improved integration with server management mods.

### Configuration

* More configurable profile behavior.
* Optional synchronization settings.
* Optional automatic backups.

### Administration

* Additional debugging commands.
* Better diagnostic information.

---

# Version 2.0.0

## Major Refactor

### Internal Architecture

* Replace hardcoded PersistentData keys with centralized constants.
* Replace hardcoded profile names with constants.
* Modular compatibility system.
* Public API for other mods.

---

## Dynamic Profiles

Instead of only supporting:

* Survival
* Creative

Support unlimited custom profiles, for example:

* Builder
* Hardcore
* Testing
* Skyblock
* Adventure

Each profile would have its own:

* Inventory
* XP
* Ender Chest
* Position
* Game Mode
* Configuration

---

## Profile Commands

Examples:

```text
/cp create builder
/cp delete builder
/cp list
/cp switch builder
/cp rename builder
```

---

## Profile Configuration

Each profile could define:

* Target world
* Default GameMode
* Separate inventory
* Separate Ender Chest
* Separate XP
* Separate health
* Separate hunger
* Separate spawn point

---

## Public API

Allow other mods to interact with Creative Profiles.

Example:

```java
ProfileManager.getCurrentProfile(player);

ProfileManager.switchProfile(player, "builder");
```

---

## Mod Compatibility Framework

Provide a compatibility API where each supported mod has its own implementation.

Example:

```
compat/
    CuriosCompat.java
    AccessoriesCompat.java
    CreateCompat.java
    RefinedStorageCompat.java
```

---

## Data Migration

Automatic migration of player data from previous versions when possible.

---

## Future Ideas

* Per-profile statistics
* Per-profile advancements
* Per-profile recipes
* Per-profile Curios
* Per-profile accessories
* Per-profile modded inventories
* Profile export/import
* Backup manager
* Automatic profile recovery
* GUI for profile management

---

# Long-Term Vision

Transform Creative Profiles from a simple Creative/Survival switcher into a complete player profile management system for NeoForge servers, allowing multiple independent gameplay profiles while remaining lightweight, server-side, and highly compatible with other mods.
