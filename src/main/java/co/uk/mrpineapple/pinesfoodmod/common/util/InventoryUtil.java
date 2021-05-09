package co.uk.mrpineapple.pinesfoodmod.common.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class InventoryUtil {
    public static boolean hasItemStack(PlayerEntity player, ItemStack find) {
        int count = 0;
        for(ItemStack stack : player.inventory.items) {
            if(!stack.isEmpty() && matches(stack, find)) {
                count += stack.getCount();
            }
        }
        return find.getCount() <= count;
    }

    private static boolean matches(ItemStack source, ItemStack target) {
        if(source.getItem() != target.getItem()) {
            return false;
        } else if(source.getDamageValue() != target.getDamageValue()) {
            return false;
        } else if(source.getTag() == null && target.getTag() != null) {
            return false;
        } else {
            return (source.getTag() == null || source.getTag().equals(target.getTag())) && source.areCapsCompatible(target);
        }
    }

    public static int getItem(PlayerEntity player, ItemStack find) {
        int count = 0;
        for(ItemStack stack : player.inventory.items) {
            if(!stack.isEmpty() && areItemStacksEqualIgnoreAmount(stack, find)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    private static boolean areItemStacksEqualIgnoreAmount(ItemStack source, ItemStack target) {
        if(source.getItem() != target.getItem()) {
            return false;
        } else if(source.getDamageValue() != target.getDamageValue()) {
            return false;
        } else if(source.getTag() == null && target.getTag() != null) {
            return false;
        } else {
            return (source.getTag() == null || source.getTag().equals(target.getTag())) && source.areCapsCompatible(target);
        }
    }
}
