package io.marrybye.github.betterthangamers.datagen;

import io.marrybye.github.betterthangamers.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "betterthangamers", existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Simple item models - will be auto-generated based on texture files
    }
}

