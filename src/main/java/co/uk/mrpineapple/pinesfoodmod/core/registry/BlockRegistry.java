package co.uk.mrpineapple.pinesfoodmod.core.registry;

import co.uk.mrpineapple.pinesfoodmod.common.blocks.CakeBase;
import co.uk.mrpineapple.pinesfoodmod.common.blocks.CropBase;
import co.uk.mrpineapple.pinesfoodmod.core.PinesFoodMod;
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

    public static final RegistryObject<Block> DECORATED_CHOCOLATE_CAKE = register("decorated_chocolate_cake", FoodList.DECORATED_CHOCOLATE_CAKE);
    public static final RegistryObject<Block> CHOCOLATE_CAKE = register("chocolate_cake", FoodList.CHOCOLATE_CAKE);
    public static final RegistryObject<Block> GLAZED_CHOCOLATE_CAKE = register("glazed_chocolate_cake", FoodList.GLAZED_CHOCOLATE_CAKE);
    public static final RegistryObject<Block> BIRTHDAY_CAKE = register("birthday_cake", FoodList.BIRTHDAY_CAKE);
    public static final RegistryObject<Block> RED_VELVET_CAKE = register("red_velvet_cake", FoodList.RED_VELVET_CAKE);

    public static <B extends Block> RegistryObject<B> register(String name, Supplier<? extends B> supplier) {
        RegistryObject<B> block = BLOCKS.register(name, supplier);
        return block;
    }

    public static <B extends Block> RegistryObject<B> register(String name, Supplier<? extends B> supplier, @Nullable ItemGroup group) {
        RegistryObject<B> block = BLOCKS.register(name, supplier);
        ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(group)));
        return block;
    }

    public static RegistryObject<Block> register(String name, @Nonnull Food food) {
        RegistryObject<Block> cake = BLOCKS.register(name, () -> new CakeBase(AbstractBlock.Properties.copy(Blocks.CAKE), food));
        ItemRegistry.ITEMS.register(name, () -> new BlockItem(cake.get(), new Item.Properties().tab(PinesFoodMod.TAB)));
        return cake;
    }
}
