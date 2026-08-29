package io.marrybye.github.larperthanwolves.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ArmorItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("larperthanwolves");

    // Basic items for early survival
    public static final DeferredItem<Item> SILICON_SHARD = ITEMS.register("silicon_shard",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DRY_GRASS = ITEMS.register("dry_grass",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> IRON_DUST = ITEMS.register("iron_dust",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> COPPER_DUST = ITEMS.register("copper_dust",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GOLD_DUST = ITEMS.register("gold_dust",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> STONE_NUGGET = ITEMS.register("stone_nugget",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DIORITE_NUGGET = ITEMS.register("diorite_nugget",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GRANITE_NUGGET = ITEMS.register("granite_nugget",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ANDESITE_NUGGET = ITEMS.register("andesite_nugget",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> TUFF_NUGGET = ITEMS.register("tuff_nugget",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> COPPER_NUGGET = ITEMS.register("copper_nugget",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ROPE = ITEMS.register("rope",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final DeferredItem<Item> LIGHTER = ITEMS.register("lighter",
            () -> new Item(new Item.Properties().durability(64)));

    // Copper tools and armor
    public static final DeferredItem<SwordItem> COPPER_SWORD = ITEMS.register("copper_sword",
            () -> new SwordItem(ModToolMaterials.COPPER, new Item.Properties()
                    .durability(ModToolMaterials.COPPER.getUses())
                    .attributes(SwordItem.createAttributes(ModToolMaterials.COPPER, 3, -2.4f))));

    public static final DeferredItem<PickaxeItem> COPPER_PICKAXE = ITEMS.register("copper_pickaxe",
            () -> new PickaxeItem(ModToolMaterials.COPPER, new Item.Properties()
                    .durability(ModToolMaterials.COPPER.getUses())
                    .attributes(PickaxeItem.createAttributes(ModToolMaterials.COPPER, 1, -2.8f))));

    public static final DeferredItem<AxeItem> COPPER_AXE = ITEMS.register("copper_axe",
            () -> new AxeItem(ModToolMaterials.COPPER, new Item.Properties()
                    .durability(ModToolMaterials.COPPER.getUses())
                    .attributes(AxeItem.createAttributes(ModToolMaterials.COPPER, 6, -3.1f))));

    public static final DeferredItem<ShovelItem> COPPER_SHOVEL = ITEMS.register("copper_shovel",
            () -> new ShovelItem(ModToolMaterials.COPPER, new Item.Properties()
                    .durability(ModToolMaterials.COPPER.getUses())
                    .attributes(ShovelItem.createAttributes(ModToolMaterials.COPPER, 1.5f, -3.0f))));

    public static final DeferredItem<HoeItem> COPPER_HOE = ITEMS.register("copper_hoe",
            () -> new HoeItem(ModToolMaterials.COPPER, new Item.Properties()
                    .durability(ModToolMaterials.COPPER.getUses())
                    .attributes(HoeItem.createAttributes(ModToolMaterials.COPPER, 0, -2.0f))));

    // Copper armor
    public static final DeferredItem<ArmorItem> COPPER_HELMET = ITEMS.register("copper_helmet",
            () -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.HELMET, new Item.Properties()
                    .durability(ArmorItem.Type.HELMET.getDurability(12))));

    public static final DeferredItem<ArmorItem> COPPER_CHESTPLATE = ITEMS.register("copper_chestplate",
            () -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.CHESTPLATE, new Item.Properties()
                    .durability(ArmorItem.Type.CHESTPLATE.getDurability(12))));

    public static final DeferredItem<ArmorItem> COPPER_LEGGINGS = ITEMS.register("copper_leggings",
            () -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.LEGGINGS, new Item.Properties()
                    .durability(ArmorItem.Type.LEGGINGS.getDurability(12))));

    public static final DeferredItem<ArmorItem> COPPER_BOOTS = ITEMS.register("copper_boots",
            () -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.BOOTS, new Item.Properties()
                    .durability(ArmorItem.Type.BOOTS.getDurability(12))));

    // Silicon tools (3x less durability than wood)
    public static final DeferredItem<PickaxeItem> SILICON_PICKAXE = ITEMS.register("silicon_pickaxe",
            () -> new PickaxeItem(ModToolMaterials.SILICON, new Item.Properties()
                    .durability(ModToolMaterials.SILICON.getUses())
                    .attributes(PickaxeItem.createAttributes(ModToolMaterials.SILICON, 1, -2.8f))));

    public static final DeferredItem<AxeItem> SILICON_AXE = ITEMS.register("silicon_axe",
            () -> new AxeItem(ModToolMaterials.SILICON, new Item.Properties()
                    .durability(ModToolMaterials.SILICON.getUses())
                    .attributes(AxeItem.createAttributes(ModToolMaterials.SILICON, 6, -3.1f))));

    public static final DeferredItem<ShovelItem> SILICON_SHOVEL = ITEMS.register("silicon_shovel",
            () -> new ShovelItem(ModToolMaterials.SILICON, new Item.Properties()
                    .durability(ModToolMaterials.SILICON.getUses())
                    .attributes(ShovelItem.createAttributes(ModToolMaterials.SILICON, 1.5f, -3.0f))));

    public static final DeferredItem<ShearsItem> SILICON_SHEARS = ITEMS.register("silicon_shears",
            () -> new ShearsItem(new Item.Properties().durability(20)));

    public static final DeferredItem<Item> SILICON_SPEAR = ITEMS.register("silicon_spear",
            () -> new SwordItem(ModToolMaterials.SILICON, new Item.Properties()
                    .durability(ModToolMaterials.SILICON.getUses())
                    .attributes(SwordItem.createAttributes(ModToolMaterials.SILICON, 2, -2.4f))));

    // Chisel Tool (Стамеска)
    public static final DeferredItem<ChiselItem> CHISEL = ITEMS.register("chisel",
            () -> new ChiselItem(new Item.Properties().durability(64)));

    // Diamond Ingot
    public static final DeferredItem<Item> DIAMOND_INGOT = ITEMS.register("diamond_ingot",
            () -> new Item(new Item.Properties()));

    // Reinforced Iron Tools
    public static final DeferredItem<SwordItem> REINFORCED_IRON_SWORD = ITEMS.register("reinforced_iron_sword",
            () -> new SwordItem(ModToolMaterials.REINFORCED_IRON, new Item.Properties()
                    .durability(ModToolMaterials.REINFORCED_IRON.getUses())
                    .attributes(SwordItem.createAttributes(ModToolMaterials.REINFORCED_IRON, 3, -2.4f))));

    public static final DeferredItem<PickaxeItem> REINFORCED_IRON_PICKAXE = ITEMS.register("reinforced_iron_pickaxe",
            () -> new PickaxeItem(ModToolMaterials.REINFORCED_IRON, new Item.Properties()
                    .durability(ModToolMaterials.REINFORCED_IRON.getUses())
                    .attributes(PickaxeItem.createAttributes(ModToolMaterials.REINFORCED_IRON, 1, -2.8f))));

    public static final DeferredItem<AxeItem> REINFORCED_IRON_AXE = ITEMS.register("reinforced_iron_axe",
            () -> new AxeItem(ModToolMaterials.REINFORCED_IRON, new Item.Properties()
                    .durability(ModToolMaterials.REINFORCED_IRON.getUses())
                    .attributes(AxeItem.createAttributes(ModToolMaterials.REINFORCED_IRON, 5, -3.0f))));

    public static final DeferredItem<ShovelItem> REINFORCED_IRON_SHOVEL = ITEMS.register("reinforced_iron_shovel",
            () -> new ShovelItem(ModToolMaterials.REINFORCED_IRON, new Item.Properties()
                    .durability(ModToolMaterials.REINFORCED_IRON.getUses())
                    .attributes(ShovelItem.createAttributes(ModToolMaterials.REINFORCED_IRON, 1.5f, -3.0f))));

    public static final DeferredItem<HoeItem> REINFORCED_IRON_HOE = ITEMS.register("reinforced_iron_hoe",
            () -> new HoeItem(ModToolMaterials.REINFORCED_IRON, new Item.Properties()
                    .durability(ModToolMaterials.REINFORCED_IRON.getUses())
                    .attributes(HoeItem.createAttributes(ModToolMaterials.REINFORCED_IRON, -3, 0.0f))));

    // Reinforced Iron Armor
    public static final DeferredItem<ArmorItem> REINFORCED_IRON_HELMET = ITEMS.register("reinforced_iron_helmet",
            () -> new ArmorItem(ModArmorMaterials.REINFORCED_IRON, ArmorItem.Type.HELMET, new Item.Properties()
                    .durability(ArmorItem.Type.HELMET.getDurability(33))));

    public static final DeferredItem<ArmorItem> REINFORCED_IRON_CHESTPLATE = ITEMS.register("reinforced_iron_chestplate",
            () -> new ArmorItem(ModArmorMaterials.REINFORCED_IRON, ArmorItem.Type.CHESTPLATE, new Item.Properties()
                    .durability(ArmorItem.Type.CHESTPLATE.getDurability(33))));

    public static final DeferredItem<ArmorItem> REINFORCED_IRON_LEGGINGS = ITEMS.register("reinforced_iron_leggings",
            () -> new ArmorItem(ModArmorMaterials.REINFORCED_IRON, ArmorItem.Type.LEGGINGS, new Item.Properties()
                    .durability(ArmorItem.Type.LEGGINGS.getDurability(33))));

    public static final DeferredItem<ArmorItem> REINFORCED_IRON_BOOTS = ITEMS.register("reinforced_iron_boots",
            () -> new ArmorItem(ModArmorMaterials.REINFORCED_IRON, ArmorItem.Type.BOOTS, new Item.Properties()
                    .durability(ArmorItem.Type.BOOTS.getDurability(33))));

    // Mesh item
    public static final DeferredItem<Item> MESH = ITEMS.register("mesh",
            () -> new Item(new Item.Properties()));

    // Block Items
    public static final DeferredItem<net.minecraft.world.item.BlockItem> BRICK_FURNACE = ITEMS.register("brick_furnace",
            () -> new net.minecraft.world.item.BlockItem(io.marrybye.github.larperthanwolves.block.ModBlocks.BRICK_FURNACE.get(), new Item.Properties()));

    public static final DeferredItem<net.minecraft.world.item.BlockItem> UNFIRED_BRICK = ITEMS.register("unfired_brick",
            () -> new net.minecraft.world.item.BlockItem(io.marrybye.github.larperthanwolves.block.ModBlocks.UNFIRED_BRICK.get(), new Item.Properties()));

    public static final DeferredItem<net.minecraft.world.item.BlockItem> ALLOY_MIXER = ITEMS.register("alloy_mixer",
            () -> new net.minecraft.world.item.BlockItem(io.marrybye.github.larperthanwolves.block.ModBlocks.ALLOY_MIXER.get(), new Item.Properties()));

    public static final DeferredItem<net.minecraft.world.item.BlockItem> SIEVE = ITEMS.register("sieve",
            () -> new net.minecraft.world.item.BlockItem(io.marrybye.github.larperthanwolves.block.ModBlocks.SIEVE.get(), new Item.Properties()));
}

