package co.uk.mrpineapple.pinesfoodmod.common.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface StorageBlock extends IInventory, INamedContainerProvider {
    NonNullList<ItemStack> getInventory();

    @Override
    default int getContainerSize() {
        return this.getInventory().size();
    }

    @Override
    default boolean isEmpty() {
        for(ItemStack itemStack : this.getInventory()) {
            if(!itemStack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    default ItemStack getItem(int slot) {
        return slot >= 0 && slot < this.getInventory().size() ? this.getInventory().get(slot) : ItemStack.EMPTY;
    }

    @Override
    default ItemStack removeItem(int slot, int count) {
        ItemStack itemStack = ItemStackHelper.removeItem(this.getInventory(), slot, count);
        if(!itemStack.isEmpty()) this.setChanged();
        return itemStack;
    }

    @Override
    default ItemStack removeItemNoUpdate(int slot) {
        ItemStack itemStack = this.getInventory().get(slot);
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.getInventory().set(slot, ItemStack.EMPTY);
            return itemStack;
        }
    }

    @Override
    default void setItem(int slot, ItemStack itemStack) {
        this.getInventory().set(slot, itemStack);
        if(itemStack.isEmpty() && itemStack.getCount() > this.getMaxStackSize())
            itemStack.setCount(this.getMaxStackSize());
        this.setChanged();
    }

    @Override
    default int getMaxStackSize() {
        return 64;
    }

    @Override
    default boolean stillValid(PlayerEntity playerEntity) {
        return false;
    }

    @Override
    default void startOpen(PlayerEntity playerEntity) {}

    @Override
    default void stopOpen(PlayerEntity p_174886_1_) {}

    @Override
    default boolean canPlaceItem(int slot, ItemStack itemStack) {
        return true;
    }

    @Override
    default void clearContent() {
        this.getInventory().clear();
    }
}
