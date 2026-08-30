# Tree Stumps, Chisel Carving & Woodcutting Progression

This system overhauls early-game wood procurement, crafting table construction, and plank conversion.

---

## ⚙️ Mechanics & Working Principles
- **Tree Stumps (`StumpBlock` / `WorkStumpBlock`)**:
  - Naturally generated trees convert their lowest trunk log into a wood-specific **Stump** (`*_stump`).
  - High hardness (`destroyTime = 25.0f`), representing deeply anchored tree root bases.
  - Stumps are the mandatory foundation for carving early crafting tables.
- **Chisel & Crafting Table Carving (`ChiselItem`)**:
  - Right-clicking a Stump with a **Chisel** (`chisel`) carves it into a **Work Stump** (`work_stump`).
  - Successive right-clicks advance the Work Stump through 3 visual carving stages (`STAGE = 1 -> 2 -> 3`).
  - The final chisel strike transforms the Work Stump into a vanilla **Crafting Table** (`Blocks.CRAFTING_TABLE`).
  - Direct crafting of crafting tables in the 2x2 player inventory is disabled and removed.
- **Woodcutting & Axe Requirements (`AxePlankRecipe` / `WorkbenchPlankRecipe`)**:
  - Logs, wood, and planks **cannot be harvested by hand** (requires an axe).
  - **2x2 Inventory Crafting**: 1 Log + 1 Axe $\rightarrow$ **2 Planks** (the axe takes 1 durability damage and remains in the crafting grid).
  - **3x3 Crafting Table Crafting**: 1 Log $\rightarrow$ **4 Planks** (full yield without consuming axe durability).
  - Supports all wood types (Oak, Birch, Spruce, Jungle, Acacia, Dark Oak, Mangrove, Cherry, Bamboo, Crimson, Warped).

---

## 📦 Crafting & Progression
- **Chisel Crafting**: 1 Silicon Shard / Flint + 1 Stick in 2x2 grid $\rightarrow$ **Chisel**.
- **Silicon Axe**: 1 Silicon Shard + 2 Sticks + 1 Rope $\rightarrow$ **Silicon Axe** (30 durability).
- **Stump Types**: Oak, Spruce, Birch, Jungle, Acacia, Dark Oak, Mangrove, Cherry, Bamboo, Crimson, Warped.

---

## 🔄 Cross-Mod Compatibility & Automation
- **Create (6.0.10+)**: Mechanical Saw processes logs and stumps directly into 4 planks.
- **JEI Integration**: `ChiselRecipeCategory` displays Stump + Chisel $\rightarrow$ Work Stump $\rightarrow$ Crafting Table step-by-step.

---

## 🧪 Testing Guide & Edge Cases
1. **Hand Punching Log**: Attempt to break a log with an empty hand; verify breaking is extremely slow and yields 0 drops.
2. **2x2 Axe Plank Crafting**: Put 1 Oak Log + 1 Silicon Axe into 2x2 inventory crafting. Verify output is 2 Oak Planks, and the axe remains in the grid with 1 less durability.
3. **Chisel Carving**: Locate an Oak Stump. Right-click with a Chisel 4 times; verify stages 1, 2, 3 and final transformation into a Crafting Table.

---

## 📂 Key Source Files
- Blocks: `src/main/java/io/marrybye/github/larperthanwolves/block/StumpBlock.java`, `WorkStumpBlock.java`
- Item: `src/main/java/io/marrybye/github/larperthanwolves/item/ChiselItem.java`
- Recipes: `src/main/java/io/marrybye/github/larperthanwolves/recipe/AxePlankRecipe.java`, `WorkbenchPlankRecipe.java`
