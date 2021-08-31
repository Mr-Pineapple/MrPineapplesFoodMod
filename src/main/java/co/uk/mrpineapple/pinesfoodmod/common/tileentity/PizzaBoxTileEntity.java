package co.uk.mrpineapple.pinesfoodmod.common.tileentity;

import co.uk.mrpineapple.pinesfoodmod.common.blocks.PizzaBoxBlock;
import co.uk.mrpineapple.pinesfoodmod.common.util.InventoryUtil;
import co.uk.mrpineapple.pinesfoodmod.common.util.TileEntityUtil;
import co.uk.mrpineapple.pinesfoodmod.core.registry.TileEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

/**
 * Author: Mr. Pineapple
 */
public class PizzaBoxTileEntity extends LockableLootTileEntity implements ISidedInventory {
    private static final int[] SLOTS = IntStream.range(0, 1).toArray();
    protected NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private int openCount;

    public PizzaBoxTileEntity() {
        super(TileEntityRegistry.PIZZA_BOX.get());
    }

    @Override
    protected ITextComponent getDefaultName() {
        return null;
    }

    @Override
    protected Container createMenu(int p_213906_1_, PlayerInventory p_213906_2_) {
        return null;
    }

    public boolean addItem(ItemStack stack) {
        if(this.items.get(0).isEmpty()) {
            ItemStack items = stack.copy();
            items.setCount(1);
            this.items.set(0, items);

            CompoundNBT compoundTag = new CompoundNBT();
            this.save(compoundTag);
            TileEntityUtil.sendUpdatePacket(this, super.save(compoundTag));

            return true;
        }
        return false;
    }

    public void removeItem(int position, PlayerEntity player) {
        if(!this.items.get(position).isEmpty())
        {
            /* Spawns the item */
            ItemStack stack = this.items.get(position).copy();
            if(player.inventory.getFreeSlot() != -1) {
                player.inventory.add(stack);
            } else if(player.inventory.contains(stack)) {
                player.inventory.add(stack);
            } else {
                level.addFreshEntity(new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.125, worldPosition.getZ() + 0.5, stack));
            }

            /* Remove the item from the inventory */
            this.items.set(position, ItemStack.EMPTY);


            /* Send updates to client */
            CompoundNBT compound = new CompoundNBT();
            this.writeItem(compound);
            TileEntityUtil.sendUpdatePacket(this, super.save(compound));
        }
    }

    protected ItemStack getItemsInMainSlot() {
        return this.items.get(0);
    }

    @Override
    public boolean isEmpty() {
        return this.items.get(0).isEmpty();
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    private CompoundNBT writeItem(CompoundNBT compoundTag) {
        InventoryUtil.saveAllItemsWithKey("Items", compoundTag, this.items, true);
        return compoundTag;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }


    @Override
    public int[] getSlotsForFace(Direction direction) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        return !(Block.byItem(itemStack.getItem()) instanceof PizzaBoxBlock);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack itemStack, Direction direction) {
        return true;
    }

    @Override
    public void load(BlockState state, CompoundNBT compoundTag) {
        super.load(state, compoundTag);
        this.loadFromTag(compoundTag);
    }

    @Override
    public CompoundNBT save(CompoundNBT compoundTag) {
        super.save(compoundTag);
        return this.saveToTag(compoundTag);
    }

    private void loadFromTag(CompoundNBT compoundTag) {
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(compoundTag) && compoundTag.contains("Items", 9)) {
            ItemStackHelper.loadAllItems(compoundTag, this.items);
        }
    }

    private CompoundNBT saveToTag(CompoundNBT compoundTag) {
        if(!this.trySaveLootTable(compoundTag)) {
            ItemStackHelper.saveAllItems(compoundTag, this.items, true);
        }
        return compoundTag;
    }
}
