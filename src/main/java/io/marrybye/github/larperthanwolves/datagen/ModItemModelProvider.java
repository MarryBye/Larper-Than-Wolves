package io.marrybye.github.larperthanwolves.datagen;

import io.marrybye.github.larperthanwolves.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "larperthanwolves", existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Simple item models - will be auto-generated based on texture files
    }
}

