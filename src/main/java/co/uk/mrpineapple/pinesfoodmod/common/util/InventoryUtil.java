package co.uk.mrpineapple.pinesfoodmod.common.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;

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

    /**
     * Load items from a specific list
     * @see net.minecraft.inventory.ItemStackHelper#loadAllItems(CompoundNBT, NonNullList)
     */
    public static void loadAllItemsWithKey(String key, CompoundNBT tag, NonNullList<ItemStack> list) {
        ListNBT listTag = tag.getList(key, Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < listTag.size(); i++) {
            CompoundNBT slotCompound = listTag.getCompound(i);
            int j = slotCompound.getByte("Slot") & 255;
            if(j < list.size()) {
                list.set(j, ItemStack.of(slotCompound));
            }
        }
    }

    /**
     * Saves items to a specific list & saves empty slots
     * @see net.minecraft.inventory.ItemStackHelper#saveAllItems(CompoundNBT, NonNullList)
     */
    public static CompoundNBT saveAllItemsWithKey(String key, CompoundNBT tag, NonNullList<ItemStack> list) {
        return saveAllItemsWithKey(key, tag, list, true);
    }

    /**
     * Saves items to a specific list
     * @see net.minecraft.inventory.ItemStackHelper#saveAllItems(CompoundNBT, NonNullList, boolean)
     */
    public static CompoundNBT saveAllItemsWithKey(String key, CompoundNBT tag, NonNullList<ItemStack> list, boolean saveEmpty) {
        ListNBT listTag = new ListNBT();
        for(int i = 0; i < list.size(); ++i) {
            ItemStack stack = list.get(i);
            if(!stack.isEmpty()) {
                CompoundNBT itemCompound = new CompoundNBT();
                itemCompound.putByte("Slot", (byte) i);
                stack.save(itemCompound);
                listTag.add(itemCompound);
            }
        } if(!listTag.isEmpty() || saveEmpty) {
            tag.put(key, listTag);
        }
        return tag;
    }
}
