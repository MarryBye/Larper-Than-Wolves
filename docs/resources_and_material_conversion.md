# Resources, Material Ratios & Conversion Economics

This document details all intermediate materials, dusts, nuggets, pebbles, alloys, and mathematical conversion rules.

---

## 💎 Intermediate Resources & Mineral Fractions

### 1. Rock Pebbles (Самородки пород)
Obtained by mining stone and geological blocks with Silicon and Bronze pickaxes:
- **`stone_nugget`**: 4 Pebbles $\rightarrow$ 1 Cobblestone.
- **`granite_nugget`**: 4 Pebbles $\rightarrow$ 1 Granite.
- **`diorite_nugget`**: 4 Pebbles $\rightarrow$ 1 Diorite.
- **`andesite_nugget`**: 4 Pebbles $\rightarrow$ 1 Andesite.
- **`calcite_nugget`**: 4 Pebbles $\rightarrow$ 1 Calcite.
- **`tuff_nugget`**: 4 Pebbles $\rightarrow$ 1 Tuff.
- **`deepslate_nugget`**: 4 Pebbles $\rightarrow$ 1 Cobbled Deepslate.
- **`dripstone_nugget`**: 4 Pebbles $\rightarrow$ 1 Dripstone Block.
- **`netherrack_nugget`**: 4 Pebbles $\rightarrow$ 1 Netherrack.

### 2. Metal Dusts & Nuggets
- **`silicon_shard`**: Obtained from digging soils and sifting in Sieve (used for Silicon tools, chisels, flint replacement).
- **`copper_dust` / `copper_nugget`**: 2 Dust $\rightarrow$ 1 Raw Copper $\rightarrow$ 1 Copper Nugget $\rightarrow$ 4 Nuggets = 1 Copper Ingot.
- **`tin_dust` / `tin_nugget` / `tin_ingot`**: 2 Dust $\rightarrow$ 1 Raw Tin $\rightarrow$ 1 Tin Nugget $\rightarrow$ 4 Nuggets = 1 Tin Ingot.
- **`bronze_dust` / `bronze_nugget` / `bronze_ingot`**: 2 Dust $\rightarrow$ 1 Bronze Nugget $\rightarrow$ 4 Nuggets = 1 Bronze Ingot.
- **`iron_dust` / `iron_nugget`**: 2 Dust $\rightarrow$ 1 Raw Iron $\rightarrow$ 1 Iron Nugget $\rightarrow$ 4 Nuggets = 1 Iron Ingot.
- **`gold_dust` / `gold_nugget`**: 2 Dust $\rightarrow$ 1 Raw Gold $\rightarrow$ 1 Gold Nugget $\rightarrow$ 4 Nuggets = 1 Gold Ingot.
- **`diamond_dust` / `diamond_nugget`**: 2 Dust $\rightarrow$ 1 Diamond Nugget $\rightarrow$ 4 Nuggets = 1 Diamond (and 1 Diamond $\rightarrow$ 4 Nuggets).

---

## ⚖️ The 4-to-1 Nugget / 8-to-1 Dust Mathematical Economy

Unlike vanilla's 9-to-1 ratio, the mod standardizes all metal, gem, and alloy conversions to a clean binary **4-to-1 / 8-to-1 scale**:
* **1 Ingot / Diamond** $=$ **4 Nuggets** (1 Ingot $\rightarrow$ 4 Nuggets shapeless; 4 Nuggets $\rightarrow$ 1 Ingot 2x2 shaped)
* **1 Nugget / Raw Chunk** $=$ **2 Dusts**
* **1 Ingot** $=$ **8 Dusts** (when ground in Hand Mill)
* **9-Nugget Recipe Elimination (`RecipeManagerMixin`)**:
  * All 9-to-1 compacting (3x3 grid) and 1-to-9 decompacting recipes across Vanilla and Create (Iron, Gold, Copper, Zinc, Brass) are dynamically purged and replaced with 4-to-1 standard recipes.

### Alloy Formulations in Alloy Mixer
* **Bronze Ingot (`bronze_ingot`)**: 2 Copper Ingots + 1 Tin Ingot $\rightarrow$ **3 Bronze Ingots** (100% metal mass conservation).
* **Brass Ingot (`create:brass_ingot`)**: 1 Copper Ingot + 1 Zinc Ingot $\rightarrow$ 1 Brass Ingot.
* **Diamond Ingot (`diamond_ingot`)**: 1 Diamond + 1 Iron Ingot + 1 Copper Ingot $\rightarrow$ 1 Diamond Ingot.
  * *Recycling in Hand Mill*: 1 Diamond Ingot $\rightarrow$ 8 Diamond Dust + 8 Iron Dust + 8 Copper Dust (100% component preservation).

---

## 🔄 Cross-Mod Compatibility & Automation
- **Create (6.0.10+)**:
  - Mechanical Mixer, Crushing Wheels, and Bulk Blasting follow exact 4:1 and 8:1 ratios.
  - 4 Zinc Nuggets $\leftrightarrow$ 1 Zinc Ingot; 4 Brass Nuggets $\leftrightarrow$ 1 Brass Ingot.
  - All external recipes requiring vanilla `Furnace` or `Blast Furnace` (e.g. Steam Engines, Boilers, machines) automatically accept `Brick Furnace` (`larperthanwolves:brick_furnace`), and recipes requiring `Smoker` accept `Food Oven` (`larperthanwolves:oven`).
- **JEI Integration**: All conversion recipes registered with accurate input/output counts.

---

## 📂 Key Source Files
- Items: `src/main/java/io/marrybye/github/larperthanwolves/item/ModItems.java`
- Recipes: `src/main/java/io/marrybye/github/larperthanwolves/recipe/`
- Mill Registry: `src/main/java/io/marrybye/github/larperthanwolves/recipe/MillRegistry.java`
- Alloy Registry: `src/main/java/io/marrybye/github/larperthanwolves/recipe/AlloyRegistry.java`
