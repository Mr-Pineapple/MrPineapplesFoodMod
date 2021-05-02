package co.uk.mrpineapple.pinesfoodmod.core.registry;

import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class FoodList {
    public static final Food CHOCOLATE_BAR = new Food.Builder().nutrition(6).saturationMod(0.9f).effect(new EffectInstance(Effects.MOVEMENT_SPEED, 20*10), 0.25f).build();
    public static final Food GREEN_APPLE = new Food.Builder().nutrition(4).saturationMod(0.25f).build();
    public static final Food ORANGE = new Food.Builder().nutrition(3).saturationMod(0.3f).build();

    public static final Food GRAPE = new Food.Builder().nutrition(3).saturationMod(0.2f).build();
    public static final Food CORN = new Food.Builder().nutrition(4).saturationMod(0.4f).build();
    public static final Food GARLIC = (new Food.Builder().nutrition(1).saturationMod(0.1f).build());
    public static final Food ONION = (new Food.Builder().nutrition(2).saturationMod(0.25f).build());
    public static final Food TOMATO = (new Food.Builder().nutrition(2).saturationMod(0.3f).build());
    public static final Food OLIVE = (new Food.Builder().nutrition(2).saturationMod(0.2f).build());
    public static final Food PINEAPPLE = (new Food.Builder().nutrition(2).saturationMod(0.2f).build());

    public static final Food DECORATED_CHOCOLATE_CAKE = new Food.Builder().nutrition(5).saturationMod(0.4f).build();
    public static final Food CHOCOLATE_CAKE = new Food.Builder().nutrition(2).saturationMod(0.4f).build();
    public static final Food GLAZED_CHOCOLATE_CAKE = new Food.Builder().nutrition(4).saturationMod(0.4f).build();
    public static final Food BIRTHDAY_CAKE = new Food.Builder().nutrition(4).saturationMod(0.4f).build();
    public static final Food RED_VELVET_CAKE = new Food.Builder().nutrition(4).saturationMod(0.4f).build();

    public static final Food CHERRY = (new Food.Builder()).nutrition(4).saturationMod(0.3f).build();
    public static final Food BLUEBERRY = (new Food.Builder()).nutrition(4).saturationMod(0.3f).build();
    public static final Food PEPPER = (new Food.Builder()).nutrition(4).saturationMod(0.5f).build();
}
