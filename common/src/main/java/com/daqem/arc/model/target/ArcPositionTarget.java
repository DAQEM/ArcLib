package com.daqem.arc.model.target;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.data.ActionData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public enum ArcPositionTarget {
    PLAYER,
    BLOCK,
    ENTITY;

    public Vec3 getPosition(ActionData actionData) {
        return switch (this) {
            case PLAYER -> actionData.getPlayer().arc$getPlayer().position();
            case BLOCK -> {
                BlockPos blockPos = actionData.getData(IActionDataType.BLOCK_POSITION);
                yield blockPos != null ? blockPos.getCenter() : actionData.getPlayer().arc$getPlayer().position();
            }
            case ENTITY -> {
                Entity entity = actionData.getData(IActionDataType.ENTITY);
                yield entity != null ? entity.position() : actionData.getPlayer().arc$getPlayer().position();
            }
        };
    }

    public Entity getEntity(ActionData actionData) {
        return switch (this) {
            case PLAYER -> actionData.getPlayer().arc$getPlayer();
            case BLOCK -> null;
            case ENTITY -> actionData.getData(IActionDataType.ENTITY);
        };
    }
}
