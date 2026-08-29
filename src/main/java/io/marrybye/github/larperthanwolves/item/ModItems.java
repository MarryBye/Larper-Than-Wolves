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
                    .attributes(SwordItem.createAttributes(ModToolMaterials.COPPER, 3, -2.4f))));

    public static final DeferredItem<PickaxeItem> COPPER_PICKAXE = ITEMS.register("copper_pickaxe",
            () -> new PickaxeItem(ModToolMaterials.COPPER, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModToolMaterials.COPPER, 1, -2.8f))));

    public static final DeferredItem<AxeItem> COPPER_AXE = ITEMS.register("copper_axe",
            () -> new AxeItem(ModToolMaterials.COPPER, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModToolMaterials.COPPER, 6, -3.1f))));

    public static final DeferredItem<ShovelItem> COPPER_SHOVEL = ITEMS.register("copper_shovel",
            () -> new ShovelItem(ModToolMaterials.COPPER, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(ModToolMaterials.COPPER, 1.5f, -3.0f))));

    public static final DeferredItem<HoeItem> COPPER_HOE = ITEMS.register("copper_hoe",
            () -> new HoeItem(ModToolMaterials.COPPER, new Item.Properties()
                    .attributes(HoeItem.createAttributes(ModToolMaterials.COPPER, 0, -2.0f))));

    // Copper armor
    public static final DeferredItem<ArmorItem> COPPER_HELMET = ITEMS.register("copper_helmet",
            () -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final DeferredItem<ArmorItem> COPPER_CHESTPLATE = ITEMS.register("copper_chestplate",
            () -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final DeferredItem<ArmorItem> COPPER_LEGGINGS = ITEMS.register("copper_leggings",
            () -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final DeferredItem<ArmorItem> COPPER_BOOTS = ITEMS.register("copper_boots",
            () -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.BOOTS, new Item.Properties()));

    // Silicon tools (3x less durability than wood)
    public static final DeferredItem<PickaxeItem> SILICON_PICKAXE = ITEMS.register("silicon_pickaxe",
            () -> new PickaxeItem(ModToolMaterials.SILICON, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModToolMaterials.SILICON, 1, -2.8f))));

    public static final DeferredItem<AxeItem> SILICON_AXE = ITEMS.register("silicon_axe",
            () -> new AxeItem(ModToolMaterials.SILICON, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModToolMaterials.SILICON, 6, -3.1f))));

    public static final DeferredItem<ShovelItem> SILICON_SHOVEL = ITEMS.register("silicon_shovel",
            () -> new ShovelItem(ModToolMaterials.SILICON, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(ModToolMaterials.SILICON, 1.5f, -3.0f))));

    public static final DeferredItem<ShearsItem> SILICON_SHEARS = ITEMS.register("silicon_shears",
            () -> new ShearsItem(new Item.Properties().durability(20)));

    public static final DeferredItem<Item> SILICON_SPEAR = ITEMS.register("silicon_spear",
            () -> new SwordItem(ModToolMaterials.SILICON, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolMaterials.SILICON, 2, -2.4f))));

    // Block Items
    public static final DeferredItem<net.minecraft.world.item.BlockItem> BRICK_FURNACE = ITEMS.register("brick_furnace",
            () -> new net.minecraft.world.item.BlockItem(io.marrybye.github.larperthanwolves.block.ModBlocks.BRICK_FURNACE.get(), new Item.Properties()));

    public static final DeferredItem<net.minecraft.world.item.BlockItem> UNFIRED_BRICK = ITEMS.register("unfired_brick",
            () -> new net.minecraft.world.item.BlockItem(io.marrybye.github.larperthanwolves.block.ModBlocks.UNFIRED_BRICK.get(), new Item.Properties()));
}

