package co.uk.mrpineapple.pinesfoodmod.client.screens.handlers;

import co.uk.mrpineapple.pinesfoodmod.common.tileentity.PizzaBoardTileEntity;
import co.uk.mrpineapple.pinesfoodmod.core.registry.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.math.BlockPos;

public class PizzaBoardScreenHandler extends Container {
    private PizzaBoardTileEntity pizzaBoardTileEntity;
    private BlockPos pos;

    public PizzaBoardScreenHandler(int windowId, IInventory playerInventory, PizzaBoardTileEntity pizzaBoard) {
        super(ScreenHandlerRegistry.PIZZA_BOARD.get(), windowId);
        this.pizzaBoardTileEntity = pizzaBoard;
        this.pos = pizzaBoard.getBlockPos();

        for(int x = 0; x < 3; x++) {
            for(int y = 0; y < 9; y++) {
                this.addSlot(new Slot(playerInventory, y + x * 9 + 9, 8 + y * 18, 120 + x * 18 - 9));
            }
        }

        for(int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 178 - 9));
        }
    }


    @Override
    public boolean stillValid(PlayerEntity p_75145_1_) {
        return true;
    }

    public PizzaBoardTileEntity getPizzaBoardTileEntity() {
        return pizzaBoardTileEntity;
    }

    public BlockPos getPos() {
        return pos;
    }
}
