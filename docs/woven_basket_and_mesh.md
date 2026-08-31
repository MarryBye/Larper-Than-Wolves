# Hand-Woven Mesh, Knitting Needles & Woven Basket (`mesh` / `unbound_mesh` / `knitting_needles` / `basket`)

This system provides early-game item storage, filtering components, hand weaving, and instant craft acceleration via knitting needles.

---

## ⚙️ Mechanics & Working Principles
- **Unbound Mesh $\rightarrow$ Mesh (`unbound_mesh` $\rightarrow$ `mesh`)**:
  - Crafting: 2 Sticks + 2 Ropes in 2x2 crafting grid $\rightarrow$ **Unbound Mesh**.
  - **Method 1 - Hand Weaving**: Hold Right-Click with Unbound Mesh in main hand for **15 continuous seconds (300 ticks)**.
    - Instantly snaps to the center of the screen with a continuous rhythmic bobbing animation.
    - Scratching cloth sounds play every 4 ticks without food particles.
    - Durability bar smoothly fills up showing progress (saved if interrupted).
    - Completion: At 15s, transforms into finished **Mesh** (`mesh`).
  - **Method 2 - Knitting Needles (Спицы)**: Combine 1 Unbound Mesh + 1 Knitting Needles in any 2x2 or 3x3 crafting grid to **instantly craft a finished Mesh**.
    - The Knitting Needles lose 1 durability and remain in the crafting grid.
- **Knitting Needles (`bronze_knitting_needles` / `iron_knitting_needles`)**:
  - **Bronze Knitting Needles**: 64 durability. Crafted from 2 Bronze Ingots placed diagonally.
  - **Iron Knitting Needles**: 256 durability. Crafted from 2 Iron Ingots placed diagonally.
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
- **Bronze Knitting Needles**: 2 Bronze Ingots (2x2 grid).
- **Iron Knitting Needles**: 2 Iron Ingots (2x2 grid).
- **Mesh**: 15 seconds hand weaving OR Unbound Mesh + Knitting Needles in crafting grid.
- **Basket**: 4 Meshes (2x2 grid).
- **Available Early**: Accessible immediately at game start without needing a crafting table.

---

## 🔄 Cross-Mod Compatibility & Automation
- **Create (6.0.10+)**: Baskets can be interacted with via Create mechanical arms, chutes, and hoppers.
- **JEI Integration**: Informational description tabs for `unbound_mesh`, `bronze_knitting_needles`, `iron_knitting_needles`, `mesh`, and `basket`.

---

## 🧪 Testing Guide & Edge Cases
1. **Mesh Weaving**: Hold right-click with Unbound Mesh. Verify it centers instantly and plays bobbing animation for 15s before turning into `mesh`.
2. **Knitting Needles Instant Craft**: Place 1 Unbound Mesh + 1 Bronze Knitting Needles into 2x2 crafting grid. Verify result is 1 `mesh` and the needles stay with 1 durability lost.
3. **Basket 3D Model**: Open inventory or drop a basket item on the ground. Verify it renders as a complete 3D model instead of a flat 2D sprite.

---

## 📂 Key Source Files
- Block: `src/main/java/io/marrybye/github/larperthanwolves/block/BasketBlock.java`
- Block Entity: `src/main/java/io/marrybye/github/larperthanwolves/block/entity/BasketBlockEntity.java`
- Items: `src/main/java/io/marrybye/github/larperthanwolves/item/UnboundMeshItem.java`, `KnittingNeedlesItem.java`, `ModItems.java`
- Client: `src/main/java/io/marrybye/github/larperthanwolves/client/UnboundMeshClientExtension.java`, `ClientProgressHelper.java`
- Recipe: `src/main/java/io/marrybye/github/larperthanwolves/recipe/KnittingMeshRecipe.java`
