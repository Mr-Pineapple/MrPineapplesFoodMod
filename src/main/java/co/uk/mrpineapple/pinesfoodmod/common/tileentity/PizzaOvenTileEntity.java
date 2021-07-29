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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Optional;

public class PizzaOvenTileEntity extends BaseTileEntity implements IClearable, ITickableTileEntity, ISidedInventory {
    public static final int[] ALL_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    public static final int[] OVEN_SLOT = new int[]{9};

    private final NonNullList<ItemStack> fuel = NonNullList.withSize(9, ItemStack.EMPTY);
    private final NonNullList<ItemStack> oven = NonNullList.withSize(1, ItemStack.EMPTY);
    private int cookingTime;
    private int cookingTotalTime;
    private float experience;
    private int remainingFuel = 0;

    public PizzaOvenTileEntity() {
        super(TileEntityRegistry.PIZZA_OVEN.get());
    }


    public NonNullList<ItemStack> getOven() {
        return this.oven;
    }

    public NonNullList<ItemStack> getFuel() {
        return this.fuel;
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
                world.playSound(null, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.0, this.worldPosition.getZ() + 0.5, SoundEvents.ANVIL_PLACE, SoundCategory.BLOCKS, 0.75F, world.random.nextFloat() * 0.2F + 0.9F);
            }

