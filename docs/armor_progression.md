# Armor Progression & Defense Balancing (`ModArmorMaterials`)

This document defines armor materials, defense values, durability multipliers, toughness, and progression balance.

---

## 🛡️ Armor Tiers & Defense Matrix

| Tier | Base Multiplier | Helmet | Chestplate | Leggings | Boots | Total Defense | Toughness | Knockback Res | Repair Ingredient |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Leather (Кожа)** | 5 | 1 | 3 | 2 | 1 | **7 (3.5 🛡️)** | 0.0 | 0.0 | `larperthanwolves:tanned_leather` |
| **Copper (Медь)** | 8 | 1 | 4 | 3 | 1 | **9 (4.5 🛡️)** | 0.0 | 0.0 | `minecraft:copper_ingot`, `copper_dust` |
| **Bronze (Бронза)** | 12 | 2 | 5 | 4 | 2 | **13 (6.5 🛡️)** | 0.0 | 0.0 | `larperthanwolves:bronze_ingot` |
| **Iron (Железо)** | 15 | 2 | 6 | 5 | 2 | **15 (7.5 🛡️)** | 0.0 | 0.0 | `minecraft:iron_ingot` |
| **Reinforced Iron (Алмазный)** | 25 | 3 | 8 | 6 | 3 | **20 (10 🛡️)** | 1.5 | 0.0 | `larperthanwolves:diamond_ingot` |

---

## ⚙️ Armor Rules & Mechanics
- **Tanned Leather Requirement**:
  - Vanilla leather armor crafting and repairs strictly require **Tanned Leather** (`tanned_leather`), produced via the Drying Rack.
  - Standard raw leather cannot be crafted into armor.
- **Disabled Armor Tiers**:
  - **Chainmail Armor**: Fully disabled, unobtainable from loot or trading.
  - **Vanilla Diamond Armor**: Direct crafting in crafting tables is disabled and removed. Diamond armor is only obtainable by upgrading Iron Armor in a Smithing Table with **Diamond Ingots** (`diamond_ingot`) $\rightarrow$ **Reinforced Iron Armor** (Endgame Final Tier).
  - **Netherite Armor & Tools**: Fully disabled and purged from the game. Reinforced Iron is the absolute pinnacle tier.
- **Equipping & Mob Purge**:
  - Mobs cannot spawn with or equip disabled armor.
  - Players attempting to wear disabled armor have it safely removed and cleared.

---

## 📦 Crafting & Upgrade Flow
1. **Copper Armor**: 24 Copper Ingots in 3x3 table (Helmet: 5, Chest: 8, Legs: 7, Boots: 4).
2. **Bronze Armor**: 24 Bronze Ingots in 3x3 table.
3. **Iron Armor**: 24 Iron Ingots in 3x3 table.
4. **Reinforced Iron Armor**: Iron Armor Piece + Diamond Ingot + Netherite Upgrade Template in Smithing Table.

---

## 🧪 Testing Guide & Edge Cases
1. **Durability Check**: Equip full Copper Armor. Take damage; verify durability scales from 8x base multiplier (Helmet: 88, Chest: 128, Legs: 120, Boots: 104).
2. **Smithing Upgrade**: In a Smithing Table, combine Iron Chestplate + Diamond Ingot + Template $\rightarrow$ Reinforced Iron Chestplate.

---

## 📂 Key Source Files
- Materials: `src/main/java/io/marrybye/github/larperthanwolves/item/ModArmorMaterials.java`
- Items: `src/main/java/io/marrybye/github/larperthanwolves/item/ModItems.java`
- Textures: `src/main/resources/assets/larperthanwolves/textures/models/armor/`
