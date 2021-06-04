package co.uk.mrpineapple.pinesfoodmod.common.tileentity;

import co.uk.mrpineapple.pinesfoodmod.core.registry.TileEntityRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IClearable;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;

public class PizzaOvenTileEntity extends BaseTileEntity implements IClearable, ITickableTileEntity, ISidedInventory {
    public static final int[] ALL_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    public static final int[] OVEN_SLOT = new int[]{9};

    private final NonNullList<ItemStack> fuel = NonNullList.withSize(9, ItemStack.EMPTY);
    private final NonNullList<ItemStack> oven = NonNullList.withSize(1, ItemStack.EMPTY);


    public PizzaOvenTileEntity() {
        super(TileEntityRegistry.PIZZA_BOARD.get());
    }

    public NonNullList<ItemStack> getFuel() {
        return this.fuel;
    }

    public NonNullList<ItemStack> getOven() {
        return this.oven;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
//        if(!this.getItem(index).isEmpty()) {
//            return false;
//        }
//        if(index - this.fuel.size() >= 0) {
//            return this.level.getRecipeManager().getRecipe(RecipeType.PIZZA_OVEN, new Inventory(stack), this.level).isPresent();
//        }
        return stack.getItem() == Items.COAL || stack.getItem() == Items.CHARCOAL;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
//        if(direction == Direction.DOWN) {
//            if(index - this.fuel.size() >= 0) {
//                index -= this.fuel.size();
//                if(this.cookingTimes[index] == this.cookingTotalTimes[index]) {
//                    Optional<PizzaOvenCookingRecipe> optional = this.world.getRecipeManager().getRecipe(RecipeType.PIZZA_OVEN, new Inventory(stack), this.world);
//                    return !optional.isPresent();
//                }
//            }
//        }
        return false;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int p_70301_1_) {
        return null;
    }

    @Override
    public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_70304_1_) {
        return null;
    }

    @Override
    public void setItem(int p_70299_1_, ItemStack p_70299_2_) {

    }

    @Override
    public boolean stillValid(PlayerEntity p_70300_1_) {
        return false;
    }

    @Override
    public void clearContent() {

    }

    @Override
    public void tick() {

    }
}
