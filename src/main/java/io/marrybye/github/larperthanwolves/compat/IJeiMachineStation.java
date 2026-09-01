package io.marrybye.github.larperthanwolves.compat;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;

/**
 * Interface implemented by workstations, furnaces, and processing machines
 * to guarantee complete JEI integration across all 5 lifecycle steps:
 * 1. Category registration
 * 2. Recipe registration
 * 3. Catalyst registration
 * 4. Screen GUI click areas
 * 5. Menu container recipe transfer handlers
 */
public interface IJeiMachineStation {
    /**
     * Registers custom JEI recipe categories for this workstation.
     */
    void registerJeiCategories(IRecipeCategoryRegistration registration, IGuiHelper guiHelper);

    /**
     * Registers all processing recipes associated with this workstation.
     */
    void registerJeiRecipes(IRecipeRegistration registration);

    /**
     * Registers this workstation block (and any accessories/cranks) as a recipe catalyst.
     */
    void registerJeiCatalysts(IRecipeCatalystRegistration registration);

    /**
     * Registers screen GUI click areas (e.g. animated progress arrows and flames).
     */
    void registerJeiGuiHandlers(IGuiHandlerRegistration registration);

    /**
     * Registers container menu recipe transfer handlers (the '+' auto-fill button).
     */
    void registerJeiRecipeTransferHandlers(IRecipeTransferRegistration registration);
}
