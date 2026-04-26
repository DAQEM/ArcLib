package com.daqem.arc.data.reward.block;

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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BlockDropMultiplierReward extends AbstractReward {

    private final INumberProvider multiplier;

    public BlockDropMultiplierReward(double chance, int priority, INumberProvider multiplier) {
        super(chance, priority);
        this.multiplier = multiplier;
    }

    @Override
    public Component getDescription() {
        return getDescription(multiplier.getDescription());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        BlockState blockState = actionData.getData(IActionDataType.BLOCK_STATE);
        if (blockState != null) {
            BlockPos blockPos = actionData.getData(IActionDataType.BLOCK_POSITION);
            if (blockPos != null) {
                Level level = actionData.getData(IActionDataType.WORLD);
                if (level == null) level = actionData.getPlayer().arc$getLevel();
                if (level instanceof ServerLevel serverLevel) {
                    Vec3 vec3 = Vec3.atCenterOf(blockPos);
                    List<ItemStack> drops = blockState.getDrops(new LootParams.Builder(serverLevel)
                            .withParameter(LootContextParams.ORIGIN, vec3)
                            .withParameter(LootContextParams.TOOL, actionData.getPlayer().arc$getPlayer().getMainHandItem())
                            .withParameter(LootContextParams.BLOCK_STATE, blockState)
                            .withParameter(LootContextParams.THIS_ENTITY, actionData.getPlayer().arc$getPlayer())
                    );
                    int resolvedMultiplier = (int) Math.round(multiplier.resolve(actionData));
                    for (ItemStack drop : drops) {
                        for (int i = 1; i < resolvedMultiplier; i++) {
                            level.addFreshEntity(new ItemEntity(level, vec3.x(), vec3.y(), vec3.z(), drop));
                        }
                    }
                }
            }
        }
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.BLOCK_DROP_MULTIPLIER;
    }

    public INumberProvider getMultiplier() {
        return multiplier;
    }

    public static class Serializer implements IRewardSerializer<BlockDropMultiplierReward> {
        @Override
        public BlockDropMultiplierReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new BlockDropMultiplierReward(
                    chance,
                    priority,
                    getNumberProvider(jsonObject, "multiplier", new ConstantNumberProvider(1))
            );
        }

        @Override
        public BlockDropMultiplierReward fromNetwork(RegistryFriendlyByteBuf buf, double chance, int priority) {
            return new BlockDropMultiplierReward(chance, priority, INumberProviderSerializer.fromNetworkStatic(buf));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, BlockDropMultiplierReward type) {
            IRewardSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.multiplier, buf);
        }
    }
}