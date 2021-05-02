package co.uk.mrpineapple.pinesfoodmod.common.blocks;

import co.uk.mrpineapple.pinesfoodmod.common.util.VoxelShapeUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import java.util.function.Supplier;

public class CropBase extends CropsBlock {
    private final Supplier<Item> itemSupplier;
    private final Supplier<Item> seedItemSupplier;

    public CropBase(Supplier<Item> itemSupplier, Supplier<Item> seedItemSupplier) {
            super(AbstractBlock.Properties.copy(Blocks.WHEAT));
            this.itemSupplier = itemSupplier;
            this.seedItemSupplier = seedItemSupplier;
    }

    @Override
    protected IItemProvider getBaseSeedId() {
        return seedItemSupplier.get();
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(seedItemSupplier.get());
    }

    @Override
    public VoxelShape getShape(BlockState blockstate, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return VoxelShapeUtil.CROP_SHAPES[blockstate.getValue(this.getAgeProperty())];
    }
}
