package io.marrybye.github.larperthanwolves.compat;

import mezz.jei.api.registration.IRecipeRegistration;

/**
 * Interface implemented by items and blocks that provide custom JEI documentation tabs.
 */
public interface IJeiDocumentationProvider {
    /**
     * Registers information pages and description tabs for this item/block in JEI.
     *
     * @param registration JEI recipe registration helper
     */
    void registerJeiInfo(IRecipeRegistration registration);
}
