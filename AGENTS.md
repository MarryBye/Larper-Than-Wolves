# AGENTS.md

This file is a comprehensive guide for AI agents working on this NeoForge Minecraft mod project.

## Project Overview
- **Mod ID**: `larperthanwolves`
- **Package**: `io.marrybye.github.larperthanwolves`
- **Minecraft**: 1.21.1
- **NeoForge**: 21.1.248
- **Java**: 21
- **Build Tool**: Gradle with NeoForge ModDev plugin 2.0.144
- **Current Version**: 1.8.0

## Project Architecture

The project is a hardcore survival mod inspired by Better Than Wolves. It overhauls Minecraft's progression system with:
- Custom tool/armor tiers: Silicon → Copper → Bronze → Iron → Reinforced Iron → Netherite
- Custom machines: Brick Furnace (replaces vanilla), Alloy Mixer, Sieve
- Disabled vanilla items: wooden/stone/diamond tools, chainmail armor, vanilla furnace
- Custom block break mechanics with tier-gated mining
- Tree stump system with chisel-based crafting table creation
- Tin ore worldgen and bronze metallurgy
- JEI integration

## File Structure
```
src/main/java/io/marrybye/github/larperthanwolves/
├── LarperThanWolves.java          — Main mod entry point, registers all systems
├── block/
│   ├── ModBlocks.java             — Block registration (DeferredRegister)
│   ├── BrickFurnaceBlock.java     — Brick furnace block (facing, 4 stages)
│   ├── AlloyMixerBlock.java       — Alloy mixer block (facing, 4 stages)
│   ├── SieveBlock.java            — Sieve block
│   ├── StumpBlock.java            — Tree stump base block (extends RotatedPillarBlock)
│   ├── WorkStumpBlock.java        — Work stump with chisel progression (3 STAGE states)
│   ├── UnfiredBrickBlock.java     — Unfired brick (dries under sunlight, 4 stages)
│   └── entity/
│       ├── ModBlockEntities.java           — BlockEntity type registration
│       ├── BrickFurnaceBlockEntity.java    — Furnace logic (7 slots, custom fuel, WorldlyContainer)
│       ├── AlloyMixerBlockEntity.java      — Alloy mixing logic (5 slots, 2 recipes)
│       ├── SieveBlockEntity.java           — Sieve passive processing (18 slots)
│       ├── UnfiredBrickBlockEntity.java    — Brick drying ticking logic
│       └── FuelRegistry.java               — Unified fuel durations, cook speeds & validation
├── recipe/
│   ├── AlloyRecipe.java           — Modular alloy mixer recipe model
│   ├── AlloyRegistry.java         — Central alloy mixer recipe registry & JEI sync
│   └── SmeltingRegistry.java      — Brick furnace custom smelting overrides & fallback
├── item/
│   ├── ModItems.java              — Item registration (all tools, armor, materials, block items)
│   ├── ModToolMaterials.java      — Custom tool tiers (SILICON, COPPER, BRONZE, REINFORCED_IRON)
│   ├── ModArmorMaterials.java     — Custom armor materials (COPPER, BRONZE, REINFORCED_IRON)
│   ├── ModCreativeTabs.java       — Creative tab registration
│   └── ChiselItem.java            — Chisel right-click logic (stump → work stump → crafting table)
├── menu/
│   ├── ModMenuTypes.java          — Menu type registration
│   ├── BrickFurnaceMenu.java      — Furnace container menu (7 slots)
│   ├── AlloyMixerMenu.java        — Mixer container menu (5 slots)
│   └── SieveMenu.java             — Sieve container menu (18 slots)
├── client/
│   ├── ModClientEvents.java       — Client-side screen registration
│   ├── BrickFurnaceScreen.java    — Furnace GUI renderer
│   ├── AlloyMixerScreen.java      — Mixer GUI renderer
│   └── SieveScreen.java           — Sieve GUI renderer
├── event/
│   ├── BlockBreakHandler.java     — Mining tier enforcement, custom drops, hoeing mechanics
│   └── DisabledItemsHandler.java  — Vanilla item removal system (creative tabs, mob equipment, trades, loot)
├── config/
│   └── ModConfig.java             — NeoForge config spec (fuel, sieve, bricks, drops)
├── compat/
│   ├── ModJeiPlugin.java          — JEI integration plugin
│   ├── AlloyMixerRecipe.java      — JEI alloy recipe POJO
│   ├── AlloyMixerRecipeCategory.java — JEI alloy mixer category
│   ├── SieveJeiRecipe.java        — JEI sieve recipe POJO
│   └── SieveRecipeCategory.java   — JEI sieve category
├── loot/
│   ├── ModLootModifiers.java      — Loot modifier registration
│   └── RemoveDisabledItemsModifier.java — Global loot modifier that strips disabled items
├── datagen/
│   ├── DataGenerators.java        — Data generation entry point
│   ├── ModRecipesProvider.java    — Recipe data generation
│   └── ModItemModelProvider.java  — Item model data generation
└── mixin/
    └── TrunkPlacerMixin.java      — Places stump blocks at tree bases during worldgen
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
├── data/larperthanwolves/
│   ├── recipe/                    — Crafting and smelting recipes
│   ├── loot_table/                — Loot tables  
│   ├── tags/                      — Block and item tags
│   ├── worldgen/                  — Tin ore generation
│   └── neoforge/                  — Biome modifiers, global loot modifiers
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

## Version Tagging
```bash
git tag -a v1.8.0 -m "Release v1.8.0"
git push origin main --tags
```
