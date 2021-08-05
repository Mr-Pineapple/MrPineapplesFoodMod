package co.uk.mrpineapple.pinesfoodmod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RenderUtil {
    public static IBakedModel getModelFromItem(Item item) {
        return Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(new ItemStack(item));
    }

    public static IBakedModel getModelFromItemStack(ItemStack itemstack) {
        return Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(itemstack);
    }

    public static boolean isMouseWithin(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}
