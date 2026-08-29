package io.marrybye.github.larperthanwolves.menu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, "larperthanwolves");

    public static final DeferredHolder<MenuType<?>, MenuType<BrickFurnaceMenu>> BRICK_FURNACE = MENUS.register(
            "brick_furnace",
            () -> IMenuTypeExtension.create(BrickFurnaceMenu::new)
    );

    public static final DeferredHolder<MenuType<?>, MenuType<AlloyMixerMenu>> ALLOY_MIXER = MENUS.register(
            "alloy_mixer",
            () -> IMenuTypeExtension.create(AlloyMixerMenu::new)
    );
}
