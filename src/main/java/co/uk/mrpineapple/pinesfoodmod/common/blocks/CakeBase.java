package co.uk.mrpineapple.pinesfoodmod.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class CakeBase extends CakeBlock {
    int nutrition;
    float saturationModifier;

    public CakeBase(Properties properties, Food food) {
        super(properties);
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
            player.getFoodData().eat(2, 0.1f);
            int i = state.getValue(BITES);
            if(i < 6) {
                world.setBlock(pos, state.setValue(BITES, i + 1), 3);
            } else {
                world.removeBlock(pos, false);
            }
            return ActionResultType.SUCCESS;
        }
    }
}
