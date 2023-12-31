package com.daqem.arc.command;

import com.daqem.arc.api.action.IAction;
import com.daqem.arc.command.argument.ActionArgument;
import com.daqem.arc.data.ActionManager;
import com.daqem.arc.networking.ClientboundActionScreenPacket;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ArcCommand {

    public static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("arc").requires(source -> source.hasPermission(2))
                .then(Commands.literal("screen")
                        .then(Commands.argument("action", ActionArgument.action())
                                .executes(context -> {
                                    IAction action = ActionArgument.getAction(context, "action");
                                    return openActionScreen(context.getSource(), action);
                                })
                        )
                .executes(context -> {
                    List<IAction> actions = ActionManager.getInstance().getActions();
                    if (actions.isEmpty()) {
                        context.getSource().sendFailure(Component.literal("No actions found"));
                        return 1;
                    } else {
                        return openActionScreen(context.getSource(), actions.get(0));
                    }
                }))
        );
    }

    private static int openActionScreen(CommandSourceStack source, IAction action) {
        if (action == null) {
            source.sendFailure(Component.literal("Unknown action"));
            return 0;
        }
        new ClientboundActionScreenPacket(action).sendTo(source.getPlayer());
        return 1;
    }
}
