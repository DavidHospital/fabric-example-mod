package net.fabricmc.example;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.fabricmc.fabric.impl.dimension.FabricDimensionInternals;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.network.message.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.TeleportTarget;
import xyz.nucleoid.fantasy.Fantasy;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;

import static net.minecraft.server.command.CommandManager.literal;

import org.apache.commons.lang3.StringUtils;

import static net.minecraft.server.command.CommandManager.argument;

public class Commands {

    public static String CMD_PREFIX = "plex";
    public static String CMD_CREATE = "create";
    public static String CMD_LIST = "list";
    public static String CMD_TP = "tp";

    public static String ARG_WORLD = "world";

    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(CMD_PREFIX).executes(Commands::commandHelp)
            .then(literal(CMD_CREATE)
                .then(argument(ARG_WORLD, StringArgumentType.greedyString())
                    .executes(Commands::commandCreate)))
            .then(literal(CMD_LIST)
                .executes(Commands::commandList))
            .then(literal(CMD_TP)
                .then(argument(ARG_WORLD, DimensionArgumentType.dimension())
                    .executes(Commands::commandTp)))
        );
    }

    private static int commandHelp(CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int commandCreate(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source = context.getSource();
        final MinecraftServer server = source.getServer();

        String world_arg = StringArgumentType.getString(context, ARG_WORLD);
        if (StringUtils.isEmpty(world_arg)) {
            broadcastError(server, String.format("Usage: %s %s <%s>", CMD_PREFIX, CMD_CREATE, ARG_WORLD));
            return -1;
        }

        Identifier world = new Identifier("worldplexer", world_arg);
        
        broadcastMessage(server, 
            String.format("Creating world [%s]...", world.toString()), 
            Formatting.LIGHT_PURPLE, 
            Formatting.ITALIC);

        Fantasy fantasy = Fantasy.get(server);
        fantasy.getOrOpenPersistentWorld(world, new RuntimeWorldConfig()
            .setGenerator(server.getOverworld().getChunkManager().getChunkGenerator())
            .setDimensionType(RegistryKey.of(Registry.DIMENSION_TYPE_KEY, new Identifier("overworld")))
            .setSeed(1234)
        );

        // server.

        return 1;
    }

    private static int commandList(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source = context.getSource();
        final MinecraftServer server = source.getServer();

        broadcastMessage(server, "Worlds:", Formatting.LIGHT_PURPLE);
        server.getWorlds().forEach(world -> {
            String name = world.getRegistryKey().getValue().toString();
            broadcastMessage(server, "- " + name, Formatting.LIGHT_PURPLE);
        });

        return 1;
    }

    private static int commandTp(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source = context.getSource();
        final MinecraftServer server = source.getServer();

        final ServerPlayerEntity player = source.getPlayer();
        ServerWorld dimension;
        try {
            dimension = DimensionArgumentType.getDimensionArgument(context, ARG_WORLD);

            final TeleportTarget target = new TeleportTarget(
                player.getPos(), player.getVelocity(), player.getYaw(), player.getPitch());
            FabricDimensionInternals.changeDimension(player, dimension, target);
        } catch (CommandSyntaxException e) {
            broadcastError(server, "Dimension does not exist");
            // e.printStackTrace();
            return -1;
        }

        return 1;
    }

    private static void broadcastMessage(MinecraftServer server, String message, Formatting... formattings) {
        server.getPlayerManager().broadcast(Text.literal(message)
            .formatted(formattings), MessageType.SYSTEM);
    }

    private static void broadcastError(MinecraftServer server, String message) {
        broadcastMessage(server, message, Formatting.RED, Formatting.ITALIC);
    }
}
