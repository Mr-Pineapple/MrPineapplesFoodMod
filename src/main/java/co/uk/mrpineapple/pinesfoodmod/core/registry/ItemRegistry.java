package co.uk.mrpineapple.pinesfoodmod.core.registry;

import co.uk.mrpineapple.pinesfoodmod.core.PinesFoodMod;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PinesFoodMod.ID);

    public static final RegistryObject<Item> WHITE_CHOCOLATE_BAR = registerChocolateBar("white");
    public static final RegistryObject<Item> ORANGE_CHOCOLATE_BAR = registerChocolateBar("orange");
    public static final RegistryObject<Item> MAGENTA_CHOCOLATE_BAR = registerChocolateBar("magenta");
    public static final RegistryObject<Item> LIGHT_BLUE_CHOCOLATE_BAR = registerChocolateBar("light_blue");
    public static final RegistryObject<Item> YELLOW_CHOCOLATE_BAR = registerChocolateBar("yellow");
    public static final RegistryObject<Item> LIME_CHOCOLATE_BAR = registerChocolateBar("lime");
    public static final RegistryObject<Item> PINK_CHOCOLATE_BAR = registerChocolateBar("pink");
    public static final RegistryObject<Item> GRAY_CHOCOLATE_BAR = registerChocolateBar("gray");
    public static final RegistryObject<Item> LIGHT_GRAY_CHOCOLATE_BAR = registerChocolateBar("light_gray");
    public static final RegistryObject<Item> CYAN_CHOCOLATE_BAR = registerChocolateBar("cyan");
    public static final RegistryObject<Item> PURPLE_CHOCOLATE_BAR = registerChocolateBar("purple");
    public static final RegistryObject<Item> BLUE_CHOCOLATE_BAR = registerChocolateBar("blue");
    public static final RegistryObject<Item> BROWN_CHOCOLATE_BAR = registerChocolateBar("brown");
    public static final RegistryObject<Item> GREEN_CHOCOLATE_BAR = registerChocolateBar("green");
    public static final RegistryObject<Item> RED_CHOCOLATE_BAR = registerChocolateBar("red");
    public static final RegistryObject<Item> BLACK_CHOCOLATE_BAR = registerChocolateBar("black");



    public static <I extends Item> RegistryObject<I> registerItem(String name, Supplier<? extends I> supplier) {
        RegistryObject<I> item = ITEMS.register(name, supplier);
        return item;
    }

    public static RegistryObject<Item> registerChocolateBar(String name) {
        RegistryObject<Item> item = ITEMS.register(name + "_chocolate_bar", () -> new Item(new Item.Properties().food(FoodList.CHOCOLATE_BAR).tab(PinesFoodMod.TAB)) );
        return item;
    }
}
