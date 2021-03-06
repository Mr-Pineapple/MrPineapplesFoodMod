package co.uk.mrpineapple.pinesfoodmod.client;

import co.uk.mrpineapple.pinesfoodmod.client.renderer.tileentity.PizzaBoxTileEntityRenderer;
import co.uk.mrpineapple.pinesfoodmod.client.renderer.tileentity.PizzaOvenTileEntityRenderer;
import co.uk.mrpineapple.pinesfoodmod.core.registry.BlockRegistry;
import co.uk.mrpineapple.pinesfoodmod.core.registry.TileEntityRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Predicate;

public class ClientEvents {
    public static void onClientSetup(FMLClientSetupEvent event) {
        Predicate<RenderType> cutoutPredicate = renderType -> renderType == RenderType.cutout();
        RenderTypeLookup.setRenderLayer(BlockRegistry.GRAPE.get(), cutoutPredicate);
        RenderTypeLookup.setRenderLayer(BlockRegistry.CORN.get(), cutoutPredicate);
        RenderTypeLookup.setRenderLayer(BlockRegistry.GARLIC.get(), cutoutPredicate);
        RenderTypeLookup.setRenderLayer(BlockRegistry.ONION.get(), cutoutPredicate);
        RenderTypeLookup.setRenderLayer(BlockRegistry.TOMATO.get(), cutoutPredicate);
        RenderTypeLookup.setRenderLayer(BlockRegistry.OLIVE.get(), cutoutPredicate);
        RenderTypeLookup.setRenderLayer(BlockRegistry.PINEAPPLE.get(), cutoutPredicate);

        RenderTypeLookup.setRenderLayer(BlockRegistry.CHERRY.get(), cutoutPredicate);
        RenderTypeLookup.setRenderLayer(BlockRegistry.BLUEBERRY.get(), cutoutPredicate);
        RenderTypeLookup.setRenderLayer(BlockRegistry.PEPPER.get(), cutoutPredicate);

        RenderTypeLookup.setRenderLayer(BlockRegistry.PIZZA_OVEN.get(), cutoutPredicate);

        Screens.screens();

        ClientRegistry.bindTileEntityRenderer(TileEntityRegistry.PIZZA_OVEN.get(), PizzaOvenTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityRegistry.PIZZA_BOX.get(), PizzaBoxTileEntityRenderer::new);
    }
}
