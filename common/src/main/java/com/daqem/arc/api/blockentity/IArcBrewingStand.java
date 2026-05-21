package com.daqem.arc.api.blockentity;

import java.util.Map;
import java.util.UUID;

public interface IArcBrewingStand {

    void arc$setLastPlayerToInteract(UUID uuid);

    UUID arc$getLastPlayerToInteract();

    void arc$addBrewingStandItemOwner(int slot, UUID uuid);

    void arc$removeBrewingStandItemOwner(int slot);

    UUID arc$getBrewingStandItemOwner(int slot);

    Map<Integer, UUID> arc$getBrewingStandItemOwners();
}