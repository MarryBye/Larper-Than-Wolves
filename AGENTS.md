# AGENTS.md

This file is a comprehensive guide for AI agents working on this NeoForge Minecraft mod project.

## Project Overview
- **Mod ID**: `larperthanwolves`
- **Package**: `io.marrybye.github.larperthanwolves`
- **Minecraft**: 1.21.1
- **NeoForge**: 21.1.248
- **Java**: 21
- **Build Tool**: Gradle with NeoForge ModDev plugin 2.0.144
- **Current Version**: 1.20.4

## Project Architecture & Progression

Inspired by *Better Than Wolves*, this hardcore survival overhaul rebuilds the early and mid-game:

### ⛏️ Tool Tiers & Dig Tier Matrix
- **Silicon (Кремень)**: Durability 30. Mines Coal (item), Copper (Copper Dust), Stone/Granite/Diorite/Andesite/Calcite (Pebbles, 2-4), Sandstone (Sand, 2-4). Cannot mine deepslate, zinc, or any other ores.
- **Copper (Медь)**: Durability 100. Mines Coal (item), Copper (Raw Copper), Tin (Tin Dust), Stone/Granite/Diorite/Andesite/Calcite (Pebbles, 2-4). Shovel can mine and harvest Rich Soils (`rich_grass_block`, `rich_dirt`, `rich_gravel`, `rich_sand`, `rich_red_sand`). Cannot mine deepslate, zinc, or iron+.
- **Bronze (Бронза)**: Durability 150. Mines Coal (item), Copper (Raw Copper), Tin (Raw Tin), Iron (Iron Dust), Stone/Granite/Diorite/Andesite/Calcite (Pebbles), Deepslate/Tuff/Dripstone/Netherrack (Pebbles: `deepslate_nugget`, `tuff_nugget`, `dripstone_nugget`, `netherrack_nugget`, 2-4). Shovel mines Rich Soils. Cannot mine zinc ore or high-tier ores.
- **Iron (Железо)**: Standard metal. Full mining access for standard rocks & ores as whole blocks / raw chunks, including Zinc Ore (`create:raw_zinc`). Shovel mines Rich Soils. Cannot mine Ancient Debris or Obsidian.
- **Reinforced Iron (Diamond Ingot) / Netherite**: Full access to all blocks including Ancient Debris and Obsidian.

### 🌲 Flora, Twigs & Forest Foraging
- **Twigs (Веточки)**: Small fallen wood branches on the ground (`twig` / `TwigBlock`).
  - **Ground Gathering**: Spawns naturally in patches under tree canopies in overworld forests and biomes (0–2 twigs per spot, frequently 0–1). Instantly harvested by clicking or breaking by hand.
  - **Tree Leaves Drop**: Breaking or decaying leaves drops 1–2 Twigs with a **35% chance** (unless harvested with shears).
  - **Crafting**: 2 Twigs $\rightarrow$ 1 Stick (`Items.STICK`).
- **Silicon Shears (Кремниевые ножницы)**: Rebuilt with authentic wooden stick handle loops bound with cord and chipped flint/silicon blades.

### 🌍 Soils, Digging, Sieve & Hand-Woven Mesh
- **Hand-Woven Mesh (`unbound_mesh` $\rightarrow$ `mesh`)**:
  - Crafting: 2 Sticks + 2 Ropes in crafting grid $\rightarrow$ **Unbound Mesh** (`unbound_mesh`).
  - Hand Weaving: Hold Right-Click with Unbound Mesh in hand for **15 seconds** (eating animation + scratching brush sound effects).
  - Progress Bar: Durability bar dynamically fills from 0 to 15 (1 tick of durability = 1 second of weaving). Progress is saved if interrupted.
  - Completion: At 15 seconds, transforms into the finished **Mesh** (`mesh`).
- **Standard Soils (Gravel, Sand, Red Sand, Dirt, Suspicious Gravel/Sand)**:
  - Digging & Sifting drops maximum: **Silicon Shards** (most common, 20-30%), **Flint** (rarer, 8-15%), **Copper Dust** (rarest, 2-5%).
  - Suspicious gravel and sand additionally yield their built-in archaeology loot tables.
  - Plain Dirt can now be sifted in the Sieve just like gravel and sand.
