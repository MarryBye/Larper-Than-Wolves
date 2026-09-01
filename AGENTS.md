# AGENTS.md

This file is a comprehensive guide for AI agents working on this NeoForge Minecraft mod project.

## Project Overview
- **Mod ID**: `larperthanwolves`
- **Package**: `io.marrybye.github.larperthanwolves`
- **Author**: MarryBye
- **AI Assistance**: Gemini (Google DeepMind)
- **License**: MIT License (Copyright (c) 2026 MarryBye)
- **Minecraft**: 1.21.1
- **NeoForge**: 21.1.248
- **Java**: 21
- **Build Tool**: Gradle with NeoForge ModDev plugin 2.0.144
- **Current Version**: 1.32.3

## Project Architecture & Progression

Inspired by *Better Than Wolves*, this hardcore survival overhaul rebuilds the early and mid-game:

### 🪨 Early Resource Gathering (Pointed Stick, Pointed Pebble & Cracked Stone)
- **Pointed Stick (`pointed_stick`)**:
  - Crafted from 1 Stick in any 2x2/3x3 crafting grid.
  - Durability: **2 uses**.
  - **Stone Harvesting & 2-Stage Cracking**:
    - Breaking `minecraft:stone` with a Pointed Stick transforms it into **Cracked Stone** (`cracked_stone`) and consumes 1 durability.
    - Breaking `cracked_stone` with a Pointed Stick converts it into **Cobblestone** (`minecraft:cobblestone`), drops **1 Stone Pebble** (`stone_nugget`), and consumes the 2nd durability (breaking the stick).
- **Pointed Pebble (`pointed_pebble`)**:
  - Crafted from any stone pebble / nugget (`#larperthanwolves:pebbles`) in crafting grid.
  - Durability: **4 uses**.
  - **Copper Ore Chipping & 1-Hit Fast Stone Harvesting**:
    - Breaking Copper Ore (`minecraft:copper_ore` / `deepslate_copper_ore`) with a Pointed Pebble drops **1 Copper Dust** (`copper_dust`) and converts the ore into **Cobblestone** (which cannot be mined with pebbles or sticks).
    - Breaking Stone (`minecraft:stone`) with a Pointed Pebble converts it into **Cobblestone** (`minecraft:cobblestone`) in **1 single hit** (faster than pointed stick, no cracked stone stage) and drops **1 Stone Pebble** (`stone_nugget`).
    - Consumes 1 durability per block harvested.
    - Cannot mine tin ore, iron ore, or any other ores / rocks.

### ⛏️ Tool Tiers & Dig Tier Matrix
- **Disabled Tools & Armor**: Wooden, Stone, Golden, vanilla Diamond, and Netherite tools and armor are completely disabled and purged from recipes, creative tabs, mob equipment/spawns, and world loot tables.
- **Workstations & Machines Harvesting Rule**: Stone/metal processing workstations (**Brick Furnace**, **Advanced Smelter**, **Food Oven**, **Alloy Mixer**, **Hand Mill**, **Kinetic Piston**, **Entity Observer**, vanilla Furnaces/Smokers) strictly require a **Pickaxe of Copper tier or higher** (Copper, Bronze, Iron, Reinforced Iron) to be mined and harvested. Breaking by hand or with a Silicon Pickaxe is blocked (mining speed = 0.0, drops = 0). When mined with Copper+ pickaxes, the workstation drops itself and spills its inventory contents.
- **Wooden Objects & Containers Harvesting Rule**: Wooden workstations, machines, and storage (**Woven Basket**, **Drying Rack**, **Wooden Hopper**, **Sieve Table**, **Filter Grate**, **Mill Crank**, stumps, logs, planks) strictly require an **Axe** (any tier: Silicon, Copper, Bronze, Iron, Reinforced Iron) to be mined and harvested. Breaking by hand is blocked (mining speed = 0.0, drops = 0).
- **Silicon (Кремень)**: Durability 55. Mines Coal (item), Copper (Copper Dust), Stone/Granite/Diorite/Andesite/Calcite (Pebbles, 2-4), Sandstone (Sand, 2-4). Shovel mines Clay (1 Clay Ball) and basic soils. Cannot mine workstations, deepslate, zinc, or any other ores.
- **Copper (Медь)**: Durability 180. Mines Coal (item), Copper (Raw Copper), Tin (Tin Dust), Workstations (Brick Furnace, Smelter, Oven, Mixer, Mill, Piston, Observer), Stone/Granite/Diorite/Andesite/Calcite (Pebbles, 2-4). Shovel can mine and harvest Rich Soils (`rich_grass_block`, `rich_dirt`, `rich_gravel`, `rich_sand`, `rich_red_sand`) and Clay. Cannot mine deepslate, zinc, or iron+.
- **Bronze (Бронза)**: Durability 280. Mines Coal (item), Copper (Raw Copper), Tin (Raw Tin), Iron (Iron Dust), Workstations, Stone/Granite/Diorite/Andesite/Calcite (Pebbles), Deepslate/Tuff/Dripstone/Netherrack (Pebbles: `deepslate_nugget`, `tuff_nugget`, `dripstone_nugget`, `netherrack_nugget`, 2-4). Shovel mines Rich Soils and Clay. Cannot mine zinc ore or high-tier ores.
- **Iron (Железо)**: Standard metal. Full mining access for standard rocks & ores as whole blocks / raw chunks, including Zinc Ore (`create:raw_zinc`) and all Workstations. Shovel mines Rich Soils and Clay. Cannot mine Ancient Debris or Obsidian.
- **Reinforced Iron (Diamond Ingot - Final Tier)**: Durability 900. Final pinnacle endgame tier. Full access to all blocks including Ancient Debris and Obsidian.

