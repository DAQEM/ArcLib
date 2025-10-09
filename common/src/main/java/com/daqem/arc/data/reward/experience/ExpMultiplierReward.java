package com.daqem.arc.data.reward.experience;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;

public class ExpMultiplierReward extends AbstractReward {

    private final int multiplier;

    public ExpMultiplierReward(double chance, int priority, int multiplier) {
        super(chance, priority);
        this.multiplier = multiplier;
    }

    @Override
    public Component getDescription() {
        return getDescription(multiplier);
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Level level = actionData.getData(IActionDataType.WORLD);
        if (level == null) {
            level = actionData.getPlayer().arc$getLevel();
        }
        if (level != null) {
            Integer exp = actionData.getData(IActionDataType.EXP_DROP);
            if (exp != null && (exp * multiplier) - exp > 0) {
                BlockPos blockPos = actionData.getData(IActionDataType.BLOCK_POSITION);
                if (blockPos != null) {
                    level.addFreshEntity(
                            new ExperienceOrb(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), (exp * multiplier) - exp));
                }
            }
        }
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.EXP_MULTIPLIER;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public static class Serializer implements IRewardSerializer<ExpMultiplierReward> {

        @Override
        public ExpMultiplierReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new ExpMultiplierReward(
                    chance,
                    priority,
                    GsonHelper.getAsInt(jsonObject, "multiplier"));
        }

        @Override
        public ExpMultiplierReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new ExpMultiplierReward(
                    chance,
                    priority,
                    friendlyByteBuf.readInt());
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ExpMultiplierReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeInt(type.multiplier);
        }
    }
}
