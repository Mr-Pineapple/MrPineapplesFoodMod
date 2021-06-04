package co.uk.mrpineapple.pinesfoodmod.common.tileentity;

import co.uk.mrpineapple.pinesfoodmod.common.util.InventoryUtil;
import co.uk.mrpineapple.pinesfoodmod.common.util.TileEntityUtil;
import co.uk.mrpineapple.pinesfoodmod.core.registry.TileEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IClearable;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class PizzaOvenTileEntity extends BaseTileEntity implements IClearable, ITickableTileEntity, ISidedInventory {
    public static final int[] ALL_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    public static final int[] OVEN_SLOT = new int[]{9};

    private final NonNullList<ItemStack> fuel = NonNullList.withSize(9, ItemStack.EMPTY);
    private final NonNullList<ItemStack> oven = NonNullList.withSize(1, ItemStack.EMPTY);
    private int cookingTimes;
    private int cookingTotalTimes;
    private int experience;
    private int remainingFuel = 0;


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
    public int[] getSlotsForFace(Direction side) {
        return side == Direction.DOWN ? OVEN_SLOT : ALL_SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        if(!this.getItem(index).isEmpty()) {
            return false;
        }
        if(index - this.fuel.size() >= 0) {
            return this.level.getRecipeManager().getRecipe(RecipeType.PIZZA_OVEN, new Inventory(stack), this.level).isPresent();
        }
        return stack.getItem() == Items.COAL || stack.getItem() == Items.CHARCOAL;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        if(direction == Direction.DOWN) {
            if(index - this.fuel.size() >= 0) {
                index -= this.fuel.size();
                if(this.cookingTimes == this.cookingTotalTimes) {
                    Optional<PizzaOvenCookingRecipe> optional = this.level.getRecipeManager().getRecipe(RecipeType.PIZZA_OVEN, new Inventory(stack), this.level);
                    return !optional.isPresent();
                }
            }
        }
        return false;
    }

    @Override
    public int getContainerSize() {
        return this.fuel.size() + this.oven.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack : this.fuel) {
            if(!stack.isEmpty()) {
                return false;
            }
        }
        for(ItemStack stack : this.oven) {
            if(!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        if(index - this.fuel.size() >= 0) {
            return this.oven.get(index - this.fuel.size());
        }
        return this.fuel.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        if(index - this.fuel.size() >= 0) {
            index -= this.fuel.size();
            ItemStack result = ItemStackHelper.removeItem(this.oven, index, count);

            if(this.oven.get(index).isEmpty()) {
                if(this.cookingTimes == this.cookingTotalTimes) {
                    double posX = worldPosition.getX() + 0.3 + 0.4 * (index % 2);
                    double posY = worldPosition.getY() + 1.0;
                    double posZ = worldPosition.getZ() + 0.3 + 0.4 * (index / 2);
                    int amount = (int) experience;
                    while(amount > 0) {
                        int splitAmount = ExperienceOrbEntity.getExperienceValue(amount);
                        amount -= splitAmount;
                        this.level.addFreshEntity(new ExperienceOrbEntity(this.level, posX, posY, posZ, splitAmount));
                    }
                }
            }

            /* Send updates to client */
            CompoundNBT compound = new CompoundNBT();
            this.writeItems(compound);
            TileEntityUtil.sendUpdatePacket(this, super.save(compound));

            return result;
        }

        ItemStack result = ItemStackHelper.removeItem(this.fuel, index, count);

        /* Send updates to client */
        CompoundNBT compound = new CompoundNBT();
        this.writeFuel(compound);
        TileEntityUtil.sendUpdatePacket(this, super.save(compound));

        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if(index - this.fuel.size() >= 0) {
            return ItemStackHelper.takeItem(this.oven, index - this.fuel.size());
        }
        return ItemStackHelper.takeItem(this.fuel, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        NonNullList<ItemStack> inventory = this.fuel;
        if(index - this.fuel.size() >= 0) {
            index -= this.fuel.size();
            inventory = this.oven;
            int finalIndex = index;
            Optional<PizzaOvenCookingRecipe> optional = this.level.getRecipeManager().getRecipe(RecipeType.PIZZA_OVEN, new Inventory(stack), this.level);
            if(optional.isPresent()) {
                PizzaOvenCookingRecipe recipe = optional.get();
                this.resetPosition(finalIndex, recipe.getCookTime(), recipe.getExperience(), (byte) 0);
            }
        }
        inventory.set(index, stack);
        if(stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        /* Send updates to client */
        CompoundNBT compound = new CompoundNBT();
        this.writeItems(compound);
        this.writeFuel(compound);
        TileEntityUtil.sendUpdatePacket(this, super.save(compound));
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64;
    }

    @Override
    public void clearContent() {
        this.fuel.clear();
        this.oven.clear();
    }

    @Override
    public void load(BlockState blockstate, CompoundNBT compound) {
        super.load(blockstate, compound);
        if(compound.contains("Oven", Constants.NBT.TAG_LIST)) {
            this.oven.clear();
            InventoryUtil.loadAllItems("Oven", compound, this.oven);
        }
        if(compound.contains("Fuel", Constants.NBT.TAG_LIST)) {
            this.fuel.clear();
            InventoryUtil.loadAllItems("Fuel", compound, this.oven);
        }
        if(compound.contains("CookingTimes", Constants.NBT.TAG_INT)) {
            this.cookingTimes = compound.getInt("CookingTimes");
        }
        if(compound.contains("CookingTotalTimes", Constants.NBT.TAG_INT)) {
            this.cookingTotalTimes = compound.getInt("CookingTotalTimes");
        }
        if(compound.contains("RemainingFuel", Constants.NBT.TAG_INT)) {
            this.remainingFuel = compound.getInt("RemainingFuel");
        }
        if(compound.contains("Experience", Constants.NBT.TAG_INT)) {
            this.experience = compound.getInt("Experience");
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compoundNBT) {
        this.writeItems(compoundNBT);
        this.writeFuel(compoundNBT);
        this.writeCookingTimes(compoundNBT);
        this.writeCookingTotalTimes(compoundNBT);
        this.writeRemainingFuel(compoundNBT);
        this.writeExperience(compoundNBT);
        return super.save(compoundNBT);
    }

    private CompoundNBT writeItems(CompoundNBT compound) {
        InventoryUtil.saveAllItems("Oven", compound, this.oven);
        return compound;
    }
    private CompoundNBT writeFuel(CompoundNBT compound) {
        InventoryUtil.saveAllItems("Fuel", compound, this.fuel);
        return compound;
    }
    private CompoundNBT writeCookingTimes(CompoundNBT compound) {
        compound.putInt("CookingTimes", this.cookingTimes);
        return compound;
    }
    private CompoundNBT writeCookingTotalTimes(CompoundNBT compound) {
        compound.putInt("CookingTotalTimes", this.cookingTotalTimes);
        return compound;
    }
    private CompoundNBT writeRemainingFuel(CompoundNBT compound) {
        compound.putInt("RemainingFuel", this.remainingFuel);
        return compound;
    }
    private CompoundNBT writeExperience(CompoundNBT compound) {
        compound.putInt("Experience", experience);
        return compound;
    }



    @Override
    public void tick() {
        if(!this.level.isClientSide) {
            boolean canCook = this.canCook();
            if(this.remainingFuel == 0 && canCook) {
                for(int i = this.fuel.size() - 1; i >= 0; i--) {
                    if(!this.fuel.get(i).isEmpty()) {
                        this.remainingFuel = net.minecraftforge.common.ForgeHooks.getBurnTime(this.fuel.get(i));
                        this.fuel.set(i, ItemStack.EMPTY);

                        /* Send updates to client */
                        CompoundNBT compound = new CompoundNBT();
                        this.writeFuel(compound);
                        this.writeRemainingFuel(compound);
                        TileEntityUtil.sendUpdatePacket(this, super.save(compound));

                        break;
                    }
                }
            }

            if(canCook && this.remainingFuel > 0) {
                this.cookItems();
                this.remainingFuel--;
                if(this.remainingFuel == 0) {
                    /* Send updates to client */
                    CompoundNBT compound = new CompoundNBT();
                    this.writeRemainingFuel(compound);
                    TileEntityUtil.sendUpdatePacket(this, super.save(compound));
                }
            }
        } else {
            this.spawnParticles();
        }
    }

    private boolean canCook() {
        return !this.oven.get(0).isEmpty() && this.cookingTimes != this.cookingTotalTimes;
    }

    private void cookItems() {
        boolean itemsChanged = false;
        for(int i = 0; i < this.oven.size(); i++) {
            if(!this.oven.get(i).isEmpty()) {
                if(this.cookingTimes < this.cookingTotalTimes) {
                    this.cookingTimes++;
                    if(this.cookingTimes == this.cookingTotalTimes) {
                        Optional<PizzaOvenCookingRecipe> optional = this.level.getRecipeManager().getRecipe(RecipeType.PIZZA_OVEN, new Inventory(this.oven.get(i)), this.level);
                        if(optional.isPresent()) {
                            this.oven.set(i, optional.get().getRecipeOutput().copy());
                        }
                        itemsChanged = true;
                    }
                }
            }
        }
        if(itemsChanged) {
            /* Send updates to client */
            CompoundNBT compound = new CompoundNBT();
            this.writeItems(compound);
            this.writeCookingTimes(compound);
            TileEntityHandler.sendUpdatePacket(this, super.save(compound));
        }
    }
}
