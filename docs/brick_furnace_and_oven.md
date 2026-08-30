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
- **Unified Fuel & Heat Mechanics (`FuelRegistry`)**:
  - Custom fuel durations and burn speeds based on fuel type (Dry Grass, Twigs, Sticks, Planks, Logs, Coal, Charcoal).
  - Unlit state when fueled until sparked by a fire starter.

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

---

## 📂 Key Source Files
- Blocks: `src/main/java/io/marrybye/github/larperthanwolves/block/BrickFurnaceBlock.java`, `OvenBlock.java`
- Block Entities: `src/main/java/io/marrybye/github/larperthanwolves/block/entity/BrickFurnaceBlockEntity.java`, `OvenBlockEntity.java`
- Fuel Registry: `src/main/java/io/marrybye/github/larperthanwolves/block/entity/FuelRegistry.java`
