package com.daqem.arc.config;

import com.daqem.arc.Arc;
import com.daqem.knot.api.platform.Platform;
import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.IConfigBuilder;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.impl.config.ConfigBuilder;

import java.util.List;

public class ArcCommonConfig {

    public static void init() {
    }

    public static final IConfigEntry<List<String>> excludedActions;

    public static final IConfigEntry<Integer> maxBlockPosCacheSize;


    static {
        IConfigBuilder builder = new ConfigBuilder(
                Arc.MOD_ID,
                "arc-common",
                ConfigExtension.YAML,
                ConfigType.COMMON,
                Platform.INFO.getConfigFolder().resolve(Arc.MOD_ID)
        );

        builder.push("actions");
        excludedActions = builder.defineStringList("excluded_actions", List.of())
                .withComments("A list of action IDs to exclude from the game. Example: [`<namespace>:<action_id>`]");
        builder.pop();

        builder.push("block");
        maxBlockPosCacheSize = builder.defineInteger("max_block_pos_cache_size", 10_000)
                .withComments("The maximum size of the block pos cache, used to not drop double drops on placed blocks");
        builder.pop();

        builder.build();
    }
}
