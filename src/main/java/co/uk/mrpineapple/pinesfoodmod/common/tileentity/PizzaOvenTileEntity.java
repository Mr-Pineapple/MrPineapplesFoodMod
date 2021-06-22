package co.uk.mrpineapple.pinesfoodmod.common.tileentity;

import co.uk.mrpineapple.pinesfoodmod.common.recipe.PizzaOvenRecipe;
import co.uk.mrpineapple.pinesfoodmod.common.recipe.util.RecipeUtil;
import co.uk.mrpineapple.pinesfoodmod.common.util.InventoryUtil;
import co.uk.mrpineapple.pinesfoodmod.common.util.TileEntityUtil;
import co.uk.mrpineapple.pinesfoodmod.core.registry.TileEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IClearable;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Optional;

public class PizzaOvenTileEntity extends BaseTileEntity implements IClearable, ITickableTileEntity, ISidedInventory {
    public static final int[] ALL_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    public static final int[] OVEN_SLOT = new int[]{9};

    private final NonNullList<ItemStack> fuel = NonNullList.withSize(9, ItemStack.EMPTY);
    private final NonNullList<ItemStack> oven = NonNullList.withSize(1, ItemStack.EMPTY);
    private int cookingTimes;
    private int cookingTotalTimes;
    private float experience;
    private int remainingFuel = 0;


    public PizzaOvenTileEntity() {
        super(TileEntityRegistry.PIZZA_OVEN.get());
    }

    public NonNullList<ItemStack> getFuel() {
        return this.fuel;
    }

    public NonNullList<ItemStack> getOven() {
        return this.oven;
    }

