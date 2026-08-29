---
name: jei-mechanics-documenter
description: >-
  Protocol to automatically create and update JEI categories, recipe POJOs, and ingredient information tabs for any unique, custom, or non-standard game mechanic to ensure total player clarity.
---

# JEI Mechanics Documentation & Integration Protocol

Hardcore survival and overhaul mods often introduce non-standard mechanics (in-world crafting, chisel carving, passive drying under sunlight, manual machine ignition, custom fueling speeds, modified tool tier drop tables). Without explicit JEI integration, players cannot discover these mechanics through standard recipes.

Whenever any custom or non-standard mechanic is added or changed in the mod, this skill mandates creating full JEI support.

---

## 📋 JEI Support Checklist

For every custom system:

### 1. In-World / Environmental Mechanics
- **Chisel / Work Stump Carving**: Show Stumps/Logs + Chisel $ightarrow$ Work Stump $ightarrow$ Crafting Table with required clicks.
- **Sun Drying**: Show Unfired Bricks $ightarrow$ direct sunlight $ightarrow$ baked Brick with duration.
- **Block Drop Overhauls (e.g. Gravel)**: Show breaking drops (Silicon Shards, Dusts) and chance percentages.

### 2. Custom Machines & Processing
- **Alloy Mixing**: Dedicated category with slots for all components and output alloy.
- **Sifting (Sieve)**: Dedicated category with siftable blocks and possible outputs.
- **Machine Fueling & Manual Ignition**: Show all accepted fuels, burn durations, cook speeds, and the required ignition catalyst (Lighter or Flint and Steel).

### 3. In-Depth Ingredient Information (ddIngredientInfo)
Always register informational description tabs for:
- Core tools (Chisel, Lighter, custom pickaxes/shears).
- Disabled or altered vanilla items (Crafting Table, Flint, Brick Furnace).
- Intermediary blocks/materials (Work Stump, Unfired Brick, Silicon Shard).

### 4. Localization
Every JEI category title, instruction label, time estimate, and info tab MUST be localized in both en_us.json and 
u_ru.json.
