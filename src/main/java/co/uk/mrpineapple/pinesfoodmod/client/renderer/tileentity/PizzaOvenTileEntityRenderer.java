package co.uk.mrpineapple.pinesfoodmod.client.renderer.tileentity;

import co.uk.mrpineapple.pinesfoodmod.common.blocks.PizzaOvenBlock;
import co.uk.mrpineapple.pinesfoodmod.common.tileentity.PizzaOvenTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;

@OnlyIn(Dist.CLIENT)
public class PizzaOvenTileEntityRenderer extends TileEntityRenderer<PizzaOvenTileEntity> {
    public PizzaOvenTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(PizzaOvenTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int combinedLight, int combinedOverlay) {
        NonNullList<ItemStack> oven = tileEntity.getOven();
        ItemStack ovenStack = oven.get(0);
        if(!ovenStack.isEmpty()) {
            matrixStack.pushPose();
                matrixStack.translate(0.5, 0.54, 0.51);
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(90F));
                matrixStack.translate(0, -0.1, 0.0);
                matrixStack.scale(0.9F, 0.9F, 0.9F);
                matrixStack.mulPose(Vector3f.ZP.rotationDegrees(90F));
                Minecraft.getInstance().getItemRenderer().renderStatic(ovenStack, ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, renderBuffer);
            matrixStack.popPose();
        }

        NonNullList<ItemStack> fuel = tileEntity.getFuel();
        for(int j = 0; j < fuel.size(); ++j) {
            ItemStack fuelStack = fuel.get(j);
            if(!fuelStack.isEmpty()) {
                matrixStack.pushPose();
                    matrixStack.translate(0.5, 0.1, 0.5);
                    matrixStack.mulPose(Vector3f.XP.rotationDegrees(90F));
                    matrixStack.translate(-0.2 + 0.2 * (j % 3), -0.2 + 0.2 * (j / 3), 0.0);
                    matrixStack.scale(0.375F, 0.375F, 0.375F);
                    matrixStack.mulPose(Vector3f.YP.rotationDegrees(10F));
                    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(10F));
                    matrixStack.mulPose(Vector3f.XP.rotationDegrees(5F));
                    Minecraft.getInstance().getItemRenderer().renderStatic(fuelStack, ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, renderBuffer);
                matrixStack.popPose();
            }
        }
    }

    /* Used to check direction of pizza oven to rotate renderer - not implemented as of yet */
    static BlockState getBlockState(RegistryObject<Block> block, Direction direction) {
        return block.get().getBlock().defaultBlockState().setValue(PizzaOvenBlock.FACING, direction);
    }
}
