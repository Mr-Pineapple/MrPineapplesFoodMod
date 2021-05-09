package co.uk.mrpineapple.pinesfoodmod.common.recipe.serializers;

import co.uk.mrpineapple.pinesfoodmod.common.recipe.PizzaRecipe;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class PizzaBoardRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<PizzaRecipe> {

    @Override
    public PizzaRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
        JsonArray input = JSONUtils.getAsJsonArray(json, "inputs");
        for(int i = 0; i < input.size(); i++) {
            JsonObject itemObject = input.get(i).getAsJsonObject();
            ItemStack itemStack = ShapedRecipe.itemFromJson(itemObject);
            builder.add(itemStack);
        } if(!json.has("result")) {
            throw new JsonSyntaxException("Missing Result item for Pizza Board Recipe!");
        }
        JsonObject resultObject = JSONUtils.getAsJsonObject(json, "result");
        ItemStack resultItem = ShapedRecipe.itemFromJson(resultObject);
        return new PizzaRecipe(recipeId, resultItem, builder.build());
    }

    @Nullable
    @Override
    public PizzaRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        ItemStack result = buffer.readItem();
        ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
        int size = buffer.readVarInt();
        for(int i = 0; i < size; i++) {
            builder.add(buffer.readItem());
        }
        return new PizzaRecipe(recipeId, result, builder.build());
    }

    @Override
    public void toNetwork(PacketBuffer buffer, PizzaRecipe recipe) {
        buffer.writeItem(recipe.getItem());
        buffer.writeVarInt(recipe.getInputs().size());
        for(ItemStack stack : recipe.getInputs()) {
            buffer.writeItem(stack);
        }
    }
}
