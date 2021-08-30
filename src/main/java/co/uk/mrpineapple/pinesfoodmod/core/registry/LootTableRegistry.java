package co.uk.mrpineapple.pinesfoodmod.core.registry;

import co.uk.mrpineapple.pinesfoodmod.core.PinesFoodMod;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Author: Mr. Pineapple
 */
public class LootTableRegistry {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, PinesFoodMod.ID);

    public static final RegistryObject<DropFromChanceModifier.Serializer> DROP_FROM_CHANCE = GLM.register("drop_from_chance", DropFromChanceModifier.Serializer::new);

    private static class DropFromChanceModifier extends LootModifier {
        private final int dropChance;
        private final Item dropItem;

        protected DropFromChanceModifier(ILootCondition[] conditionsIn, int chance, Item drop) {
            super(conditionsIn);
            dropChance = chance;
            dropItem = drop;
        }

        @Nonnull
        @Override
        protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
            if(context.getRandom().nextInt(dropChance - 1) >= 1) {
                generatedLoot.add(new ItemStack(dropItem));
            }
            return generatedLoot;
        }

        private static class Serializer extends GlobalLootModifierSerializer<DropFromChanceModifier> {

            @Override
            public DropFromChanceModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
                int numSeeds = JSONUtils.getAsInt(object, "chance");
                Item dropItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getAsString(object, "drop")));
                return new DropFromChanceModifier(ailootcondition, numSeeds, dropItem);
            }

            @Override
            public JsonObject write(DropFromChanceModifier instance) {
                JsonObject json = makeConditions(instance.conditions);
                json.addProperty("chance", instance.dropChance);
                json.addProperty("drop", ForgeRegistries.ITEMS.getKey(instance.dropItem).toString());
                return json;
            }
        }
    }

}
