package co.uk.mrpineapple.pinesfoodmod.core.registry;

import co.uk.mrpineapple.pinesfoodmod.common.items.BottleBase;
import co.uk.mrpineapple.pinesfoodmod.core.PinesFoodMod;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PinesFoodMod.ID);

    /* Chocolate Bars */
    public static final RegistryObject<Item> WHITE_CHOCOLATE_BAR = register("white");
    public static final RegistryObject<Item> ORANGE_CHOCOLATE_BAR = register("orange");
    public static final RegistryObject<Item> MAGENTA_CHOCOLATE_BAR = register("magenta");
    public static final RegistryObject<Item> LIGHT_BLUE_CHOCOLATE_BAR = register("light_blue");
    public static final RegistryObject<Item> YELLOW_CHOCOLATE_BAR = register("yellow");
    public static final RegistryObject<Item> LIME_CHOCOLATE_BAR = register("lime");
    public static final RegistryObject<Item> PINK_CHOCOLATE_BAR = register("pink");
    public static final RegistryObject<Item> GRAY_CHOCOLATE_BAR = register("gray");
    public static final RegistryObject<Item> LIGHT_GRAY_CHOCOLATE_BAR = register("light_gray");
    public static final RegistryObject<Item> CYAN_CHOCOLATE_BAR = register("cyan");
    public static final RegistryObject<Item> PURPLE_CHOCOLATE_BAR = register("purple");
    public static final RegistryObject<Item> BLUE_CHOCOLATE_BAR = register("blue");
    public static final RegistryObject<Item> BROWN_CHOCOLATE_BAR = register("brown");
    public static final RegistryObject<Item> GREEN_CHOCOLATE_BAR = register("green");
    public static final RegistryObject<Item> RED_CHOCOLATE_BAR = register("red");
    public static final RegistryObject<Item> BLACK_CHOCOLATE_BAR = register("black");

    /* Fruit */
    public static final RegistryObject<Item> GREEN_APPLE = register("green_apple", FoodList.GREEN_APPLE);
    public static final RegistryObject<Item> ORANGE = register("orange", FoodList.ORANGE);

    /* Crops */
    public static final RegistryObject<BlockItem> GRAPE = register("grapes", BlockRegistry.GRAPE, FoodList.GRAPE);
    public static final RegistryObject<BlockItem> CORN = register("corn", BlockRegistry.CORN, FoodList.CORN);
    public static final RegistryObject<BlockItem> GARLIC = register("garlic", BlockRegistry.GARLIC, FoodList.GARLIC);
    public static final RegistryObject<BlockItem> ONION = register("onion", BlockRegistry.ONION, FoodList.ONION);
    public static final RegistryObject<BlockItem> TOMATO = register("tomato", BlockRegistry.TOMATO, FoodList.TOMATO);
    public static final RegistryObject<BlockItem> OLIVE = register("olive", BlockRegistry.OLIVE, FoodList.OLIVE);
    public static final RegistryObject<BlockItem> PINEAPPLE = register("pineapple", BlockRegistry.PINEAPPLE, FoodList.PINEAPPLE);

    /* Bushes */
    public static final RegistryObject<BlockItem> CHERRY = register("cherry_bush", () -> new BlockNamedItem(BlockRegistry.CHERRY.get(), new Item.Properties().tab(PinesFoodMod.TAB).food(FoodList.CHERRY)));
    public static final RegistryObject<BlockItem> BLUEBERRY = register("blueberry_bush", () -> new BlockNamedItem(BlockRegistry.BLUEBERRY.get(), new Item.Properties().tab(PinesFoodMod.TAB).food(FoodList.BLUEBERRY)));
    public static final RegistryObject<BlockItem> PEPPER = register("pepper_bush", () -> new BlockNamedItem(BlockRegistry.PEPPER.get(), new Item.Properties().tab(PinesFoodMod.TAB).food(FoodList.PEPPER)));

    /* Bottles */
    public static final RegistryObject<Item> WINE = register("wine", BottleBase.WineBottle::new);
    public static final RegistryObject<Item> MILK_BOTTLE = register("milk_bottle", BottleBase.MilkBottle::new);
    public static final RegistryObject<Item> CHOCOLATE_SAUCE = register("bottle_of_chocolate_sauce", BottleBase.ChocolateSauceBottle::new);


    public static RegistryObject<BlockItem> register(String name, RegistryObject<Block> block, Food food) {
        RegistryObject<BlockItem> item = ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(PinesFoodMod.TAB).food(food)));
        return item;
    }

    public static RegistryObject<Item> register(String name) {
        RegistryObject<Item> item = ITEMS.register(name + "_chocolate_bar", () -> new Item(new Item.Properties().food(FoodList.CHOCOLATE_BAR).tab(PinesFoodMod.TAB)) );
        return item;
    }

    public static RegistryObject<Item> register(String name, Food food) {
        RegistryObject<Item> item = ITEMS.register(name, () -> new Item(new Item.Properties().food(food).tab(PinesFoodMod.TAB)));
        return item;
    }

    public static <I extends Item> RegistryObject<I> register(String name, Supplier<I> supplier) {
        RegistryObject<I> item = ITEMS.register(name, supplier);
        return item;
    }
}
