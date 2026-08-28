# Texture Placement Guide

This mod requires textures for all custom items and blocks. Please place textures in the following directories:

## Item Textures

All item textures should be placed in:
```
src/main/resources/assets/betterthangamers/textures/item/
```

Required item textures (PNG format, 16x16 or 32x32 pixels):

### Basic Items
- silicon_shard.png
- dry_grass.png
- iron_dust.png
- copper_dust.png
- gold_dust.png
- stone_nugget.png
- diorite_nugget.png
- granite_nugget.png
- andesite_nugget.png
- tuff_nugget.png
- rope.png
- lighter.png

### Silicon Tools
- silicon_pickaxe.png
- silicon_axe.png
- silicon_shovel.png
- silicon_shears.png
- silicon_spear.png

### Copper Tools
- copper_sword.png
- copper_pickaxe.png
- copper_axe.png
- copper_shovel.png
- copper_hoe.png

### Copper Armor
- copper_helmet.png
- copper_chestplate.png
- copper_leggings.png
- copper_boots.png

## Block Textures

All block textures should be placed in:
```
src/main/resources/assets/betterthangamers/textures/block/
```

Required block textures:
- brick_furnace_front.png (when lit: brick_furnace_front_lit.png)
- brick_furnace_side.png
- brick_furnace_top.png
- brick_furnace_bottom.png
- brick_slab_top.png
- brick_slab_side.png
- brick_slab_bottom.png

## Block Models

All block models should be placed in:
```
src/main/resources/assets/betterthangamers/models/block/
```

Models should be in JSON format according to Minecraft's block model specification.

## Item Models

All item models should be placed in:
```
src/main/resources/assets/betterthangamers/models/item/
```

Models should be in JSON format according to Minecraft's item model specification.

## Language Files

Language files are in:
```
src/main/resources/assets/betterthangamers/lang/
```

Currently included: en_us.json

To add more languages, create files like:
- ru_ru.json (Russian)
- de_de.json (German)
- etc.

## Using Minecraft Textures

For copper tools and armor, you can extract textures from Minecraft's own resource pack:
- Copper ingot texture location: `assets/minecraft/textures/item/copper_ingot.png`
- You can use these as a base for creating copper tool and armor textures

## Notes

- All PNG textures should be 16x16 pixels for items
- Block textures can be 16x16 pixels
- Use transparency (RGBA) for items to have proper display
- Follow Minecraft's texture naming conventions (lowercase, underscores for spaces)
- Ensure proper lighting and shading for better appearance

