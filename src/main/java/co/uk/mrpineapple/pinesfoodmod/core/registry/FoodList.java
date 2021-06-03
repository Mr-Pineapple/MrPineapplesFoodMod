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

    public static final Food CHOCOLATE_SAUCE = (new Food.Builder()).nutrition(4).saturationMod(0.1F).build();
    public static final Food WINE = (new Food.Builder()).nutrition(3).saturationMod(0.25f).build();
    public static final Food MILK_BOTTLE = (new Food.Builder()).nutrition(1).saturationMod(0.3f).build();

    public static final Food PEPPERONI = (new Food.Builder().nutrition(1).saturationMod(0.2f).build());
    public static final Food BACON = (new Food.Builder().nutrition(1).saturationMod(0.1f).build());
    public static final Food COOKED_BACON = (new Food.Builder().nutrition(2).saturationMod(0.2f).build());

    public static final Food CHEESE = (new Food.Builder().nutrition(3).saturationMod(0.3f).build());

    public static final Food RAW_PIZZA = new Food.Builder().nutrition(0).saturationMod(0.1F).build();

    public static final Food PIZZA_BASE = new Food.Builder().nutrition(2).saturationMod(0.1F).build();
    public static final Food PIZZA_TOMATO_BASE = new Food.Builder().nutrition(2).saturationMod(0.1F).build();
    public static final Food PIZZA_CHEESE = new Food.Builder().nutrition(2).saturationMod(0.1F).build();
    public static final Food PIZZA_VEGETABLE = new Food.Builder().nutrition(4).saturationMod(0.4F).build();
    public static final Food PIZZA_GARLIC = new Food.Builder().nutrition(2).saturationMod(0.2F).build();
    public static final Food PIZZA_HAWAIIAN = new Food.Builder().nutrition(5).saturationMod(0.6F).build();
    public static final Food PIZZA_VEGAN = new Food.Builder().nutrition(3).saturationMod(0.2F).build();
    public static final Food PIZZA_PEPPERONI = new Food.Builder().nutrition(3).saturationMod(0.15F).build();
    public static final Food PIZZA_SEAFOOD = new Food.Builder().nutrition(3).saturationMod(0.5F).build();
    public static final Food PIZZA_RICOTTA = new Food.Builder().nutrition(2).saturationMod(0.25F).build();
}
