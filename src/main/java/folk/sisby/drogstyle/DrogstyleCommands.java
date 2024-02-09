package folk.sisby.drogstyle;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import eu.pb4.stylednicknames.NicknameHolder;
import eu.pb4.stylednicknames.config.ConfigManager;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DrogstyleCommands {
	private static final Pattern ESCAPE_PATTERN = Pattern.compile("\\\\.");

	private static int setNick(ServerPlayerEntity player, DrogstylePlayer drogstylePlayer, String nick, Consumer<Text> feedback) {
		Text oldDn = player.getDisplayName();
		if (nick != null && !nick.isEmpty()) {
			String newDn = nick.replace("§", "");
			drogstylePlayer.drogtor$setNickname(newDn);
			feedback.accept(new LiteralText("Your display name is now ").formatted(Formatting.YELLOW).append(new LiteralText("").append(player.getDisplayName()).formatted(Formatting.WHITE)));
		} else {
			drogstylePlayer.drogtor$setNickname(null);
			feedback.accept(new LiteralText("Your display name has been cleared.").formatted(Formatting.YELLOW));
		}
		Drogstyle.LOGGER.info("[Drogstyle] Player Nickname Change: '" + oldDn.getString() + "' -> '" + player.getDisplayName().getString() + "' [" + player.getGameProfile().getName() + "]");
		return 1;
	}

	private static int setColor(ServerPlayerEntity player, DrogstylePlayer drogstylePlayer, String colorString, Consumer<Text> feedback) {
		if (colorString != null) {
			TextColor color = TextColor.parse(colorString);
			if (color == null) {
				feedback.accept(new LiteralText("Invalid color! must be a color name or # followed by a 6-digit hex code.").formatted(Formatting.RED));
				return -1;
			}
			drogstylePlayer.drogstyle$setNameColor(color);
			feedback.accept(new LiteralText("Your display name is now ").formatted(Formatting.YELLOW).append(new LiteralText("").append(player.getDisplayName()).formatted(Formatting.WHITE)));
		} else {
			drogstylePlayer.drogstyle$setNameColor(null);
			feedback.accept(new LiteralText("Your color has been cleared.").formatted(Formatting.YELLOW));
		}
		return 1;
	}

	private static int setBio(ServerPlayerEntity player, DrogstylePlayer drogstylePlayer, String bio, Consumer<Text> feedback) {
		if (bio != null) {
			bio = ESCAPE_PATTERN.matcher(bio).replaceAll(res -> {
				String s = res.group().substring(1);
				if (s.equals("n")) {
					return "\n";
				} else if (s.equals("\\")) {
					return "\\\\";
				}
				return s;
			});
			drogstylePlayer.drogtor$setBio(bio);
			feedback.accept(new LiteralText("Your bio is now:\n").formatted(Formatting.YELLOW).append(bio));
		} else {
			drogstylePlayer.drogtor$setBio(null);
			feedback.accept(new LiteralText("Your bio has been cleared.").formatted(Formatting.YELLOW));
		}
		return 1;
	}

	public interface DrogstyleCommandExecutor {
		int execute(ServerPlayerEntity player, DrogstylePlayer drogstylePlayer, String arg, Consumer<Text> feedback);
	}

	public static int execute(CommandContext<ServerCommandSource> context, String arg, DrogstyleCommandExecutor executor) {
		ServerPlayerEntity player;
		try {
			player = context.getSource().getPlayer();
		} catch (CommandSyntaxException e) {
			Drogstyle.LOGGER.error("[Drogstyle] Commands cannot be invoked by a non-player");
			return 0;
		}

		DrogstylePlayer drogstylePlayer = ((DrogstylePlayer) player);
		try {
			return executor.execute(player, drogstylePlayer, arg != null ? context.getArgument(arg, String.class) : null, t -> context.getSource().sendFeedback(t, false));
		} catch (Exception e) {
			context.getSource().sendFeedback(new LiteralText("Command failed! Check log for details.").formatted(Formatting.RED), false);
			Drogstyle.LOGGER.error("[Drogstyle] Error while executing command: {}", context.getInput(), e);
			return 0;
		}
	}

	private static int reloadConfig(CommandContext<ServerCommandSource> context) {
		if (ConfigManager.loadConfig()) {
			context.getSource().sendFeedback(new LiteralText("Reloaded config!"), false);
		} else {
			context.getSource().sendError(new LiteralText("Error occurred while reloading config!").formatted(Formatting.RED));
		}
		return 1;
	}

	private static int username(CommandContext<ServerCommandSource> context) {
		String nickname = StringArgumentType.getString(context, "nickname");
		List<ServerPlayerEntity> players = context.getSource().getServer().getPlayerManager().getPlayerList();
		Map<ServerPlayerEntity, MutableText> foundPlayers = new HashMap<>();
		for (ServerPlayerEntity player : players) {
			MutableText output = NicknameHolder.of(player).sn_getOutput();
			if (output == null) continue;
			if (output.getString().equals(nickname)) {
				foundPlayers.put(player, output);
			}
		}
		if (foundPlayers.isEmpty()) {
			context.getSource().sendError(new LiteralText("No player with that name is currently online."));
		} else {
			if (foundPlayers.size() > 1) {
				context.getSource().sendFeedback(new LiteralText("Found %s players with that name:".formatted(foundPlayers.size())), false);
			}
			foundPlayers.forEach((serverPlayerEntity, mutableText) -> {
                context.getSource().sendFeedback(new LiteralText("The username of ").append(serverPlayerEntity.getDisplayName()).append(new LiteralText(" is ")).append(serverPlayerEntity.getName()), false);
            });
		}
		return 0;
	}

	private static final SuggestionProvider<ServerCommandSource> NICKNAME_PROVIDER = (source, builder) -> {
		List<ServerPlayerEntity> players = source.getSource().getServer().getPlayerManager().getPlayerList();
		Set<String> nicknames = players.stream()
			.map(player -> NicknameHolder.of(player).sn_getOutput())
			.filter(Objects::nonNull)
			.map(Text::getString)
			.collect(Collectors.toSet());
		return CommandSource.suggestMatching(nicknames, builder);
	};

	public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
		dispatcher.register(
			CommandManager.literal("nick")
				.then(CommandManager.argument("nick", StringArgumentType.greedyString())
					.executes((c) -> execute(c, "nick", DrogstyleCommands::setNick))).executes((c) -> execute(c, null, DrogstyleCommands::setNick)));
		dispatcher.register(
			CommandManager.literal("color")
				.then(CommandManager.argument("color", StringArgumentType.greedyString())
					.suggests((c, s) -> CommandSource.suggestMatching(Formatting.getNames(true, false), s))
					.executes((c) -> execute(c, "color", DrogstyleCommands::setColor)))
				.executes((c) -> execute(c, null, DrogstyleCommands::setColor)));
		dispatcher.register(
			CommandManager.literal("bio")
				.then(CommandManager.argument("bio", StringArgumentType.greedyString())
					.executes((c) -> execute(c, "bio", DrogstyleCommands::setBio)))
				.executes((c) -> execute(c, null, DrogstyleCommands::setBio)));
		dispatcher.register(
			CommandManager.literal("drogstyle")
				.then(CommandManager.literal("username")
					.then(CommandManager.argument("nickname", StringArgumentType.greedyString()).suggests(NICKNAME_PROVIDER)
						.executes(DrogstyleCommands::username)
					))
				.then(CommandManager.literal("reload")
					.requires(src -> src.hasPermissionLevel(3))
					.executes(DrogstyleCommands::reloadConfig))
		);
	}
}