            return true;
        }
        return false;
    }

    private void resetPosition(int cookTime, float experience) {
        this.cookingTime = 0;
        this.cookingTotalTime = cookTime;
        this.experience = experience;

        /* Send updates to client */
        CompoundNBT compound = new CompoundNBT();
        this.writeItems(compound);
        this.writeCookingTime(compound);
        this.writeCookingTotalTime(compound);
        TileEntityUtil.sendUpdatePacket(this, super.save(compound));
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

    public void removeItem(int position) {
        if(!this.oven.get(position).isEmpty()) {
            double posX = worldPosition.getX() + 0.3 + 0.4 * (position % 2);
            double posY = worldPosition.getY() + 1.0;
            double posZ = worldPosition.getZ() + 0.3 + 0.4 * (position / 2);

            ItemEntity entity = new ItemEntity(this.level, posX, posY + 0.1, posZ, this.oven.get(position).copy());
            this.level.addFreshEntity(entity);

            this.oven.set(position, ItemStack.EMPTY);

            if(this.cookingTime == this.cookingTotalTime) {
                int amount = (int) experience;
                while(amount > 0) {
                    int splitAmount = ExperienceOrbEntity.getExperienceValue(amount);
                    amount -= splitAmount;
                    this.level.addFreshEntity(new ExperienceOrbEntity(this.level, posX, posY, posZ, splitAmount));
                }
            }

            CompoundNBT compound = new CompoundNBT();
            this.writeItems(compound);
            TileEntityUtil.sendUpdatePacket(this, super.save(compound));
        }
    }

    @Override
    public void tick() {
        if(!this.level.isClientSide()) {
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
                this.cookContents();
                this.remainingFuel--;
                if(this.remainingFuel == 0) {
                    /* Send updates to client */
                    CompoundNBT compound = new CompoundNBT();
                    this.writeRemainingFuel(compound);
                    TileEntityUtil.sendUpdatePacket(this, super.save(compound));
                }
            }
        } else {
            createParticleEffects();
        }
    }

    private boolean canCook() {
        return !this.oven.get(0).isEmpty() && this.cookingTime != this.cookingTotalTime;
    }

    private void cookContents() {
        boolean itemsChanged = false;
        for(int i = 0; i < this.oven.size(); i++) {
            if(!this.oven.get(i).isEmpty()) {
                if(this.cookingTime < this.cookingTotalTime) {
                    this.cookingTime++;
                    if(this.cookingTime == this.cookingTotalTime) {
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
            this.writeCookingTime(compound);
            TileEntityUtil.sendUpdatePacket(this, super.save(compound));
        }
    }

    private void spawnParticles() {
        World world = this.getLevel();
        if(world != null) {
            if(this.isCooking() && this.remainingFuel > 0) {
                double posX, posY, posZ;
                posX = worldPosition.getX() + 0.1 + 0.8 * world.random.nextDouble();
                posY = worldPosition.getY() + 0.25;
                posZ = worldPosition.getZ() + 0.2 + 0.6 * world.random.nextDouble();
                world.addParticle(ParticleTypes.FLAME, posX, posY, posZ, 0.0, 0.0, 0.0);
                if(this.cookingTime == this.cookingTotalTime) {
                    posX = worldPosition.getX() + 0.5;
                    posY = worldPosition.getY() + .7;
                    posZ = worldPosition.getZ() + 0.5;
                    world.addParticle(ParticleTypes.SMOKE, posX, posY, posZ, 0.0D, 5.0E-4D, 0.0D);
                }
            }
        }
    }

    private boolean isCooking() {
        return !this.oven.get(0).isEmpty() && (this.cookingTime != this.cookingTotalTime);
    }

    public Optional<PizzaOvenRecipe> findMatchingRecipe(ItemStack input) {
        return this.oven.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.level.getRecipeManager().getRecipeFor(RecipeUtil.PIZZA_OVEN, new Inventory(input), this.level);
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
            ItemStack result = net.minecraft.inventory.ItemStackHelper.removeItem(this.oven, index, count);

            if(this.oven.get(index).isEmpty()) {
                if(this.cookingTime == this.cookingTotalTime) {
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

        ItemStack result = net.minecraft.inventory.ItemStackHelper.removeItem(this.fuel, index, count);

        /* Send updates to client */
        CompoundNBT compound = new CompoundNBT();
        this.writeFuel(compound);
        TileEntityUtil.sendUpdatePacket(this, super.save(compound));

        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if(index - this.fuel.size() >= 0) {
            return net.minecraft.inventory.ItemStackHelper.takeItem(this.oven, index - this.fuel.size());
        }
        return net.minecraft.inventory.ItemStackHelper.takeItem(this.fuel, index);
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
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void clearContent() {
        this.fuel.clear();
        this.oven.clear();
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compound) {
        super.load(blockState, compound);
        if(compound.contains("Oven", Constants.NBT.TAG_LIST)) {
            this.oven.clear();
            InventoryUtil.loadAllItemsWithKey("Oven", compound, this.oven);
        }
        if(compound.contains("Fuel", Constants.NBT.TAG_LIST)) {
            this.fuel.clear();
            InventoryUtil.loadAllItemsWithKey("Fuel", compound, this.fuel);
        }
        if(compound.contains("RemainingFuel", Constants.NBT.TAG_INT)) {
            this.remainingFuel = compound.getInt("RemainingFuel");
        }
        if(compound.contains("CookingTime", Constants.NBT.TAG_INT_ARRAY)) {
            this.cookingTime = compound.getInt("CookingTime");
        }
        if(compound.contains("CookingTotalTime", Constants.NBT.TAG_INT_ARRAY)) {
            this.cookingTime = compound.getInt("CookingTotalTime");
        }
        if(compound.contains("Experience", Constants.NBT.TAG_INT_ARRAY)) {
            this.experience = compound.getFloat("Experience");
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compoundNBT) {
        this.writeItems(compoundNBT);
        this.writeFuel(compoundNBT);
        this.writeCookingTime(compoundNBT);
        this.writeCookingTotalTime(compoundNBT);
        this.writeExperience(compoundNBT);
        this.writeRemainingFuel(compoundNBT);
        return super.save(compoundNBT);
    }

    private CompoundNBT writeItems(CompoundNBT compound) {
        InventoryUtil.saveAllItemsWithKey("Oven", compound, this.oven, true);
        return compound;
    }

    private CompoundNBT writeFuel(CompoundNBT compound) {
        InventoryUtil.saveAllItemsWithKey("Fuel", compound, this.fuel, true);
        return compound;
    }

    private CompoundNBT writeRemainingFuel(CompoundNBT compound) {
        compound.putInt("RemainingFuel", this.remainingFuel);
        return compound;
    }

    private CompoundNBT writeCookingTime(CompoundNBT compound) {
        compound.putInt("CookingTime", this.cookingTime);
        return compound;
    }

    private CompoundNBT writeCookingTotalTime(CompoundNBT compound) {
        compound.putInt("CookingTotalTime", this.cookingTotalTime);
        return compound;
    }

    private CompoundNBT writeExperience(CompoundNBT compound) {
        compound.putFloat("Experience", this.experience);
        return compound;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64;
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
                if(this.cookingTime == this.cookingTotalTime) {
                    Optional<PizzaOvenRecipe> optional = this.level.getRecipeManager().getRecipeFor(RecipeUtil.PIZZA_OVEN, new Inventory(stack), this.level);
                    return !optional.isPresent();
                }
            }
        }
        return false;
    }
}