### ⚙️ Hand Mill & Mill Crank (Молотилка и Рукоять)
- **Hand Mill (`mill` / `MillBlock`)**:
  - Crafted from 4 Smooth Stone + 2 Bronze Ingots + 2 Planks + 1 Stone.
  - Grind Items into Dusts: 1 input slot, 3 output slots, 100% progress threshold.
  - GUI Progress Indicator: Animated filling arrow (24x17) smoothly indicates active grinding progress (0% $\rightarrow$ 100%).
  - Mechanical Crank Operation: Operated via a **Mill Crank** placed on any face (top, sides, bottom).
  - Right-Click Cranking: Right-clicking the crank rotates the handle 360° over **0.5 seconds (10 ticks)** and advances progress by **5%** (20 rotations = 100% completion). Subsequent right-clicks are locked until the current 0.5s rotation finishes.
  - **Create Rotational Force Automation**:
    - Optional integration with Create 6.0.10+.
    - Connecting Create rotating shafts, cogs, or engines directly to the **top face** of the Mill automatically grinds items continuously (strictly accepts rotation from the top face).
    - Grinding speed scales directly with rotational RPM (e.g. 16 RPM = normal speed, 64 RPM = 4x speed, 256 RPM = 16x speed).
    - Supports Create Hand Cranks placed on the mill, and Mill Crank can be mounted on Create kinetic blocks and turned to provide manual kinetic rotational force to the connected network.
  - Base Grinding Ratios:
    - 1 Ingot (Iron, Copper, Gold, Tin, Bronze) $\rightarrow$ 8 Dusts (2 Dust = 1 Nugget, 4 Nuggets = 1 Ingot).
    - 1 Diamond $\rightarrow$ 8 Diamond Dust.
    - 1 Diamond Ingot $\rightarrow$ 8 Diamond Dust + 8 Iron Dust + 8 Copper Dust (conserves alloy ingredients).
    - 1 Raw Ore chunk / Metal Nugget $\rightarrow$ 2 Dusts.
    - 1 Bone $\rightarrow$ 1 Bone Meal (`minecraft:bone_meal`).
    - Rocks / Stones $\rightarrow$ Gravel / Sand.
  - Hopper & Automation: Inputs through top/sides, outputs extracted through bottom face (`WorldlyContainer`).
- **Mill Crank (`mill_crank` / `MillCrankBlock`) & Unified Kinetic Interface (`IKineticReceiver`)**:
  - Crafted from 3 Sticks + 1 Rope.
  - Directional in all 6 orientations (Up, Down, North, South, East, West). Can be placed on floors, ceilings, and horizontal walls.
  - **Unified `IKineticReceiver` Interface**: Operates seamlessly across any machine implementing the unified kinetic interface (`MillBlock`, `SieveBlock`) and Create kinetic components.
  - **Sieve Table Compatibility**: Can be mounted directly on top of the Sieve Table or on its side axle sockets to manually shake/sift resources (1 crank rotation = 1 shake, 5 rotations = 100% sifting).
  - Custom Create-style 3D block model and full 3D inventory item model (wide hub, extended horizontal lever arm, vertical grip handle with top knob).
  - Animated 3D rotating handle rendered via client-side `MillCrankRenderer` with tick interpolation across all 6 facings.
- **Bone Meal & Mob Drops Overhaul**:
  - Vanilla bone meal crafting (bone $\rightarrow$ 3 bone meal, bone block $\rightarrow$ 9 bone meal) is disabled and purged.
  - Bones can only be turned into bone meal in the Hand Mill (1 Bone $\rightarrow$ 1 Bone Meal).
  - Peaceful animals (cows, sheep, pigs, horses, goats, camels, sniffers, etc.) drop 1–2 bones (30% chance for large animals, 15% for small).
  - Zombies and subspecies (Zombie, Husk, Drowned, Zombie Villager, Zombified Piglin, Zoglin) drop 1 bone with a 25% chance.

### 🌲 Flora, Twigs & Forest Foraging
- **Twigs (Веточки)**: Small fallen wood branches on the ground (`twig` / `TwigBlock`).
  - **Ground Gathering**: Spawns naturally in patches under tree canopies in overworld forests and biomes (0–2 twigs per spot, frequently 0–1). Instantly harvested by clicking or breaking by hand.
  - **Tree Leaves Drop**: Breaking or decaying leaves drops 1–2 Twigs with a **35% chance** (unless harvested with shears).
  - **Crafting**: 2 Twigs $\rightarrow$ 1 Stick (`Items.STICK`).
- **Silicon Shears (Кремниевые ножницы)**: Rebuilt with authentic wooden stick handle loops bound with cord and chipped flint/silicon blades.

