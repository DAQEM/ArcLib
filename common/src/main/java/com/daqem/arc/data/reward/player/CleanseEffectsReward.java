package com.daqem.arc.data.reward.player;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;

public class CleanseEffectsReward extends AbstractReward {

    private final boolean removePositive;
    private final boolean removeNegative;
    private final boolean removeNeutral;

    public CleanseEffectsReward(double chance, int priority, boolean removePositive, boolean removeNegative, boolean removeNeutral) {
        super(chance, priority);
        this.removePositive = removePositive;
        this.removeNegative = removeNegative;
        this.removeNeutral = removeNeutral;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        player.getActiveEffects().stream()
                .filter(effect -> (removePositive && effect.getEffect().value().getCategory() == MobEffectCategory.BENEFICIAL)
                        || (removeNegative && effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL)
                        || (removeNeutral && effect.getEffect().value().getCategory() == MobEffectCategory.NEUTRAL))
                .forEach(effect -> player.removeEffect(effect.getEffect()));
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.CLEANSE_EFFECTS;
    }

    public static class Serializer implements IRewardSerializer<CleanseEffectsReward> {

        @Override
        public CleanseEffectsReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new CleanseEffectsReward(
                    chance,
                    priority,
                    GsonHelper.getAsBoolean(jsonObject, "remove_positive", false),
                    GsonHelper.getAsBoolean(jsonObject, "remove_negative", true),
                    GsonHelper.getAsBoolean(jsonObject, "remove_neutral", false)
            );
        }

        @Override
        public CleanseEffectsReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new CleanseEffectsReward(
                    chance,
                    priority,
                    friendlyByteBuf.readBoolean(),
                    friendlyByteBuf.readBoolean(),
                    friendlyByteBuf.readBoolean()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, CleanseEffectsReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeBoolean(type.removePositive);
            friendlyByteBuf.writeBoolean(type.removeNegative);
            friendlyByteBuf.writeBoolean(type.removeNeutral);
        }
    }
}