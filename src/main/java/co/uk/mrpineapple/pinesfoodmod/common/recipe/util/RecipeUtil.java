package co.uk.mrpineapple.pinesfoodmod.common.recipe.util;

import co.uk.mrpineapple.pinesfoodmod.common.recipe.PizzaOvenRecipe;
import co.uk.mrpineapple.pinesfoodmod.common.recipe.PizzaRecipe;
import co.uk.mrpineapple.pinesfoodmod.core.PinesFoodMod;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.stream.Collectors;

public class RecipeUtil {

    public static class Pizza {
        public static NonNullList<PizzaRecipe> getAll(World world)  {
            return world.getRecipeManager().getRecipes().stream()
                    .filter(recipe -> recipe.getType() == RecipeUtil.PIZZA)
                    .map(recipe -> (PizzaRecipe) recipe)
                    .collect(Collectors.toCollection(NonNullList::create));
        }

        @Nullable
        public static PizzaRecipe getRecipeById(World world, ResourceLocation id) {
            return world.getRecipeManager().getRecipes().stream()
                    .filter(recipe -> recipe.getType() == RecipeUtil.PIZZA)
                    .map(recipe -> (PizzaRecipe) recipe)
                    .filter(recipe -> recipe.getId().equals(id))
                    .findFirst().orElse(null);
        }
    }

    public static final IRecipeType<PizzaRecipe> PIZZA = register(PinesFoodMod.ID + ":pizza_board");
    public static final IRecipeType<PizzaOvenRecipe> PIZZA_OVEN = register(PinesFoodMod.ID + ":pizza_oven");

    static <T extends IRecipe<?>> IRecipeType<T> register(final String key) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(key), new IRecipeType<T>() {
            @Override
            public String toString() {
                return key;
            }
        });
    }
}
