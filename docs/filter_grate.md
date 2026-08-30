# Filter Grate (`filter_grate` / `FilterGrateBlock` / `FilterGrateBlockEntity`)

The **Filter Grate** is a mechanical item sorting and filtering half-block with an oak plank frame and a wire mesh top. It allows selective item entity pass-through based on a 9-slot phantom filter grid with redstone power inversion.

---

## ⚙️ Mechanics & Working Principles
- **Block States & Properties**:
  - `POWERED` (`Boolean`): Inverts filter logic when redstone power is applied.
- **Collision & Solid Properties**:
  - **Living Entities**: Full solid collision surface (`16x8x16` slab geometry). Players and mobs walk across the top without falling through.
  - **Item Entities**: Pass-through collision enabled. Items falling or resting on top of the grate are evaluated by the filter logic.
- **Phantom Filter Grid (GUI)**:
  - 9 phantom/ghost item slots (3x3 grid).
  - Clicking with an item creates a ghost filter entry without consuming the player's item.
  - Clicking with an empty hand clears the ghost filter in that slot.
  - Quick-clearing or replacing filters requires no item management overhead.
- **Item Passing Logic**:
  - **Unpowered (Normal Mode)**:
    - If filter grid is empty: Blocks ALL items on top.
    - If filter grid contains items: Only items matching the ghost filters pass through the grating to the space below.
  - **Powered by Redstone (Inverted Mode)**:
    - If filter grid contains items: Filter is inverted — items matching the ghost filters remain blocked on top, while all non-matching items pass through below.
- **Rendering & Cutout**:
  - Top mesh texture uses `RenderType.cutout()` for sharp transparency.
  - Hollow hopper interior prevents Z-fighting with blocks underneath.

---

## 📦 Crafting & Progression
- **Recipe**: 4 Planks (corners) + 4 Sticks (edges) + 1 Mesh (center).
- **Station**: 3x3 Crafting Table.
- **Tier Requirement**: Requires woven `Mesh` (`mesh`), making it available as soon as hand-weaving and woodworking are unlocked.

---

## 🔄 Cross-Mod Compatibility & Automation
- **Create (6.0.10+)**: Acts as a compact pre-filter above Create chutes, water streams, brass tunnels, and hoppers.
- **Hopper Integration**: Can sit directly above a hopper, chest, or basket to filter incoming drops from mob farms or querns.
- **JEI Integration**: Supports phantom dragging and item clicking from JEI search into filter slots.

---

## 🧪 Testing Guide & Edge Cases
1. **Solid Collision for Mobs/Players**: Walk across the filter grate. Verify player and mobs cannot fall through.
2. **Normal Filter Mode**: Set filter slot 0 to `Iron Nugget`. Drop `Iron Nugget` and `Cobblestone` onto the grate. Verify `Iron Nugget` falls through to the floor below, while `Cobblestone` remains on top.
3. **Inverted Redstone Mode**: Power the grate with redstone. Drop `Iron Nugget` and `Cobblestone`. Verify `Cobblestone` passes through while `Iron Nugget` stays on top.
4. **GUI Phantom Preservation**: Reopen the GUI after dropping items or restarting the world. Verify phantom items persist and player inventory was not modified.

---

## 📂 Key Source Files
- Block: `src/main/java/io/marrybye/github/larperthanwolves/block/FilterGrateBlock.java`
- Block Entity: `src/main/java/io/marrybye/github/larperthanwolves/block/entity/FilterGrateBlockEntity.java`
- Menu: `src/main/java/io/marrybye/github/larperthanwolves/menu/FilterGrateMenu.java`
- Screen: `src/main/java/io/marrybye/github/larperthanwolves/client/FilterGrateScreen.java`
- Blockstates & Models: `src/main/resources/assets/larperthanwolves/models/block/filter_grate.json`
