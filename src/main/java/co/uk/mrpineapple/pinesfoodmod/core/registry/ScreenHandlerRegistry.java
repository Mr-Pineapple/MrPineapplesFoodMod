package co.uk.mrpineapple.pinesfoodmod.core.registry;

import co.uk.mrpineapple.pinesfoodmod.client.screens.handlers.PizzaBoardScreenHandler;
import co.uk.mrpineapple.pinesfoodmod.common.tileentity.PizzaBoardTileEntity;
import co.uk.mrpineapple.pinesfoodmod.core.PinesFoodMod;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ScreenHandlerRegistry {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, PinesFoodMod.ID);

    public static final RegistryObject<ContainerType<PizzaBoardScreenHandler>> PIZZA_BOARD = register("pizza_board", (IContainerFactory<PizzaBoardScreenHandler>) (windowId, playerInventory, data) -> {
        PizzaBoardTileEntity te = (PizzaBoardTileEntity)playerInventory.player.level.getBlockEntity(data.readBlockPos());
        return new PizzaBoardScreenHandler(windowId, playerInventory, te);
    });

    private static <T extends Container> RegistryObject<ContainerType<T>> register(String id, ContainerType.IFactory<T> factory) {
        return CONTAINERS.register(id, () -> new ContainerType<>(factory));
    }
}
