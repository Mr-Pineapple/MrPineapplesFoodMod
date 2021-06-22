package co.uk.mrpineapple.pinesfoodmod.core.registry;

import co.uk.mrpineapple.pinesfoodmod.common.recipe.PizzaOvenRecipe;
import co.uk.mrpineapple.pinesfoodmod.common.recipe.serializers.PizzaBoardRecipeSerializer;
import co.uk.mrpineapple.pinesfoodmod.core.PinesFoodMod;
import net.minecraft.item.crafting.AccessCookingRecipeSerializer;
import net.minecraft.item.crafting.CookingRecipeSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeSerializerRegistry {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PinesFoodMod.ID);

    public static final RegistryObject<PizzaBoardRecipeSerializer> PIZZA_BOARD = RECIPE_SERIALIZER.register("pizza_board", PizzaBoardRecipeSerializer::new);
    public static final RegistryObject<CookingRecipeSerializer<PizzaOvenRecipe>> PIZZA_OVEN = RECIPE_SERIALIZER.register("pizza_oven", () -> AccessCookingRecipeSerializer.createCookingRecipeSerializer(PizzaOvenRecipe::new, 100));
}
