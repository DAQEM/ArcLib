package com.daqem.arc.data.reward.world;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.event.NextTickScheduler;
import com.daqem.arc.model.ArcBlockState;
import com.daqem.arc.model.target.ArcPositionTarget;
import com.daqem.knot.api.platform.KnotPlatform;
import com.daqem.knot.api.platform.Platform;
import com.daqem.knot.events.EventResult;
import com.daqem.knot.events.common.block.BlockEvent;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class SetBlockReward extends AbstractReward {

    private final ArcBlockState blockState;
    private final ArcPositionTarget target;
    private final boolean placeAsPlayer;

    public SetBlockReward(double chance, int priority, ArcBlockState blockState, ArcPositionTarget target, boolean placeAsPlayer) {
        super(chance, priority);
        this.blockState = blockState;
        this.target = target;
        this.placeAsPlayer = placeAsPlayer;
    }

    @Override
    public Component getDescription() {
        return getDescription(blockState.block().getName());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Vec3 positionVector = target.getPosition(actionData);
        if (positionVector != null) {
            BlockPos pos = BlockPos.containing(positionVector);
            Level level = actionData.getPlayer().arc$getLevel();
            BlockState state = blockState.create();
            Player player = actionData.getPlayer().arc$getPlayer();

            boolean shouldPlace = true;

            if (placeAsPlayer) {
                BlockState stateBefore = level.getBlockState(pos);
                EventResult result = BlockEvent.PLACE_BLOCK.invoker().onPlaceBlock(level, pos, state, player);
                BlockState stateAfter = level.getBlockState(pos);

                if (result == EventResult.INTERRUPT_FALSE || !stateBefore.equals(stateAfter)) {
                    shouldPlace = false;
                }
            }

            if (shouldPlace) {
                if (Platform.INFO.getPlatform() == KnotPlatform.FABRIC && actionData.getActionType().equals(IActionType.PLACE_BLOCK)) NextTickScheduler.schedule(() -> placeBlock(level, pos, state, player));
                else placeBlock(level, pos, state, player);
            }
        }
        return new ActionResult();
    }

    private void placeBlock(Level level, BlockPos pos, BlockState state, Player player) {
        if (level.setBlock(pos, state, 3)) {
            if (placeAsPlayer) {
                level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, state));
                ItemStack stack = new ItemStack(state.getBlock());
                state.getBlock().setPlacedBy(level, pos, state, player, stack);
            }
        }
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.SET_BLOCK;
    }

    public static class Serializer implements IRewardSerializer<SetBlockReward> {

        @Override
        public SetBlockReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new SetBlockReward(
                    chance,
                    priority,
                    getBlockState(jsonObject, "block"),
                    getPositionTarget(jsonObject, "target", ArcPositionTarget.BLOCK),
                    GsonHelper.getAsBoolean(jsonObject, "place_as_player", true)
            );
        }

        @Override
        public SetBlockReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new SetBlockReward(
                    chance,
                    priority,
                    ArcBlockState.STREAM_CODEC.decode(friendlyByteBuf),
                    friendlyByteBuf.readEnum(ArcPositionTarget.class),
                    friendlyByteBuf.readBoolean()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, SetBlockReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            ArcBlockState.STREAM_CODEC.encode(friendlyByteBuf, type.blockState);
            friendlyByteBuf.writeEnum(type.target);
            friendlyByteBuf.writeBoolean(type.placeAsPlayer);
        }
    }
}