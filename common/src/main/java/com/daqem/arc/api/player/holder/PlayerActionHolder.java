package com.daqem.arc.api.player.holder;

import com.daqem.arc.api.action.holder.AbstractActionHolder;
import com.daqem.arc.api.action.holder.serializer.IActionHolderSerializer;
import com.daqem.arc.api.action.holder.type.ActionHolderType;
import com.daqem.arc.api.action.holder.type.IActionHolderType;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class PlayerActionHolder extends AbstractActionHolder {

    public PlayerActionHolder(ResourceLocation location) {
        super(location);
    }

    @Override
    public IActionHolderType<?> getType() {
        return ActionHolderType.PLAYER_ACTION_TYPE;
    }

    public static class Serializer implements IActionHolderSerializer<PlayerActionHolder> {

        @Override
        public PlayerActionHolder fromJson(ResourceLocation location, JsonObject jsonObject, int i) {
            return new PlayerActionHolder(location);
        }

        @Override
        public PlayerActionHolder fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf, int i) {
            return new PlayerActionHolder(location);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, PlayerActionHolder type) {
            IActionHolderSerializer.super.toNetwork(friendlyByteBuf, type);
        }
    }
}
