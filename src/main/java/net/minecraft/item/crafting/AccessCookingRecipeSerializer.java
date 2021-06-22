package net.minecraft.item.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Used to create cooking recipe serializers, could probably replace with something more streamline, but this works
 */
public class AccessCookingRecipeSerializer {
    public static <T extends AbstractCookingRecipe> CookingRecipeSerializer<T> createCookingRecipeSerializer(IFactory<T> factory, int cookingTime) {
        return new CookingRecipeSerializer<>(factory::create, cookingTime);
    }

    public interface IFactory<T extends AbstractCookingRecipe> {
        T create(ResourceLocation id, String group, Ingredient ingredient, ItemStack itemStack, float experience, int cookTime);
    }
}
