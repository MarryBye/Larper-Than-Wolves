# Mithril Endgame Tier & Metallurgy Philosophy

This document outlines the design philosophy, acquisition mechanics, thermal processing pipeline, and equipment statistics for **Mithril** — the premier endgame tier of *Larper Than Wolves*.

---

## 🌟 Endgame Design Philosophy

Endgame tiers in *Larper Than Wolves* follow a deliberate multi-stage metallurgic progression rather than simple direct crafting:

1. **Nether Ore Genesis**: High-tier endgame ores are found strictly in hostile dimensions (the Nether) and require high-tier pickaxes to chip away.
2. **2 Dusts $\rightarrow$ 1 Raw Chunk**: Lower-tier tools extract raw dust from the ore; 2 dusts combine in the crafting grid into a single raw ore chunk.
3. **Previous Tier Smelter Yields Nuggets Only**: The previous tier smelter (e.g. Advanced Smelter) can only produce **nuggets** from the raw endgame ore.
4. **No Direct Ingot Crafting (No 4 Nuggets $\rightarrow$ 1 Ingot)**: Endgame nuggets cannot be crafted into ingots on a crafting table.
5. **New Tier Furnace Construction**: To produce full ingots, the player must construct a specialized furnace of that tier by combining the previous tier smelter with nuggets of the new tier (e.g. 1 Advanced Smelter + 2 Mithril Nuggets $\rightarrow$ 1 Mithril Furnace).
6. **New Furnace Unlocks Full Ingots**: The new tier furnace smelts raw ore chunks directly into full ingots.
7. **Higher Pickaxe Yields Whole Raw Ore**: Mining the ore with the corresponding tier pickaxe (e.g. Mithril Pickaxe) bypasses the dust phase, directly dropping the raw ore chunk.
8. **Smithing Table Equipment Upgrades**: Ingots are combined with the previous tier equipment (Reinforced Iron) on a Smithing Table to forge final endgame tools and armor.

---

## ⛏️ Mithril Ore & Harvesting Rules

- **World Generation**: Spawns naturally embedded in Netherrack across Nether biomes between $Y = 10$ and $Y = 115$ in veins of up to 4 blocks.
- **Light Emission**: Naturally emits a subtle magical glow (light level 3).
- **Mining Requirements**:
  - **Reinforced Iron Pickaxe**: Chips Mithril Ore into **1 Mithril Dust** (`mithril_dust`).
  - **Mithril Pickaxe**: Extracts **1 Raw Mithril** (`raw_mithril`) directly.
  - **Hand, Silicon, Copper, Bronze, and standard Iron Pickaxes**: Completely blocked (mining speed = 0.0, drops = 0).

---

## 🏭 Mithril Metallurgy Pipeline

```
                       [ Mithril Ore (Nether) ]
                                  │
          ┌───────────────────────┴───────────────────────┐
          │ (Reinforced Iron Pickaxe)                     │ (Mithril Pickaxe)
          ▼                                               ▼
   [ Mithril Dust ]                                [ Raw Mithril ]
          │ (2 Dusts Crafting)                            │
          └───────────────────────┬───────────────────────┘
                                  │
                    ┌─────────────┴─────────────┐
                    ▼                           ▼
          [ Advanced Smelter ]          [ Mithril Furnace ]
                    │                           │
                    ▼                           ▼
          [ Mithril Nugget ]            [ Mithril Ingot ]
                    │                           │
    (2 Nuggets + Advanced Smelter)              ├─► Smithing Table Equipment Upgrades
                    │                           └─► Decorative Mithril Block (4 Ingots)
                    ▼
          [ Mithril Furnace ]
```

### Processing Stations Matrix:
| Processing Machine | Input | Smelting Result |
| :--- | :--- | :--- |
| **Brick Furnace** | Raw Mithril / Mithril Dust | ❌ **Rejected / Cannot Smelt** |
| **Advanced Smelter** | Raw Mithril (`raw_mithril`) | **1 Mithril Nugget** (`mithril_nugget`) |
| **Mithril Furnace** | Raw Mithril (`raw_mithril`) | **1 Mithril Ingot** (`mithril_ingot`) |

---

## ⚔️ Mithril Equipment & Armor Attributes

