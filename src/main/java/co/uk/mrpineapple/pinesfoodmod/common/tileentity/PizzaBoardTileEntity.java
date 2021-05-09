package co.uk.mrpineapple.pinesfoodmod.common.tileentity;

import co.uk.mrpineapple.pinesfoodmod.client.screens.handlers.PizzaBoardScreenHandler;
import co.uk.mrpineapple.pinesfoodmod.core.registry.TileEntityRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class PizzaBoardTileEntity extends BaseTileEntity implements StorageBlock {

    public PizzaBoardTileEntity() {
        super(TileEntityRegistry.PIZZA_BOARD.get());
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.pinesfoodmod.pizza_board");
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new PizzaBoardScreenHandler(windowId, playerInventory, this);
    }

    @Override
    public NonNullList<ItemStack> getInventory() {
        return null;
    }
}
