package co.uk.mrpineapple.pinesfoodmod.client;

import co.uk.mrpineapple.pinesfoodmod.client.screens.PizzaBoardScreen;
import co.uk.mrpineapple.pinesfoodmod.core.registry.ScreenHandlerRegistry;
import net.minecraft.client.gui.ScreenManager;

public class Screens {
    public static void screens() {
        ScreenManager.register(ScreenHandlerRegistry.PIZZA_BOARD.get(), PizzaBoardScreen::new);
    }
}
