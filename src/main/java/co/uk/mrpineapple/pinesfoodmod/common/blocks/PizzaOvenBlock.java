package co.uk.mrpineapple.pinesfoodmod.common.blocks;

import co.uk.mrpineapple.pinesfoodmod.common.recipe.PizzaOvenRecipe;
import co.uk.mrpineapple.pinesfoodmod.common.tileentity.PizzaOvenTileEntity;
import co.uk.mrpineapple.pinesfoodmod.common.util.VoxelShapeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ISidedInventoryProvider;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class PizzaOvenBlock extends Block implements ISidedInventoryProvider {
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final BooleanProperty COOKING = BooleanProperty.create("cooking");

    public PizzaOvenBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(COOKING, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        switch(state.getValue(FACING)) {
            case NORTH:
                return VoxelShapeUtil.PIZZA_OVEN;
            case EAST:
                return VoxelShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, VoxelShapeUtil.PIZZA_OVEN);
            case SOUTH:
                return VoxelShapeUtil.rotateShape(Direction.NORTH, Direction.SOUTH, VoxelShapeUtil.PIZZA_OVEN);
            case WEST:
                return VoxelShapeUtil.rotateShape(Direction.NORTH, Direction.WEST, VoxelShapeUtil.PIZZA_OVEN);
            default:
                return VoxelShapeUtil.PIZZA_OVEN;
        }
    }

    @Override
    public void onRemove(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if(oldState.getBlock() != newState.getBlock()) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if(tileEntity instanceof PizzaOvenTileEntity) {
                PizzaOvenTileEntity grillTileEntity = (PizzaOvenTileEntity) tileEntity;
                InventoryHelper.dropContents(world, pos, grillTileEntity.getOven());
                InventoryHelper.dropContents(world, pos, grillTileEntity.getFuel());
            }
            super.onRemove(oldState, world, pos, newState, isMoving);
        }
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if(!world.isClientSide && result.getDirection() == Direction.UP || result.getDirection() == state.getValue(FACING)) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof PizzaOvenTileEntity) {
                PizzaOvenTileEntity ovenTileEntity = (PizzaOvenTileEntity) tileEntity;
                ItemStack stack = player.getItemInHand(hand);
                if (stack.getItem() == Items.COAL || stack.getItem() == Items.CHARCOAL) {
                    if (ovenTileEntity.addFuel(stack)) {
                        stack.shrink(1);
                    }
                } else if (!stack.isEmpty()) {
                    Optional<PizzaOvenRecipe> optional = ovenTileEntity.findMatchingRecipe(stack);
                    if (optional.isPresent()) {
                        PizzaOvenRecipe recipe = optional.get();
                        if (ovenTileEntity.addItem(stack, 0, recipe.getCookingTime(), recipe.getExperience())) {
                            if (!player.abilities.instabuild) {
                                stack.shrink(1);
                            }
                        }
                    }
                } else {
                    ovenTileEntity.removeItem(player);
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PizzaOvenTileEntity();
    }

    @Override
    public ISidedInventory getContainer(BlockState state, IWorld world, BlockPos pos) {
        if(!world.isClientSide()) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if(tileEntity instanceof PizzaOvenTileEntity) {
                return (PizzaOvenTileEntity) tileEntity;
            }
        }
        return null;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getStateDefinition().any().setValue(FACING, context.getHorizontalDirection().getOpposite());
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
        builder.add(FACING, COOKING);
    }
}
