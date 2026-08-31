# Hand-Woven Mesh & Woven Basket (`mesh` / `unbound_mesh` / `basket`)

This system provides very early-game item storage and filtering components before crafting tables or chests are accessible.

---

## ⚙️ Mechanics & Working Principles
- **Unbound Mesh $\rightarrow$ Mesh (`unbound_mesh` $\rightarrow$ `mesh`)**:
  - Crafting: 2 Sticks + 2 Ropes in 2x2 crafting grid $\rightarrow$ **Unbound Mesh**.
  - Hand Weaving Mechanic: Hold Right-Click with Unbound Mesh in main hand for **15 continuous seconds (300 ticks)**.
  - Progress Animation & Sound: Plays eating/working arm animation and continuous brush/cloth scratching sound effects.
  - Durability Bar as Progress: Durability fills from 0 to 15 (1 tick = 1s of work). Progress is saved if interrupted.
  - Completion: At 15s, transforms into the finished **Mesh** (`mesh`).
- **Woven Basket (`basket` / `BasketBlock` / `BasketBlockEntity`)**:
  - **9-slot container** (3x3 inventory).
  - Crafting: 4 Meshes in 2x2 grid.
  - **3D Inventory Item & In-World Model**: Rendered as a full 3D isometric woven wicker basket with handle in the GUI inventory, player hand, and in-world.
  - Supports horizontal placement rotation (`FACING`).
  - Supports hopper insertion and extraction through all faces (`WorldlyContainer`).
  - Tool requirement: Broken with an axe; drops inventory items on removal.

---

## 📦 Crafting & Progression
- **Unbound Mesh**: 2 Sticks + 2 Ropes (2x2 grid).
- **Mesh**: 15 seconds hand weaving.
- **Basket**: 4 Meshes (2x2 grid).
- **Available Early**: Accessible immediately at game start without needing a crafting table.

---

## 🔄 Cross-Mod Compatibility & Automation
- **Create (6.0.10+)**: Baskets can be interacted with via Create mechanical arms, chutes, and hoppers.
- **JEI Integration**: Informational description tabs for `unbound_mesh`, `mesh`, and `basket`.

---

## 🧪 Testing Guide & Edge Cases
1. **Mesh Weaving**: Hold right-click with Unbound Mesh for 7 seconds and release. Verify durability bar is ~half full. Hold again for 8 seconds; verify it completes and transforms into `mesh`.
2. **Basket 3D Model**: Open inventory or drop a basket item on the ground. Verify it renders as a complete 3D model instead of a flat 2D sprite.
3. **Basket Storage**: Place Basket. Open GUI and insert 9 different items. Break basket with an axe; verify all 9 items drop on the ground.

---

## 📂 Key Source Files
- Block: `src/main/java/io/marrybye/github/larperthanwolves/block/BasketBlock.java`
- Block Entity: `src/main/java/io/marrybye/github/larperthanwolves/block/entity/BasketBlockEntity.java`
- Models: `src/main/resources/assets/larperthanwolves/models/item/basket.json`, `models/block/basket.json`
- Item: `src/main/java/io/marrybye/github/larperthanwolves/item/ModItems.java` (`UNBOUND_MESH`, `MESH`)
