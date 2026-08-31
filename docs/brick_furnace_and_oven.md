# Brick Furnace & Food Oven Specialization

This system splits high-temperature thermal processing into two strictly specialized appliances: the **Brick Furnace** for mineral smelting and the **Oven** for cooking food.

---

## ⚙️ Mechanics & Working Principles
- **Brick Furnace (`brick_furnace` / `BrickFurnaceBlock` / `BrickFurnaceBlockEntity`)**:
  - Replaces the vanilla Furnace and Blast Furnace.
  - **Ores & Blocks ONLY**: Accepts raw ores, mineral dusts, clay, stone, sand, and refractory materials.
  - **Food Strictly Blocked**: Food items cannot be inserted into the input slot or smelted.
  - Requires manual fueling and ignition with a Lighter or Flint & Steel.
- **Food Oven (`oven` / `OvenBlock` / `OvenBlockEntity`)**:
  - Crafted from 1 Brick Furnace + 1 Iron Nugget or 2 Brick Slabs + 2 Bricks.
  - Replaces the vanilla Smoker.
  - **Food ONLY**: Accepts raw beef, pork, mutton, chicken, rabbit, fish, potatoes, and kelp.
  - **Non-Food Strictly Blocked**: Ores, cobblestone, and raw minerals cannot be placed into the oven.
  - Requires manual fueling and ignition.
- **Unified Fuel & Heat Matrix (`FuelRegistry`)**:
  - Every fuel in the game provides a distinct **Burn Duration** and **Cooking Speed (Heat Level)**.
  - Burn durations are extended ~2–2.5x compared to standard rates for realistic long-lasting burns.
  - Fuels are logically tiered across 9 realistic solid combustible tiers (from twigs and dry grass up to coal blocks). Molten lava buckets are completely disabled and purged as unrealistic furnace fuel.

### 🔥 Fuel Hierarchy & Characteristics Table

| Tier | Fuel Type | Included Items | Burn Duration (Ticks / Sec) | Smelting Speed (Cook Time per Item) | Smelt Yield per Piece |
|---|---|---|---|---|---|
| **Tier 1** | **Листва и ветки (*Kindling & Foliage*)** | `ModItems.TWIG`, `ModItems.DRY_GRASS`, `Items.DEAD_BUSH`, `#minecraft:saplings`, `#minecraft:leaves` | **900 ticks** (45s) | **260 ticks** (13.0s) | ~3.4 items |
| **Tier 2** | **Палки и мелкое дерево (*Sticks & Bowls*)** | `Items.STICK`, `ModItems.POINTED_STICK`, `Items.BOWL` | **1300 ticks** (65s / 1m 5s) | **240 ticks** (12.0s) | ~5.4 items |
| **Tier 3** | **Деревянные плиты и ступени (*Slabs & Stairs*)** | `#minecraft:wooden_slabs`, `#minecraft:wooden_stairs`, `#minecraft:wooden_trapdoors`, `#minecraft:wooden_fences`, `#minecraft:fence_gates` | **1800 ticks** (90s / 1m 30s) | **200 ticks** (10.0s) | 9.0 items |
| **Tier 4** | **Доски и изделия (*Planks & Wooden Objects*)** | `#minecraft:planks`, `#minecraft:wooden_doors`, `#minecraft:wooden_pressure_plates`, `#minecraft:wooden_buttons`, `#minecraft:boats`, `#minecraft:signs`, `#minecraft:hanging_signs` | **2400 ticks** (120s / 2m) | **180 ticks** (9.0s) | ~13.3 items |
| **Tier 5** | **Брёвна, пни и древесина (*Logs, Wood & Stumps*)** | `#minecraft:logs`, `ModBlocks.isStump()`, `#larperthanwolves:stumps`, stripped logs, wood | **3300 ticks** (165s / 2m 45s) | **150 ticks** (7.5s) | ~22.0 items |
| **Tier 6** | **Древесный уголь (*Charcoal*)** | `Items.CHARCOAL` | **3600 ticks** (180s / 3m) | **120 ticks** (6.0s) | ~30.0 items |
| **Tier 7** | **Каменный уголь (*Mineral Coal*)** | `Items.COAL` | **4500 ticks** (225s / 3m 45s) | **100 ticks** (5.0s) | 45.0 items |
| **Tier 8** | **Стержень ифрита (*Blaze Rod*)** | `Items.BLAZE_ROD` | **6000 ticks** (300s / 5m) | **70 ticks** (3.5s) | ~85.7 items |
| **Tier 9** | **Угольный блок (*Coal Block*)** | `Items.COAL_BLOCK` | **36000 ticks** (1800s / 30 мин) | **80 ticks** (4.0s) | 450.0 items |

---

## 📦 Crafting & Progression
- **Unfired Bricks $\rightarrow$ Brick Furnace**:
  - Unfired Bricks dry in direct sunlight into baked Bricks.
  - 8 Bricks $\rightarrow$ Brick Furnace.
- **Oven Recipe**: 1 Brick Furnace + 1 Iron Nugget.

---

## 🔄 Cross-Mod Compatibility & Automation
- **Create (6.0.10+)**: Bulk Blasting smelts raw ores into metal nuggets; Bulk Smoking cooks foods.
- **JEI Integration**: `MachineFuelRecipeCategory`, `SunDryingRecipeCategory`, and specialized furnace/oven recipe registries.

---

## 🧪 Testing Guide & Edge Cases
1. **Food in Brick Furnace**: Attempt to insert Raw Beef into a Brick Furnace. Verify insertion is rejected.
2. **Ore in Oven**: Attempt to insert Raw Copper into an Oven. Verify insertion is rejected.
3. **Manual Ignition**: Insert coal and iron dust into Brick Furnace. Verify it remains idle until clicked with a Lighter or Flint & Steel.
4. **Coal Block Refueling**: Insert a Coal Block into Brick Furnace. Verify it receives 36000 burn ticks (30 minutes) at 80 ticks/item cooking speed.
5. **Twigs/Planks/Logs Differentiation**: Compare burn durations in JEI and gameplay for Twigs (45s), Planks (120s), and Logs (165s).

---

## 📂 Key Source Files
- Blocks: `src/main/java/io/marrybye/github/larperthanwolves/block/BrickFurnaceBlock.java`, `OvenBlock.java`, `AlloyMixerBlock.java`
- Block Entities: `src/main/java/io/marrybye/github/larperthanwolves/block/entity/BrickFurnaceBlockEntity.java`, `OvenBlockEntity.java`, `AlloyMixerBlockEntity.java`
- Fuel Registry: `src/main/java/io/marrybye/github/larperthanwolves/block/entity/FuelRegistry.java`
- JEI Plugin: `src/main/java/io/marrybye/github/larperthanwolves/compat/ModJeiPlugin.java`
- Configuration: `src/main/java/io/marrybye/github/larperthanwolves/config/ModConfig.java`
