# Brick Furnace & Food Oven Specialization

This system splits high-temperature thermal processing into two strictly specialized appliances: the **Brick Furnace** for mineral smelting and the **Oven** for cooking food.

---

## ⚙️ Mechanics & Working Principles
- **Brick Furnace (`brick_furnace` / `BrickFurnaceBlock` / `BrickFurnaceBlockEntity`)**:
  - Replaces the vanilla Furnace and Blast Furnace.
  - **Ores & Blocks ONLY**: Accepts raw ores, mineral dusts, clay, stone, sand, and refractory materials. Smelts raw chunks into **Nuggets** (Iron, Copper, Gold, Tin, Zinc).
  - **Food Strictly Blocked**: Food items cannot be inserted into the input slot or smelted.
  - Requires manual fueling and ignition with a Lighter or Flint & Steel.
- **Advanced Smelter (`advanced_smelter` / `AdvancedSmelterBlock` / `AdvancedSmelterBlockEntity`)**:
  - Crafted on a 3x3 Crafting Table from **5 Iron Ingots + 1 Brick Furnace + 3 Smooth Stone**.
  - Reinforced with heavy iron plating, corner rivets, and an industrial firebox grate.
  - **Full Ingot Smelting**: Smelts raw ores and metal chunks (Iron, Copper, Gold, Tin, Zinc) directly into **Full Ingots** rather than single nuggets.
  - Food strictly blocked, accepts all 9 fuel tiers, requires manual fuel loading and ignition.
- **Wooden Hopper (`wooden_hopper` / `WoodenHopperBlock` / `WoodenHopperBlockEntity`)**:
  - Crafted on a 3x3 Crafting Table from **5 Planks + 1 Woven Basket** in a V-shape.
  - Features 1 buffer item slot.
  - Transfer speed: **14 ticks per item** (slightly slower than vanilla's 8 ticks).
  - Can be attached in 5 directions (down, north, south, west, east) to feed fuel into the back/sides of furnaces and ovens, or extract output from the bottom.
- **Food Oven (`oven` / `OvenBlock` / `OvenBlockEntity`)**:
  - Crafted from **2 Brick Slabs + 2 Bricks** in a 2x2 grid (`SS / ##`).
  - Replaces the vanilla Smoker.
  - **Food ONLY**: Accepts raw beef, pork, mutton, chicken, rabbit, fish, potatoes, and kelp.
  - **Non-Food Strictly Blocked**: Ores, cobblestone, and raw minerals cannot be placed into the oven.
  - Requires manual fueling and ignition.
- **Unified Heated Machine Interface (`IFueledMachine`) & Smart Auto-Refueling**:
  - Implemented across all five thermal appliances: **Brick Furnace**, **Advanced Smelter**, **Mithril Furnace**, **Food Oven**, and **Alloy Mixer**.
  - **Machine-Specific Fuel Efficiency & Speed Modifiers**:
    - Each machine features a unique **efficiency multiplier** modifying smelting/cooking speed per item while strictly **leaving fuel burn duration untouched**:
      - **Brick Furnace (`brick_furnace`)**: `0.85x` (85% speed — clay brick heat absorption makes early smelting slightly slower per item).
      - **Food Oven (`oven`)**: `0.90x` (90% speed — gentle baking insulated chamber).
      - **Advanced Smelter (`advanced_smelter`)**: `1.00x` (100% speed — baseline standard industrial smelting speed).
      - **Alloy Mixer (`alloy_mixer`)**: `1.00x` (100% speed — standard alloy fusion speed scaled proportionally with fuel heat).
      - **Mithril Furnace (`mithril_furnace`)**: `1.35x` (135% speed — hyper-conductive magical mithril lining speeds up smelting by 35%).
    - **Speed Formula**: $\text{Cook Time per Item} = \max\left(1, \text{round}\left(\frac{\text{Base Fuel Cook Speed}}{\text{Machine Efficiency Modifier}}\right)\right)$.
    - **Burn Duration Invariant**: Fuel burn duration is never shortened or modified; higher efficiency stations simply produce more output per fuel unit.
    - **Interactive GUI Badge & Tooltip**: Each fueled machine displays an efficiency badge in the header (`0.85x`, `1.00x`, `1.35x`) and renders a rich hover tooltip over the badge, flame, or arrow with speed details, per-item cook seconds, and remaining burn time.
  - **Manual Fueling (Excess Allowed)**: Players can right-click an active machine with fuel in hand to add burn duration in excess (`burnTime += duration`), or place stacks of fuel directly in the GUI.
  - **Smart Hopper Fueling (Strict Just-in-Time Delivery)**: Hoppers connected to the **BACK** face insert fuel strictly when:
    1. The machine is unlit and has no fuel in its slot (`burnTime <= 0`, loads 1 piece ready for ignition).
    2. OR the machine is burning and is within **20 ticks** of burning out (`burnTime <= 20`). The hopper inserts exactly 1 fuel item into the empty fuel slot.
  - **Seamless 5-Tick Auto-Refueling**: When `burnTime <= 5` ticks, the burning fire automatically consumes the fuel piece from the slot, resetting `burnTime` and keeping the fire continuously burning without ever extinguishing or overloading with excess fuel!
  - **Cold Machine Rekindling**: If all hopper fuel is exhausted and `burnTime` reaches 0, the machine cools down and must be rekindled with a Lighter or Flint & Steel when fuel returns.
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
- **Oven Recipe**: 2 Brick Slabs + 2 Bricks (2x2 grid).
- **Harvesting & Minimum Tool Tier**:
  - Brick Furnace, Advanced Smelter, and Food Oven strictly require a **Pickaxe of Copper tier or higher** (Copper, Bronze, Iron, Reinforced Iron, Netherite) to be mined and harvested.
  - Mining with bare hands, wrong tools, or a Silicon Pickaxe is blocked (mining speed = 0.0, drops = 0).
  - Wooden Hopper strictly requires an **Axe** (any tier: Silicon, Copper, Bronze, Iron, etc.) to be mined and harvested.
  - When broken with the correct tool, the station drops its block item and automatically spills all stored items and remaining fuels on the ground.

---

- **JEI Integration**:
  - `BrickFurnaceRecipeCategory` (`larperthanwolves:brick_furnace_smelting`): displays precise Brick Furnace smelting (Raw Ores $\rightarrow$ Nuggets, Cobblestone $\rightarrow$ Stone, etc.).
  - `RecipeTypes.SMELTING`: displays Advanced Smelter full ingot smelting (Raw Ores $\rightarrow$ Full Ingots).
  - `MachineFuelRecipeCategory`: displays accepted fuels, burn times, and cook speeds for all machines.

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
