package co.uk.mrpineapple.pinesfoodmod.client.renderer.tileentity;

import co.uk.mrpineapple.pinesfoodmod.client.RenderUtil;
import co.uk.mrpineapple.pinesfoodmod.common.tileentity.PizzaBoxTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Author: Mr. Pineapple
 */
@OnlyIn(Dist.CLIENT)
public class PizzaBoxTileEntityRenderer extends TileEntityRenderer<PizzaBoxTileEntity> {

    public PizzaBoxTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(PizzaBoxTileEntity tileEntity, float partialTicks, MatrixStack matrices, IRenderTypeBuffer buffer, int light, int overlay) {
        NonNullList<ItemStack> slot = tileEntity.getItems();
        ItemStack stack = slot.get(0);

        if(!stack.isEmpty()) {
            matrices.pushPose();
            matrices.scale(0.95f, 1.0f, 0.95f);
            matrices.translate(0.525D, -0.077D, 0.525D);
            matrices.mulPose(Vector3f.XP.rotationDegrees(90F));
            RenderUtil.renderItem(stack, matrices, buffer, light);
            matrices.popPose();
        }
    }

}
