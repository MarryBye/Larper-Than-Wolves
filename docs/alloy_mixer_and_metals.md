# Alloy Mixer, Metal Progression & Material Conversion

This system manages metallurgical alloying, ratio conversions, and tool/armor tier progression.

---

## ⚙️ Mechanics & Working Principles
- **Alloy Mixer (`alloy_mixer` / `AlloyMixerBlock` / `AlloyMixerBlockEntity`)**:
  - Crafted on a 3x3 Crafting Table from **4 Copper Ingots + 1 Brick Furnace + 4 Brick Slabs** (`SCS / CFC / SCS`).
  - 5-slot container (3 input slots, 1 output slot, 1 fuel slot).
  - Handles high-temperature alloy metallurgy in the Copper Age without requiring any iron.
  - Mining & Harvesting: Strictly requires a **Pickaxe of Copper tier or higher** (Copper, Bronze, Iron, Reinforced Iron, Netherite) to be mined and harvested. Drops itself and spills inventory contents.
- **Metal & Material Ratios**:
  - **Natural Metals (Iron, Copper, Gold, Tin, Zinc)**:
    - 2 Ore Dust $\rightarrow$ 1 Raw Ore (chunk)
    - 1 Raw Ore in Brick Furnace $\rightarrow$ 1 Metal Nugget
    - 4 Metal Nuggets $\rightarrow$ 1 Ingot
  - **Bronze**:
    - 2 Bronze Dust $\rightarrow$ 1 Bronze Nugget
    - 4 Bronze Nuggets $\rightarrow$ 1 Bronze Ingot
    - 2 Copper Ingot + 1 Tin Ingot in Alloy Mixer $\rightarrow$ **3 Bronze Ingots**
  - **Brass (`create:brass_ingot`)**:
    - 1 Copper Ingot + 1 Zinc Ingot in Alloy Mixer $\rightarrow$ 1 Brass Ingot
  - **Diamond**:
    - 2 Diamond Dust $\rightarrow$ 1 Diamond Nugget
    - 4 Diamond Nuggets $\rightarrow$ 1 Diamond (and 1 Diamond $\rightarrow$ 4 Diamond Nuggets)
  - **Reinforced Iron (Diamond Ingot)**:
    - 1 Diamond + 1 Iron Ingot + 1 Copper Ingot in Alloy Mixer $\rightarrow$ 1 Diamond Ingot
    - Used in Smithing Table to upgrade Iron tools & armor to Reinforced Iron.

---

## 📦 Tool & Armor Tiers
- **Silicon**: Durability 30. Coal, Copper Dust, Stone Pebbles.
- **Copper**: Durability 100. Coal, Raw Copper, Tin Dust, Rich Soils.
- **Bronze**: Durability 150. Raw Tin, Iron Dust, Deepslate/Tuff/Netherrack nuggets.
- **Iron**: Durability 250. Standard ores, Raw Zinc, full blocks.
- **Reinforced Iron**: High durability & mining speed, Ancient Debris & Obsidian.

---

## 🔄 Cross-Mod Compatibility & Automation
- **Create (6.0.10+)**: Heated Mechanical Mixer recipes registered for Bronze Ingot (3x), Brass Ingot, and Diamond Ingot.
- **JEI Integration**: `AlloyMixerRecipeCategory` displaying all alloy recipes and required components.

---

## 🧪 Testing Guide & Edge Cases
1. **Bronze Alloy Mixing**: Place 2 Copper Ingots + 1 Tin Ingot + Fuel into Alloy Mixer. Ignite mixer; verify 3 Bronze Ingots are produced.
2. **Reinforced Iron Smithing**: Place Iron Pickaxe + Diamond Ingot + Netherite Upgrade Template into Smithing Table; verify Reinforced Iron Pickaxe.

---

## 📂 Key Source Files
- Block: `src/main/java/io/marrybye/github/larperthanwolves/block/AlloyMixerBlock.java`
- Block Entity: `src/main/java/io/marrybye/github/larperthanwolves/block/entity/AlloyMixerBlockEntity.java`
- Registry: `src/main/java/io/marrybye/github/larperthanwolves/recipe/AlloyRegistry.java`
