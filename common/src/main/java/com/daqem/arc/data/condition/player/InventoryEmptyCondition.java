package com.daqem.arc.data.condition.player;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class InventoryEmptyCondition extends AbstractCondition {

    public InventoryEmptyCondition(boolean inverted) {
        super(inverted);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        return player.getInventory().isEmpty();
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.INVENTORY_EMPTY;
    }

    public static class Serializer implements IConditionSerializer<InventoryEmptyCondition> {

        @Override
        public InventoryEmptyCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new InventoryEmptyCondition(inverted);
        }

        @Override
        public InventoryEmptyCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new InventoryEmptyCondition(inverted);
        }
    }
}