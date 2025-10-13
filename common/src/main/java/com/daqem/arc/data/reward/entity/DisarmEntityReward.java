package com.daqem.arc.data.reward.entity;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.target.ArcItemTarget;
import com.daqem.arc.model.target.ArcPositionTarget;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class DisarmEntityReward extends AbstractReward {

    private final ArcItemTarget itemTarget;
    private final ArcPositionTarget positionTarget;

    public DisarmEntityReward(double chance, int priority, ArcItemTarget itemTarget, ArcPositionTarget positionTarget) {
        super(chance, priority);
        this.itemTarget = itemTarget;
        this.positionTarget = positionTarget;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        if (actionData.getPlayer() instanceof ServerPlayer serverPlayer) {
            if (actionData.getData(IActionDataType.ENTITY) instanceof LivingEntity target) {
                ItemStack stack = itemTarget.getItemStack(actionData, target);
                EquipmentSlot slot = itemTarget.getEquipmentSlot(actionData, target);
                if (!stack.isEmpty() && slot != null) {
                    Vec3 position = positionTarget.getPosition(actionData);
                    if (position != null) {
                        serverPlayer.level().addFreshEntity(new ItemEntity(serverPlayer.level(), position.x, position.y, position.z, stack));
                        target.setItemSlot(slot, ItemStack.EMPTY);
                    }
                }
            }
        }
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.DISARM_ENTITY;
    }

    public static class Serializer implements IRewardSerializer<DisarmEntityReward> {

        @Override
        public DisarmEntityReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new DisarmEntityReward(
                    chance,
                    priority,
                    getItemTarget(jsonObject, "item_target", ArcItemTarget.MAIN_HAND),
                    getPositionTarget(jsonObject, "position_target", ArcPositionTarget.ENTITY)
            );
        }

        @Override
        public DisarmEntityReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new DisarmEntityReward(
                    chance,
                    priority,
                    friendlyByteBuf.readEnum(ArcItemTarget.class),
                    friendlyByteBuf.readEnum(ArcPositionTarget.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, DisarmEntityReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeEnum(type.itemTarget);
            friendlyByteBuf.writeEnum(type.positionTarget);
        }
    }
}