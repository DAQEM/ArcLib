package com.daqem.arc.command;

import com.daqem.arc.data.ActionManager;
import com.daqem.arc.networking.ClientboundActionScreenPacket;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;

public class ArcCommand {

    public static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("arc")
                .then(Commands.literal("screen")
                        .then(Commands.argument("action", StringArgumentType.string())
                                .executes(context -> {
                                    new ClientboundActionScreenPacket(ActionManager.getInstance().byKey(new ResourceLocation(StringArgumentType.getString(context, "action"))).orElse(null)).sendTo(context.getSource().getPlayer());
                                    return 1;
                                })
                        )
                .executes(context -> {
                        new ClientboundActionScreenPacket(ActionManager.getInstance().getActions().get(0)).sendTo(context.getSource().getPlayer());
                        return 1;
                }))
        );
    }
}
