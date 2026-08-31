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
  - **Hand Digging Drops**: Bare hand digging drops only Silicon Shards (20%) or the soil block (80%). Flint and Copper Dust CANNOT be extracted by bare hands.
  - **Shovel Digging & Sifting Drops**:
    - Silicon Shards (20–30%)
    - Flint / Silicon (8–15%)
    - Copper Dust (2–5%)
  - Dirt is fully siftable in the Sieve.
- **Rich Soils (`RichGrassBlock`, `RichFallingBlock`, `rich_*`)**:
  - 5 variants: Rich Grass Block, Rich Dirt, Rich Gravel, Rich Sand, Rich Red Sand.
  - Visuals: Silver/white mineral flecks on counterpart vanilla textures.
  - Mining requirement: Strictly requires a shovel of **Copper tier or higher** (Copper, Bronze, Iron, Reinforced Iron, Netherite) to drop the rich soil block item. Digging with hands or silicon shovel drops standard soil.
  - **Sifting Drops (Pure Natural Metal Dusts ONLY)**:
    - Bronze Dust (artificial alloy), Silicon Shards, and Flint are strictly excluded.
    - 1. **Copper Dust** (50%)
    - 2. **Tin Dust** (30%)
    - 3. **Iron Dust** (12%)
    - 4. **Gold Dust** (6%)
    - 5. **Diamond Dust** (2%)
  - Worldgen: Generates in large veins (size 20, 10–12 attempts/chunk) inside soil biomes.
- **Sieve Processing & Mechanics (`sieve` / `SieveBlock` / `SieveBlockEntity`)**:
  - **No Passive Sifting**: The sieve does NOT passively sift resources over time. Input is on the left (9 slots, 3x3), output is on the right (9 slots, 3x3).
  - **Active Manual Sifting (Shift + Right-Click)**:
    - Hold Shift and Right-Click on the Sieve block to perform a manual shake cycle.
    - Each block requires **5 shakes** to complete (1 click = 1 shake, 20% progress).
    - **Cooldown**: 0.5 seconds (10 ticks) between shakes.
    - **Visuals & Audio**: The wire mesh screen tray shakes and vibrates horizontally ($\pm 1$ pixel) with sand/gravel scraping sounds and flying block particles.
    - Subsequent clicks during the 0.5s animation are locked.
  - **Automated Rotational Sifting (Create 6.0.10+ Integration)**:
    - Connect Create rotational shafts, cogs, or kinetic power to the side axle drive connector sockets on the Sieve table.
    - Sifting speed scales directly with rotational RPM (e.g. 16 RPM = 1 shake per 10 ticks, 64 RPM = 4x speed).
    - Automatically vibrates the mesh and processes input continuously.
  - **Active Only When Input Is Present**: Both manual shaking and Create rotation animate and function strictly when valid siftable soils are loaded in the input slots.
  - **Catch Basin & Side Axle Socket 3D Model**: Features a wooden collection basin underneath the mesh where sifted mineral dusts drop, and protruding bronze/iron bearing sockets on the side walls for kinetic shafts.
  - **Increased Sifting Drop Rates**:
    - **Standard Soils (Gravel, Sand, Red Sand, Dirt, Grass Block, Suspicious)**:
      - Silicon Shards: **40%** (was 30%)
      - Flint: **22%** (was 15%)
      - Copper Dust: **8%** (was 5%)
    - **Rich Soils (Rich Grass Block, Rich Dirt, Rich Gravel, Rich Sand, Rich Red Sand)**:
      - 1. **Copper Dust**: **55%** (was 50%)
      - 2. **Tin Dust**: **32%** (was 30%)
      - 3. **Iron Dust**: **15%** (was 12%)
      - 4. **Gold Dust**: **8%** (was 6%)
      - 5. **Diamond Dust**: **3%** (was 2%)

---

## 📦 Crafting & Progression
- **Sieve Recipe**: 4 Sticks + 2 Planks + 1 Mesh.
- **Tool Tier Requirement**:
  - Silicon Shovel: Basic soils (yields Flint & Copper Dust), Clay (1 Clay Ball).
  - Copper Shovel+: Rich soils, Clay.

---

## 🔄 Cross-Mod Compatibility & Automation
- **Create (6.0.10+)**: 
  - Mechanical rotation directly automates the Sieve via side axle connections.
  - Crushing Wheels and Millstone process gravel and sand into dusts and pebbles.
- **JEI Integration**: `SieveRecipeCategory` (showing 5x Shift shakes / RPM requirement) and `GravelDiggingRecipeCategory`.

---

## 🧪 Testing Guide & Edge Cases
1. **Clay Hand Break**: Break clay block with an empty hand; verify 0 drops.
2. **Clay Shovel Break**: Break clay block with a Silicon Shovel; verify exactly 1 Clay Ball drops.
3. **Gravel Hand Break**: Break gravel with bare hands; verify it never drops Flint or Copper Dust.
4. **Sieve Empty Shift-Click**: Shift + Right-Click an empty Sieve; verify it does not trigger animations or sounds.
5. **Sieve Manual Shaking**: Put gravel in Sieve, Shift + Right-Click 5 times with 0.5s interval; verify 1 gravel is consumed and sifted drops appear in output.
6. **Sieve Create Automation**: Attach Create shaft at 16+ RPM to Sieve; verify it automatically shakes and sifts items.
7. **Rich Soil Sifting**: Sift Rich Dirt in the Sieve; verify it produces only Copper, Tin, Iron, Gold, and Diamond dusts.

---

## 📂 Key Source Files
- Blocks: `src/main/java/io/marrybye/github/larperthanwolves/block/RichGrassBlock.java`, `RichFallingBlock.java`, `SieveBlock.java`
- Block Entity: `src/main/java/io/marrybye/github/larperthanwolves/block/entity/SieveBlockEntity.java`
- Renderer: `src/main/java/io/marrybye/github/larperthanwolves/client/SieveBlockEntityRenderer.java`
- Handlers: `src/main/java/io/marrybye/github/larperthanwolves/event/BlockBreakHandler.java`
