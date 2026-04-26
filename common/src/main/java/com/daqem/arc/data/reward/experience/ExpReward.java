package com.daqem.arc.data.reward.experience;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ExpReward extends AbstractReward {

    private final INumberProvider min;
    private final INumberProvider max;

    public ExpReward(double chance, int priority, INumberProvider min, INumberProvider max) {
        super(chance, priority);
        this.min = min;
        this.max = max;
    }

    @Override
    public Component getDescription() {
        return getDescription(min.toString(), max.toString());
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.EXP;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        ArcPlayer player = actionData.getPlayer();
        int resolvedMin = (int) Math.round(min.resolve(actionData));
        int resolvedMax = (int) Math.round(max.resolve(actionData));

        if (resolvedMin > resolvedMax) {
            int temp = resolvedMin;
            resolvedMin = resolvedMax;
            resolvedMax = temp;
        }

        int exp = ((ServerPlayer) player).getRandom().nextInt(resolvedMin, resolvedMax + 1);
        ((ServerPlayer) player).giveExperiencePoints(exp);
        return new ActionResult();
    }

    public INumberProvider getMin() {
        return min;
    }

    public INumberProvider getMax() {
        return max;
    }

    public static class Serializer implements IRewardSerializer<ExpReward> {
        @Override
        public ExpReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new ExpReward(
                    chance, priority,
                    getNumberProvider(jsonObject, "min", new ConstantNumberProvider(0)),
                    getNumberProvider(jsonObject, "max", new ConstantNumberProvider(0))
            );
        }

        @Override
        public ExpReward fromNetwork(RegistryFriendlyByteBuf buf, double chance, int priority) {
            return new ExpReward(chance, priority, INumberProviderSerializer.fromNetworkStatic(buf), INumberProviderSerializer.fromNetworkStatic(buf));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, ExpReward type) {
            IRewardSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.min, buf);
            INumberProviderSerializer.toNetwork(type.max, buf);
        }
    }
}