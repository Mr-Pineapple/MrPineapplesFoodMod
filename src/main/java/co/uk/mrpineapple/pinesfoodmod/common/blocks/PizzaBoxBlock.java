package co.uk.mrpineapple.pinesfoodmod.common.blocks;

import co.uk.mrpineapple.pinesfoodmod.common.tileentity.PizzaBoxTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

/**
 * Author: Mr. Pineapple
 */
public class PizzaBoxBlock extends ContainerBlock {
    public static final ResourceLocation CONTENTS = new ResourceLocation("contents");
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    public static final VoxelShape OPEN_SHAPE = Stream.of(
            Block.box(1, 0, 1, 15, 0.25, 15),
            Block.box(0, 0, 0, 16, 1, 1),
            Block.box(0, 0, 15, 16, 1, 16),
            Block.box(0, 0, 1, 1, 1, 15),
            Block.box(15, 0, 1, 16, 1, 15)
    ).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();

    public static final VoxelShape CLOSED_SHAPE = Stream.of(
            Block.box(0, 1, 0, 16, 2, 1),
            Block.box(0, 1, 15, 16, 2, 16),
            Block.box(0, 1, 1, 1, 2, 15),
            Block.box(15, 1, 1, 16, 2, 15),
            Block.box(1, 1.75, 1, 15, 2, 15),
            Block.box(15, 0, 1, 16, 1, 15),
            Block.box(1, 0, 1, 15, 0.25, 15),
            Block.box(0, 0, 0, 16, 1, 1),
            Block.box(0, 0, 15, 16, 1, 16),
            Block.box(0, 0, 1, 1, 1, 15)
    ).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();

    public PizzaBoxBlock() {
        super(AbstractBlock.Properties.of(Material.BAMBOO).strength(0.5F).dynamicShape().noOcclusion());
        this.registerDefaultState(this.getStateDefinition().any().setValue(OPEN, false).setValue(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader world) {
        return new PizzaBoxTileEntity();
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        if(state.getValue(OPEN)) {
            return OPEN_SHAPE;
        } else {
            return CLOSED_SHAPE;
        }
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if(world.isClientSide || player.isSpectator()) {
            return ActionResultType.SUCCESS;
        } else {
            TileEntity tileEntity = world.getBlockEntity(pos);
            ItemStack stack = player.getItemInHand(hand);
            if(tileEntity instanceof PizzaBoxTileEntity) {
                if(stack.isEmpty()) {
                    if(player.isCrouching()) {
                        ((PizzaBoxTileEntity)tileEntity).removeItem(0, player);
                    } else {
                        world.setBlock(pos,state.cycle(OPEN), 2);
                    }
                }
                if(((PizzaBoxTileEntity)tileEntity).addItem(stack)) {
                    stack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.PASS;
            }
        }
    }

    @Override
    public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if(tileEntity instanceof PizzaBoxTileEntity) {
            PizzaBoxTileEntity pizzaBoxTileEntity = (PizzaBoxTileEntity) tileEntity;
            if(world.isClientSide && player.isCreative() && !pizzaBoxTileEntity.isEmpty()) {
                return;
            } else {
                pizzaBoxTileEntity.unpackLootTable(player);
            }
        }
        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        TileEntity tileentity = builder.getOptionalParameter(LootParameters.BLOCK_ENTITY);
        if (tileentity instanceof PizzaBoxTileEntity) {
            PizzaBoxTileEntity pizzaboxtileentity = (PizzaBoxTileEntity)tileentity;
            builder = builder.withDynamicDrop(CONTENTS, (p_220168_1_, p_220168_2_) -> {
                for(int i = 0; i < pizzaboxtileentity.getContainerSize(); ++i) {
                    p_220168_2_.accept(pizzaboxtileentity.getItem(i));
                }
            });
        }
        return super.getDrops(state, builder);
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if(state.getBlock() != newState.getBlock()) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if(tileEntity instanceof PizzaBoxTileEntity) {
                world.updateNeighbourForOutputSignal(pos, state.getBlock());
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    /**
     * Other methods to be added for itemstack information, positions and items
     */

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, OPEN);
    }
}
