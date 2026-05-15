package com.daqem.arc.data.condition.block;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.ArcBlockState;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class BlocksCondition extends AbstractCondition {

    private final List<ArcBlockState> blockStates;
    private final List<TagKey<Block>> blockTags;

    public BlocksCondition(boolean inverted, List<ArcBlockState> blockStates, List<TagKey<Block>> blockTags) {
        super(inverted);
        this.blockStates = blockStates;
        this.blockTags = blockTags;
    }

    @Override
    public Component getDescription() {
        return getDescription(blockStates.stream().map(x -> x.block().getName()).reduce(
                (a, b) -> a.append(", ").append(b)
        ).orElse(Component.literal("No Blocks")
        ), blockTags.stream().map(TagKey::location).map(ResourceLocation::toString).reduce(
                (a, b) -> a + ", " + b
        ).orElse("No Block Tags"));
    }

    @Override
    public boolean isMet(ActionData actionData) {
        BlockState blockState = actionData.getData(IActionDataType.BLOCK_STATE);
        return blockState != null
                && (this.blockStates.stream().anyMatch(x -> x.matches(blockState))
                || this.blockTags.stream().anyMatch(blockState::is));
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.BLOCKS;
    }


    public List<Block> getBlocks() {
        return blockStates.stream().map(ArcBlockState::block).toList();
    }

    public List<ArcBlockState> getBlockStates() {
        return blockStates;
    }

    public List<TagKey<Block>> getBlockTags() {
        return blockTags;
    }

    public List<Block> getAllBlocks(RegistryAccess registryAccess) {
        List<Block> allBlocks = new ArrayList<>(getBlocks());
        for (TagKey<Block> tag : blockTags) {
            registryAccess.lookupOrThrow(Registries.BLOCK)
                    .get(tag)
                    .ifPresent(x -> allBlocks.addAll(x.stream().map(Holder::value).toList()));
        }
        return allBlocks;
    }

    public static class Serializer implements IConditionSerializer<BlocksCondition> {

        @Override
        public BlocksCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new BlocksCondition(
                    inverted,
                    getBlockStates(jsonObject, "blocks"),
                    getBlockTags(jsonObject, "blocks"));
        }

        @Override
        public BlocksCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            List<ArcBlockState> blocks = friendlyByteBuf.readList(buf ->
                    ArcBlockState.STREAM_CODEC.decode((RegistryFriendlyByteBuf) buf));
            List<TagKey<Block>> blockTags = new ArrayList<>();

            int tagCount = friendlyByteBuf.readVarInt();
            for (int i = 0; i < tagCount; i++) {
                blockTags.add(TagKey.create(BuiltInRegistries.BLOCK.key(), friendlyByteBuf.readResourceLocation()));
            }


            return new BlocksCondition(
                    inverted,
                    blocks,
                    blockTags
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, BlocksCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeCollection(type.blockStates, (buf, blockState) ->
                    ArcBlockState.STREAM_CODEC.encode((RegistryFriendlyByteBuf) buf, blockState));
            friendlyByteBuf.writeVarInt(type.blockTags.size());
            type.blockTags.forEach(tag -> friendlyByteBuf.writeResourceLocation(tag.location()));
        }
    }
}