    void resetPosition(int cookTime, float experience) {
        this.cookingTimes = 0;
        this.cookingTotalTimes = cookTime;
        this.experience = experience;

        CompoundNBT compound = new CompoundNBT();
        this.writeItems(compound);
        this.writeCookingTimes(compound);
        this.writeCookingTotalTimes(compound);
        TileEntityUtil.sendUpdatePacket(this, super.save(compound));
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
            return this.level.getRecipeManager().getRecipeFor(RecipeUtil.PIZZA_OVEN, new Inventory(stack), this.level).isPresent();
        }
        return stack.getItem() == Items.COAL || stack.getItem() == Items.CHARCOAL;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        if(direction == Direction.DOWN) {
            if(index - this.fuel.size() >= 0) {
                index -= this.fuel.size();
                if(this.cookingTimes == this.cookingTotalTimes) {
                    Optional<PizzaOvenRecipe> optional = this.level.getRecipeManager().getRecipeFor(RecipeUtil.PIZZA_OVEN, new Inventory(stack), this.level);
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

    public boolean addItem(ItemStack stack, int position, int cookTime, float experience) {
        if(this.oven.get(position).isEmpty()) {
            ItemStack copy = stack.copy();
            copy.setCount(1);
            this.oven.set(position, copy);
            this.resetPosition(cookTime, experience);

            /* Play place sound */
            World world = this.getLevel();
            if(world != null) {
                world.playSound(null, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.0, this.worldPosition.getZ() + 0.5, SoundEvents.ANVIL_BREAK, SoundCategory.BLOCKS, 0.75F, world.random.nextFloat() * 0.2F + 0.9F);
            }

            return true;
        }
        return false;
    }

    public boolean addFuel(ItemStack stack) {
        for(int i = 0; i < this.fuel.size(); i++) {
            if(this.fuel.get(i).isEmpty()) {
                ItemStack fuel = stack.copy();
                fuel.setCount(1);
                this.fuel.set(i, fuel);

                /* Send updates to client */
                CompoundNBT compound = new CompoundNBT();
                this.writeFuel(compound);
                TileEntityUtil.sendUpdatePacket(this, super.save(compound));

                return true;
            }
        }
        return false;
    }

    /**
     * To remove item from oven automatically selects first slot, if adding double pizza ovens (was original plan lol), then will need to change this
     *
     * @param player the player who is removing the item - will give the player this item, or drop it
     */
    public void removeItem(PlayerEntity player) {
        if(!this.oven.get(0).isEmpty()) {
            double posX = worldPosition.getX() + 0.7 * (0 % 2);
            double posY = worldPosition.getY() + 1.0;
            double posZ = worldPosition.getZ() + 0.3 + 0.4 * (0 / 2);

            /* Spawns the item */
            ItemStack stack = this.oven.get(0).copy();
            if(player.inventory.getFreeSlot() != -1) {
                player.inventory.add(stack);
            } else if(player.inventory.contains(stack)) {
                player.inventory.add(stack);
            } else {
                level.addFreshEntity(new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.125, worldPosition.getZ() + 0.5, stack));
            }

            /* Remove the item from the inventory */
            this.oven.set(0, ItemStack.EMPTY);

            /* Spawn experience orbs */
            if(this.cookingTimes == this.cookingTotalTimes) {
                int amount = (int) experience;
                while(amount > 0) {
                    int splitAmount = ExperienceOrbEntity.getExperienceValue(amount);
                    amount -= splitAmount;
                    this.level.addFreshEntity(new ExperienceOrbEntity(this.level, posX, posY, posZ, splitAmount));
                }
            }

            /* Send updates to client */
            CompoundNBT compound = new CompoundNBT();
            this.writeItems(compound);
            TileEntityUtil.sendUpdatePacket(this, super.save(compound));
        }
    }

    /**
    * Directly remove item from specific slot
    */
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
            Optional<PizzaOvenRecipe> optional = this.level.getRecipeManager().getRecipeFor(RecipeUtil.PIZZA_OVEN, new Inventory(stack), this.level);
            if(optional.isPresent()) {
                PizzaOvenRecipe recipe = optional.get();
                this.resetPosition(recipe.getCookingTime(), recipe.getExperience());
            }
        }
        inventory.set(index, stack);
        if(stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
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
            this.experience = compound.getFloat("Experience");
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
        compound.putFloat("Experience", experience);
        return compound;
    }



    @Override
    public void tick() {
        if(!this.level.isClientSide) {
            boolean canCook = this.canCook();
            if(this.remainingFuel == 0 && canCook) {
                setEmptyFuel();
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

    private void setEmptyFuel() {
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
                        Optional<PizzaOvenRecipe> optional = this.level.getRecipeManager().getRecipeFor(RecipeUtil.PIZZA_OVEN, new Inventory(this.oven.get(i)), this.level);
                        if(optional.isPresent()) {
                            this.oven.set(i, optional.get().getResultItem().copy());
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
            TileEntityUtil.sendUpdatePacket(this, super.save(compound));
        }
    }

    void spawnParticles() {
        World world = this.getLevel();
        if(world != null) {
            if(this.isCooking() && this.remainingFuel > 0) {
                double posX = worldPosition.getX() + 0.1 + 0.8 * world.random.nextDouble();
                double posY = worldPosition.getY() + 0.25;
                double posZ = worldPosition.getZ() + 0.2 + 0.6 * world.random.nextDouble();
                world.addParticle(ParticleTypes.FLAME, posX, posY, posZ, 0.0, 0.0, 0.0);
            }

            BlockPos pos = this.getBlockPos();
            for(int i = 0; i < this.oven.size(); i++) {
                if(!this.oven.get(i).isEmpty() && world.random.nextFloat() < 0.1F) {
                    double posX = pos.getX() + 0.5;
                    double posY = pos.getY() + .7;
                    double posZ = pos.getZ() + 0.5;
                    if(this.cookingTimes == this.cookingTotalTimes) {
                        for(int j = 0; j < 4; j++) {
                            world.addParticle(ParticleTypes.SMOKE, posX, posY, posZ, 0.0D, 5.0E-4D, 0.0D);
                        }
                    }
                }
            }
        }
    }

    boolean isCooking() {
        for(int i = 0; i < this.oven.size(); i++) {
            if(!this.oven.get(i).isEmpty() && (this.cookingTimes != this.cookingTotalTimes)) {
                return true;
            }
        }
        return false;
    }

    public Optional<PizzaOvenRecipe> findMatchingRecipe(ItemStack input) {
        return this.oven.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.level.getRecipeManager().getRecipeFor(RecipeUtil.PIZZA_OVEN, new Inventory(input), this.level);
    }
}
