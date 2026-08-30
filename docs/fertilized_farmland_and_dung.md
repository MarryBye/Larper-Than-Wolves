# Fertilized Farmland, Animal Dung & 2-Stage Tilling

This overhaul introduces mandatory soil fertilization for crop cultivation, animal digestion cycles yielding Dung, and a 2-stage hoe tilling process for wild seed harvesting.

---

## ⚙️ Mechanics & Working Principles
- **Mandatory Soil Fertilization (`fertilized_farmland` / `FertilizedFarmlandBlock`)**:
  - Crops planted on vanilla unfertilized farmland (`Blocks.FARMLAND`) will **NOT** grow. Growth ticks are completely intercepted and cancelled.
  - **Fertilization Types & Charges**:
    - **Bone Meal**: Right-clicking farmland or planted crops with Bone Meal creates Fertilized Farmland with **1 charge** (`charges = 1`).
    - **Dung (`dung`)**: Right-clicking farmland or crops with Dung provides powerful long-lasting fertilization with **3 charges** (`charges = 3`).
  - **Visuals**: Fertilized farmland features distinct ivory/white mineral flecks on both dry and hydrated textures.
  - **Harvest Cycle Consumption**: When a fully mature crop on fertilized farmland is broken/harvested, the block decrements 1 charge. When charges reach 0, the soil resets to standard unfertilized farmland.
- **Animal Dung Digestion Cycle (`AnimalDungHandler`)**:
  - Animals (Cows, Sheep, Pigs, Horses, Goats, Camels, Sniffers, Wolves, etc.) digest food and drop **Dung** after **3600 ticks (3 minutes)**.
  - Digestion timer starts when:
    - Players breed/feed animals.
    - Sheep graze on grass blocks.
    - Wild wolves hunt and kill prey animals.
- **2-Stage Hoe Tilling**:
  - **Stage 1 (Grassy Surface $\rightarrow$ Plain Dirt / Rich Dirt)**:
    - Right-clicking grass, podzol, mycelium, or rich grass with any hoe strips the grass layer into plain dirt (or rich dirt).
    - **35% chance** to harvest wild seeds: `Wheat Seeds` (50%), `Carrot` (15%), `Potato` (15%), `Beetroot Seeds` (10%), `Pumpkin Seeds` (5%), `Melon Seeds` (5%).
  - **Stage 2 (Dirt / Rich Dirt $\rightarrow$ Farmland)**:
    - Right-clicking dirt or rich dirt with any hoe tills it into standard unfertilized farmland ready for fertilization.

---

## 📦 Crafting & Progression
- **Bone Meal Source**: Only obtained by grinding 2 bones in the Hand Mill.
- **Dung Source**: Collected from living animal pens and pastures.
- **Hoe Progression**: Silicon Hoe (30 uses), Copper Hoe (100 uses), Bronze Hoe (150 uses), Iron Hoe (250 uses).

---

## 🔄 Cross-Mod Compatibility & Automation
- **Create (6.0.10+)**: Mechanical harvesters and deployers interact with fertilized farmland; deployers can apply bone meal or dung automatically.
- **JEI Integration**: Informational description tabs for `dung`, `fertilized_farmland`, and wild seed foraging.

---

## 🧪 Testing Guide & Edge Cases
1. **Unfertilized Growth Block**: Plant wheat on vanilla farmland. Advance random tick speed (`/gamerule randomTickSpeed 1000`); verify wheat remains at stage 0.
2. **Bone Meal Fertilization**: Right-click farmland with Bone Meal. Verify texture changes to ivory-flecked fertilized farmland. Advance time; verify wheat grows to stage 7. Break wheat; verify soil resets to plain farmland.
3. **Dung Fertilization**: Apply Dung. Verify 3 harvest cycles are supported before resetting.
4. **2-Stage Tilling**: Right-click wild grass with a Silicon Hoe. Verify it becomes Dirt and rolls for seeds. Right-click Dirt again; verify it becomes Farmland.

---

## 📂 Key Source Files
- Block: `src/main/java/io/marrybye/github/larperthanwolves/block/FertilizedFarmlandBlock.java`
- Handlers:
  - `src/main/java/io/marrybye/github/larperthanwolves/event/FarmlandGrowthHandler.java`
  - `src/main/java/io/marrybye/github/larperthanwolves/event/AnimalDungHandler.java`
  - `src/main/java/io/marrybye/github/larperthanwolves/event/HoeTillingHandler.java`
- Item: `src/main/java/io/marrybye/github/larperthanwolves/item/ModItems.java` (`DUNG`)
