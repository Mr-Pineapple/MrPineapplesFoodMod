package co.uk.mrpineapple.pinesfoodmod.core.registry;

import co.uk.mrpineapple.pinesfoodmod.core.PinesFoodMod;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.util.SoundEvents;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.InvocationTargetException;

public class EntityRegistry {
    public static final DeferredRegister<PointOfInterestType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, PinesFoodMod.ID);
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, PinesFoodMod.ID);

    public static final RegistryObject<PointOfInterestType> PIZZA_MAKER_POI = POI_TYPES.register("pizza_maker", () -> new PointOfInterestType("pizza_maker", PointOfInterestType.getBlockStates(BlockRegistry.PIZZA_BOARD_ACACIA.get()), 1, 1));
    public static final RegistryObject<VillagerProfession> PIZZA_MAKER = PROFESSIONS.register("pizza_maker", () -> new VillagerProfession("pizza_maker", PIZZA_MAKER_POI.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_CARTOGRAPHER));

    public static void registerPizzaMakerPOI() {
        try {
            ObfuscationReflectionHelper.findMethod(PointOfInterestType.class, "registerBlockStates", PointOfInterestType.class).invoke(null, PIZZA_MAKER_POI.get());
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //TODO: Convert to JSON data for datapack compatibility
    public static void createTradeData() {
        VillagerTrades.ITrade[] pizzaMakerLevel1 = new VillagerTrades.ITrade[]{
                new VillagerTrades.EmeraldForItemsTrade(ItemRegistry.FLOUR.get(),3,16,4),
                new VillagerTrades.ItemsForEmeraldsTrade(ItemRegistry.SALT.get(), 1, 12, 16, 6)
        };
        VillagerTrades.ITrade[] pizzaMakerLevel2 = new VillagerTrades.ITrade[]{
                new VillagerTrades.EmeraldForItemsTrade(ItemRegistry.MOZZARELLA_CHEESE.get(),5,16,5),
                new VillagerTrades.ItemsForEmeraldsTrade(ItemRegistry.PINEAPPLE.get(), 2, 5, 16, 6)

        };
        VillagerTrades.ITrade[] pizzaMakerLevel3 = new VillagerTrades.ITrade[]{
                new VillagerTrades.EmeraldForItemsTrade(ItemRegistry.CARDBOARD.get(),3,16,5),
                new VillagerTrades.EmeraldForItemsTrade(ItemRegistry.OLIVE_OIL.get(),5,16,6),

        };
        VillagerTrades.ITrade[] pizzaMakerLevel4 = new VillagerTrades.ITrade[]{
                new VillagerTrades.EmeraldForItemsTrade(ItemRegistry.TOMATO.get(),3,7,5),
                new VillagerTrades.EmeraldForItemsTrade(BlockRegistry.PIZZA_BOARD_BIRCH.get().asItem(),7,3,10)
        };
        VillagerTrades.ITrade[] pizzaMakerLevel5 = new VillagerTrades.ITrade[]{
                new VillagerTrades.EmeraldForItemsTrade(ItemRegistry.BACON.get(),3,5,3)
        };
        VillagerTrades.TRADES.put(PIZZA_MAKER.get(), new Int2ObjectOpenHashMap<>(ImmutableMap.of(1, pizzaMakerLevel1, 2, pizzaMakerLevel2, 3, pizzaMakerLevel3, 4, pizzaMakerLevel4, 5, pizzaMakerLevel5)));
    }

}
