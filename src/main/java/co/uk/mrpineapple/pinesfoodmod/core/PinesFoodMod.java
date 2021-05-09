package co.uk.mrpineapple.pinesfoodmod.core;

import co.uk.mrpineapple.pinesfoodmod.client.ClientEvents;
import co.uk.mrpineapple.pinesfoodmod.common.recipe.util.RecipeUtil;
import co.uk.mrpineapple.pinesfoodmod.core.registry.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = PinesFoodMod.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
@Mod(PinesFoodMod.ID)
public class PinesFoodMod {
    public static final String ID = "pinesfoodmod";
    public static final ItemGroup TAB = new ItemGroup(ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemRegistry.WHITE_CHOCOLATE_BAR.get());
        }
    };

    public PinesFoodMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        ItemRegistry.ITEMS.register(bus);
        BlockRegistry.BLOCKS.register(bus);
        TileEntityRegistry.TILE_ENTITY.register(bus);
        ScreenHandlerRegistry.CONTAINERS.register(bus);
        RecipeSerializerRegistry.RECIPE_SERIALIZER.register(bus);

        bus.addListener(ClientEvents::onClientSetup);
    }
}