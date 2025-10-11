package com.daqem.arc.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.function.Supplier;

public interface ArcBlockEvent {

    Event<BreakBlock> BREAK_BLOCK = EventFactory.createEventResult(BreakBlock.class);
    Event<PlaceBlock> PLACE_BLOCK = EventFactory.createEventResult(PlaceBlock.class);
    Event<RightClickBlock> RIGHT_CLICK_BLOCK = EventFactory.createEventResult(RightClickBlock.class);

    Event<PlantCrop> PLANT_CROP = EventFactory.createEventResult(PlantCrop.class);
    Event<HarvestCrop> HARVEST_CROP = EventFactory.createEventResult(HarvestCrop.class);

    Event<GetDestroySpeed> GET_DESTROY_SPEED = EventFactory.createEventResult(GetDestroySpeed.class);

    interface BreakBlock {
        EventResult onBreakBlock(ServerLevel level, BlockPos blockPos, BlockState blockState, ServerPlayer serverPlayer, Supplier<Integer> xp);
    }

    interface PlaceBlock {
        EventResult onPlaceBlock(Level level, BlockPos blockPos, BlockState blockState, Entity placer);
    }

    interface RightClickBlock {
        EventResult onRightClickBlock(ItemStack itemStack, Level level, Player player, InteractionHand hand, BlockState state, BlockPos blockPos);
    }

    interface PlantCrop {
        EventResult onPlantCrop(Level level, BlockPos blockPos, BlockState blockState, Player planter);
    }

    interface HarvestCrop {
        EventResult onHarvestCrop(ServerLevel level, BlockPos blockPos, BlockState blockState, ServerPlayer serverPlayer, Supplier<Integer> xp);
    }

    interface GetDestroySpeed {
        EventResult onGetDestroySpeed(Player player, BlockState blockState, BlockPos blockPos, ItemStack itemStack, MutableFloat speed);
    }
}
