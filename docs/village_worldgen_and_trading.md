# Village Generation, World Exploration & Economy

This system controls the world generation rarity of villages and outposts, replaces vanilla workstations, and rebalances trading economics.

---

## ⚙️ Mechanics & Working Principles
- **Village Rarity & Distance**:
  - Villages generate rarely (`spacing: 200`, `separation: 80`).
  - Strictly prevented from generating within **3000 blocks** of world spawn origin (`villageMinDistanceFromSpawn: 3000.0`).
- **Workstations & POI Replacement**:
  - Blast Furnaces automatically convert to **Brick Furnaces** (Armorer POI).
  - Smokers convert to **Ovens** (Butcher POI).
- **Villager & Trader Economy**:
  - Armorer, Weaponsmith, and Toolsmith villagers sell tools, weapons, and armor up to **Bronze tier** in exchange for emeralds.
  - Early-game survival items (sticks, pebbles, silicon shards, saplings, wild seeds, dry grass) can be bartered.
  - All iron/diamond/chainmail trades and supernatural vanilla purchases are purged.
- **Chest Loot Rebalancing**:
  - Rebalances dungeon, temple, outpost, and mineshaft chests to replace iron gear with copper/bronze gear, ingots with dusts/nuggets, and furnaces with brick furnaces/ovens.

---

## 🧪 Testing Guide & Edge Cases
1. **Spawn Distance**: Teleport to (0, 0) in a new world. Run `/locate structure #minecraft:village`; verify the nearest village is $\ge 3000$ blocks away.
2. **Workstation Conversion**: Visit a generated village armorer house; verify the blast furnace is replaced by a Brick Furnace.

---

## 📂 Key Source Files
- Handlers: `src/main/java/io/marrybye/github/larperthanwolves/event/VillageDistanceHandler.java`, `VillagerTradeHandler.java`, `LootTableModifierHandler.java`
