# Village Generation, World Exploration & Economy

This system controls the world generation rarity of villages and outposts, replaces vanilla workstations, and rebalances trading economics and universal generated loot.

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
- **Universal Loot Table Rebalancing (`ChestLootModifier` & `RemoveDisabledItemsModifier`)**:
  - Rebalances all chests, ruined portals, archaeology, trial chambers, spawners, pots, and gameplay loot:
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

---

## 🧪 Testing Guide & Edge Cases
1. **Spawn Distance**: Teleport to (0, 0) in a new world. Run `/locate structure #minecraft:village`; verify the nearest village is $\ge 3000$ blocks away.
2. **Ruined Portal & Chest Loot**: Locate a ruined portal chest. Verify that gold tools, iron ingots, and iron nuggets are replaced with copper/tin items and rare bronze tools.
3. **Workstation Conversion**: Visit a generated village armorer house; verify the blast furnace is replaced by a Brick Furnace.

---

## 📂 Key Source Files
- Handlers: `src/main/java/io/marrybye/github/larperthanwolves/event/VillagerTradeHandler.java`
- Loot Modifiers: `src/main/java/io/marrybye/github/larperthanwolves/loot/ChestLootModifier.java`, `RemoveDisabledItemsModifier.java`
