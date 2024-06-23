package com.cubecode.content;

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
                .then(literal("exec").then(argument("scriptId", MessageArgumentType.message()).executes(CubeCodeCommand::execScript)))
        ));
    }

    private static int execScript(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        HashMap<String, Object> properties = CubeCodeCommand.createProperties(context);
        String scriptId = MessageArgumentType.getMessage(context, "scriptId").getString();

        CubeCode.scriptManager.updateScripts();

        try {
            CubeCode.scriptManager.executeScript(scriptId, properties);
        } catch (CubeCodeException exception) {
            context.getSource().sendError(Text.of(exception.getMessage()));
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int evalCode(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        HashMap<String, Object> properties = CubeCodeCommand.createProperties(context);
        String code = MessageArgumentType.getMessage(context, "script").getString();

        try {
            ScriptManager.evalCode(code, 1, "eval", properties);
        } catch (CubeCodeException exception) {
            context.getSource().sendError(Text.of(exception.getMessage()));
        }

        return Command.SINGLE_SUCCESS;
    }

    private static HashMap<String, Object> createProperties(CommandContext<ServerCommandSource> context) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("Player", ScriptEntity.create(context.getSource().getPlayer()));
        properties.put("Server", new ScriptServer(context.getSource().getServer()));
        properties.put("World", new ScriptWorld(context.getSource().getWorld()));
        properties.put("CubeCode", new ScriptFactory());

        return properties;
    }
}
