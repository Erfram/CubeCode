package com.cubecode.content;

import com.cubecode.api.scripts.code.ScriptEvent;
import com.cubecode.client.config.CubeCodeConfig;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import com.cubecode.CubeCode;
import com.cubecode.api.scripts.ScriptManager;
import com.cubecode.api.scripts.code.ScriptFactory;
import com.cubecode.api.scripts.code.ScriptServer;
import com.cubecode.api.scripts.code.ScriptWorld;
import com.cubecode.api.scripts.code.entities.ScriptEntity;
import com.cubecode.utils.CubeCodeException;

import java.util.HashMap;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class CubeCodeCommand {
    public static void init(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("cubecode").then(literal("script")
                .then(literal("eval").then(argument("script", MessageArgumentType.message()).executes(CubeCodeCommand::evalCode)))
                .then(literal("exec").then(argument("scriptName", MessageArgumentType.message()).executes(CubeCodeCommand::execScript)))
        ));
    }

    private static int execScript(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String scriptName = MessageArgumentType.getMessage(context, "scriptName").getString();
        HashMap<String, Object> properties = CubeCodeCommand.createProperties(scriptName, context);

        CubeCode.scriptManager.updateScripts();

        try {
            CubeCode.scriptManager.executeScript(scriptName, properties);
        } catch (CubeCodeException exception) {
            context.getSource().sendError(Text.of(exception.getMessage()));
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int evalCode(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        HashMap<String, Object> properties = CubeCodeCommand.createProperties(null, context);
        String code = MessageArgumentType.getMessage(context, "script").getString();

        try {
            ScriptManager.evalCode(code, 0, "eval", properties);
        } catch (CubeCodeException exception) {
            context.getSource().sendError(Text.of(exception.getMessage()));
        }

        return Command.SINGLE_SUCCESS;
    }

    private static HashMap<String, Object> createProperties(String scriptName, CommandContext<ServerCommandSource> context) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put(CubeCodeConfig.getScriptConfig().contextName, new ScriptEvent(
                scriptName,
                null,
                ScriptEntity.create(context.getSource().getPlayer()),
                null,
                new ScriptWorld(context.getSource().getWorld()),
                new ScriptServer(context.getSource().getServer())
        ));

        properties.put("CubeCode", new ScriptFactory());

        return properties;
    }
}
