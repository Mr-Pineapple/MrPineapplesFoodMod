package co.uk.mrpineapple.pinesfoodmod.core;

import co.uk.mrpineapple.pinesfoodmod.client.ClientEvents;
import co.uk.mrpineapple.pinesfoodmod.common.CommonEvents;
import co.uk.mrpineapple.pinesfoodmod.common.event.Jigsaw;
import co.uk.mrpineapple.pinesfoodmod.core.registry.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
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
        EntityRegistry.PROFESSIONS.register(bus);
        EntityRegistry.POI_TYPES.register(bus);
        LootTableRegistry.GLM.register(bus);

        bus.addListener(ClientEvents::onClientSetup);
        bus.addListener(CommonEvents::onCommonSetup);
    }

    @SubscribeEvent
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        Jigsaw.registerJigsaw(event.getServer(), new ResourceLocation("minecraft:village/plains/houses"), new ResourceLocation("pinesfoodmod:village/plains/plains_pizzeria"), 500);
        Jigsaw.registerJigsaw(event.getServer(), new ResourceLocation("minecraft:village/taiga/houses"), new ResourceLocation("pinesfoodmod:village/taiga/plains_pizzeria"), 500);
        Jigsaw.registerJigsaw(event.getServer(), new ResourceLocation("minecraft:village/savanna/houses"), new ResourceLocation("pinesfoodmod:village/savanna/plains_pizzeria"), 500);
        Jigsaw.registerJigsaw(event.getServer(), new ResourceLocation("minecraft:village/snowy/houses"), new ResourceLocation("pinesfoodmod:village/snowy/plains_pizzeria"), 500);
        Jigsaw.registerJigsaw(event.getServer(), new ResourceLocation("minecraft:village/desert/houses"), new ResourceLocation("pinesfoodmod:village/desert/plains_pizzeria"), 500);
    }
}