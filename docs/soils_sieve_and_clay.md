# Soils, Sieve, Rich Soils & Clay Harvesting

This system overhauls soil digging, clay block harvesting rules, mineral sifting in the Sieve, and the discovery of Rich Soil mineral veins.

---

## ⚙️ Mechanics & Working Principles
- **Clay Mining & Harvesting Rules**:
  - **Shovel Mandatory**: Clay blocks (`minecraft:clay`) can **strictly be harvested ONLY with a shovel**.
  - Breaking clay by hand or with non-shovel tools completely destroys the block with **0 drops**.
  - Shovel yield: **1 Clay Ball** per clay block.
  - Mining hardness: 1.5x–1.8x slower digging time.
- **Standard Soils (Dirt, Gravel, Sand, Red Sand)**:
  - Digging & Sifting drops:
    - Silicon Shards (20–30%)
    - Flint / Silicon (8–15%)
    - Copper Dust (2–5%)
  - Dirt is fully siftable in the Sieve.
- **Rich Soils (`RichGrassBlock`, `RichFallingBlock`, `rich_*`)**:
  - 5 variants: Rich Grass Block, Rich Dirt, Rich Gravel, Rich Sand, Rich Red Sand.
  - Visuals: Silver/white mineral flecks on counterpart vanilla textures.
  - Mining requirement: Strictly requires a shovel of **Copper tier or higher** (Copper, Bronze, Iron, Reinforced Iron, Netherite) to drop the rich soil block item. Digging with hands or silicon shovel drops standard soil.
  - Sifting drops in order of rarity:
    1. Silicon Shards
    2. Flint
    3. Copper Dust
    4. Tin Dust
    5. Bronze Dust
    6. Iron Dust
    7. Gold Dust
    8. Diamond Dust
  - Worldgen: Generates in large veins (size 20, 10–12 attempts/chunk) inside soil biomes.
- **Sieve Processing (`sieve` / `SieveBlock` / `SieveBlockEntity`)**:
  - 18-slot container for automated and passive sifting of soils.

---

## 📦 Crafting & Progression
- **Sieve Recipe**: 4 Sticks + 2 Planks + 1 Mesh.
- **Tool Tier Requirement**:
  - Silicon Shovel: Basic soils, Clay (1 Clay Ball).
  - Copper Shovel+: Rich soils, Clay.

---

## 🔄 Cross-Mod Compatibility & Automation
- **Create (6.0.10+)**: Crushing Wheels and Millstone process gravel and sand into dusts and pebbles.
- **JEI Integration**: `SieveRecipeCategory` and `GravelDiggingRecipeCategory` showing drop odds and requirements.

---

## 🧪 Testing Guide & Edge Cases
1. **Clay Hand Break**: Break clay block with an empty hand; verify 0 drops.
2. **Clay Shovel Break**: Break clay block with a Silicon Shovel; verify exactly 1 Clay Ball drops.
3. **Rich Grass Harvest**: Break Rich Grass with Silicon Shovel $\rightarrow$ drops plain Dirt. Break with Copper Shovel $\rightarrow$ drops Rich Dirt. Break with Silk Touch $\rightarrow$ drops Rich Grass Block.

---

## 📂 Key Source Files
- Blocks: `src/main/java/io/marrybye/github/larperthanwolves/block/RichGrassBlock.java`, `RichFallingBlock.java`, `SieveBlock.java`
- Block Entity: `src/main/java/io/marrybye/github/larperthanwolves/block/entity/SieveBlockEntity.java`
- Handlers: `src/main/java/io/marrybye/github/larperthanwolves/event/BlockBreakHandler.java`