- **Rich Soils (Богатая почва)**:
  - 5 variants: **Rich Grass Block** (`rich_grass_block`), **Rich Dirt** (`rich_dirt`), **Rich Gravel** (`rich_gravel`), **Rich Sand** (`rich_sand`), **Rich Red Sand** (`rich_red_sand`).
  - Textures: Counterpart vanilla textures with distinct white/silver mineral flecks and top-left lighting.
  - Digging Hardness: 2x slower (`destroyTime: 1.0f - 1.2f`).
  - Mining Requirement: Strictly requires a shovel of **Copper tier or higher** (Copper, Bronze, Iron, Reinforced Iron, Netherite) to drop the rich block item (Rich Grass drops Rich Dirt unless Silk Touch is used). Breaking with hands or other tools drops standard soil (Dirt/Gravel/Sand/Red Sand).
  - Sifting in Sieve: Yields metal dusts and materials in order of rarity:
    1. Silicon Shards (`silicon_shard`)
    2. Flint / Silicon (`flint`)
    3. Copper Dust (`copper_dust`)
    4. Tin Dust (`tin_dust`)
    5. Bronze Dust (`bronze_dust`)
    6. Iron Dust (`iron_dust`)
    7. Gold Dust (`gold_dust`)
    8. Diamond Dust (`diamond_dust`)
  - World Generation: Spawns in small veins (size 14) embedded naturally inside their ordinary soil counterparts in overworld biomes (meadow surface converts to Rich Grass Block).

### 🌾 Farming & 2-Stage Hoe Tilling
- **2-Stage Tilling**:
  - **Stage 1 (Grass/Podzol/Mycelium/Rich Grass $\rightarrow$ Dirt/Rich Dirt)**: Right-click grassy ground with any hoe to till away the grass layer into plain dirt (or rich dirt) with a **35% chance** to harvest wild seeds (`Wheat Seeds` 50%, `Carrot` 15%, `Potato` 15%, `Beetroot Seeds` 10%, `Pumpkin Seeds` 5%, `Melon Seeds` 5%).
  - **Stage 2 (Dirt/Rich Dirt $\rightarrow$ Farmland)**: Right-click plain dirt or rich dirt with any hoe to prepare farmland for crop planting.
  - Breaking (left-clicking) grass blocks simply breaks the block normally into dirt without special seed harvesting.

### 🏘️ World Exploration, Villages & Trading
- **Village Rarity & Distance**: Villages generate rarely (`spacing: 200`, `separation: 80`) and are strictly prevented from generating within **3000 blocks** of world spawn / origin (`villageMinDistanceFromSpawn: 3000.0`). Pillager Outposts are also spaced out.
- **Workstations & POIs**: Vanilla Smoker and Blast Furnace are disabled and removed. In villages and worldgen, Blast Furnaces automatically convert to **Brick Furnaces** (Armorer POI) and Smokers convert to **Ovens** (Butcher POI).
- **Iron Golem Balance**: Iron Golems drop **no iron ingots or nuggets** upon death (only poppies).
- **Villager & Wandering Trader Trades**:
  - Armorer, Weaponsmith, and Toolsmith villagers sell tools, weapons, and armor up to **Bronze tier** in exchange for emeralds.
  - Basic / flavor trades (sticks, pebbles, silicon shards, saplings, dyes, wild crops, dry grass) allow fair early-game bartering.
  - All iron/diamond/chainmail tool and armor offers, as well as disabled item purchases, are purged.
- **Chest Loot Rebalancing**: All generated chest loot tables (villages, pillager outposts, mineshafts, dungeons, temples) are rebalanced: iron gear is replaced with copper/bronze tiers, raw metals/ingots with dusts/nuggets, furnaces/smokers with brick furnaces/ovens, and supernatural endgame items with grounded survival alternatives.

### 🪓 Woodcutting & Plank Crafting Rules
- **Tree & Plank Harvesting**: Trees (logs, wood, stripped wood) and planks cannot be broken by hand. An axe is strictly required.
- **Plank Crafting**:
  - **2x2 Inventory Grid**: 1 Log/Wood + 1 Axe $\rightarrow$ 2 Planks (the axe loses 1 durability and remains in the crafting grid).
  - **3x3 Crafting Table**: 1 Log/Wood $\rightarrow$ 4 Planks (standard full yield without requiring an axe).
  - Full support for all wood types (Oak, Spruce, Birch, Jungle, Acacia, Dark Oak, Mangrove, Cherry, Bamboo, Crimson, Warped, and Mod Stumps).

