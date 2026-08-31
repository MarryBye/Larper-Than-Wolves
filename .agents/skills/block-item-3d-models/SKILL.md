---
name: block-item-3d-models
description: >-
  Mandatory protocol ensuring all placeable world objects and blocks (baskets, drying racks, machines, workstations, containers)
  have authentic 3D inventory item models and display transformations instead of flat 2D sprite textures.
---

# 3D Inventory Models Protocol for Placeable World Blocks

This skill mandates that **all blocks and placeable world objects** in the mod (except minor flat ground scatter items like twigs or dusts like redstone) MUST have authentic **3D block models in the player's inventory and GUI**, rather than flat 2D sprite textures.

---

## 🏛️ Core Principles & Scope

### 1. Mandatory 3D Block Items
Every item corresponding to a placeable world block/object MUST render as a 3D isometric block model in the GUI, in the player's hand, when dropped on the ground, and in item frames.
This includes:
- Containers & Storage (e.g., **Woven Basket** `basket`, chests, barrels).
- Workstations & Apparatus (e.g., **Drying Rack** `drying_rack`, **Sieve** `sieve`, **Work Stump** `work_stump`, **Alloy Mixer** `alloy_mixer`, **Brick Furnace** `brick_furnace`, **Oven** `oven`, **Hand Mill** `mill`, **Mill Crank** `mill_crank`).
- Redstone & Kinetic Mechanisms (e.g., **Kinetic Piston** `kinetic_piston`, **Filter Grate** `filter_grate`, **Entity Observer** `entity_observer`).
- Decorative & Construction Blocks (e.g., Ores, Stumps, Bronze blocks, Fertilized Farmland, Unfired Bricks).

### 2. Exceptions (Pure 2D Items)
Only small, flat ground clutter or particulate dusts can use flat 2D item textures:
- **Twigs** (`twig` — small ground stick scatter).
- **Dusts & Shards** (`silicon_shard`, `iron_dust`, `redstone`, etc.).

---

## 📐 Standard 3D Item Model Structure (`models/item/<block>.json`)

Placeable block items must inherit from the block's model (or an empty/default variation) and define standard isometric display transformations:

```json
{
  "parent": "larperthanwolves:block/<block_name>",
  "display": {
    "thirdperson_righthand": {
      "rotation": [75, 45, 0],
      "translation": [0, 2.5, 0],
      "scale": [0.375, 0.375, 0.375]
    },
    "thirdperson_lefthand": {
      "rotation": [75, 45, 0],
      "translation": [0, 2.5, 0],
      "scale": [0.375, 0.375, 0.375]
    },
    "firstperson_righthand": {
      "rotation": [0, 45, 0],
      "translation": [0, 0, 0],
      "scale": [0.4, 0.4, 0.4]
    },
    "firstperson_lefthand": {
      "rotation": [0, 225, 0],
      "translation": [0, 0, 0],
      "scale": [0.4, 0.4, 0.4]
    },
    "ground": {
      "rotation": [0, 0, 0],
      "translation": [0, 3, 0],
      "scale": [0.25, 0.25, 0.25]
    },
    "gui": {
      "rotation": [30, 225, 0],
      "translation": [0, 0, 0],
      "scale": [0.625, 0.625, 0.625]
    },
    "fixed": {
      "rotation": [0, 0, 0],
      "translation": [0, 0, 0],
      "scale": [0.5, 0.5, 0.5]
    }
  }
}
```

---

## 📋 Verification Checklist

When adding or modifying any block:
- [ ] Is `models/item/<block_name>.json` pointing to a 3D block model (`larperthanwolves:block/...`)?
- [ ] Are `display` transformations present for GUI, hands, ground, and fixed views?
- [ ] Has any 2D `"parent": "minecraft:item/generated"` been replaced with the 3D model?
