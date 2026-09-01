---
name: jei-mechanics-documenter
description: >-
  Protocol to automatically create and update JEI categories, recipe POJOs, and ingredient information tabs for any unique, custom, or non-standard game mechanic to ensure total player clarity.
---

# JEI Mechanics Documentation & Integration Protocol

Hardcore survival and overhaul mods introduce non-standard mechanics (in-world crafting, chisel carving, passive drying under sunlight, manual machine ignition, custom fueling speeds, modified tool tier drop tables). Without explicit JEI integration and bidirectional recipe indexing, players cannot discover these mechanics through standard recipes.

Whenever any custom machine, block, item, or non-standard mechanic is added or changed in the mod, this skill mandates creating full JEI support with mandatory in-game documentation and bidirectional recipe synchronization.

---

## 📋 Core Mandates

### 1. Mandatory In-Game Documentation (`addIngredientInfo`)
- **Universal Description Coverage**: EVERY item and block registered in the mod (and overhauled vanilla items like crafting tables, hoes, axes, clay, bones, gravel, flint) MUST have a dedicated "Information" tab in JEI.
- **Unified Architecture**:
  - All workstations and functional machines implement `IJeiMachineStation` (which extends `IJeiDocumentationProvider`).
  - Base items and blocks extend `ModItem` or `ModBlock`, specifying their translation key upon creation.
  - Overhauled vanilla items and tool/armor tier sets are registered in `ModJeiPlugin.registerOverhauledVanillaDocumentation(registration)`.
- **Content Requirements**:
  - What the item/block is and its role in the mod's progression.
  - Required tool tiers and dig rules (e.g. Copper+ pickaxe for workstations, shovel for clay/rich soil, axe for wooden workstations).
  - Processing steps, operating controls (e.g. Shift + Right-Click, cranking, solar conditions, manual ignition).
  - Both `ru_ru.json` and `en_us.json` must contain detailed, grammatically correct translations.

---

### 2. Bidirectional Recipe Synchronization (Reverse Recipe Lookup / "Uses" on Stations)
- **The Station-Recipe Symmetry Law**:
  > *If Items A, B, and C are crafted or processed at Station S (e.g., Drying Rack, Sieve Table, Hand Mill, Brick Furnace, Advanced Smelter, Mithril Furnace, Alloy Mixer), then pressing **"U" (Show Uses)** on Station S MUST display all recipes processed at Station S together in that category.*
- **How to Guarantee Reverse Lookup in JEI 19.x+**:
  JEI's "Uses" (U key) lookup inspects the ingredients indexed in the recipe layout under `RecipeIngredientRole.INPUT` and `RecipeIngredientRole.CATALYST`. If a category only registers `addRecipeCatalyst` globally but omits the station from the recipe layout, JEI's ingredient focus query will NOT find the recipes when pressing "U" on the station!
  
  **Mandatory Implementation Rules**:
  1. **Global Catalyst Registration**: Register the block/item via `registration.addRecipeCatalyst(new ItemStack(station), CATEGORY.TYPE)`.
  2. **Recipe Layout Indexing**: In `IRecipeCategory.setRecipe(IRecipeLayoutBuilder builder, RECIPE recipe, IFocusGroup focuses)`:
     - For categories with dedicated station/catalyst slots (e.g., Drying Rack, Chisel):
       ```java
       builder.addSlot(RecipeIngredientRole.CATALYST, x, y)
              .addItemStack(new ItemStack(ModBlocks.STATION.get()))
              .setBackground(this.slotBackground, -1, -1);
       ```
     - For GUI container categories with custom backgrounds (e.g., Sieve, Hand Mill, Brick Furnace, Advanced Smelter, Mithril Furnace, Alloy Mixer, Sun Drying):
       ```java
       builder.addInvisibleIngredients(RecipeIngredientRole.CATALYST)
              .addItemStacks(List.of(new ItemStack(ModBlocks.STATION.get()), new ItemStack(ModBlocks.ACCESSORY.get())));
       ```
  3. **Multi-Tool & Accessory Support**:
     - If a station can be operated by an accessory or tool (e.g., `MillCrankBlock` on `MillBlock` and `SieveBlock`, or shovels for `GravelDiggingRecipeCategory`), all compatible items must be included in the `CATALYST` ingredient list so that pressing "U" on any of them opens the recipes.

