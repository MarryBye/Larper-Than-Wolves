package io.marrybye.github.larperthanwolves.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.marrybye.github.larperthanwolves.event.DisabledItemsHandler;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.function.Supplier;

public class RemoveDisabledItemsModifier extends LootModifier {
    public static final Supplier<MapCodec<RemoveDisabledItemsModifier>> CODEC =
            Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(inst ->
                    codecStart(inst).apply(inst, RemoveDisabledItemsModifier::new)
            ));

    public RemoveDisabledItemsModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.removeIf(stack -> !stack.isEmpty() && DisabledItemsHandler.isDisabled(stack.getItem()));
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