### 🌍 Soils, Digging, Sieve & Hand-Woven Mesh
- **Clay Mining & Harvesting Rules (Добыча глины)**:
  - **Shovel Requirement**: Clay blocks (`minecraft:clay`) can **strictly be harvested ONLY with a shovel** (Silicon, Copper, Bronze, Iron, etc.).
  - **Hand / Tool Penalty**: Breaking clay blocks by hand or using any tool other than a shovel will destroy the block completely with **0 drops**.
  - **Drop Yield**: Digging clay with a shovel drops **1 Clay Ball** (`minecraft:clay_ball`) per block (reduced from vanilla's 4).
  - **Mining Speed**: Clay block digging speed is adjusted to take slightly longer to dig (~1.5x - 1.8x longer).
- **Hand-Woven Mesh & Knitting Needles (`unbound_mesh` $\rightarrow$ `mesh` / `knitting_needles`)**:
  - Crafting: 2 Sticks + 2 Ropes in crafting grid $\rightarrow$ **Unbound Mesh** (`unbound_mesh`).
  - **Method 1 - Hand Weaving**: Hold Right-Click with Unbound Mesh in hand for **15 seconds** (instant center snap, eating bobbing animation, and scratching brush sounds). Durability bar fills from 0 to 300 ticks. Progress is saved if interrupted.
  - **Method 2 - Knitting Needles (Спицы)**: Combine Unbound Mesh + Knitting Needles in any 2x2 or 3x3 crafting grid to **instantly craft a finished Mesh** (`mesh`). Needles lose 1 durability and remain in the crafting grid.
  - **Knitting Needles (`bronze_knitting_needles` / `iron_knitting_needles`)**:
    - **Bronze Knitting Needles**: 110 durability. Crafted from 2 Bronze Ingots.
    - **Iron Knitting Needles**: 450 durability. Crafted from 2 Iron Ingots.
- **Standard Soils (Gravel, Sand, Red Sand, Dirt, Suspicious Gravel/Sand)**:
  - **Bare Hand Digging**: Drops only **Silicon Shards** (20%) or the soil block (80%). Whole Flint and Copper Dust **NEVER** drop from bare hand digging.
  - **Shovel Digging**: Drops **Silicon Shards** (20%), **Flint** (8%), **Copper Dust** (2%), **Gravel/Soil** (70%).
  - Suspicious gravel and sand additionally yield their built-in archaeology loot tables.
  - Plain Dirt can now be sifted in the Sieve just like gravel and sand.
- **Sieve Table (`sieve` / `SieveBlock` / `SieveBlockEntity` / `SieveBlockEntityRenderer`)**:
  - Crafted from 4 Sticks + 2 Planks + 1 Mesh.
  - **Container**: 9 input slots (left 3x3) and 9 output slots (right 3x3).
  - **No Passive Sifting**: The sieve does NOT sift blocks on its own over time.
  - **Active Manual Sifting (Shift + Right-Click)**:
    - Hold Shift and Right-Click on the Sieve block to perform a manual shake cycle.
    - Each block requires **5 shakes** to complete (1 click = 1 shake, 20% progress).
    - **Cooldown**: 0.5 seconds (10 ticks) between shakes.
    - **Visuals & Audio**: The wire mesh screen tray vibrates horizontally ($\pm 1$ pixel) with sand/gravel scraping sounds and flying block particles.
    - Subsequent clicks during the 0.5s animation are locked.
  - **Automated Rotational Sifting (Create 6.0.10+ Integration)**:
    - Connecting Create rotating shafts, cogs, or kinetic engines directly to the side axle connector sockets automatically vibrates and sifts the sieve continuously.
    - Grinding speed scales directly with rotational RPM (e.g. 16 RPM = 1 shake per 10 ticks, 64 RPM = 4x speed).
  - **Active Only When Input Is Present**: Both manual shaking and Create rotation animate and function strictly when valid siftable soils are loaded in the input slots.
  - **Catch Basin & Side Axle Socket 3D Model**: Features a wooden collection basin underneath the mesh where sifted mineral dusts drop, and protruding bronze/iron bearing sockets on the side walls for kinetic shafts.
  - **Balanced Sifting Drop Matrix (Independent Dice Roll Per Item)**:
    - Standard Soils (Gravel, Sand, Red Sand, Dirt, Grass Block, Suspicious):
      - Silicon Shards: **68%** (Common, 1.5x boost)
      - Flint: **33%** (Uncommon, 1.5x boost)
      - Copper Dust: **12%** (Rare, 1.5x boost)
      - Tin Dust: **12%** (Rare, equal chance with copper)
      - Iron Dust: **3%** (Very rare / small chance)
- **Rich Soils (Богатая почва)**:
  - 5 variants: **Rich Grass Block** (`rich_grass_block`), **Rich Dirt** (`rich_dirt`), **Rich Gravel** (`rich_gravel`), **Rich Sand** (`rich_sand`), **Rich Red Sand** (`rich_red_sand`).
  - Textures: Counterpart vanilla textures with distinct white/silver mineral flecks and top-left lighting.
  - Digging Hardness: 2x slower (`destroyTime: 1.0f - 1.2f`).
  - Mining Requirement: Strictly requires a shovel of **Copper tier or higher** (Copper, Bronze, Iron, Reinforced Iron, Netherite) to drop the rich block item (Rich Grass drops Rich Dirt unless Silk Touch is used). Breaking with hands or other tools drops standard soil (Dirt/Gravel/Sand/Red Sand).
  - **Sifting in Sieve (Copper Dust, Tin Dust, Iron Dust & Flint)**:
    - High-tier minerals (Gold, Diamond) and artificial alloys (Bronze) are strictly removed from rich soils.
    - Rich Grass Block, Rich Dirt, Rich Gravel, Rich Sand, and Rich Red Sand are all siftable.
    - Yields copper-age resources, iron dust, and flint:
      1. Copper Dust (`copper_dust`) — **68%** (1.5x boost)
      2. Tin Dust (`tin_dust`) — **68%** (1.5x boost, on par with copper)
      3. Iron Dust (`iron_dust`) — **10%** (small chance)
      4. Flint (`minecraft:flint`) — **15%** (1.5x boost)
  - World Generation: Spawns in large veins (size 20, 10–12 attempts/chunk) embedded naturally inside their ordinary soil counterparts in overworld biomes (meadow surface converts to Rich Grass Block).

### 🌾 Farming, Mandatory Fertilization (Bone Meal & Dung) & 2-Stage Hoe Tilling
- **Mandatory Soil Fertilization (`fertilized_farmland` / `FertilizedFarmlandBlock`)**:
  - Crops planted on standard farmland will **NOT** grow (growth ticks are completely blocked on unfertilized soil).
  - **Bone Meal Fertilization**: Right-clicking farmland or a planted crop with **Bone Meal** fertilizes the soil for **1 harvest cycle** (`charges = 1`).
  - **Dung Fertilization (Навоз)**: Right-clicking farmland or a planted crop with **Dung** (`dung`) provides powerful long-lasting fertilization for **3 full harvest cycles** (`charges = 3`).
  - **Growth Behavior**: Fertilizers do not skip or instantly advance crop growth stages; they unlock natural crop growth.
  - **Visuals**: Fertilized farmland features distinct ivory/white bone meal mineral flecks on both dry and moist soil textures.
  - **Crop Harvesting Cycle**: When a mature crop is harvested, the soil decrements 1 charge. When all charges are exhausted, the soil resets back to standard unfertilized farmland (`Blocks.FARMLAND`).
- **Animal Digestion & Dung Production (`dung` / `AnimalDungHandler`)**:
  - Animals (Cows, Sheep, Pigs, Horses, Goats, Camels, Sniffers, Wolves, etc.) produce Dung **3 minutes (3600 ticks)** after eating.
  - Triggered by player feeding, sheep grazing on grass, and wild wolves hunting and killing prey.
- **Wildlife & Animal Behavior Overhaul (`AnimalBehaviorHandler`)**:
  - **Wild Wolf Predation**: Wild, untamed wolves hunt **ALL peaceful animals** (cows, pigs, sheep, chickens, rabbits, horses, llamas, goats, etc.), creating active ecosystem danger.
  - **Persistent Animal Fleeing (`PersistentFleeGoal`)**: When damaged by a player or predator, peaceful animals do NOT just run for 2 seconds. They enter persistent flight mode (**Speed II**, 1.8x speed) and dynamically pathfind away from the attacker until reaching at least **30 blocks away**. If pursued, they continue fleeing without stopping.
  - **Cow Defensive Kick**: If an attacker strikes a cow in close melee range ($\le 3.0$ blocks), the cow immediately delivers a powerful rear hoof kick (deals 5.0 damage / 2.5 hearts, applies heavy backwards knockback, plays impact sound and crit particles) before sprinting away with **Speed III**.
- **2-Stage Tilling**:
  - **Stage 1 (Grass/Podzol/Mycelium/Rich Grass $\rightarrow$ Dirt/Rich Dirt)**: Right-click grassy ground with any hoe to till away the grass layer into plain dirt (or rich dirt) with a **35% chance** to harvest wild seeds (`Wheat Seeds` 50%, `Carrot` 15%, `Potato` 15%, `Beetroot Seeds` 10%, `Pumpkin Seeds` 5%, `Melon Seeds` 5%).
  - **Stage 2 (Dirt/Rich Dirt $\rightarrow$ Farmland)**: Right-click plain dirt or rich dirt with any hoe to prepare farmland for crop planting.
  - Breaking (left-clicking) grass blocks simply breaks the block normally into dirt without special seed harvesting.

### 🏘️ World Exploration, Universal Structure Mixins & Trading
- **Village Rarity & Distance**: Villages generate rarely (`spacing: 200`, `separation: 80`) and are strictly prevented from generating within **3000 blocks** of world spawn / origin (`villageMinDistanceFromSpawn: 3000.0`). Pillager Outposts are also spaced out.
- **Universal Workstations & POIs Conversion Across All Mods (`StructureTemplateMixin`)**:
  - In ALL structures (vanilla and modded like *The Broken Script*, YUNG's, Towns & Towers):
    - Vanilla Crafting Tables are replaced with **Tree Stumps** (players must carve a stump with a chisel).
    - Vanilla Furnaces and Blast Furnaces automatically convert to **Brick Furnaces** retaining exact orientation.
    - Vanilla Smokers convert to **Ovens** retaining exact orientation.
- **World Chest Removal & Loot Stripping (`StructureTemplateMixin` + `RandomizableContainerMixin`)**:
  - All non-bastion chests, trapped chests, and barrels across all structures (vanilla and modded) have their loot tables and items stripped to yield 0 unearned loot.
  - **Bastion Remnant chests** are preserved, allowing access to Netherite Upgrade Smithing Templates and trims.
- **Iron Golem Balance**: Iron Golems drop **no iron ingots or nuggets** upon death (only poppies).
- **Villager & Wandering Trader Trades**:
  - Armorer, Weaponsmith, and Toolsmith villagers sell tools, weapons, and armor up to **Bronze tier** in exchange for emeralds.
  - Basic / flavor trades (sticks, pebbles, silicon shards, saplings, dyes, wild crops, dry grass) allow fair early-game bartering.
  - All iron/diamond/chainmail tool and armor offers, as well as disabled item purchases, are purged.
- **Universal Loot Table Rebalancing (`LootTableMixin` + `ChestLootModifier` & `RemoveDisabledItemsModifier`)**:
  - All generated loot tables (chests, ruined portals, archaeology, trial chambers, spawners, pots, fishing, bartering) are rebalanced:
    - Gold, Iron, Diamond, and Netherite tools/weapons $\rightarrow$ Copper (75%) or Bronze (25% on high luck) retaining enchantments.
    - Gold, Iron, Chainmail, Diamond armor $\rightarrow$ Copper (75%) or Bronze (25%).
    - Iron nuggets $\rightarrow$ Copper nuggets (65%), Tin nuggets (25%), Bronze nuggets (10%).
    - Gold nuggets $\rightarrow$ Copper nuggets (65%), Tin nuggets (25%), Gold dust (10%).
    - Iron/Gold ingots $\rightarrow$ Copper/Tin ingots (80%), Bronze/Gold dust/nuggets (20%).
    - Raw Iron/Gold $\rightarrow$ Raw Copper/Tin (85%), Iron/Gold dust (15%).
    - Diamonds $\rightarrow$ Diamond dust (75%), Diamond nugget (25%).
    - Netherite scrap/ingots $\rightarrow$ Diamond dust / Bronze.
    - Furnaces/Smokers $\rightarrow$ Brick Furnaces / Ovens.
    - Bone Meal $\rightarrow$ Bones (must be ground in Hand Mill).

### 🌲 Stumps, Chisel Carving & Crafting Table Construction
- **Tree Stumps (`StumpBlock` / `WorkStumpBlock`)**: Naturally generated trees have their lowest trunk block replaced with a stump of very high hardness (25.0).
- **Copper Chisel (`chisel` / `ChiselItem`)**: Crafted from **1 Copper Ingot + 1 Stick** in 2x2/3x3 crafting grid (110 durability).
- **Crafting Table Carving**: Right-clicking a stump 4 times with a Chisel carves it through 3 visual stages into a vanilla Crafting Table. Direct crafting of crafting tables in 2x2 inventory is disabled.

### 🪓 Woodcutting & Plank Crafting Rules
- **Tree & Plank Harvesting**: Trees (logs, wood, stripped wood) and planks cannot be broken by hand. An axe is strictly required.
- **Plank Crafting**:
  - **2x2 Inventory Grid**: 1 Log/Wood + 1 Axe $\rightarrow$ 2 Planks (the axe loses 1 durability and remains in the crafting grid).
  - **3x3 Crafting Table**: 1 Log/Wood $\rightarrow$ 4 Planks (standard full yield without requiring an axe).
  - Full support for all wood types (Oak, Spruce, Birch, Jungle, Acacia, Dark Oak, Mangrove, Cherry, Bamboo, Crimson, Warped, and Mod Stumps).

### ☀️ Drying Rack & Material Processing
- **Drying Rack (Сушилка)**: Crafted in 2x2 grid from 4 sticks (`drying_rack`). Full 3D isometric inventory item model. Operates passively when placed outdoors under open sky during daytime (`isDay() && canSeeSky() && !isRaining()`).
  - **Grass** (Short Grass, Tall Grass, Fern, Large Fern, Seagrass) $\rightarrow$ **Dry Grass** (wilted straw, used for ropes & furnace fuel). Shears are required to harvest grass.
  - **Leather** $\rightarrow$ **Tanned Leather (Дублёная кожа)** (dark oiled hide, required to craft Leather Armor and ropes).
  - **Rope Crafting**: Ropes can only be crafted from Tanned Leather + Shears, or **2 plant fibers / vines** (2 Dry Grass, 2 Vines, 2 String, 2 Dried Kelp, 2 Hanging Roots) in 2x2 grid without requiring a crafting table. Standard raw leather rope crafting is removed.

### 🍞 Oven, Brick Furnace, Advanced Smelter, Alloy Mixer & Wooden Hopper
- **Oven (Духовка)**: Crafted from 2 Brick Slabs + 2 Bricks (`oven`).
  - **Food Only**: Accepts exclusively food items (raw beef, porkchop, mutton, chicken, rabbit, fish, potato, kelp). Non-food materials and ores cannot be placed in the oven.
  - Requires manual fueling and ignition with a Lighter or Flint & Steel.
- **Brick Furnace (Кирпичная печь)**: Replaces vanilla Furnace and Blast Furnace.
  - **Ores & Blocks Only**: Food items cannot be placed or smelted in the Brick Furnace. Smelts raw chunks into **nuggets**.
  - **Dedicated JEI Category (`BrickFurnaceRecipeCategory`)**: Displays exact nugget outputs (`Items.RAW_IRON` $\rightarrow$ `Items.IRON_NUGGET`, etc.) rather than full ingots to prevent player confusion.
- **Alloy Mixer (Смеситель сплавов / `alloy_mixer` / `AlloyMixerBlock`)**:
  - Crafted on a 3x3 Crafting Table from **4 Copper Ingots + 1 Brick Furnace + 4 Brick Slabs** (`SCS / CFC / SCS`).
  - Accessible in the Copper Age without requiring iron, enabling Bronze production (2 Copper Ingots + 1 Tin Ingot $\rightarrow$ **3 Bronze Ingots**).
  - Requires manual fuel loading and ignition.
- **Advanced Smelter (Продвинутая плавильня / `advanced_smelter` / `AdvancedSmelterBlock`)**:
  - Crafted on a 3x3 Crafting Table from **5 Iron Ingots + 1 Brick Furnace + 3 Smooth Stone**.
  - Reinforced iron-plated design with corner rivets, dark steel trim, and industrial firebox grating.
  - **Full Ingot Smelting (`RecipeTypes.SMELTING` in JEI)**: Smelts raw ores and metal chunks (Iron, Copper, Gold, Tin, Zinc) directly into **Full Ingots** instead of single nuggets.
  - Requires manual fuel loading and lighter/flint ignition.
- **Wooden Hopper (Деревянная воронка / `wooden_hopper` / `WoodenHopperBlock`)**:
  - Crafted on a 3x3 Crafting Table from **5 Planks + 1 Woven Basket** in a V-shape.
  - Features 1 buffer slot, slightly slower transfer speed (**14 ticks per item**).
  - Can connect in 5 directions (down, north, south, west, east) to feed fuels into furnaces/ovens or collect machine outputs.
- **Unified Heated Machine Interface (`IFueledMachine`) & Smart Auto-Refueling**:
  - Unified across **Brick Furnace**, **Advanced Smelter**, **Food Oven**, and **Alloy Mixer**.
  - **Manual Feeding with Excess**: Hand right-clicking with fuel allows feeding fuel with excess directly into `burnTime`.
  - **Smart Just-In-Time Hopper Feeding**: Hoppers connected to the **BACK** face insert exactly 1 fuel item into the fuel slot ONLY when unlit (`burnTime <= 0`) or within **20 ticks** before burnout (`burnTime <= 20`).
  - **Seamless 5-Tick Auto-Refueling**: When burning (`burnTime > 0`) and remaining `burnTime <= 5` ticks, machines automatically consume the fuel piece from the slot, extending the flame without extinguishing and without overloading machines with excess fuel.
- **Unified 9-Tier Fuel Matrix (`FuelRegistry`)**:
  - Every fuel item in Minecraft provides a distinct **Burn Duration** (~2–2.5x longer than standard) and **Cooking Speed / Temperature**:
    1. **Tier 1 (Foliage & Twigs)**: 900 ticks (45s), 260t speed. (`ModItems.TWIG`, `DRY_GRASS`, `DEAD_BUSH`, saplings, leaves).
    2. **Tier 2 (Sticks & Small Wood)**: 1300 ticks (65s / 1m 5s), 240t speed. (`STICK`, `POINTED_STICK`, `BOWL`).
    3. **Tier 3 (Wooden Slabs & Stairs)**: 1800 ticks (90s / 1m 30s), 200t speed. (slabs, stairs, fences, gates, trapdoors).
    4. **Tier 4 (Planks & Wooden Objects)**: 2400 ticks (120s / 2m), 180t speed. (planks, doors, boats, signs, buttons, pressure plates).
    5. **Tier 5 (Logs, Wood & Tree Stumps)**: 3300 ticks (165s / 2m 45s), 150t speed. (all logs, stripped logs, wood, all mod tree stumps).
    6. **Tier 6 (Charcoal)**: 3600 ticks (180s / 3m), 120t speed.
    7. **Tier 7 (Mineral Coal)**: 4500 ticks (225s / 3m 45s), 100t speed.
    8. **Tier 8 (Blaze Rod)**: 6000 ticks (300s / 5m), 70t speed.
    9. **Tier 9 (Coal Block)**: 36000 ticks (1800s / 30 min), 80t speed.

### 🔄 Material Conversion & Create 6.0.10 Compatibility
- **Natural Metals (Iron, Copper, Gold, Tin, Zinc)**:
  - 2 Ore Dust $\rightarrow$ 1 Raw Ore (chunk)
  - 1 Raw Ore in Brick Furnace $\rightarrow$ 1 Metal Nugget (Iron, Copper, Gold, Tin, Zinc)
  - 4 Metal Nuggets $\rightarrow$ 1 Ingot
- **Bronze**:
  - 2 Bronze Dust $\rightarrow$ 1 Bronze Nugget
  - 4 Bronze Nuggets $\rightarrow$ 1 Bronze Ingot
  - 2 Copper Ingot + 1 Tin Ingot in Alloy Mixer $\rightarrow$ 1 Bronze Ingot
- **Brass (Латунь)**:
  - 1 Copper Ingot + 1 Zinc Ingot in Alloy Mixer $\rightarrow$ 1 Brass Ingot (`create:brass_ingot`)
- **Diamond**:
  - 2 Diamond Dust $\rightarrow$ 1 Diamond Nugget
  - 4 Diamond Nuggets $\rightarrow$ 1 Diamond (and 1 Diamond $\rightarrow$ 4 Diamond Nuggets)
- **Reinforced Iron (Diamond Ingot)**:
  - 1 Diamond + 1 Iron Ingot + 1 Copper Ingot in Alloy Mixer $\rightarrow$ 1 Diamond Ingot
  - Used in Smithing Table to upgrade Iron tools & armor to Reinforced Iron.
- **Create Machine Parity**:
  - Mechanical Mixer (Heated): Bronze Ingot, Brass Ingot, Diamond Ingot.
  - Millstone / Crushing Wheels: Gravel, Sand, and Ore chunk processing into dusts and pebbles.
  - Fan Bulk Blasting: Raw Ores into Nuggets.
  - Fan Bulk Smoking: Grass to Dry Grass, Leather to Tanned Leather.
  - Mechanical Saw: Log and Tree Stump cutting into 4 Planks.

### 🧺 Early Storage: Woven Basket (`basket` / `BasketBlock`)
- **Crafting**: 4 Meshes in 2x2 grid $\rightarrow$ **Basket** (`basket`). Accessible very early before crafting tables or chests are available.
- **Storage**: 9 slots (3x3 inventory container).
- **Design & Model**: Custom 3D block model and 3D isometric inventory item model with woven wicker panels, reinforced rim, arched handle, and rope cross-bindings. Supports horizontal rotation and proper collision voxel bounds (`14x12x14`).
- **Interactions & Automation**: Right-click opens custom 3x3 GUI. Supports hopper input/output through all faces (`WorldlyContainer`). Drops contents when broken with an axe.

### ⚙️ Redstone & Advanced Mechanisms (Kinetic Piston, Filter Grate, Entity Observer)
- **Kinetic Piston (`kinetic_piston` / `KineticPistonBlock`)**:
  - Crafted from 3 Bronze Blocks + 4 Cobblestone + 1 Bronze Ingot + 1 Redstone Dust.
  - Directional in all 6 orientations (Up, Down, North, South, East, West).
  - **Visuals & Model**: Dark stone base casing, heavy bronze metal pusher face, and fully animated extending piston shaft mechanism (`extended` property).
  - **Block Projectile Launch**: Checks the front face. If there are 2 or more blocks in front, it does NOT trigger. If there is exactly 1 block in front, launches it as a physical projectile / falling block entity ~10 blocks forward (or until impacting a wall, dropping under gravity and settling back as a solid block).
  - **Entity Catapult**: Launches players, mobs, and items ~10 blocks in the facing direction.
- **Filter Grate (`filter_grate` / `FilterGrateBlock` / `FilterGrateBlockEntity`)**:
  - Crafted from 4 Planks + 4 Sticks + 1 Mesh.
  - **Visuals & Model**: Oak plank half-block / slab hopper frame hollowed from the bottom up to a transparent wire mesh grating top (`RenderType.cutout`).
  - Solid collision for players and living mobs (entities cannot fall or walk through the grating).
  - **Phantom Filter GUI**: Right-click opens a 3x3 filter grid. Clicking with an item sets a ghost/phantom filter copy (original item is not consumed); clicking with an empty hand clears the slot.
  - **Item Passing Logic**:
    - **Unpowered (Normal Mode)**: Only item entities matching the set filters are allowed to pass through the grate to below.
    - **Powered by Redstone (Inverted Mode)**: Filter is inverted — items matching the filters are blocked on top, while all other items pass through.
- **Entity Observer (`entity_observer` / `EntityObserverBlock`)**:
  - Crafted from 6 Cobblestone + 2 Redstone Dust + 1 Bone.
  - **Visuals**: Authentic vanilla Observer stone casing, with a carved Minecraft **Skeleton face parody** on the front sensor side, and vanilla redstone output port on the back.
  - Directional sensor block detecting any entity (players, mobs, dropped items, vehicles) moving in front of its sensor face.
  - Emits a **4-tick redstone pulse** on its back face upon entity detection.

## File Structure
```
src/main/java/io/marrybye/github/larperthanwolves/
├── LarperThanWolves.java          — Main mod entry point, registers all systems
├── block/
│   ├── ModBlocks.java             — Block registration (DeferredRegister)
│   ├── MillBlock.java             — Hand mill / quern block
│   ├── MillCrankBlock.java        — Rotating mill crank handle block
│   ├── KineticPistonBlock.java    — 10-block projectile kinetic catapult piston
│   ├── FilterGrateBlock.java      — Filter grate with phantom slots & redstone inversion
│   ├── EntityObserverBlock.java   — Directional entity motion detection sensor
│   ├── FertilizedFarmlandBlock.java — Fertilized farmland required for crop growth
│   ├── RichGrassBlock.java        — Rich grass block with daylight spreading & snowy states
│   ├── RichFallingBlock.java      — Falling block with custom dust particle color for rich gravel & sand
│   ├── TwigBlock.java             — Small ground forest twig block
│   ├── BasketBlock.java           — 3x3 early storage woven basket block
│   ├── BrickFurnaceBlock.java     — Brick furnace block (facing, 4 stages)
│   ├── OvenBlock.java             — Food oven block (facing, 4 stages)
│   ├── AlloyMixerBlock.java       — Alloy mixer block (facing, 4 stages)
│   ├── SieveBlock.java            — Sieve block
│   ├── DryingRackBlock.java       — Daylight drying rack block (horizontal facing, 5 content states)
│   ├── StumpBlock.java            — Tree stump base block (extends RotatedPillarBlock)
│   ├── WorkStumpBlock.java        — Work stump with chisel progression (3 STAGE states)
│   ├── UnfiredBrickBlock.java     — Unfired brick (dries under sunlight, 4 stages)
│   └── entity/
│       ├── ModBlockEntities.java           — BlockEntity type registration
│       ├── MillBlockEntity.java            — Hand mill logic (4 slots, WorldlyContainer, MenuProvider)
│       ├── MillCrankBlockEntity.java       — Mill crank rotation animation logic (10-tick duration, synced)
│       ├── FilterGrateBlockEntity.java     — 9-slot phantom filter & item passing logic
│       ├── BasketBlockEntity.java          — 9-slot storage basket container logic
│       ├── BrickFurnaceBlockEntity.java    — Furnace logic (7 slots, custom fuel, WorldlyContainer, no food)
│       ├── OvenBlockEntity.java            — Food oven logic (7 slots, custom fuel, WorldlyContainer, food only)
│       ├── AlloyMixerBlockEntity.java      — Alloy mixing logic (5 slots, 3 recipes)
│       ├── SieveBlockEntity.java           — Sieve passive processing (18 slots)
│       ├── DryingRackBlockEntity.java      — Daylight drying rack ticking logic (1 slot, WorldlyContainer)
│       ├── UnfiredBrickBlockEntity.java    — Brick drying ticking logic
│       └── FuelRegistry.java               — Unified fuel durations, cook speeds & validation
├── recipe/
│   ├── ModRecipeSerializers.java  — Custom recipe serializers (AxePlank, WorkbenchPlank)
│   ├── WoodToPlanksHelper.java    — Wood type mapping and axe detection utility
│   ├── AxePlankRecipe.java        — Log + Axe -> 2 Planks crafting recipe (axe damage retention)
│   ├── WorkbenchPlankRecipe.java  — 3x3 Crafting Table Log -> 4 Planks crafting recipe
│   ├── AlloyRecipe.java           — Modular alloy mixer recipe model
│   ├── AlloyRegistry.java         — Central alloy mixer recipe registry & JEI sync (Bronze, Brass, Diamond Ingot)
│   ├── MillRecipe.java            — Hand mill recipe POJO model
│   ├── MillRegistry.java          — Hand mill recipe registry & JEI sync (Ingots -> 8 dusts, bones -> bone meal)
│   ├── SmeltingRegistry.java      — Brick furnace custom smelting overrides & fallback (no food, zinc support)
│   └── FoodCookingRegistry.java   — Food oven cooking recipes & validator
├── item/
│   ├── ModItems.java              — Item registration (all tools, armor, materials, block items)
│   ├── ModToolMaterials.java      — Custom tool tiers (SILICON, COPPER, BRONZE, REINFORCED_IRON)
│   ├── ModArmorMaterials.java     — Custom armor materials (COPPER, BRONZE, REINFORCED_IRON)
│   ├── ModCreativeTabs.java       — Creative tab registration
│   └── ChiselItem.java            — Chisel right-click logic (stump → work stump → crafting table)
├── menu/
│   ├── ModMenuTypes.java          — Menu type registration
│   ├── MillMenu.java              — Hand mill container menu (4 slots)
│   ├── FilterGrateMenu.java       — Filter grate container menu (9 phantom slots)
│   ├── BrickFurnaceMenu.java      — Furnace container menu (7 slots, no food)
│   ├── OvenMenu.java              — Food oven container menu (7 slots, food only)
│   ├── AlloyMixerMenu.java        — Mixer container menu (5 slots)
│   └── SieveMenu.java             — Sieve container menu (18 slots)
├── client/
│   ├── ModClientEvents.java       — Client-side screen registration & renderer registration
│   ├── MillScreen.java            — Hand mill GUI renderer
│   ├── MillCrankRenderer.java     — Animated 3D rotating crank handle BlockEntityRenderer
│   ├── FilterGrateScreen.java     — Filter grate GUI renderer
│   ├── BrickFurnaceScreen.java    — Furnace GUI renderer
│   ├── OvenScreen.java            — Oven GUI renderer
│   ├── AlloyMixerScreen.java      — Mixer GUI renderer
│   └── SieveScreen.java           — Sieve GUI renderer
├── event/
│   ├── BlockBreakHandler.java     — Mining tier enforcement (Zinc iron-only), axe wood breaking, shears drops, 2-stage hoe tilling, bone meal & dung fertilization
│   ├── AnimalDungHandler.java     — Animal feeding, grazing & hunting 3-min digestion timer to drop Dung
│   ├── AnimalBehaviorHandler.java — Wild wolf predation on all animals, animal panic speed boost, and cow defensive kick
│   ├── DisabledItemsHandler.java  — Vanilla item removal (Blast furnace/smoker/furnace/chainmail/diamond gear, chunk replacement, mob drops: realistic bones from animals & zombies)
│   └── VillagerTradeHandler.java  — Balanced villager professions (up to Bronze) and wandering trader trades
├── config/
│   └── ModConfig.java             — NeoForge config spec (fuel, sieve, bricks, drying, drops, village distance, farming)
├── compat/
│   ├── CreateCompatHelper.java    — Safe optional reflection helper for Create 6.0.10 kinetic rotation automation
│   ├── ModJeiPlugin.java          — JEI integration plugin (all categories & info tabs)
│   ├── MillJeiRecipe.java         — JEI hand mill recipe POJO
│   ├── MillRecipeCategory.java    — JEI hand mill category
│   ├── AlloyMixerRecipe.java      — JEI alloy recipe POJO
│   ├── AlloyMixerRecipeCategory.java — JEI alloy mixer category
│   ├── SieveJeiRecipe.java        — JEI sieve recipe POJO
│   ├── SieveRecipeCategory.java   — JEI sieve category
│   ├── ChiselRecipe.java          — JEI chisel carving recipe POJO
│   ├── ChiselRecipeCategory.java  — JEI chisel carving category
│   ├── SunDryingRecipe.java       — JEI sun drying recipe POJO
│   ├── SunDryingRecipeCategory.java — JEI sun drying category
│   ├── DryingRackRecipe.java      — JEI drying rack recipe POJO
│   ├── DryingRackRecipeCategory.java — JEI drying rack category
│   ├── MachineFuelRecipe.java     — JEI machine fuel & ignition recipe POJO
│   ├── MachineFuelRecipeCategory.java — JEI machine fuel & ignition category
│   ├── GravelDiggingRecipe.java   — JEI gravel drops recipe POJO
│   └── GravelDiggingRecipeCategory.java — JEI gravel drops category
├── loot/
│   ├── ModLootModifiers.java      — Loot modifier registration
│   ├── RemoveDisabledItemsModifier.java — Global loot modifier that strips disabled items
│   └── ChestLootModifier.java     — Global loot modifier rebalancing chest loot across structures
├── datagen/
│   ├── DataGenerators.java        — Data generation entry point
│   ├── ModRecipesProvider.java    — Recipe data generation
│   └── ModItemModelProvider.java  — Item model data generation
└── mixin/
    ├── TrunkPlacerMixin.java      — Places stump blocks at tree bases during worldgen
    └── StructureMixin.java        — Enforces minimum distance from spawn for village generation
```

## Resources
```
src/main/resources/
├── META-INF/neoforge.mods.toml    — Mod metadata
├── assets/larperthanwolves/
│   ├── blockstates/               — Block state JSONs
│   ├── lang/en_us.json, ru_ru.json — Localization
│   ├── models/block/, models/item/ — Block and item models
│   ├── textures/block/, item/, gui/container/, models/armor/ — Textures
│   └── ...                        — Other assets
├── data/
│   ├── create/recipe/             — Create machine parity recipes (mixing, splashing, milling, crushing, smoking, blasting, cutting)
│   ├── minecraft/recipe/          — Disabled vanilla recipes (blast furnace, smoker)
│   ├── minecraft/tags/point_of_interest_type/ — Villager workstation tags (armorer -> brick_furnace, butcher -> oven)
│   └── larperthanwolves/
│       ├── recipe/                — Crafting and smelting recipes
│       ├── loot_tables/           — Loot tables
│       ├── tags/                  — Block and item tags
│       ├── worldgen/              — Tin ore generation
│       └── neoforge/              — Biome modifiers, global loot modifiers
└── larperthanwolves.mixins.json   — Mixin configuration
```

## Key Patterns

### Adding a New Item
1. Register in `ModItems.java` using `ITEMS.register()`
2. Add texture to `textures/item/<id>.png`
3. Add model in `ModItemModelProvider.java` datagen or create JSON manually
4. Add to creative tab in `ModCreativeTabs.java`
5. Add localization in `lang/en_us.json` and `lang/ru_ru.json`

### Adding a New Block
1. Create block class in `block/` package
2. Register in `ModBlocks.java` using `BLOCKS.register()`
3. Register BlockItem in `ModItems.java`
4. Add blockstate JSON, block model, and textures
5. Add to creative tab, add localization
6. If functional: create BlockEntity in `block/entity/`, register in `ModBlockEntities.java`
7. If has GUI: create Menu in `menu/`, Screen in `client/`, register in `ModMenuTypes.java`

### Adding a New Tool Tier
1. Add tier to `ModToolMaterials.java`
2. Register tool items in `ModItems.java`
3. Update mining restrictions in `BlockBreakHandler.java`
4. Add recipes in `ModRecipesProvider.java`

### Adding a New Armor Set
1. Add material to `ModArmorMaterials.java`
2. Register armor items in `ModItems.java`
3. Add armor layer textures to `textures/models/armor/`

### Adding a New Alloy Recipe
1. Add entry in `AlloyRegistry.registerDefaults()` or call `AlloyRegistry.register(new AlloyRecipe(...))`
2. No block entity changes needed: mixer logic, hopper validation, and JEI automatically support the new recipe.

### Adding a Custom Smelting Override
1. Add entry in `SmeltingRegistry.registerDefaults()` or call `SmeltingRegistry.register(item, () -> output)`
2. All brick furnaces will automatically use the new custom smelting recipe.

### Adding a New Machine/Processing Block
1. Create block class with state properties (FACING, STAGE, etc.)
2. Implement `onRemove` to drop contents when destroyed (`Containers.dropContents`)
3. Create BlockEntity with tick logic, implement `WorldlyContainer` for hopper support
4. Create Menu for inventory management
5. Create Screen for GUI rendering
6. Add GUI texture to `textures/gui/container/`
7. Register all components

### Config Values
- All configurable values are in `ModConfig.java`
- Access pattern: `ModConfig.SERVER != null ? ModConfig.SERVER.<field>.get() : <default>`
- Config file: `larperthanwolves-server.toml`

## 🎨 JAPPA Art & Texture Standards

All pixel art and models MUST follow the **JAPPA** modern vanilla style:
1. **Top-Left Lighting**: Highlights and specular glints belong strictly on the top-left edges; deep shadows and grounding outlines belong on the bottom-right.
2. **Master Templates (`StandartTextures/`)**:
   - `ingot.png`: Standard metal ingot base.
   - `nugget.png`: Standard metal nugget base.
   - `dust.png`: Standard dust pile base (top-left lit).
   - `raw_ore.png`: Standard raw ore chunk base.
   - `raw_ore_block.png`: Standard raw ore block base.
   - `stone_ore.png`, `deepslate_ore.png`: Standard stone & deepslate ore bases.
3. **Missing Textures Rule**: If a requested item or block does not have a texture, generate it automatically in 16x16 RGBA PNG using the appropriate template and material palette.

## 🤖 Agent Protocols & Skills (`.agents/skills/`)

### 1. Global Architectural Consultation (`architecture-consultant`)
If any requested or planned change requires **global modifications** (overhauling a major mechanic, altering progression, breaking save compatibility, or removing systems), **STOP and consult the user first**. Present:
- Option A (Recommended) with trade-offs.
- Option B (Alternative) with trade-offs.
- Exact consequences and affected classes.
- Wait for explicit user confirmation before proceeding.

### 2. Semantic Versioning & Tagging (`git-release-manager`)
Follow standard `MAJOR.MINOR.PATCH`:
- **PATCH** (`X.Y.Z+1`): Bug fixes, texture tweaks, minor balance polish.
- **MINOR** (`X.Y+1.0`): New features, machines, tools/armor sets, non-breaking refactors.
- **MAJOR** (`X+1.0.0`): Breaking save/API changes, full overhauls, engine version upgrades.
- Always update `gradle.properties` and create an annotated git tag (`git tag -a vX.Y.Z -m "Release vX.Y.Z"`).

### 3. Standardized Conventional Commits
All commits must follow:
- `feat:` New features, items, blocks, recipes.
- `fix:` Bug fixes, drop calculation fixes, texture fixes.
- `refactor:` Code refactoring without behavior change.
- `docs:` Documentation or skill changes.
- `chore:` Gradle tasks, file removals, housekeeping.

### 4. Push Permission Check
**MANDATORY**: Never push to remote (`git push`, `git push origin --tags`) automatically. At the end of every action, ask the user for confirmation (Yes / No).

### 5. Mandatory AGENTS.md & README.md Update (`agents-md-updater`)
**MANDATORY**: After completing any changes in the project, review the diff and update `AGENTS.md` to guarantee that the technical documentation always accurately reflects current systems, registries, and item lists. In addition, update `README.md` whenever game mechanics, features, recipes, or balance rules are added or altered.

### 6. Full JEI Mechanics Integration (`jei-mechanics-documenter`)
**MANDATORY**: Whenever adding any unique, custom, or non-standard mechanic (in-world crafting, chisel carving, sun drying, custom fuels/speeds, manual ignition, altered block drops), automatically create corresponding JEI categories, recipes, and `addIngredientInfo` tabs to guarantee total in-game discoverability.

### 7. Mojang Asset Adaptation & JAPPA Artistry (`mojang-asset-artist`)
**MANDATORY**: When creating or modifying any pixel art textures (items, blocks, UI, armor layers), always follow JAPPA standards and MUST base the artwork upon official Mojang vanilla assets (from `minecraft_1.21.1_client.jar`), adapting, combining, and justifiable re-coloring them to guarantee seamless aesthetic harmony with modern Minecraft.

### 8. Optional Libraries & Cross-Mod Compatibility (`optional-libs-compat`)
**MANDATORY**: All mod JAR files in `libs/` (e.g. `create-*.jar`, `jei-*.jar`, etc.) are strictly **optional dependencies**.
- The mod must compile, launch, and run standalone without any optional mods installed.
- Guard all mod-specific calls with `net.neoforged.fml.ModList.get().isLoaded(...)` or isolated compatibility classes to avoid `ClassNotFoundException` / `NoClassDefFoundError`.
- Whenever developing new features, machines, blocks, recipes, or balance updates, always inspect all mods currently located in `libs/` and implement thoughtful cross-mod compatibility (e.g. Create rotational force, mechanical mixers/saws/fans, JEI categories/catalysts/phantom handlers, and conventional tags).

### 9. Granular Feature Documentation (`docs/` -> `feature-docs-manager`)
**MANDATORY**: Every mechanic, machine, tool, and overhaul system must have a dedicated markdown file in `docs/<feature_name>.md`.
- **Pre-Modification Reading**: Before modifying or refactoring any feature, the agent **MUST** read its corresponding documentation in `docs/` to maintain full context and design invariants.
- **Post-Modification Sync (3-Pillar Rule)**: Every change must be documented across all three tiers: `docs/<feature>.md` (deep technical details & testing), `AGENTS.md` (codebase architecture & registries), and `README.md` (player gameplay guide).

### 10. CI/CD & Automated Publishing (CurseForge + Modrinth)
The project utilizes GitHub Actions for continuous integration and automated releases:
- **Build Workflow** (`.github/workflows/build.yml`): Compiles the project and runs checks on every push and pull request.
- **Release Workflow** (`.github/workflows/release.yml`): Triggers on tag pushes (`v*`) or manual `workflow_dispatch`.
  - Builds the mod JAR (`build/libs/larperthanwolves-*.jar`).
  - Creates a GitHub Release with automated changelog via `softprops/action-gh-release@v2`.
  - Automatically publishes to **CurseForge** (Project ID: `1675627`) and **Modrinth** (Project ID: `rnq9KWpr`) via `Kir-Antipov/mc-publish@v3.3`.
  - Uses GitHub Environment `CURSEFORGE_API` with secrets `CURSEFORGE_API` and `MODRINTH_API`.

## Best Practices & Guidelines
- Use `ThreadLocalRandom.current()` instead of `new Random()` for thread safety in server event handlers.
- Use `FuelRegistry` for any fuel checks or durations.
- Check interaction hand (`hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND`) when hurting player items.
- Always use static final arrays for `getSlotsForFace()` in block entities to prevent per-tick heap allocations.

## Build Commands
```bash
./gradlew build          # Build the mod JAR
./gradlew runClient      # Launch Minecraft client
./gradlew runServer      # Launch dedicated server  
./gradlew runData        # Run data generation
```
