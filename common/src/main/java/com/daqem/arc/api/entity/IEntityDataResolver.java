package com.daqem.arc.api.entity;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

public interface IEntityDataResolver<T> {

    Identifier getId();

    Function<Entity, T> getDataFetcher();

    Class<T> getType();
}