---

## 📋 JEI Category Checklist

1. **Drying Rack (`DryingRackRecipeCategory`)**:
   - Inputs: Foliage / Raw Leather.
   - Catalyst: Drying Rack (`ModBlocks.DRYING_RACK.get()`).
   - Outputs: Dry Grass / Tanned Leather.
   - Operating conditions: Outdoor daytime under open sky.

2. **Soil Sifting (`SieveRecipeCategory`)**:
   - Inputs: Gravel, Sand, Red Sand, Dirt, Grass Block, Rich Soils.
   - Catalysts: Sieve Table (`ModBlocks.SIEVE.get()`), Mill Crank (`ModBlocks.MILL_CRANK.get()`).
   - Outputs: 3x3 grid of mineral dusts, nuggets, shards, gems.
   - Shaking requirements: 5 shakes (Shift + Right-Click) or Create rotation.

3. **Hand Milling (`MillRecipeCategory`)**:
   - Inputs: Ingots, Gems, Raw Ores, Rocks, Bones.
   - Catalysts: Hand Mill (`ModBlocks.MILL.get()`), Mill Crank (`ModBlocks.MILL_CRANK.get()`).
   - Outputs: 3 output slots for dusts, gravel, sand, bone meal.
   - Cranking requirements: 20 rotations (0.5s per turn) or Create top shaft rotation.

4. **Tree Stump Chisel Carving (`ChiselRecipeCategory`)**:
   - Inputs: Wood Stumps / Logs.
   - Catalyst: Copper Chisel (`ModItems.CHISEL.get()`).
   - Intermediary: Work Stump.
   - Output: Crafting Table.
   - Steps: 4 manual chisel carvings.

5. **Brick Furnace Smelting (`BrickFurnaceRecipeCategory`)**:
   - Inputs: Raw Ores, Sand, Unfired Brick, Clay, Wet Sponge.
   - Catalyst: Brick Furnace (`ModBlocks.BRICK_FURNACE.get()`).
   - Outputs: Metal Nuggets, Glass, Brick, Terracotta, Sponge.

6. **Advanced Smelting (`AdvancedSmelterRecipeCategory`)**:
   - Inputs: Raw Ores, Raw Mithril, Sand, Unfired Brick.
   - Catalyst: Advanced Smelter (`ModBlocks.ADVANCED_SMELTER.get()`).
   - Outputs: Full Ingots, 1 Mithril Nugget, Glass, Brick.

7. **Mithril Smelting (`MithrilFurnaceRecipeCategory`)**:
   - Inputs: Raw Mithril, Raw Ores.
   - Catalyst: Mithril Furnace (`ModBlocks.MITHRIL_FURNACE.get()`).
   - Outputs: Full Mithril Ingot, Full Metal Ingots.

8. **Alloy Mixing (`AlloyMixerRecipeCategory`)**:
   - Inputs: 3 input slots (e.g. 2 Copper + 1 Tin).
   - Catalyst: Alloy Mixer (`ModBlocks.ALLOY_MIXER.get()`).
   - Output: 3 Bronze Ingots, Diamond Ingot.

9. **Sun Drying (`SunDryingRecipeCategory`)**:
   - Input: Unfired Brick.
   - Catalyst: Unfired Brick (`ModBlocks.UNFIRED_BRICK.asItem()`).
   - Output: Fired Brick.
   - Condition: Outdoor direct sunlight curing.

10. **Machine Fuels & Manual Ignition (`MachineFuelRecipeCategory`)**:
    - Fuels: 9 combustion fuel tiers.
    - Catalysts: Ignition tools (Lighter, Flint & Steel), Heated Machines (Brick Furnace, Smelter, Mithril Furnace, Oven, Alloy Mixer).

11. **Soil Excavation Drops (`GravelDiggingRecipeCategory`)**:
    - Blocks: Gravel, Sand, Red Sand, Dirt, Rich Soils.
    - Catalysts: All tool tier shovels (Silicon, Copper, Bronze, Iron, Reinforced Iron, Mithril, Diamond, Netherite).
    - Outputs: Shards, Flint, Copper Dust, Soil blocks with exact probability labels.
