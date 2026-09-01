# Tool Tiers & Mining Matrix (`ModToolMaterials` / `BlockBreakHandler`)

This document defines the complete tool tier progression, durability, harvest speeds, the exhaustive block-by-block mining access and drop matrix, and disabled tool tiers.

---

## 🚫 Disabled Vanilla Tools & Armor
- **Wooden & Stone Tools**: Purged from recipes, creative tabs, and world drops. Replaced by Silicon tools.
- **Golden Tools & Armor**: Purged from recipes, creative tabs, mob equipment/spawns, and all world loot tables.
- **Chainmail Armor**: Purged and disabled.
- **Diamond Tools & Armor**: Purged from crafting; replaced by Reinforced Iron (Diamond Ingot) upgrade progression.

---

## ⛏️ Tool Tiers & Attributes

| Tier | Durability | Speed | Attack Bonus | Repair Ingredient | Incorrect For Tag |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Silicon (Кремень)** | 55 | 2.5 | +0.0 | `larperthanwolves:silicon_shard` | `incorrect_for_silicon_tool` |
| **Copper (Медь)** | 180 | 4.5 | +1.5 | `minecraft:copper_ingot`, `copper_dust` | `incorrect_for_copper_tool` |
| **Bronze (Бронза)** | 280 | 5.5 | +2.0 | `larperthanwolves:bronze_ingot` | `incorrect_for_bronze_tool` |
| **Iron (Железо)** | 250 | 6.0 | +2.0 | `minecraft:iron_ingot` | Vanilla `incorrect_for_iron_tool` |
| **Reinforced Iron (Алмазный слиток)** | 900 | 8.0 | +3.0 | `larperthanwolves:diamond_ingot` | Vanilla `incorrect_for_diamond_tool` |
| **Mithril (Мифрил - Эндгейм)** | 1500 | 10.0 | +4.0 | `larperthanwolves:mithril_ingot` | Vanilla `incorrect_for_netherite_tool` |

---

## 🔍 Exhaustive Mining Matrix & Drop Rules

### 0. Primitive Tools (Заострённая палка и Заострённый камешек)
* **Pointed Stick (`pointed_stick`, 2 uses)**:
  * **Stone (`minecraft:stone`)**: 1st hit fractures stone into **Cracked Stone** (`cracked_stone`); 2nd hit converts cracked stone into **Cobblestone** and drops **1 Stone Pebble** (`stone_nugget`).
  * **Strictly Blocked**: Cannot mine any other block, ore, or rock (speed = 0.0, cancelled).
* **Pointed Pebble (`pointed_pebble`, 4 uses)**:
  * **Copper Ore (`minecraft:copper_ore` / `deepslate_copper_ore`)**: drops **1 Copper Dust** (`copper_dust`) and converts the ore block into **Cobblestone** (`minecraft:cobblestone`).
  * **Stone (`minecraft:stone`)**: 1-hit fast mining (speed `2.0f`, no cracked stone stage) converts stone into **Cobblestone** and drops **1 Stone Pebble** (`stone_nugget`).
  * **Strictly Blocked**: Cannot mine Tin Ore, Iron Ore, Coal Ore, Sandstone, Granite, Diorite, Andesite, Deepslate, or any other ores/rocks (speed = 0.0, cancelled).

### 1. Silicon Tier (Кремниевая кирка / лопата)
* **Allowed Mining & Drops**:
  * **Coal Ore**: drops 1 `Items.COAL`.
  * **Copper Ore**: drops 1 `ModItems.COPPER_DUST`.
  * **Stone / Cobblestone**: drops 2–4 `ModItems.STONE_NUGGET` (Stone Pebbles).
  * **Granite**: drops 2–4 `ModItems.GRANITE_NUGGET`.
  * **Diorite**: drops 2–4 `ModItems.DIORITE_NUGGET`.
  * **Andesite**: drops 2–4 `ModItems.ANDESITE_NUGGET`.
  * **Calcite**: drops 2–4 `ModItems.CALCITE_NUGGET`.
  * **Sandstone (all variants)**: drops 2–4 `Items.SAND` (or `Items.RED_SAND`).
  * **Clay (Shovel only)**: drops 1 `Items.CLAY_BALL`.
  * **Basic Soils (Dirt, Gravel, Sand, Red Sand)**: drops silicon shards (20%), flint (8%), copper dust (2%), or standard soil.
