package com.daqem.arc.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.enchantment.Enchantment;

public record ArcEnchantment(Holder<Enchantment> enchantment, int level) {

    public static final Codec<ArcEnchantment> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Enchantment.CODEC.fieldOf("id").forGetter(ArcEnchantment::enchantment),
                    Codec.intRange(1, 255).fieldOf("level").forGetter(ArcEnchantment::level)
            ).apply(instance, ArcEnchantment::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ArcEnchantment> STREAM_CODEC = StreamCodec.composite(
            Enchantment.STREAM_CODEC,
            ArcEnchantment::enchantment,
            ByteBufCodecs.INT,
            ArcEnchantment::level,
            ArcEnchantment::new
    );
}