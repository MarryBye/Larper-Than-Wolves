package io.marrybye.github.larperthanwolves.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.marrybye.github.larperthanwolves.event.DisabledItemsHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

    private static final Set<String> NUGGET_DECOMPACTING_RECIPES = Set.of(
            // Vanilla 9-nugget recipes
            "minecraft:iron_nugget_from_ingot",
            "minecraft:iron_nugget_from_iron_ingot",
            "minecraft:gold_nugget_from_ingot",
            "minecraft:gold_nugget_from_gold_ingot",
            "minecraft:iron_ingot_from_nuggets",
            "minecraft:iron_ingot_from_iron_nuggets",
            "minecraft:gold_ingot_from_nuggets",
            "minecraft:gold_ingot_from_gold_nuggets",

            // Create 9-nugget compacting & decompacting recipes
            "create:crafting/materials/copper_nugget_from_decompacting",
            "create:crafting/materials/copper_ingot_from_compacting",
            "create:crafting/materials/zinc_nugget_from_decompacting",
            "create:crafting/materials/zinc_ingot_from_compacting",
            "create:crafting/materials/brass_nugget_from_decompacting",
            "create:crafting/materials/brass_ingot_from_compacting"
    );

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("HEAD"))
    protected void onApply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        Iterator<Map.Entry<ResourceLocation, JsonElement>> iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<ResourceLocation, JsonElement> entry = iterator.next();
            ResourceLocation id = entry.getKey();
            JsonElement json = entry.getValue();

            if (json == null || !json.isJsonObject()) continue;
            JsonObject obj = json.getAsJsonObject();

            // 1. Remove explicit 9-nugget recipes from vanilla & Create (unless defined by larperthanwolves)
            if (!"larperthanwolves".equals(id.getNamespace())) {
                String fullId = id.toString();
                if (NUGGET_DECOMPACTING_RECIPES.contains(fullId) ||
                        fullId.contains("_nugget_from_decompacting") ||
                        fullId.contains("_ingot_from_compacting") ||
                        fullId.endsWith("_nugget_from_ingot") ||
                        fullId.endsWith("_nuggets_from_ingot") ||
                        fullId.endsWith("_ingot_from_nuggets") ||
                        fullId.endsWith("_ingots_from_nuggets")) {
                    iterator.remove();
                    continue;
                }
            }

            // 2. Remove recipes producing disabled items (furnace, blast furnace, smoker, disabled tools/armor)
            if (!"larperthanwolves".equals(id.getNamespace())) {
                String resultItemId = getResultItemId(obj);
                if (resultItemId != null) {
                    ResourceLocation itemLoc = ResourceLocation.tryParse(resultItemId);
                    if (itemLoc != null) {
                        Item item = BuiltInRegistries.ITEM.get(itemLoc);
                        if (item != null && DisabledItemsHandler.isDisabled(item)) {
                            iterator.remove();
                            continue;
                        }
                    }
                }
            }

            // 3. Remove 3x3 9-nugget compacting recipes dynamically
            if (!"larperthanwolves".equals(id.getNamespace()) && isNineNuggetCompactingRecipe(obj)) {
                iterator.remove();
                continue;
            }

            // 4. Dynamically replace furnace, blast_furnace, smoker in all remaining recipe ingredients
            replaceWorkstationIngredients(obj);
        }
    }

    private static String getResultItemId(JsonObject obj) {
        if (!obj.has("result")) return null;
        JsonElement res = obj.get("result");
        if (res.isJsonPrimitive() && res.getAsJsonPrimitive().isString()) {
            return res.getAsString();
        } else if (res.isJsonObject()) {
            JsonObject resObj = res.getAsJsonObject();
            if (resObj.has("id")) return resObj.get("id").getAsString();
            if (resObj.has("item")) return resObj.get("item").getAsString();
        }
        return null;
    }

    private static boolean isNineNuggetCompactingRecipe(JsonObject obj) {
        if (obj.has("pattern")) {
            JsonArray pattern = obj.getAsJsonArray("pattern");
            if (pattern != null && pattern.size() == 3) {
                boolean allRowsThree = true;
                for (JsonElement row : pattern) {
                    if (row.isJsonPrimitive() && row.getAsString().length() != 3) {
                        allRowsThree = false;
                        break;
                    }
                }
                if (allRowsThree && obj.has("key")) {
                    JsonObject key = obj.getAsJsonObject("key");
                    boolean allKeysAreNuggets = true;
                    int keyCount = 0;
                    for (Map.Entry<String, JsonElement> k : key.entrySet()) {
                        keyCount++;
                        String keyItem = getIngredientItemOrTag(k.getValue());
                        if (keyItem == null || (!keyItem.contains("nugget") && !keyItem.contains("pebble"))) {
                            allKeysAreNuggets = false;
                            break;
                        }
                    }
                    if (keyCount > 0 && allKeysAreNuggets) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static String getIngredientItemOrTag(JsonElement element) {
        if (element == null) return null;
        if (element.isJsonObject()) {
            JsonObject o = element.getAsJsonObject();
            if (o.has("item")) return o.get("item").getAsString();
            if (o.has("tag")) return o.get("tag").getAsString();
        } else if (element.isJsonArray()) {
            JsonArray arr = element.getAsJsonArray();
            for (JsonElement e : arr) {
                String sub = getIngredientItemOrTag(e);
                if (sub != null) return sub;
            }
        }
        return null;
    }

    private static void replaceWorkstationIngredients(JsonElement element) {
        if (element == null || element.isJsonNull()) return;

        if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                String key = entry.getKey();
                // Never modify the result of a recipe, only ingredients/keys/catalysts
                if ("result".equals(key)) continue;

                JsonElement val = entry.getValue();
                if (val.isJsonPrimitive() && val.getAsJsonPrimitive().isString()) {
                    String str = val.getAsString();
                    if ("minecraft:furnace".equals(str) || "minecraft:blast_furnace".equals(str)) {
                        entry.setValue(new JsonPrimitive("larperthanwolves:brick_furnace"));
                    } else if ("minecraft:smoker".equals(str)) {
                        entry.setValue(new JsonPrimitive("larperthanwolves:oven"));
                    }
                } else {
                    replaceWorkstationIngredients(val);
                }
            }
        } else if (element.isJsonArray()) {
            JsonArray arr = element.getAsJsonArray();
            for (int i = 0; i < arr.size(); i++) {
                JsonElement val = arr.get(i);
                if (val.isJsonPrimitive() && val.getAsJsonPrimitive().isString()) {
                    String str = val.getAsString();
                    if ("minecraft:furnace".equals(str) || "minecraft:blast_furnace".equals(str)) {
                        arr.set(i, new JsonPrimitive("larperthanwolves:brick_furnace"));
                    } else if ("minecraft:smoker".equals(str)) {
                        arr.set(i, new JsonPrimitive("larperthanwolves:oven"));
                    }
                } else {
                    replaceWorkstationIngredients(val);
                }
            }
        }
    }
}
