package com.daqem.arc.data.reward.experience;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;

public class ExpMultiplierReward extends AbstractReward {

    private final INumberProvider multiplier;

    public ExpMultiplierReward(double chance, int priority, INumberProvider multiplier) {
        super(chance, priority);
        this.multiplier = multiplier;
    }

    @Override
    public Component getDescription() {
        return getDescription(multiplier.getDescription());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Level level = actionData.getData(IActionDataType.WORLD);
        if (level == null) level = actionData.getPlayer().arc$getLevel();

        if (level != null) {
            Integer exp = actionData.getData(IActionDataType.EXP_DROP);
            if (exp != null) {
                double resolvedMultiplier = multiplier.resolve(actionData);
                int extraExp = (int) Math.round((exp * resolvedMultiplier) - exp);
                if (extraExp > 0) {
                    BlockPos blockPos = actionData.getData(IActionDataType.BLOCK_POSITION);
                    if (blockPos != null) {
                        level.addFreshEntity(new ExperienceOrb(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), extraExp));
                    }
                }
            }
        }
        return new ActionResult();
    }

    public INumberProvider getMultiplier() {
        return multiplier;
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.EXP_MULTIPLIER;
    }

    public static class Serializer implements IRewardSerializer<ExpMultiplierReward> {
        @Override
        public ExpMultiplierReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new ExpMultiplierReward(chance, priority, getNumberProvider(jsonObject, "multiplier", new ConstantNumberProvider(1.0)));
        }

        @Override
        public ExpMultiplierReward fromNetwork(RegistryFriendlyByteBuf buf, double chance, int priority) {
            return new ExpMultiplierReward(chance, priority, INumberProviderSerializer.fromNetworkStatic(buf));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, ExpMultiplierReward type) {
            IRewardSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.multiplier, buf);
        }
    }
}