* **Strictly Blocked (Hardness = $\infty$ / 0 Drops)**:
  * Deepslate, Tuff, Dripstone, Netherrack, Tin Ore, Iron Ore, Zinc Ore, Gold/Diamond/Redstone/Lapis/Emerald ores, Rich Soils, Obsidian, Ancient Debris, any block at or below $Y \le 0$.

---

### 2. Copper Tier (Медная кирка / лопата)
* **Allowed Mining & Drops**:
  * **Everything in Silicon Tier** (Stone/Granite/Diorite/Andesite/Calcite drop 2–4 Pebbles).
  * **Copper Ore**: drops 1 `Items.RAW_COPPER` (advances from dust to raw chunk).
  * **Tin Ore**: drops 1 `ModItems.TIN_DUST`.
  * **Tin / Bronze Blocks**: can be harvested.
  * **Rich Soils (Shovel)**: harvests Rich Grass $\rightarrow$ Rich Dirt, Rich Dirt/Gravel/Sand $\rightarrow$ respective rich soil blocks.
* **Strictly Blocked**:
  * Iron Ore, Zinc Ore, Deepslate/Tuff/Netherrack, Gold/Diamond/Redstone ores, Obsidian, Ancient Debris, $Y \le 0$ layer.

---

### 3. Bronze Tier (Бронзовая кирка / лопата)
* **Allowed Mining & Drops**:
  * **Everything in Copper Tier**.
  * **Tin Ore**: drops 1 `ModItems.RAW_TIN` (advances from dust to raw chunk).
  * **Iron Ore**: drops 1 `ModItems.IRON_DUST`.
  * **Deepslate / Cobbled Deepslate**: drops 2–4 `ModItems.DEEPSLATE_NUGGET`.
  * **Tuff**: drops 2–4 `ModItems.TUFF_NUGGET`.
  * **Dripstone Block / Pointed Dripstone**: drops 2–4 / 1 `ModItems.DRIPSTONE_NUGGET`.
  * **Netherrack**: drops 2–4 `ModItems.NETHERRACK_NUGGET`.
  * **Deepslate Coal / Copper / Tin / Iron Ores**: can be mined at $Y \le 0$, dropping their respective nuggets/dusts.
* **Strictly Blocked**:
  * Zinc Ore (`create:zinc_ore`), Gold Ore, Diamond Ore, Redstone Ore, Lapis Ore, Emerald Ore, Nether/End rocks (Blackstone, Basalt, Quartz Ore, End Stone), Obsidian, Ancient Debris.

---

### 4. Iron Tier (Железная кирка / лопата)
* **Allowed Mining & Drops**:
  * Full access to all standard Overworld rocks, deepslate, and ores.
  * **Whole Blocks & Raw Chunks**: drops vanilla `RAW_IRON`, `RAW_COPPER` (1 piece base, scaled strictly via Fortune), `RAW_GOLD`, `COBBLESTONE`, `COBBLED_DEEPSLATE`, etc.
  * **Raw Copper Drop Rate Balance**: Copper Ore (`minecraft:copper_ore` / `deepslate_copper_ore`) drops strictly **1 Raw Copper** by default (vanilla 2-5 multi-drop is removed); additional yield scales exclusively through the Fortune enchantment.
  * **Zinc Ore (`create:zinc_ore` / `create:raw_zinc`)**: fully harvestable.
  * **Nether & End Blocks**: Nether Quartz, Gold Ore, Blackstone, Basalt, End Stone, Amethyst.
* **Strictly Blocked**:
  * Obsidian, Crying Obsidian, Respawn Anchor, Ancient Debris.

---

### 5. Reinforced Iron (Diamond Ingot) Tier
* **Full Access**:
  * Obsidian, Crying Obsidian, Respawn Anchor, Ancient Debris.
  * **Mithril Ore (`mithril_ore`)**: Mines Mithril Ore into **Mithril Dust** (`mithril_dust`).

