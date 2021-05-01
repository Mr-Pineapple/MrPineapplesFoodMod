package co.uk.mrpineapple.pinesfoodmod.core.registry;

import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class FoodList {
    public static final Food CHOCOLATE_BAR = (new Food.Builder()).nutrition(6).saturationMod(0.9f).effect(new EffectInstance(Effects.MOVEMENT_SPEED, 20*10), 0.25f).build();
}
