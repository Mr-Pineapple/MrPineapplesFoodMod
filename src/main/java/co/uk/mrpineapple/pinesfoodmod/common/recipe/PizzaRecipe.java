package co.uk.mrpineapple.pinesfoodmod.common.recipe;

import co.uk.mrpineapple.pinesfoodmod.common.recipe.util.RecipeUtil;
import co.uk.mrpineapple.pinesfoodmod.common.tileentity.PizzaBoardTileEntity;
import co.uk.mrpineapple.pinesfoodmod.core.registry.RecipeSerializerRegistry;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class PizzaRecipe implements IRecipe<PizzaBoardTileEntity> {
    private ResourceLocation id;
    private ItemStack item;
    private ImmutableList<ItemStack> inputs;

    public PizzaRecipe(ResourceLocation id, ItemStack item, ImmutableList<ItemStack> inputs) {
        this.id = id;
        this.item = item;
        this.inputs = inputs;
    }

    public ItemStack getItem() {
        return this.item.copy();
    }

    public ImmutableList<ItemStack> getInputs() {
        return inputs;
    }

    @Override
    public boolean matches(PizzaBoardTileEntity inv, World world) {
        return false;
    }

    @Override
    public ItemStack assemble(PizzaBoardTileEntity inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.PIZZA_BOARD.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeUtil.PIZZA;
    }
}
