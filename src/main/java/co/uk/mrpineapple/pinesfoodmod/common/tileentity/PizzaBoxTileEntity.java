package co.uk.mrpineapple.pinesfoodmod.common.tileentity;

import co.uk.mrpineapple.pinesfoodmod.common.blocks.PizzaBoxBlock;
import co.uk.mrpineapple.pinesfoodmod.common.util.InventoryUtil;
import co.uk.mrpineapple.pinesfoodmod.common.util.TileEntityUtil;
import co.uk.mrpineapple.pinesfoodmod.core.registry.TileEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

/**
 * Author: Mr. Pineapple
 */
public class PizzaBoxTileEntity extends BaseTileEntity implements ISidedInventory {
    private static final int[] SLOTS = IntStream.range(0, 1).toArray();
    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    public PizzaBoxTileEntity() {
        super(TileEntityRegistry.PIZZA_BOX.get());
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
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
        if(!this.items.get(position).isEmpty()) {
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

    public ItemStack getItemsInMainSlot() {
        return this.items.get(0);
    }

    @Override
    public boolean isEmpty() {
        return this.items.get(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack result = ItemStackHelper.removeItem(this.items, index, count);

        CompoundNBT compoundTag = new CompoundNBT();
        this.writeItem(compoundTag);
        TileEntityUtil.sendUpdatePacket(this, super.save(compoundTag));

        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStackHelper.takeItem(this.items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        NonNullList<ItemStack> inventory = this.items;
        inventory.set(index, stack);

        CompoundNBT compoundTag = new CompoundNBT();
        this.writeItem(compoundTag);
        TileEntityUtil.sendUpdatePacket(this, super.save(compoundTag));
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64;
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
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public CompoundNBT save(CompoundNBT compoundTag) {
        this.writeItem(compoundTag);
        return super.save(compoundTag);
    }

    private CompoundNBT writeItem(CompoundNBT compoundTag) {
        InventoryUtil.saveAllItemsWithKey("Items", compoundTag, this.items, true);
        return compoundTag;
    }

    @Override
    public void load(BlockState state, CompoundNBT compoundTag) {
        super.load(state, compoundTag);
        if(compoundTag.contains("Items", Constants.NBT.TAG_LIST)) {
            this.items.clear();
            InventoryUtil.loadAllItemsWithKey("Items", compoundTag, this.items);
        }
    }
}
