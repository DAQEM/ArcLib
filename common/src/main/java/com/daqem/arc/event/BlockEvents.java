package com.daqem.arc.event;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.data.IActionDataType;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEvents {

    public static void registerEvents() {
        BlockEvent.PLACE.register((level, pos, state, placer) -> {
            if (placer instanceof ArcServerPlayer arcServerPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, IActionType.PLACE_BLOCK)
                        .withData(IActionDataType.BLOCK_STATE, state)
                        .withData(IActionDataType.BLOCK_POSITION, pos)
                        .withData(IActionDataType.WORLD, level)
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.interruptFalse();
                }

                if (state.getBlock() instanceof CropBlock) {
                    ActionResult actionResult1 = onPlantCrop(arcServerPlayer, state, pos, level);
                    if (actionResult1.shouldCancelAction()) {
                        return EventResult.interruptFalse();
                    }
                }
                arcServerPlayer.arc$getBlockPosCache().add(pos);
            }
            return EventResult.pass();
        });
        BlockEvent.BREAK.register((level, pos, state, player, xp) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, IActionType.BREAK_BLOCK)
                        .withData(IActionDataType.BLOCK_STATE, state)
                        .withData(IActionDataType.BLOCK_POSITION, pos)
                        .withData(IActionDataType.EXP_DROP, xp == null ? 0 : xp.get())
                        .withData(IActionDataType.WORLD, level)
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.interruptFalse();
                }

                if (state.getBlock() instanceof CropBlock) {
                    ActionResult actionResult1 = onHarvestCrop(arcServerPlayer, state, pos, level);
                    if (actionResult1.shouldCancelAction()) {
                        return EventResult.interruptFalse();
                    }
                }
            }
            return EventResult.pass();
        });
    }

    public static ActionResult onBlockInteract(ArcServerPlayer player, BlockState state, BlockPos pos, Level level) {
        return new ActionDataBuilder(player, IActionType.INTERACT_BLOCK)
                .withData(IActionDataType.BLOCK_STATE, state)
                .withData(IActionDataType.BLOCK_POSITION, pos)
                .withData(IActionDataType.WORLD, level)
                .build()
                .sendToAction();
    }

    public static ActionResult onPlantCrop(ArcServerPlayer player, BlockState state, BlockPos pos, Level level) {
        return new ActionDataBuilder(player, IActionType.PLANT_CROP)
                .withData(IActionDataType.BLOCK_STATE, state)
                .withData(IActionDataType.BLOCK_POSITION, pos)
                .withData(IActionDataType.WORLD, level)
                .build()
                .sendToAction();
    }

    public static ActionResult onHarvestCrop(ArcServerPlayer player, BlockState state, BlockPos pos, Level level) {
        return new ActionDataBuilder(player, IActionType.HARVEST_CROP)
                .withData(IActionDataType.BLOCK_STATE, state)
                .withData(IActionDataType.BLOCK_POSITION, pos)
                .withData(IActionDataType.WORLD, level)
                .build()
                .sendToAction();
    }
}
