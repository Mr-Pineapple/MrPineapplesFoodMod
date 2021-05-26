package co.uk.mrpineapple.pinesfoodmod.common.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

import java.util.stream.Stream;

public class VoxelShapeUtil {
    public static final VoxelShape[] CROP_SHAPES = new VoxelShape[]{Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D)};

    public static final VoxelShape[] PIZZA_SHAPES = new VoxelShape[]{
            Stream.of(
                    Block.box(3, 0, 3, 13, 1, 13),
                    Block.box(3, 0, 2, 5, 1.25, 3),
                    Block.box(2, 0, 3, 3, 1.25, 5),
                    Block.box(2, 0, 11, 3, 1.25, 13),
                    Block.box(3, 0, 13, 5, 1.25, 14),
                    Block.box(11, 0, 2, 13, 1.25, 3),
                    Block.box(13, 0, 3, 14, 1.25, 5),
                    Block.box(13, 0, 11, 14, 1.25, 13),
                    Block.box(11, 0, 13, 13, 1.25, 14),
                    Block.box(5, 0, 2, 11, 1, 3),
                    Block.box(2, 0, 5, 3, 1, 11),
                    Block.box(5, 0, 13, 11, 1, 14),
                    Block.box(13, 0, 5, 14, 1, 11),
                    Block.box(5, 0, 1, 11, 1.25, 2),
                    Block.box(5, 0, 14, 11, 1.25, 15),
                    Block.box(1, 0, 5, 2, 1.25, 11),
                    Block.box(14, 0, 5, 15, 1.25, 11)
            ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get(),
            Stream.of(
                    Block.box(8, 0, 1, 11, 1.25, 2),
                    Block.box(11, 0, 2, 13, 1.25, 3),
                    Block.box(13, 0, 3, 14, 1.25, 5),
                    Block.box(8, 0, 2, 11, 1, 3),
                    Block.box(1, 0, 8, 2, 1.25, 11),
                    Block.box(2, 0, 8, 3, 1, 11),
                    Block.box(8, 0, 3, 13, 1, 13),
                    Block.box(13, 0, 5, 14, 1, 11),
                    Block.box(5, 0, 13, 11, 1, 14),
                    Block.box(3, 0, 8, 8, 1, 13),
                    Block.box(11, 0, 13, 13, 1.25, 14),
                    Block.box(13, 0, 11, 14, 1.25, 13),
                    Block.box(14, 0, 5, 15, 1.25, 11),
                    Block.box(5, 0, 14, 11, 1.25, 15),
                    Block.box(3, 0, 13, 5, 1.25, 14),
                    Block.box(2, 0, 11, 3, 1.25, 13)
            ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get(),
            Stream.of(
                    Block.box(1, 0, 8, 2, 1.25, 11),
                    Block.box(2, 0, 8, 3, 1, 11),
                    Block.box(8, 0, 8, 13, 1, 13),
                    Block.box(13, 0, 8, 14, 1, 11),
                    Block.box(5, 0, 13, 11, 1, 14),
                    Block.box(3, 0, 8, 8, 1, 13),
                    Block.box(11, 0, 13, 13, 1.25, 14),
                    Block.box(13, 0, 11, 14, 1.25, 13),
                    Block.box(14, 0, 8, 15, 1.25, 11),
                    Block.box(5, 0, 14, 11, 1.25, 15),
                    Block.box(3, 0, 13, 5, 1.25, 14),
                    Block.box(2, 0, 11, 3, 1.25, 13)
            ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get(),
            Stream.of(
                    Block.box(1, 0, 8, 2, 1.25, 11),
                    Block.box(2, 0, 8, 3, 1, 11),
                    Block.box(5, 0, 13, 8, 1, 14),
                    Block.box(3, 0, 8, 8, 1, 13),
                    Block.box(5, 0, 14, 8, 1.25, 15),
                    Block.box(3, 0, 13, 5, 1.25, 14),
                    Block.box(2, 0, 11, 3, 1.25, 13)
            ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get()

    };

    public static final VoxelShape PIZZA_BOARD = Stream.of(
            Block.box(3, 0, 3, 13, 1, 13),
            Block.box(3, 0, 2, 5, 1, 3),
            Block.box(2, 0, 3, 3, 1, 5),
            Block.box(2, 0, 11, 3, 1, 13),
            Block.box(3, 0, 13, 5, 1, 14),
            Block.box(11, 0, 2, 13, 1, 3),
            Block.box(13, 0, 3, 14, 1, 5),
            Block.box(13, 0, 11, 14, 1, 13),
            Block.box(11, 0, 13, 13, 1, 14),
            Block.box(5, 0, 2, 11, 1, 3),
            Block.box(2, 0, 5, 3, 1, 11),
            Block.box(5, 0, 13, 11, 1, 14),
            Block.box(13, 0, 5, 14, 1, 11),
            Block.box(5, 0, 1, 11, 1, 2),
            Block.box(5, 0, 14, 11, 1, 15),
            Block.box(1, 0, 5, 2, 1, 11),
            Block.box(14, 0, 5, 15, 1, 11),
            Block.box(0, 0, 5, 1, 1, 11),
            Block.box(14, 0, 3, 15, 1, 5),
            Block.box(14, 0, 11, 15, 1, 13),
            Block.box(1, 0, 11, 2, 1, 13),
            Block.box(1, 0, 3, 2, 1, 5),
            Block.box(13, 0, 2, 14, 1, 3),
            Block.box(13, 0, 13, 14, 1, 14),
            Block.box(2, 0, 13, 3, 1, 14),
            Block.box(2, 0, 2, 3, 1, 3),
            Block.box(5, 0, 15, 11, 1, 16),
            Block.box(5, 0, 0, 11, 1, 1),
            Block.box(11, 0, 1, 13, 1, 2),
            Block.box(11, 0, 14, 13, 1, 15),
            Block.box(3, 0, 14, 5, 1, 15),
            Block.box(3, 0, 1, 5, 1, 2),
            Block.box(15, 0, 5, 16, 1, 11)
    ).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();


}
