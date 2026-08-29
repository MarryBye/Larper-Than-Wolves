package io.marrybye.github.larperthanwolves.item;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.EnumMap;
import java.util.List;

public class ModArmorMaterials {
    public static Holder<ArmorMaterial> COPPER;
    public static Holder<ArmorMaterial> REINFORCED_IRON;

    static {
        COPPER = Holder.direct(new ArmorMaterial(
                Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                    map.put(ArmorItem.Type.BOOTS, 2);
                    map.put(ArmorItem.Type.LEGGINGS, 5);
                    map.put(ArmorItem.Type.CHESTPLATE, 6);
                    map.put(ArmorItem.Type.HELMET, 2);
                }),
                15,
                SoundEvents.ARMOR_EQUIP_IRON,
                () -> Ingredient.of(net.minecraft.world.item.Items.COPPER_INGOT, ModItems.COPPER_DUST.get()),
                List.of(new ArmorMaterial.Layer(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("larperthanwolves", "copper"))),
                0.0f,
                0.0f
        ));

        REINFORCED_IRON = Holder.direct(new ArmorMaterial(
                Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                    map.put(ArmorItem.Type.BOOTS, 3);
                    map.put(ArmorItem.Type.LEGGINGS, 6);
                    map.put(ArmorItem.Type.CHESTPLATE, 8);
                    map.put(ArmorItem.Type.HELMET, 3);
                }),
                10,
                SoundEvents.ARMOR_EQUIP_IRON,
                () -> Ingredient.of(ModItems.DIAMOND_INGOT.get()),
                List.of(new ArmorMaterial.Layer(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("larperthanwolves", "reinforced_iron"))),
                2.0f,
                0.0f
        ));
    }
}

