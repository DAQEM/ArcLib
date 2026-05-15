package com.daqem.arc.data.condition.player;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class InventoryFullCondition extends AbstractCondition {

    public InventoryFullCondition(boolean inverted) {
        super(inverted);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        return player.getInventory().getFreeSlot() == -1;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.INVENTORY_FULL;
    }

    public static class Serializer implements IConditionSerializer<InventoryFullCondition> {

        @Override
        public InventoryFullCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new InventoryFullCondition(inverted);
        }

        @Override
        public InventoryFullCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new InventoryFullCondition(inverted);
        }
    }
}