package com.cubecode.content;

import com.cubecode.api.scripts.Properties;
import com.cubecode.api.scripts.Script;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import com.cubecode.CubeCode;
import com.cubecode.utils.CubeCodeException;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class CubeCodeCommand {
    public static void init(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("cubecode").then(literal("script")
                .then(literal("eval").then(argument("script", MessageArgumentType.message()).executes(CubeCodeCommand::evalCode)))
                .then(literal("exec").then(argument("scriptName", MessageArgumentType.message()).executes(CubeCodeCommand::execScript)))
                .then(literal("exec").then(argument("scriptName", MessageArgumentType.message()).then(argument("function", MessageArgumentType.message()).executes(CubeCodeCommand::execScript)))
        )));
    }

    private static int execScript(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String scriptName = MessageArgumentType.getMessage(context, "scriptName").getString();
        String function = MessageArgumentType.getMessage(context, "function").getString();

        Properties properties = CubeCodeCommand.createProperties(scriptName, "main", context);

        Script script = CubeCode.projectManager.getScript(scriptName);

        try {
            script.run(function.isEmpty() ? "main" : function, scriptName, properties);
        } catch (CubeCodeException exception) {
            context.getSource().sendError(Text.of(exception.getMessage()));
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int evalCode(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Properties properties = CubeCodeCommand.createProperties(null, null, context);
        String code = MessageArgumentType.getMessage(context, "script").getString();

        try {
            CubeCode.projectManager.evalCode(code, "eval", properties.getMap());
        } catch (CubeCodeException exception) {
            context.getSource().sendError(Text.of(exception.getMessage()));
        }

        return Command.SINGLE_SUCCESS;
    }

    public static Properties createProperties(String scriptName, String function, CommandContext<ServerCommandSource> context) {
        return Properties.create(
            scriptName,
            function,
            context.getSource().getPlayer(),
            null,
            context.getSource().getWorld(),
            context.getSource().getServer()
        );
    }
}
