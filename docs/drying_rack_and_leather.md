# Daylight Drying Rack, Tanned Leather & Ropes

This system provides passive outdoor solar dehydration for converting raw plant fibers into Dry Grass and animal hide into Tanned Leather.

---

## ⚙️ Mechanics & Working Principles
- **Drying Rack (`drying_rack` / `DryingRackBlock` / `DryingRackBlockEntity`)**:
  - Crafted from 4 sticks in a 2x2 grid.
  - Directional placement (`FACING`).
  - **3D Inventory Item & In-World Model**: Rendered as a full 3D isometric wooden frame model in the GUI inventory, player hand, and in-world.
  - **Drying Conditions**: Operates strictly under open sky during daytime:
    - `isDay() && canSeeSky() && !isRaining()`.
- **Processing Recipes**:
  - **Grass (Short Grass, Tall Grass, Fern, Seagrass) $\rightarrow$ Dry Grass (`dry_grass`)**:
    - Takes **600 ticks (30 seconds)** of daylight drying.
    - Dry grass is used for furnace fuel and crafting basic ropes.
  - **Leather $\rightarrow$ Tanned Leather (`tanned_leather`)**:
    - Takes **1200 ticks (60 seconds)** of daylight drying.
    - Tanned leather is dark oiled hide required to craft Leather Armor and heavy ropes.
- **Rope Crafting Rules**:
  - Ropes can only be crafted from:
    1. Tanned Leather + Shears (gives 4 ropes).
    2. Dry Grass / Vines.
  - Direct crafting of ropes from raw vanilla leather is removed.

---

## 📦 Crafting & Progression
- **Drying Rack Recipe**: 4 Sticks (2x2 grid).
- **Tanned Leather Uses**: Leather Armor, Saddles, Ropes.

---

## 🔄 Cross-Mod Compatibility & Automation
- **Create (6.0.10+)**: Bulk Smoking (Encased Fan + Campfire/Fire) dries grass into Dry Grass and leather into Tanned Leather.
- **JEI Integration**: `DryingRackRecipeCategory` displays sunlight requirements, inputs, outputs, and drying times.

---

## 🧪 Testing Guide & Edge Cases
1. **Daylight Drying**: Place Drying Rack outdoors at noon (`/time set day`, `/weather clear`). Place raw Leather in the rack. Verify visual leather rendered on the rack. Wait 60s; verify item transforms into Tanned Leather.
2. **Night / Rain Pausing**: Set weather to rain (`/weather rain`). Verify drying timer pauses completely.
3. **3D Inventory Model**: Verify Drying Rack renders as a 3D isometric frame in inventory slots and GUI.

---

## 📂 Key Source Files
- Block: `src/main/java/io/marrybye/github/larperthanwolves/block/DryingRackBlock.java`
- Block Entity: `src/main/java/io/marrybye/github/larperthanwolves/block/entity/DryingRackBlockEntity.java`
- Models: `src/main/resources/assets/larperthanwolves/models/item/drying_rack.json`, `models/block/drying_rack_empty.json`
- Category: `src/main/java/io/marrybye/github/larperthanwolves/compat/DryingRackRecipeCategory.java`
