package co.uk.mrpineapple.pinesfoodmod.common.blocks;

import co.uk.mrpineapple.pinesfoodmod.common.util.VoxelShapeUtil;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;


public class PizzaBlock extends Block {
    public static final IntegerProperty SLICES = IntegerProperty.create("slices", 0, 3);
    int nutrition;
    float saturationModifier;

    public PizzaBlock(Food food) {
        super(AbstractBlock.Properties.of(Material.CAKE).strength(1.0f).sound(SoundType.WOOL));
        this.registerDefaultState(this.getStateDefinition().any().setValue(SLICES, 0));
        this.nutrition = food.getNutrition();
        this.saturationModifier = food.getSaturationModifier();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rtr) {
        if(world.isClientSide) {
            ItemStack itemStack = player.getItemInHand(hand);
            if(this.eat(world, pos, state, player).consumesAction()) {
                return ActionResultType.SUCCESS;
            }
            if(itemStack.isEmpty()) {
                return ActionResultType.CONSUME;
            }
        }

        return this.eat(world, pos, state, player);
    }

    private ActionResultType eat(IWorld world, BlockPos pos, BlockState state, PlayerEntity player) {
        if(!player.canEat(false)) {
            return ActionResultType.PASS;
        } else {
            player.awardStat(Stats.EAT_CAKE_SLICE);
            player.getFoodData().eat(nutrition, saturationModifier);
            int i = state.getValue(SLICES);
            if(i < 3) {
                world.setBlock(pos, state.setValue(SLICES, i + 1), 3);
            } else {
                world.removeBlock(pos, false);
            }
            return ActionResultType.SUCCESS;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return VoxelShapeUtil.PIZZA_SHAPES[state.getValue(SLICES)];
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).getMaterial().isSolid();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState newState, IWorld world, BlockPos pos, BlockPos newPos) {
        return dir == Direction.DOWN && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, dir, newState, world, pos, newPos);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SLICES);
    }
}
