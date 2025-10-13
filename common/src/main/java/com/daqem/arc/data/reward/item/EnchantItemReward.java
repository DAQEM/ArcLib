package com.daqem.arc.data.reward.item;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.ArcEnchantment;
import com.daqem.arc.model.target.ArcItemTarget;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class EnchantItemReward extends AbstractReward {

    private final List<ArcEnchantment> enchantments;
    private final ArcItemTarget itemTarget;

    public EnchantItemReward(double chance, int priority, List<ArcEnchantment> enchantment, ArcItemTarget itemTarget) {
        super(chance, priority);
        this.enchantments = enchantment;
        this.itemTarget = itemTarget;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        ItemStack stack = itemTarget.getItemStack(actionData, player);
        if (stack.isEnchantable()) {
            for (ArcEnchantment arcEnchantment : enchantments) {
                stack.enchant(arcEnchantment.enchantment(), arcEnchantment.level());
            }
        }
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.ENCHANT_HELD_ITEM;
    }

    public static class Serializer implements IRewardSerializer<EnchantItemReward> {

        @Override
        public EnchantItemReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new EnchantItemReward(
                    chance,
                    priority,
                    getEnchantments(jsonObject, "enchantments"),
                    getItemTarget(jsonObject, "target")
            );
        }

        @Override
        public EnchantItemReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new EnchantItemReward(
                    chance,
                    priority,
                    friendlyByteBuf.readList(buf ->
                            ArcEnchantment.STREAM_CODEC.decode((RegistryFriendlyByteBuf) buf)),
                    friendlyByteBuf.readEnum(ArcItemTarget.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, EnchantItemReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeCollection(type.enchantments, (buf, enchantment) ->
                    ArcEnchantment.STREAM_CODEC.encode((RegistryFriendlyByteBuf) buf, enchantment));
            friendlyByteBuf.writeEnum(type.itemTarget);
        }
    }
}