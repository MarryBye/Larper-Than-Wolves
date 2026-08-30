# Hand Mill & Mill Crank (`mill` / `mill_crank` / `MillBlock` / `MillCrankBlock`)

The **Hand Mill** (Quern / Молотилка) and **Mill Crank** (Рукоять) form the central mechanical grinding station for grinding metal ingots, raw ores, minerals, and bones into fine dusts and bone meal.

---

## ⚙️ Mechanics & Working Principles
- **Block Layout & Components**:
  - **Hand Mill (`mill`)**: 4-slot container (1 input slot, 3 output slots).
  - **Mill Crank (`mill_crank`)**: Placed on top of the Mill. Rotates 360° upon right-click.
- **Grinding Cycle & Progress**:
  - Requires **100% grinding progress** to complete 1 recipe.
  - GUI features an animated 24x17 filling progress arrow indicating 0% to 100%.
- **Manual Operation (Cranking)**:
  - Right-clicking the crank rotates the handle 360° over **10 ticks (0.5 seconds)**.
  - Adds **5% progress** per full rotation (20 full rotations = 100% completion).
  - Interaction lock prevents spamming clicks faster than the 0.5s rotation animation.
- **Automated Operation (Create Kinetic Energy)**:
  - If Create is loaded, shafts, cogwheels, or rotational power sources connected to the Mill grind items continuously.
  - Processing speed scales linearly with rotational RPM:
    - 16 RPM: standard baseline speed.
    - 64 RPM: 4x speed.
    - 256 RPM: 16x speed.
  - Create Hand Crank can also be mounted directly on top of the Mill.
- **Core Grinding Ratios**:
  - 1 Metal Ingot (Iron, Copper, Gold, Tin, Bronze) $\rightarrow$ 8 Dusts (2 Dust = 1 Nugget, 4 Nuggets = 1 Ingot).
  - 1 Diamond $\rightarrow$ 8 Diamond Dust.
  - 1 Diamond Ingot $\rightarrow$ 8 Diamond Dust + 8 Iron Dust + 8 Copper Dust (conserves alloy ingredients).
  - 1 Raw Ore chunk / Metal Nugget $\rightarrow$ 2 Dusts.
  - 2 Bones $\rightarrow$ 1 Bone Meal (Vanilla bone meal crafting is removed).
  - Cobblestone / Stone / Pebbles $\rightarrow$ Gravel / Sand.
- **Hopper & Automation**:
  - Top & Side faces: Input extraction/insertion.
  - Bottom face: Output extraction (`WorldlyContainer`).

---

## 📦 Crafting & Progression
- **Hand Mill Recipe**: 4 Smooth Stone (corners) + 2 Bronze Ingots (sides) + 2 Planks (top/bottom) + 1 Stone (center).
- **Mill Crank Recipe**: 3 Sticks + 1 Rope.
- **Station**: 3x3 Crafting Table.
- **Progression Role**: Unlocks Bone Meal, metal dust doubling, and alloy component recycling.

---

## 🔄 Cross-Mod Compatibility & Automation
- **Create (6.0.10+)**: Full rotational kinetic drive integration via `CreateKineticCompat`.
- **JEI (19.44+)**: Dedicated `MillRecipeCategory` displaying input items, dust outputs, and required rotations/energy.

---

## 🧪 Testing Guide & Edge Cases
1. **Manual Grinding**: Insert 2 bones into the Hand Mill. Mount the Mill Crank on top. Right-click 20 times; verify bone meal is produced and progress resets.
2. **Animation Interpolation**: Observe the client-side crank handle rotation. Verify smooth 360° visual rotation with partial tick interpolation.
3. **Create RPM Scaling**: Connect a Create motor at 128 RPM. Verify rapid automatic grinding without player intervention.
4. **Hopper Insertion/Extraction**: Attach a hopper to the top face and another to the bottom face. Verify items insert and extract accurately without jamming.

---

## 📂 Key Source Files
- Blocks: `src/main/java/io/marrybye/github/larperthanwolves/block/MillBlock.java`, `MillCrankBlock.java`
- Block Entities: `src/main/java/io/marrybye/github/larperthanwolves/block/entity/MillBlockEntity.java`, `MillCrankBlockEntity.java`
- Renderer: `src/main/java/io/marrybye/github/larperthanwolves/client/MillCrankRenderer.java`
- Menu & Screen: `src/main/java/io/marrybye/github/larperthanwolves/menu/MillMenu.java`, `MillScreen.java`
- Registry: `src/main/java/io/marrybye/github/larperthanwolves/recipe/MillRegistry.java`