---

### 6. Mithril Tier — Premier Endgame Tier
* **Supreme Speed & Access**:
  * 1500 Durability, 10.0f Mining Speed, +4.0 Attack Bonus.
  * **Direct Raw Mithril Harvesting**: Mining Nether Mithril Ore directly yields **Raw Mithril** (`raw_mithril`), bypassing the dust requirement.
  * Full access to Obsidian, Ancient Debris, workstations, and all blocks across all dimensions.

---

## 🪓 Woodcutting, Workstation & Shovel Rules

* **Workstations & Thermal Machines (Copper Pickaxe or Higher Mandatory)**:
  * Stone and metal workstations (**Brick Furnace**, **Advanced Smelter**, **Mithril Furnace**, **Food Oven**, **Alloy Mixer**, **Hand Mill**, **Kinetic Piston**, **Entity Observer**, vanilla Furnaces/Smokers) strictly require a **Pickaxe of Copper tier or higher** (Copper, Bronze, Iron, Reinforced Iron, Mithril) to be mined and harvested.
  * Breaking workstations by hand, with tools other than pickaxes, or with a Silicon Pickaxe is blocked (mining speed = 0.0, drops = 0).
* **Axe Mandatory for Wood & Wooden Objects**:
  * Hand punching logs, wood, stripped wood, stumps, work stumps, planks, slabs, stairs, fences, gates, doors, trapdoors, as well as wooden machines and containers (**Woven Basket**, **Drying Rack**, **Wooden Hopper**, **Sieve Table**, **Filter Grate**, **Mill Crank**) is completely blocked (mining speed = 0.0, drops = 0).
  * Any axe (Silicon, Copper, Bronze, Iron, Reinforced Iron, Mithril) can mine and harvest all wooden blocks, dropping the block item and any stored container contents.
* **Shovel Mandatory for Clay**:
  * Hand breaking or non-shovel breaking of clay blocks drops 0 items.
  * Shovel harvest yields **1 Clay Ball** (down from vanilla 4) with ~1.6x longer digging time.

---

## 🧪 Testing Guide & Edge Cases
1. **Station Mining Test**: Place a Brick Furnace / Oven / Advanced Smelter / Mill / Alloy Mixer. Try mining with a Silicon Pickaxe $\rightarrow$ blocked (speed = 0). Mine with a Copper Pickaxe $\rightarrow$ harvests the station item and drops any stored inventory contents.
2. **Wooden Block Axe Test**: Place a Basket, Drying Rack, Wooden Hopper, Sieve Table, Filter Grate, or Mill Crank. Try punching by hand $\rightarrow$ blocked (speed = 0). Mine with any axe (Silicon, Copper, Bronze, Iron) $\rightarrow$ harvests the block item and drops stored items.
3. **Tier Restriction Test**: Place an Iron Ore block. Mine it with Silicon Pickaxe $\rightarrow$ breaking is cancelled. Mine with Bronze Pickaxe $\rightarrow$ drops 1 `iron_dust`. Mine with Iron Pickaxe $\rightarrow$ drops 1 `raw_iron`.
4. **Deepslate Layer Test**: Teleport to $Y = -10$. Try mining stone/deepslate with a Copper Pickaxe $\rightarrow$ blocked. Mine with Bronze Pickaxe $\rightarrow$ drops `deepslate_nugget`.
5. **Zinc Ore Test**: Place `create:zinc_ore`. Try mining with Bronze Pickaxe $\rightarrow$ blocked. Mine with Iron Pickaxe $\rightarrow$ drops `create:raw_zinc`.

---

## 📂 Key Source Files
- Materials: `src/main/java/io/marrybye/github/larperthanwolves/item/ModToolMaterials.java`
- Mining Logic: `src/main/java/io/marrybye/github/larperthanwolves/event/BlockBreakHandler.java`
- Disabled Items: `src/main/java/io/marrybye/github/larperthanwolves/event/DisabledItemsHandler.java`
- Items: `src/main/java/io/marrybye/github/larperthanwolves/item/ModItems.java`
