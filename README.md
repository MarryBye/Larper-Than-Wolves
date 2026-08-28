# BetterThanGamers - Minecraft NeoForge Mod

A gameplay modification for Minecraft 1.21.1 using NeoForge that improves early-game survival mechanics and progression.

## Features

### New Items

- **Silicon Shards** - Crafted from gravel with lower drop rate
- **Dry Grass** - Early game fuel source for furnaces
- **Ore Dusts** - Drop from mining ore with silicon tools
- **Stone Nuggets** - Drop from stone variants
- **Rope** - Crafted from leather using silicon shears
- **Lighter** - Fire-starting wooden tool
- **Copper Tools & Armor** - Complete sets for early progression
- **Silicon Tools** - Budget-friendly early-game tools

### Blocks

- **Brick Furnace** - Custom furnace with unique mechanics
- **Brick Slab** - Building block and furnace component

### Key Mechanics

#### Furnace System
- Fuel efficiency varies by type (dry grass, wood, coal, etc.)
- Customizable input/output slots
- Ignited with lighter tool

#### Mining Progression
- Silicon tools for ore dust and stone
- Copper tools for iron and copper
- Traditional progression for higher ores

#### Survival Changes
- Gravel drops silicon shards (not flint)
- Grass doesn't drop seeds
- Hoe interaction with grass creates farmable dirt

## Installation

1. Download JAR from Releases
2. Place in NeoForge `mods` folder
3. Requires Minecraft 1.21.1 + NeoForge 21.1.248

## Building

```bash
./gradlew build
```

Output: `build/libs/betterthangamers-1.0.0.jar`

## Texture Files

Add textures to corresponding folders:
- Items: `assets/betterthangamers/textures/item/`
- Blocks: `assets/betterthangamers/textures/block/`

See `TEXTURE_GUIDE.md` for complete requirements.

## Version

**v1.0.0** - Initial release for NeoForge 1.21.1

## License

All Rights Reserved
