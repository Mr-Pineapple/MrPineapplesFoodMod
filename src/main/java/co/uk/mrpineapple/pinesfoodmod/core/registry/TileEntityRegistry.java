package co.uk.mrpineapple.pinesfoodmod.core.registry;

import co.uk.mrpineapple.pinesfoodmod.common.tileentity.PizzaBoardTileEntity;
import co.uk.mrpineapple.pinesfoodmod.common.tileentity.PizzaBoxTileEntity;
import co.uk.mrpineapple.pinesfoodmod.common.tileentity.PizzaOvenTileEntity;
import co.uk.mrpineapple.pinesfoodmod.core.PinesFoodMod;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class TileEntityRegistry {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, PinesFoodMod.ID);

    public static final RegistryObject<TileEntityType<PizzaBoardTileEntity>> PIZZA_BOARD = register("pizza_board", PizzaBoardTileEntity::new, () -> new Block[]{BlockRegistry.PIZZA_BOARD_ACACIA.get(), BlockRegistry.PIZZA_BOARD_BIRCH.get(), BlockRegistry.PIZZA_BOARD_DARK_OAK.get(), BlockRegistry.PIZZA_BOARD_JUNGLE.get(), BlockRegistry.PIZZA_BOARD_OAK.get(), BlockRegistry.PIZZA_BOARD_SPRUCE.get()});
    public static final RegistryObject<TileEntityType<PizzaOvenTileEntity>> PIZZA_OVEN = register("pizza_oven", PizzaOvenTileEntity::new, () -> new Block[]{BlockRegistry.PIZZA_OVEN.get()});
    public static final RegistryObject<TileEntityType<PizzaBoxTileEntity>> PIZZA_BOX = register("pizza_box", PizzaBoxTileEntity::new, () -> new Block[]{BlockRegistry.PIZZA_BOX.get()});

    private static <T extends TileEntity>RegistryObject<TileEntityType<T>> register(String id, Supplier<T> factory, Supplier<Block[]> validBlocks) {
        return TILE_ENTITY.register(id, () -> TileEntityType.Builder.of(factory, validBlocks.get()).build(null));
    }
}
