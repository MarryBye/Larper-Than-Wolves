# Early Survival Mini-Mechanics & Environment

This document consolidates all subtle, essential early-game mini-mechanics, environmental rules, foraging systems, and survival invariants.

---

## 🌲 Forest Foraging & Twigs
- **Ground Twig Patches (`twig` / `TwigBlock`)**:
  - Naturally spawn under overworld forest canopies during world generation (0–2 twigs per patch).
  - Instantly gathered by clicking or breaking by hand.
- **Tree Leaves Twig Drop**:
  - Breaking or naturally decaying leaf blocks has a **35% chance** to drop 1–2 Twigs (`ModItems.TWIG`).
  - Harvesting with Shears yields leaf blocks without dropping twigs.
- **Stick Crafting**:
  - 2 Twigs in 2x2 grid $\rightarrow$ 1 Stick (`Items.STICK`).

---

## ✂️ Silicon Shears & Grass Harvesting
- **Silicon Shears (`silicon_shears`)**:
  - Crafted from 2 Silicon Shards + 2 Sticks + 1 Rope.
  - Durability: **20 uses**.
  - Essential for harvesting short grass, tall grass, ferns, seagrass, and dead bushes.
- **Grass Cutting Rule**:
  - Breaking grass/ferns by hand destroys them with **0 drops**.
  - Harvesting with shears drops the plant items for drying on the Drying Rack.

---

## 🔥 Fire Starting, Lighter & Torch Crafting
- **Lighter (`lighter`)**:
  - Crafted from 1 Silicon Shard + 1 Iron Nugget (or copper/bronze alternative).
  - Durability: **64 uses**.
  - Used for manual ignition of Brick Furnaces, Ovens, and Alloy Mixers.
- **Torch Crafting**:
  - 1 Dry Grass / Coal + 1 Stick $\rightarrow$ Torches.

---

## 🚫 Purged Vanilla Items & Auto-Conversion
The following vanilla items are strictly purged from creative tabs, recipes, mob spawns, chest loot, and player inventories:
- **Wooden & Stone Tools**: Sword, Pickaxe, Axe, Shovel, Hoe.
- **Chainmail Armor**: All pieces.
- **Direct Diamond Tools & Armor**: Sword, Pickaxe, Axe, Shovel, Hoe, Helmet, Chestplate, Leggings, Boots, Horse Armor.
- **Vanilla Furnace, Blast Furnace, Smoker**:
  - Existing or naturally generated Blast Furnaces automatically convert to **Brick Furnaces**.
  - Smokers automatically convert to **Ovens**.
- **Iron Golems**:
  - Drop **0 iron ingots / nuggets / dusts** upon death (only poppies).

---

## 🧪 Testing Guide & Edge Cases
1. **Twig Ground Gathering**: Walk under dark oak/forest trees. Verify twig clusters on dirt/grass. Left-click to gather.
2. **Leaf Decay Twig Drop**: Break 10 leaf blocks by hand; verify ~3–4 drop twigs. Break with shears; verify leaf blocks drop instead.
3. **Disabled Item Purge**: In creative or via command `/give @p wooden_pickaxe`, attempt to acquire a wooden pickaxe; verify the item is immediately discarded.

---

## 📂 Key Source Files
- Blocks: `src/main/java/io/marrybye/github/larperthanwolves/block/TwigBlock.java`
- Handlers:
  - `src/main/java/io/marrybye/github/larperthanwolves/event/BlockBreakHandler.java`
  - `src/main/java/io/marrybye/github/larperthanwolves/event/DisabledItemsHandler.java`
- Items: `src/main/java/io/marrybye/github/larperthanwolves/item/ModItems.java`
