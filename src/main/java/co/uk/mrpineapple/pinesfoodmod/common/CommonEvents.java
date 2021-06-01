package co.uk.mrpineapple.pinesfoodmod.common;

import co.uk.mrpineapple.pinesfoodmod.core.registry.EntityRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonEvents {
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        EntityRegistry.createTradeData();
        EntityRegistry.registerPizzaMakerPOI();
    }
}