### ☀️ Drying Rack & Material Processing
- **Drying Rack (Сушилка)**: Crafted in 2x2 grid from 4 sticks (`drying_rack`). Operates passively when placed outdoors under open sky during daytime (`isDay() && canSeeSky() && !isRaining()`).
  - **Grass** (Short Grass, Tall Grass, Fern, Large Fern, Seagrass) $\rightarrow$ **Dry Grass** (wilted straw, used for ropes & furnace fuel). Shears are required to harvest grass.
  - **Leather** $\rightarrow$ **Tanned Leather (Дублёная кожа)** (dark oiled hide, required to craft Leather Armor and ropes).
  - **Rope Crafting**: Ropes can only be crafted from Tanned Leather + Shears (or Dry Grass / vines). Standard leather rope crafting is removed.

### 🍞 Oven (Духовка) & Brick Furnace Specialization
- **Oven (Духовка)**: Crafted from 1 Brick Furnace + 1 Iron Nugget or 2 Brick Slabs + 2 Bricks (`oven`).
  - **Food Only**: Accepts exclusively food items (raw beef, porkchop, mutton, chicken, rabbit, fish, potato, kelp). Non-food materials and ores cannot be placed in the oven.
  - Requires manual fueling and ignition with a Lighter or Flint & Steel.
- **Brick Furnace (Кирпичная печь)**: Replaces vanilla Furnace and Blast Furnace.
  - **Ores & Blocks Only**: Food items cannot be placed or smelted in the Brick Furnace.

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
- **Design & Model**: Custom 3D block model with woven wicker panels, reinforced rim, arched handle, and rope cross-bindings. Supports horizontal rotation and proper collision voxel bounds (`14x12x14`).
- **Interactions & Automation**: Right-click opens custom 3x3 GUI. Supports hopper input/output through all faces (`WorldlyContainer`). Drops contents when broken with an axe.

## File Structure
```
src/main/java/io/marrybye/github/larperthanwolves/
├── LarperThanWolves.java          — Main mod entry point, registers all systems
├── block/
│   ├── ModBlocks.java             — Block registration (DeferredRegister)
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
│   ├── BrickFurnaceMenu.java      — Furnace container menu (7 slots, no food)
│   ├── OvenMenu.java              — Food oven container menu (7 slots, food only)
│   ├── AlloyMixerMenu.java        — Mixer container menu (5 slots)
│   └── SieveMenu.java             — Sieve container menu (18 slots)
├── client/
│   ├── ModClientEvents.java       — Client-side screen registration
│   ├── BrickFurnaceScreen.java    — Furnace GUI renderer
│   ├── OvenScreen.java            — Oven GUI renderer
│   ├── AlloyMixerScreen.java      — Mixer GUI renderer
│   └── SieveScreen.java           — Sieve GUI renderer
├── event/
│   ├── BlockBreakHandler.java     — Mining tier enforcement (Zinc iron-only), axe wood breaking, shears drops, 2-stage hoe tilling
│   ├── DisabledItemsHandler.java  — Vanilla item removal (Blast furnace/smoker/furnace/chainmail/diamond gear, chunk replacement, player inventory)
│   └── VillagerTradeHandler.java  — Balanced villager professions (up to Bronze) and wandering trader trades
├── config/
│   └── ModConfig.java             — NeoForge config spec (fuel, sieve, bricks, drying, drops, village distance, farming)
├── compat/
│   ├── ModJeiPlugin.java          — JEI integration plugin (all categories & info tabs)
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

### 5. Mandatory AGENTS.md Update (`agents-md-updater`)
**MANDATORY**: After completing any changes in the project, review the diff and update `AGENTS.md` to guarantee that the documentation always accurately reflects current systems, registries, and item lists.

### 6. Full JEI Mechanics Integration (`jei-mechanics-documenter`)
**MANDATORY**: Whenever adding any unique, custom, or non-standard mechanic (in-world crafting, chisel carving, sun drying, custom fuels/speeds, manual ignition, altered block drops), automatically create corresponding JEI categories, recipes, and `addIngredientInfo` tabs to guarantee total in-game discoverability.

### 7. Mojang Asset Adaptation & JAPPA Artistry (`mojang-asset-artist`)
**MANDATORY**: When creating or modifying any pixel art textures (items, blocks, UI, armor layers), always follow JAPPA standards and MUST base the artwork upon official Mojang vanilla assets (from `minecraft_1.21.1_client.jar`), adapting, combining, and justifiable re-coloring them to guarantee seamless aesthetic harmony with modern Minecraft.

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
