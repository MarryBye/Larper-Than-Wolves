---
name: optional-libs-compat
description: >-
  Mandatory protocol treating all JAR mods located in the libs/ folder as optional dependencies. Guarantees that the mod compiles and functions seamlessly standalone without them, while actively developing and maintaining deep cross-compatibility and feature parity whenever they are present.
---

# Optional Libraries & Cross-Mod Compatibility Protocol (`libs/`)

All JAR files located in the `libs/` directory (e.g., `create-*.jar`, `jei-*.jar`, and any other mod JARs added in the future) are strictly **optional dependencies** for `larperthanwolves`.

This skill establishes the mandatory rules for maintaining standalone stability while actively engineering rich cross-mod compatibility with all libraries present in `libs/`.

---

## 🎯 Core Principles

1. **Standalone Purity (Soft Dependencies Only)**:
   - The mod must compile, launch, and run with 100% stability in a vanilla NeoForge environment without any optional mods present.
   - Never introduce hard runtime class references that cause `ClassNotFoundException` or `NoClassDefFoundError` when optional mods are absent.
   - Always guard mod-specific logic with `net.neoforged.fml.ModList.get().isLoaded("modid")`.
   - Isolate mod integrations in dedicated classes/packages (e.g. `compat/create/`, `compat/jei/`) that are only loaded when the target mod is active.

2. **Active Feature & Update Compatibility**:
   - Whenever designing, adding, or modifying mechanics, machines, blocks, items, recipes, or balance systems, you **MUST** inspect the mods present in `libs/` and implement corresponding compatibility features.
   - Never write code in isolation; always consider how new features interact with active mods in `libs/`.

---

## 📦 Compatibility Matrix for Mods in `libs/`

### 1. Create (`create-*.jar`)
When Create is present in `libs/`:
- **Rotational Force & Kinetic Processing**:
  - Support kinetic rotational power on mechanical blocks (e.g. Hand Mill / Quern). Grinding speed should scale dynamically with RPM.
  - Allow Create hand cranks, shafts, cogwheels, and engines to interface with relevant mod blocks.
- **Machine Parity & Bulk Processing**:
  - Mechanical Mixer (Heated/Unheated): Register recipes for all custom alloys (Bronze, Brass, Diamond Ingot).
  - Millstone & Crushing Wheels: Support crushing of gravel, sand, ores, and stones into dusts, nuggets, and pebbles.
  - Bulk Blasting (Encased Fan + Lava): Smelt raw ores to metal nuggets.
  - Bulk Smoking (Encased Fan + Fire/Campfire): Process grass into dry grass, leather into tanned leather.
  - Mechanical Saw: Support cutting logs and tree stumps into 4 planks.
- **Kinetic Pistons & Automation**:
  - Ensure compatibility with Create chutes, funnels, and mechanical arms where applicable.

### 2. Just Enough Items (`jei-*.jar`)
When JEI is present in `libs/`:
- **Follow `jei-mechanics-documenter` Protocol**: Strictly adhere to all rules in `jei-mechanics-documenter`.
- **Mandatory In-Game Documentation (`addIngredientInfo`)**: Every single mod item, block, and overhauled vanilla mechanic MUST have a localized "Information" page in JEI.
- **Bidirectional Recipe Synchronization (Station-Recipe Symmetry)**: When viewing a station's uses (U key), all recipes that can be processed at that station MUST be displayed together (via `RecipeIngredientRole.CATALYST` indexing in `setRecipe`).
- **Register Custom Recipe Categories**: For all custom processing stations (Hand Mill, Alloy Mixer, Sieve Table, Drying Rack, Brick Furnace, Advanced Smelter, Mithril Furnace, Oven, Chisel progression, Solar drying, Soil digging, Machine fuels).
- **Ghost / Phantom Slots**: Implement phantom slot interactions for filter devices (e.g., Filter Grate GUI).
- **Full Localization**: Localize all JEI category titles, labels, conditions, and info tabs in both `en_us.json` and `ru_ru.json`.

### 3. Future / Newly Added Mods in `libs/`
When any new mod JAR is added to `libs/`:
- **Inspect Mod ID & APIs**: Identify the mod's ID, key mechanics, item tags, and integration hooks.
- **Add Optional Metadata**: Declare the dependency in `neoforge.mods.toml` as `type="optional"`, `ordering="AFTER"`.
- **Implement Cross-Mod Systems**: Add appropriate recipes, tool interactions, machine inputs/outputs, or block behavior bridges guarded by `ModList.get().isLoaded(...)`.

---

## 🛠️ Build & Metadata Configuration

1. **`build.gradle`**:
   - All JARs in `libs/` must be included via `compileOnly fileTree(dir: 'libs', include: ['*.jar'])` (or `compileOnly` API coordinates) so they remain compile-time dependencies without being packaged into the runtime jar as hard requirements.
2. **`neoforge.mods.toml`**:
   - Every optional dependency must be declared in `src/main/templates/META-INF/neoforge.mods.toml`:
     ```toml
     [[dependencies.${mod_id}]]
         modId="<optional_mod_id>"
         type="optional"
         versionRange="[<min_version>,)"
         ordering="AFTER"
         side="BOTH"
     ```
3. **Common Tags (`c:`)**:
   - Always use conventional/common tags (e.g. `c:ingots/*`, `c:dusts/*`, `c:raw_materials/*`, `c:tools/*`, `c:foods/*`) in recipes, loot tables, and item checks to ensure seamless out-of-the-box interoperability with other mods.

---

## 📋 Checklist for New Features & Updates

Before completing any task or update:
- [ ] Are all mods currently in `libs/` accounted for in the new feature/mechanic?
- [ ] Does the mod compile and run properly both with and without the optional mods?
- [ ] Are all optional mod API calls safely guarded behind `ModList.get().isLoaded(...)` or isolated in compat classes?
- [ ] Are recipes and interactions registered for relevant machines (Create mixers/mills/fans/saws, JEI categories, etc.)?
- [ ] Is the dependency declared as `type="optional"` in `neoforge.mods.toml`?
