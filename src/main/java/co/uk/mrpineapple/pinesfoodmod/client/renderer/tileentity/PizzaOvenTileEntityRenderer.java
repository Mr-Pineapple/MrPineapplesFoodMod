package co.uk.mrpineapple.pinesfoodmod.client.renderer.tileentity;

import co.uk.mrpineapple.pinesfoodmod.common.blocks.PizzaOvenBlock;
import co.uk.mrpineapple.pinesfoodmod.common.tileentity.PizzaOvenTileEntity;
import co.uk.mrpineapple.pinesfoodmod.core.registry.BlockRegistry;
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
    public void render(PizzaOvenTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLight, int combinedOverlay) {
        NonNullList<ItemStack> oven = tileEntity.getOven();
        ItemStack stack = oven.get(0);
        if(!stack.isEmpty()) {
            matrixStack.pushPose();

            if(tileEntity.getBlockState() == getBlockState(BlockRegistry.PIZZA_OVEN, Direction.NORTH) /*|| tileEntity.getBlockState() == getBlockState(BlockList.PIZZA_OVEN, Direction.NORTH)*/) {
                matrixStack.translate(0.5, 0.54, 0.51);
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(90F));
                matrixStack.translate(0, -0.1, 0.0);
                matrixStack.scale(0.9F, 0.9F, 0.9F);

                Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, renderTypeBuffer);

                matrixStack.popPose();
            } else if(tileEntity.getBlockState() == getBlockState(BlockRegistry.PIZZA_OVEN, Direction.EAST) /*|| tileEntity.getBlockState() == getBlockState(BlockList.PIZZA_OVEN, Direction.EAST)*/) {
                matrixStack.translate(0.59, 0.54, 0.7);
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(90F));
                matrixStack.translate(0, -0.2 + 0.4 * 0.5, 0.0);
                matrixStack.scale(0.9F, 0.9F, 0.9F);

                Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, renderTypeBuffer);

                matrixStack.popPose();
            } else if(tileEntity.getBlockState() == getBlockState(BlockRegistry.PIZZA_OVEN, Direction.SOUTH) /*|| tileEntity.getBlockState() == getBlockState(BlockList.PIZZA_OVEN, Direction.SOUTH)*/) {
                matrixStack.translate(0.5, 0.54, 0.79);
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(90F));
                matrixStack.translate(0, -0.2 + 0.4 * 0.5, 0.0);
                matrixStack.scale(0.9F, 0.9F, 0.9F);

                Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, renderTypeBuffer);

                matrixStack.popPose();
            } else if(tileEntity.getBlockState() == getBlockState(BlockRegistry.PIZZA_OVEN, Direction.WEST) /*|| tileEntity.getBlockState() == getBlockState(BlockList.PIZZA_OVEN, Direction.WEST)*/) {
                matrixStack.translate(0.41, 0.54, 0.7);
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(90F));
                matrixStack.translate(0, -0.2 + 0.4 * 0.5, 0.0);
                matrixStack.scale(0.9F, 0.9F, 0.9F);


                Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, renderTypeBuffer);

                matrixStack.popPose();
            }
        }
    }

    static BlockState getBlockState(RegistryObject<Block> block, Direction direction) {
        return block.get().getBlock().defaultBlockState().setValue(PizzaOvenBlock.FACING, direction);
    }
}
