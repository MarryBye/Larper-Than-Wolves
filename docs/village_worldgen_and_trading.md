# Village Generation, World Exploration & Economy

This system controls the world generation rarity of villages and outposts, replaces and purges vanilla workstations, removes worldgen chests (except Bastion Remnants), and rebalances trading economics.

---

## ⚙️ Mechanics & Working Principles
- **Village Rarity & Distance**:
  - Villages generate rarely (`spacing: 200`, `separation: 80`).
  - Strictly prevented from generating within **3000 blocks** of world spawn origin (`villageMinDistanceFromSpawn: 3000.0`).
- **Complete Workstation Purging & Conversion**:
  - **Crafting Tables**: Purged from all naturally generated structures (witch huts, village houses, woodland mansions, trail ruins). Players MUST carve a Crafting Table from a tree stump using a Chisel.
  - **Furnaces & Blast Furnaces**: Automatically converted into **Brick Furnaces** (Armorer POI).
  - **Smokers**: Automatically converted into **Ovens** (Butcher POI).
- **Worldgen Chest Removal (Exempting Bastions)**:
  - All chests, trapped chests, and barrels in standard structures (villages, dungeons, mineshafts, temples, ancient cities, trial chambers, ruined portals, shipwrecks, buried treasure, igloos) are **completely removed from worldgen and yield 0 loot**.
  - **Bastion Remnants (`minecraft:chests/bastion_*`)**: The ONLY chests retained in the world, preserving **Netherite Upgrade Smithing Templates** and snout trims for high-end armor progression.
- **Villager & Trader Economy**:
  - Armorer, Weaponsmith, and Toolsmith villagers sell tools, weapons, and armor up to **Bronze tier** in exchange for emeralds.
  - Early-game survival items (sticks, pebbles, silicon shards, saplings, wild seeds, dry grass) can be bartered.
  - All iron/diamond/chainmail trades and supernatural vanilla purchases are purged.

---

## 🧪 Testing Guide & Edge Cases
1. **Spawn Distance**: Teleport to (0, 0) in a new world. Run `/locate structure #minecraft:village`; verify the nearest village is $\ge 3000$ blocks away.
2. **Workstation Conversion**: Visit a generated village armorer house; verify the blast furnace/furnace is replaced by a Brick Furnace, and crafting tables are absent.
3. **Bastion Chests**: Locate a Nether Bastion Remnant. Verify chests still spawn containing Netherite Upgrade Templates.
4. **Non-Bastion Structures**: Locate a dungeon, desert temple, or village. Verify chests are absent or yield 0 loot.

---

## 📂 Key Source Files
- Handlers: `src/main/java/io/marrybye/github/larperthanwolves/event/DisabledItemsHandler.java`, `VillagerTradeHandler.java`
- Loot Modifiers: `src/main/java/io/marrybye/github/larperthanwolves/loot/ChestLootModifier.java`, `RemoveDisabledItemsModifier.java`