### Tools (`ModToolMaterials.MITHRIL`)
- **Durability**: **1500 uses** (highest in the game).
- **Mining Speed**: **10.0f** (extremely fast).
- **Attack Damage Bonus**: **+4.0f**.
- **Enchantability**: **15**.
- **Repair Ingredient**: `larperthanwolves:mithril_ingot`.

| Tool | Durability | Base Attack Damage | Attack Speed | Special Traits |
| :--- | :--- | :--- | :--- | :--- |
| **Mithril Sword** | 1500 | 7.0 (3.5 Hearts) | 1.6 | Fast high-damage melee |
| **Mithril Pickaxe** | 1500 | 5.0 | 1.2 | Mines Raw Mithril directly, mines Obsidian / Ancient Debris |
| **Mithril Axe** | 1500 | 9.0 (4.5 Hearts) | 1.0 | Supreme tree felling and shield disable |
| **Mithril Shovel** | 1500 | 5.5 | 1.0 | Supreme soil digging & rich soil harvesting |
| **Mithril Hoe** | 1500 | 1.0 | 4.0 | Fast 2-stage tilling and seed harvesting |

### Armor (`ModArmorMaterials.MITHRIL`)
- **Durability Multiplier**: **35** (Helmet 385, Chestplate 560, Leggings 525, Boots 455).
- **Defense Points**: Helmet: 3, Chestplate: 8, Leggings: 6, Boots: 3 (**Total: 20 armor points / full bar**).
- **Toughness**: **2.5** per piece.
- **Knockback Resistance**: **0.1** (10% knockback reduction per piece).
- **Enchantability**: **15**.
- **Repair Ingredient**: `larperthanwolves:mithril_ingot`.

---

## 🔨 Crafting & Smithing Recipes Summary

1. **Raw Mithril**: 2 `mithril_dust` (Shapeless crafting) $\rightarrow$ 1 `raw_mithril`.
2. **Mithril Furnace**: 1 `advanced_smelter` + 2 `mithril_nugget` (Shapeless crafting) $\rightarrow$ 1 `mithril_furnace`.
3. **Mithril Block**: 4 `mithril_ingot` (2x2 shaped) $\rightarrow$ 1 `mithril_block`.
4. **Mithril Ingots from Block**: 1 `mithril_block` $\rightarrow$ 4 `mithril_ingot`.
5. **Smithing Upgrades**:
   - `reinforced_iron_sword` + `mithril_ingot` $\rightarrow$ `mithril_sword`
   - `reinforced_iron_pickaxe` + `mithril_ingot` $\rightarrow$ `mithril_pickaxe`
   - `reinforced_iron_axe` + `mithril_ingot` $\rightarrow$ `mithril_axe`
   - `reinforced_iron_shovel` + `mithril_ingot` $\rightarrow$ `mithril_shovel`
   - `reinforced_iron_hoe` + `mithril_ingot` $\rightarrow$ `mithril_hoe`
   - `reinforced_iron_helmet` + `mithril_ingot` $\rightarrow$ `mithril_helmet`
   - `reinforced_iron_chestplate` + `mithril_ingot` $\rightarrow$ `mithril_chestplate`
   - `reinforced_iron_leggings` + `mithril_ingot` $\rightarrow$ `mithril_leggings`
   - `reinforced_iron_boots` + `mithril_ingot` $\rightarrow$ `mithril_boots`

---

## 📂 Source Files
- Block Entities: `src/main/java/io/marrybye/github/larperthanwolves/block/entity/MithrilFurnaceBlockEntity.java`
- Block: `src/main/java/io/marrybye/github/larperthanwolves/block/MithrilFurnaceBlock.java`
- Tools: `src/main/java/io/marrybye/github/larperthanwolves/item/ModToolMaterials.java`
- Armor: `src/main/java/io/marrybye/github/larperthanwolves/item/ModArmorMaterials.java`
- Mining Events: `src/main/java/io/marrybye/github/larperthanwolves/event/BlockBreakHandler.java`
- Worldgen: `src/main/resources/data/larperthanwolves/worldgen/`
- JEI: `src/main/java/io/marrybye/github/larperthanwolves/compat/ModJeiPlugin.java`
