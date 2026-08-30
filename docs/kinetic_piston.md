# Kinetic Piston (`kinetic_piston` / `KineticPistonBlock`)

The **Kinetic Piston** is an early/mid-game heavy mechanical pusher block capable of catapulting entities (players, mobs, dropped items) and launching loose single blocks as airborne projectiles across ~10 blocks.

---

## ⚙️ Mechanics & Working Principles
- **Block States & Properties**:
  - `FACING` (`Direction.NORTH, SOUTH, EAST, WEST, UP, DOWN`): Supports full 6-directional placement.
  - `EXTENDED` (`Boolean`): Set to `true` during the 2-tick extension stroke when powered by redstone.
- **Bounding Box & Collision**:
  - Base casing: 12px deep base (0 to 12 along facing axis).
  - Piston head: 4px thick pusher head extending to 16 (or beyond during extension).
  - Proper VoxelShape geometry without Z-fighting or entity clipping.
- **Activation Logic**:
  - Triggers **once per rising redstone pulse** (unpowered $\rightarrow$ powered transition).
  - Remains extended until redstone power is removed.
  - Retracts to idle (`EXTENDED = false`) when unpowered.
- **Entity Launching (Catapult Mode)**:
  - Scans an AABB box $1.0 \times 1.0 \times 1.0$ directly in front of the piston face.
  - Applies a directional impulse velocity vector of magnitude **1.5–1.8** in the facing direction + slight upward lift for grounded entities.
  - Works on players, living mobs, items, and minecarts.
- **Block Launching (Projectile Mode)**:
  - Inspects the blocks in front of the piston face:
    - If **0 blocks** in front: Only entities are launched.
    - If **exactly 1 solid block** in front and the block beyond is air: Removes the block and spawns a `FallingBlockEntity` with directional horizontal/vertical momentum.
    - If **2 or more consecutive blocks** in front: Does not launch blocks (piston push limit is 1 block).
- **Audio & Visual Effects**:
  - Plays heavy mechanical piston extend/retract sound effects and piston steam particles.

---

## 📦 Crafting & Progression
- **Recipe**: 3 Bronze Blocks (top row) + 4 Cobblestone (sides) + 1 Bronze Ingot (center) + 1 Redstone Dust (bottom).
- **Station**: 3x3 Crafting Table.
- **Tier Requirement**: Requires Bronze Ingot and Bronze Blocks, placing it squarely in the Bronze Age before Iron automation.

---

## 🔄 Cross-Mod Compatibility & Automation
- **Create (6.0.10+)**: Can launch items directly onto conveyor belts, into chutes, or across item collectors.
- **Redstone Automation**: Easily chained with repeaters, clocks, and `EntityObserverBlock` to build automated player launchers, mob traps, and item transport lines.

---

## 🧪 Testing Guide & Edge Cases
1. **Directional Testing**: Place kinetic pistons facing UP, DOWN, NORTH, SOUTH, EAST, WEST. Verify models and piston heads align with orientation.
2. **Single Pulse Verification**: Place a continuous redstone lever on the piston. Turn it ON; verify piston fires exactly once and remains extended without looping sound/ticks. Turn OFF; verify it smoothly retracts.
3. **1-Block Launch Test**: Place 1 cobblestone block in front of a horizontal piston. Power the piston; verify the block turns into a falling block entity and flies ~10 blocks forward before landing.
4. **2-Block Obstruction Test**: Place 2 cobblestone blocks in front. Power the piston; verify neither block is launched.
5. **Entity Launch Test**: Stand directly in front of the piston and trigger it; verify player receives immediate knockback/impulse in the facing direction.

---

## 📂 Key Source Files
- Block: `src/main/java/io/marrybye/github/larperthanwolves/block/KineticPistonBlock.java`
- Blockstates: `src/main/resources/assets/larperthanwolves/blockstates/kinetic_piston.json`
- Models:
  - Base: `src/main/resources/assets/larperthanwolves/models/block/kinetic_piston_base.json`
  - Base Extended: `src/main/resources/assets/larperthanwolves/models/block/kinetic_piston_base_extended.json`
  - Head: `src/main/resources/assets/larperthanwolves/models/block/kinetic_piston_head.json`
- Item Model: `src/main/resources/assets/larperthanwolves/models/item/kinetic_piston.json`
