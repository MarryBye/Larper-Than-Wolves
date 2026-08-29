package io.marrybye.github.larperthanwolves.loot;

import com.mojang.serialization.MapCodec;
import io.marrybye.github.larperthanwolves.LarperThanWolves;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, LarperThanWolves.MODID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<RemoveDisabledItemsModifier>> REMOVE_DISABLED_ITEMS =
            LOOT_MODIFIERS.register("remove_disabled_items", RemoveDisabledItemsModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ChestLootModifier>> CHEST_LOOT =
            LOOT_MODIFIERS.register("chest_loot", ChestLootModifier.CODEC);
}
