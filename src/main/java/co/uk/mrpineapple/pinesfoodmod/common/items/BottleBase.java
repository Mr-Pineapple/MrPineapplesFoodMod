package co.uk.mrpineapple.pinesfoodmod.common.items;

import co.uk.mrpineapple.pinesfoodmod.core.PinesFoodMod;
import co.uk.mrpineapple.pinesfoodmod.core.registry.FoodList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BottleBase extends HoneyBottleItem {

    public BottleBase(Food food) {
        super(new Item.Properties().tab(PinesFoodMod.TAB).stacksTo(16).food(food).craftRemainder(Items.GLASS_BOTTLE));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
        super.finishUsingItem(stack, world, entity);
        if(entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)entity;
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.awardStat(Stats.ITEM_USED.get(this));
        }
        if(!world.isClientSide) {
            effects(entity);
        }
        if(stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
            if(entity instanceof PlayerEntity && !((PlayerEntity)entity).abilities.instabuild) {
                ItemStack itemStack = new ItemStack(Items.GLASS_BOTTLE);
                PlayerEntity playerEntity = (PlayerEntity)entity;
                if(!playerEntity.inventory.add(itemStack)) {
                    playerEntity.drop(itemStack, false);
                }
            }
            return stack;
        }
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    void effects(@Nonnull LivingEntity entity) {
        entity.removeEffect(Effects.POISON);
    }

    public static class WineBottle extends BottleBase {
        public WineBottle() {
            super(FoodList.WINE);
        }

        @Override
        void effects(@Nonnull LivingEntity entity) {
            entity.addEffect(new EffectInstance(Effects.CONFUSION, 20*30, 2, false, false));
            entity.addEffect(new EffectInstance(Effects.BLINDNESS, 20*5, 1, false, false));
            entity.addEffect(new EffectInstance(Effects.ABSORPTION, 20*60, 1, false, false));
        }
    }

    public static class ChocolateSauceBottle extends BottleBase {
        public ChocolateSauceBottle() {
            super(FoodList.CHOCOLATE_SAUCE);
        }

        @Override
        void effects(@Nonnull LivingEntity entity) {
            int chance = random.nextInt(3);
            if(chance == 1) {
                entity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 20*15));
            }
            entity.removeEffect(Effects.MOVEMENT_SLOWDOWN);
        }
    }

    public static class MilkBottle extends BottleBase {
        public MilkBottle() {
            super(FoodList.MILK_BOTTLE);
        }

        @Override
        void effects(@Nonnull LivingEntity entity) {
            entity.removeAllEffects();
        }
    }

}
