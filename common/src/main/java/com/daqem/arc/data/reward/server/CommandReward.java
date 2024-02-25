package com.daqem.arc.data.reward.server;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.data.type.ActionDataType;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IReward;
import com.daqem.arc.api.reward.serializer.IRewardSerializer;
import com.daqem.arc.api.reward.serializer.RewardSerializer;
import com.daqem.arc.api.reward.type.IRewardType;
import com.daqem.arc.api.reward.type.RewardType;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CommandReward extends AbstractReward {

    private final String command;

    public CommandReward(double chance, int priority, String command) {
        super(chance, priority);
        this.command = command;
    }

    @Override
    public IRewardType<?> getType() {
        return RewardType.COMMAND;
    }

    @Override
    public IRewardSerializer<? extends IReward> getSerializer() {
        return RewardSerializer.COMMAND;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        ArcPlayer player = actionData.getPlayer();
        if (player instanceof ServerPlayer serverPlayer) {

            String command = this.command
                    .replace("%player%", serverPlayer.getGameProfile().getName())
                    .replace("%uuid%", serverPlayer.getGameProfile().getId().toString())
                    .replace("%world%", serverPlayer.level().dimension().location().toString())
                    .replace("%player_location%", serverPlayer.blockPosition().getX() + " " + serverPlayer.blockPosition().getY() + " " + serverPlayer.blockPosition().getZ());

            BlockPos blockPos = actionData.getData(ActionDataType.BLOCK_POSITION);
            if (blockPos != null) {
                command = command.replace("%block_location%", blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ());
            }

            Item item = actionData.getData(ActionDataType.ITEM);
            if (item != null) {
                command = command.replace("%item%", BuiltInRegistries.ITEM.getKey(item).toString());
            }

            ItemStack itemStack = actionData.getData(ActionDataType.ITEM_STACK);
            if (itemStack != null) {
                command = command.replace("%item_stack%", BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString());

                CompoundTag tag = itemStack.getTag();
                if (tag != null) {
                    command = command.replace("%item_stack_with_tag%", BuiltInRegistries.ITEM.getKey(itemStack.getItem()) + tag.toString());
                } else {
                    command = command.replace("%item_stack_with_tag%", BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString());
                }
            }

            MinecraftServer server = serverPlayer.getServer();
            if (server != null) {
                server.getCommands().performPrefixedCommand(serverPlayer.createCommandSourceStack(), command);
            }
        }
        return new ActionResult();
    }

    public static class Serializer implements RewardSerializer<CommandReward> {

        @Override
        public CommandReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new CommandReward(
                    chance,
                    priority,
                    GsonHelper.getAsString(jsonObject, "command"));
        }

        @Override
        public CommandReward fromNetwork(FriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new CommandReward(
                    chance,
                    priority,
                    friendlyByteBuf.readUtf());
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, CommandReward type) {
            RewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeUtf(type.command);
        }
    }
}
