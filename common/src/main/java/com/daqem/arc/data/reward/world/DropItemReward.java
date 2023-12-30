package com.daqem.arc.data.reward.world;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.data.type.ActionDataType;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.serializer.IRewardSerializer;
import com.daqem.arc.api.reward.serializer.RewardSerializer;
import com.daqem.arc.api.reward.type.IRewardType;
import com.daqem.arc.api.reward.type.RewardType;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class DropItemReward extends AbstractReward {

    private final ItemStack itemStack;
    private final int amount;

    public DropItemReward(double chance, int priority, ItemStack itemStack, int amount) {
        super(chance, priority);
        this.itemStack = itemStack;
        this.amount = amount;
    }

    @Override
    public Component getDescription() {
        return getDescription(amount, itemStack.getHoverName());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        BlockPos pos = actionData.getData(ActionDataType.BLOCK_POSITION);
        if (pos != null) {
            Level level = actionData.getData(ActionDataType.WORLD);
            if (level == null) level = actionData.getPlayer().arc$getLevel();
            if (level instanceof ServerLevel serverLevel) {
                if (!itemStack.isEmpty()) {
                    for (int i = 0; i < amount; i++) {
                        ItemEntity entity = new ItemEntity(
                                serverLevel,
                                pos.getX(),
                                pos.getY(),
                                pos.getZ(),
                                itemStack.copy());
                        entity.setDefaultPickUpDelay();
                        serverLevel.addFreshEntity(entity);
                    }
                } else {
                    BlockState state = actionData.getData(ActionDataType.BLOCK_STATE);
                    if (state != null) {
                        List<ItemStack> drops = state.getDrops(
                                new LootParams.Builder(serverLevel)
                                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                                        .withParameter(LootContextParams.TOOL, actionData.getPlayer().arc$getPlayer().getMainHandItem())
                                        .withParameter(LootContextParams.BLOCK_STATE, state)
                                        .withParameter(LootContextParams.THIS_ENTITY, actionData.getPlayer().arc$getPlayer())
                        );
                        for (int i = 0; i < amount; i++) {
                            ItemStack randomDrop = drops.get(serverLevel.random.nextInt(drops.size()));
                            ItemEntity entity = new ItemEntity(
                                    serverLevel,
                                    pos.getX(),
                                    pos.getY(),
                                    pos.getZ(),
                                    randomDrop);
                            entity.setDefaultPickUpDelay();
                            serverLevel.addFreshEntity(entity);
                        }
                    }
                }
            }
        }
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return RewardType.DROP_ITEM;
    }

    @Override
    public IRewardSerializer<?> getSerializer() {
        return RewardSerializer.DROP_ITEM;
    }

    public static class Serializer implements RewardSerializer<DropItemReward> {

        @Override
        public DropItemReward fromJson(JsonObject jsonObject, double chance, int priority) {
            int amount = GsonHelper.getAsInt(jsonObject, "amount", 1);
            Item item = GsonHelper.getAsItem(jsonObject, "item", null);
            if (item == null) return new DropItemReward(chance, priority, ItemStack.EMPTY, amount);

            ItemStack itemStack = new ItemStack(item);
            CompoundTag tag = getCompoundTag(jsonObject);
            if (tag != null) itemStack.setTag(tag);
            return new DropItemReward(
                    chance,
                    priority,
                    itemStack,
                    amount);
        }

        @Override
        public DropItemReward fromNetwork(FriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new DropItemReward(
                    chance,
                    priority,
                    friendlyByteBuf.readItem(),
                    friendlyByteBuf.readInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, DropItemReward type) {
            RewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeItem(type.itemStack);
            friendlyByteBuf.writeInt(type.amount);
        }
    }
}
