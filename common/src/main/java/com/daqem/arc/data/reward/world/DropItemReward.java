package com.daqem.arc.data.reward.world;

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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class DropItemReward extends AbstractReward {

    private final ItemStack itemStack;

    public DropItemReward(double chance, int priority, ItemStack itemStack) {
        super(chance, priority);
        this.itemStack = itemStack;
    }

    @Override
    public Component getDescription() {
        return getDescription(itemStack.getCount(), itemStack.getHoverName());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        BlockPos pos = actionData.getData(IActionDataType.BLOCK_POSITION);
        if (pos != null) {
            Level level = actionData.getData(IActionDataType.WORLD);
            if (level == null) level = actionData.getPlayer().arc$getLevel();
            if (level instanceof ServerLevel serverLevel) {
                if (!itemStack.isEmpty()) {
                    for (int i = 0; i < itemStack.getCount(); i++) {
                        ItemEntity entity = new ItemEntity(
                                serverLevel,
                                pos.getX(),
                                pos.getY(),
                                pos.getZ(),
                                itemStack.copyWithCount(1));
                        entity.setDefaultPickUpDelay();
                        serverLevel.addFreshEntity(entity);
                    }
                } else {
                    BlockState state = actionData.getData(IActionDataType.BLOCK_STATE);
                    if (state != null) {
                        List<ItemStack> drops = state.getDrops(
                                new LootParams.Builder(serverLevel)
                                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                                        .withParameter(LootContextParams.TOOL, actionData.getPlayer().arc$getPlayer().getMainHandItem())
                                        .withParameter(LootContextParams.BLOCK_STATE, state)
                                        .withParameter(LootContextParams.THIS_ENTITY, actionData.getPlayer().arc$getPlayer())
                        );
                        for (int i = 0; i < itemStack.getCount(); i++) {
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
        return IRewardType.DROP_ITEM;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public static class Serializer implements IRewardSerializer<DropItemReward> {

        @Override
        public DropItemReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new DropItemReward(chance, priority, getItemStack(jsonObject, "item"));
        }

        @Override
        public DropItemReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new DropItemReward(
                    chance,
                    priority,
                    ItemStack.STREAM_CODEC.decode(friendlyByteBuf));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, DropItemReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            ItemStack.STREAM_CODEC.encode(friendlyByteBuf, type.itemStack);
        }
    }
}
