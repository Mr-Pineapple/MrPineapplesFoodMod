package co.uk.mrpineapple.pinesfoodmod.common.recipe;

import co.uk.mrpineapple.pinesfoodmod.common.recipe.util.RecipeUtil;
import co.uk.mrpineapple.pinesfoodmod.core.registry.BlockRegistry;
import co.uk.mrpineapple.pinesfoodmod.core.registry.RecipeSerializerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class PizzaOvenRecipe extends AbstractCookingRecipe {

    public PizzaOvenRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookTime) {
        super(RecipeUtil.PIZZA_OVEN, id, group, ingredient, result, experience, cookTime);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.PIZZA_OVEN.get());
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.PIZZA_OVEN.get();
    }
}