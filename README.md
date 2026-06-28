# Creative Profiles

Creative Profiles is a **server-side NeoForge mod** for **Minecraft 1.21.1** that provides independent player profiles for Creative and Survival gameplay.

Instead of sharing the same inventory, Ender Chest, XP and position across worlds, each profile maintains its own data, allowing players to switch between a Survival world and a Creative building world without affecting their progress.

---

# Features

* Separate Survival and Creative inventories
* Separate Ender Chests
* Separate XP levels
* Separate player positions
* Automatic profile restoration after reconnecting
* Multiplayer support
* Configurable world names
* Diagnostic and maintenance commands 
* Curios compatibility 
* Cosmetic Armor Reworked compatibility

---

# Commands

| Command      | Description                                                  |
| ------------ | ------------------------------------------------------------ |
| `/creative`  | Switch to the Creative profile                               |
| `/survival`  | Return to the Survival profile                               |
| `/cp status` | Display profile information                                  |
| `/cp backup` | Save the current profile manually                            |
| `/cp fix`    | Repair the current profile state without deleting saved data |

---

# Requirements

* Minecraft **1.21.1**
* **NeoForge 21.1.228**
* **Multiworld** (used to manage separate worlds)

---

# Installation

1. Install NeoForge 1.21.1 on your server.
2. Install Multiworld.
3. Place the Creative Profiles `.jar` inside the `mods` folder.
4. Start the server once to generate the configuration file.
5. Configure your world names if necessary.
6. Restart the server.

---

# Configuration

Configuration file:

```text
config/creativeprofiles-common.toml
```

Default values:

```toml
creativeWorld = "creative"
survivalWorld = "minecraft:overworld"
```

---

# How it Works

When switching to the Creative profile, the mod:

* Saves the Survival inventory
* Saves the Survival XP
* Saves the Survival Ender Chest
* Saves the Survival position
* Loads the Creative profile

When returning to Survival, the process is reversed.

Each player has completely independent data stored in their own persistent player data.

---

# Multiplayer

Creative Profiles stores data independently for every player.

Each player has their own:

* Inventory
* Ender Chest
* XP
* Position
* Current profile

Using `/creative`, `/survival` or `/cp` commands only affects the player executing the command.

---

# Compatibility

### Native Compatibility

| Mod | Status |
|------|--------|
| Curios API | ✅ Supported |
| Cosmetic Armor Reworked | 🚧 In Development |

### Automatically Supported

Because these mods use the Curios API, they work without additional integration:

- KubeJS Curios
- Refined Storage Curios Integration
- Gravestone Curios Compatibility
- Create Aeronautics Curios Compat

---

# Current Version

**v1.0.0**

Initial stable release.

---

# License

This project is licensed under the MIT License.
