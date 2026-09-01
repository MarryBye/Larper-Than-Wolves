# JEI Unified Integration Architecture

## Overview
Larper Than Wolves features complete and exhaustive integration with Just Enough Items (JEI) 19.x+. To maintain compile-time safety and prevent undocumented mechanics or missing machine recipes, the mod utilizes a unified interface architecture across blocks and items.

## Architecture

### 1. `IJeiMachineStation` Interface
Applied to all functional processing machines and workstation blocks (`BrickFurnaceBlock`, `AdvancedSmelterBlock`, `MithrilFurnaceBlock`, `OvenBlock`, `AlloyMixerBlock`, `SieveBlock`, `MillBlock`, `DryingRackBlock`, `WorkStumpBlock`, `UnfiredBrickBlock`):
```java
public interface IJeiMachineStation extends IJeiDocumentationProvider {
    void registerJeiCategories(IRecipeCategoryRegistration registration, IGuiHelper guiHelper);
    void registerJeiRecipes(IRecipeRegistration registration);
    void registerJeiCatalysts(IRecipeCatalystRegistration registration);
    void registerJeiGuiHandlers(IGuiHandlerRegistration registration);
    void registerJeiRecipeTransferHandlers(IRecipeTransferRegistration registration);
    void registerJeiInfo(IRecipeRegistration registration);
}
```

### 2. `IJeiDocumentationProvider` Interface
Applied to items and blocks providing in-game explanatory descriptions and mechanics tabs in JEI:
```java
public interface IJeiDocumentationProvider {
    void registerJeiInfo(IRecipeRegistration registration);
}
```
Base classes `ModItem` and `ModBlock` allow declarative registration of item/block JEI description keys upon instantiation:
```java
public static final DeferredItem<Item> POINTED_STICK = ITEMS.register("pointed_stick",
        () -> new ModItem(new Item.Properties().durability(2).stacksTo(1), "jei.larperthanwolves.info.pointed_stick"));
```

### 3. Dynamic Dispatcher (`ModJeiPlugin`)
`ModJeiPlugin` dynamically iterates over `ModBlocks.BLOCKS` and `ModItems.ITEMS`, invoking machine station lifecycles and documentation registrations automatically.

## Registered Custom Categories
1. **Brick Furnace Smelting** (`BrickFurnaceRecipeCategory`): Ores & raw chunks $\rightarrow$ metal nuggets.
2. **Advanced Smelting** (`AdvancedSmelterRecipeCategory`): High-temperature melting (Raw Mithril $\rightarrow$ Mithril Nugget, Ores $\rightarrow$ Ingots).
3. **Mithril Smelting** (`MithrilFurnaceRecipeCategory`): Supreme nether smelting (Raw Mithril $\rightarrow$ Mithril Ingot).
4. **Alloy Mixing** (`AlloyMixerRecipeCategory`): 2 Copper Ingots + 1 Tin Ingot $\rightarrow$ 3 Bronze Ingots.
5. **Soil Sifting** (`SieveRecipeCategory`): Sifting ordinary soils and mineral-rich soils for nuggets, dusts, and gems.
6. **Hand Milling** (`MillRecipeCategory`): Grinding ingots, gems, ores, and bones into fine dusts.
7. **Tree Stump Chisel Carving** (`ChiselRecipeCategory`): 4-stage manual carving of tree stumps into crafting tables.
8. **Sun Drying** (`SunDryingRecipeCategory`): Outdoor curing of wet clay bricks into solid fired bricks.
9. **Drying Rack** (`DryingRackRecipeCategory`): Dehydrating wild foliage into dry grass and raw hides into tanned leather.
10. **Machine Fuels & Ignition** (`MachineFuelRecipeCategory`): 9-tier combustion fuel comparison and manual ignition tools.
11. **Soil Digging Drops** (`GravelDiggingRecipeCategory`): Exact drop tables and probabilities for bare-hand vs shovel soil excavation.
