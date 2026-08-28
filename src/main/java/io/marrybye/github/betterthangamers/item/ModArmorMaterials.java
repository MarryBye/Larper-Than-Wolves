package io.marrybye.github.betterthangamers.item;

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
                () -> Ingredient.of(ModItems.COPPER_DUST.get()),
                List.of(new ArmorMaterial.Layer(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("betterthangamers", "copper"))),
                0.0f,
                0.0f
        ));
    }
}

