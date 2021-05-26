package co.uk.mrpineapple.pinesfoodmod.core.registry;

import co.uk.mrpineapple.pinesfoodmod.common.blocks.*;
import co.uk.mrpineapple.pinesfoodmod.core.PinesFoodMod;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.function.Supplier;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PinesFoodMod.ID);

    public static final RegistryObject<Block> GRAPE = register("grapes", () -> new CropBase(ItemRegistry.GRAPE::get, ItemRegistry.GRAPE::get));
    public static final RegistryObject<Block> CORN = register("corn", () -> new CropBase(ItemRegistry.CORN::get, ItemRegistry.CORN::get));
    public static final RegistryObject<Block> GARLIC = register("garlic", () -> new CropBase(ItemRegistry.GARLIC::get, ItemRegistry.GARLIC::get));
    public static final RegistryObject<Block> ONION = register("onion", () -> new CropBase(ItemRegistry.ONION::get, ItemRegistry.ONION::get));
    public static final RegistryObject<Block> TOMATO = register("tomato", () -> new CropBase(ItemRegistry.TOMATO::get, ItemRegistry.TOMATO::get));
    public static final RegistryObject<Block> OLIVE = register("olive", () -> new CropBase(ItemRegistry.OLIVE::get, ItemRegistry.OLIVE::get));
    public static final RegistryObject<Block> PINEAPPLE = register("pineapple", () -> new CropBase(ItemRegistry.PINEAPPLE::get, ItemRegistry.PINEAPPLE::get));

    public static final RegistryObject<Block> CHERRY = register("cherry_bush", () -> new BerryBlockBase(ItemRegistry.CHERRY::get, false, false));
    public static final RegistryObject<Block> BLUEBERRY = register("blueberry_bush", () -> new BerryBlockBase(ItemRegistry.BLUEBERRY::get, false, false));
    public static final RegistryObject<Block> PEPPER = register("pepper_bush", () -> new BerryBlockBase(ItemRegistry.PEPPER::get, true, false));

    public static final RegistryObject<Block> DECORATED_CHOCOLATE_CAKE = register("decorated_chocolate_cake", FoodList.DECORATED_CHOCOLATE_CAKE);
    public static final RegistryObject<Block> CHOCOLATE_CAKE = register("chocolate_cake", FoodList.CHOCOLATE_CAKE);
    public static final RegistryObject<Block> GLAZED_CHOCOLATE_CAKE = register("glazed_chocolate_cake", FoodList.GLAZED_CHOCOLATE_CAKE);
    public static final RegistryObject<Block> BIRTHDAY_CAKE = register("birthday_cake", FoodList.BIRTHDAY_CAKE);
    public static final RegistryObject<Block> RED_VELVET_CAKE = register("red_velvet_cake", FoodList.RED_VELVET_CAKE);

    public static final RegistryObject<Block> PIZZA_BOARD_ACACIA = register("pizza_board_acacia", () -> new PizzaBoardBlock(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS)), PinesFoodMod.TAB);
    public static final RegistryObject<Block> PIZZA_BOARD_BIRCH = register("pizza_board_birch", () -> new PizzaBoardBlock(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS)), PinesFoodMod.TAB);
    public static final RegistryObject<Block> PIZZA_BOARD_DARK_OAK = register("pizza_board_dark_oak", () -> new PizzaBoardBlock(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS)), PinesFoodMod.TAB);
    public static final RegistryObject<Block> PIZZA_BOARD_JUNGLE = register("pizza_board_jungle", () -> new PizzaBoardBlock(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS)), PinesFoodMod.TAB);
    public static final RegistryObject<Block> PIZZA_BOARD_OAK = register("pizza_board_oak", () -> new PizzaBoardBlock(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS)), PinesFoodMod.TAB);
    public static final RegistryObject<Block> PIZZA_BOARD_SPRUCE = register("pizza_board_spruce", () -> new PizzaBoardBlock(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS)), PinesFoodMod.TAB);

    //For blocks - not registering an item
    public static <B extends Block> RegistryObject<B> register(String name, Supplier<? extends B> supplier) {
        RegistryObject<B> block = BLOCKS.register(name, supplier);
        return block;
    }

    //Registering a block with a normal BlockItem - only with a tab property
    //Should use alternate methods when needed, likely to be removed.
    @Deprecated
    public static <B extends Block> RegistryObject<B> register(String name, Supplier<? extends B> supplier, @Nullable ItemGroup group) {
        RegistryObject<B> block = BLOCKS.register(name, supplier);
        ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(group)));
        return block;
    }

    //Cakes
    public static RegistryObject<Block> register(String name, @Nonnull Food food) {
        RegistryObject<Block> cake = BLOCKS.register(name, () -> new CakeBase(AbstractBlock.Properties.copy(Blocks.CAKE), food));
        ItemRegistry.ITEMS.register(name, () -> new BlockItem(cake.get(), new Item.Properties().tab(PinesFoodMod.TAB)));
        return cake;
    }
}
