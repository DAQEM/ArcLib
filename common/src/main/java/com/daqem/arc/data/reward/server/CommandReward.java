package com.daqem.arc.data.reward.server;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
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
        return IRewardType.COMMAND;
    }

    @Override
    public Component getDescription() {
        return getDescription(command);
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        ArcPlayer player = actionData.getPlayer();
        if (player instanceof ServerPlayer serverPlayer) {

            String command = this.command
                    .replace("%player%", serverPlayer.getGameProfile().name())
                    .replace("%uuid%", serverPlayer.getGameProfile().id().toString())
                    .replace("%world%", serverPlayer.level().dimension().location().toString())
                    .replace("%player_location%", serverPlayer.blockPosition().getX() + " " + serverPlayer.blockPosition().getY() + " " + serverPlayer.blockPosition().getZ());

            BlockPos blockPos = actionData.getData(IActionDataType.BLOCK_POSITION);
            if (blockPos != null) {
                command = command.replace("%block_location%", blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ());
            }

            Item item = actionData.getData(IActionDataType.ITEM);
            if (item != null) {
                command = command.replace("%item%", BuiltInRegistries.ITEM.getKey(item).toString());
            }

            ItemStack itemStack = actionData.getData(IActionDataType.ITEM_STACK);
            if (itemStack != null) {
                command = command.replace("%item_stack%", BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString());
            }

            MinecraftServer server = serverPlayer.level().getServer();
            server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), command);
        }
        return new ActionResult();
    }

    public String getCommand() {
        return command;
    }

    public static class Serializer implements IRewardSerializer<CommandReward> {

        @Override
        public CommandReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new CommandReward(
                    chance,
                    priority,
                    GsonHelper.getAsString(jsonObject, "command"));
        }

        @Override
        public CommandReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new CommandReward(
                    chance,
                    priority,
                    friendlyByteBuf.readUtf());
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, CommandReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeUtf(type.command);
        }
    }
}
