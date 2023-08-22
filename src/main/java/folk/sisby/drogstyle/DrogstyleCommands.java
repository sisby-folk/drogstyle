package folk.sisby.drogstyle;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import eu.pb4.stylednicknames.config.ConfigManager;
import net.minecraft.command.CommandBuildContext;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.function.Consumer;
import java.util.regex.Pattern;

public class DrogstyleCommands {
	private static final Pattern ESCAPE_PATTERN = Pattern.compile("\\\\.");

	private static int setNick(ServerPlayerEntity player, DrogstylePlayer drogstylePlayer, String nick, Consumer<Text> feedback) {
		Text oldDn = player.getDisplayName();
		if (nick != null && !nick.isEmpty()) {
			String newDn = nick.replace("§", "");
			drogstylePlayer.drogtor$setNickname(newDn);
			feedback.accept(Text.literal("Your display name is now ").formatted(Formatting.YELLOW).append(player.getDisplayName().copy().formatted(Formatting.WHITE)));
		} else {
			drogstylePlayer.drogtor$setNickname(null);
			feedback.accept(Text.literal("Your display name has been cleared.").formatted(Formatting.YELLOW));
		}
		Drogstyle.LOGGER.info("[Drogstyle] Player Nickname Change: '" + oldDn.getString() + "' -> '" + player.getDisplayName().getString() + "' [" + player.getGameProfile().getName() + "]");
		return 1;
	}

	private static int setColor(ServerPlayerEntity player, DrogstylePlayer drogstylePlayer, String colorString, Consumer<Text> feedback) {
		if (colorString != null) {
			TextColor color = TextColor.parse(colorString);
			if (color == null) {
				feedback.accept(Text.literal("Invalid color! must be a color name or # followed by a 6-digit hex code.").formatted(Formatting.RED));
				return -1;
			}
			drogstylePlayer.drogstyle$setNameColor(color);
			feedback.accept(Text.literal("Your display name is now ").formatted(Formatting.YELLOW).append(player.getDisplayName().copy().formatted(Formatting.WHITE)));
		} else {
			drogstylePlayer.drogstyle$setNameColor(null);
			feedback.accept(Text.literal("Your color has been cleared.").formatted(Formatting.YELLOW));
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
			feedback.accept(Text.literal("Your bio is now:\n").formatted(Formatting.YELLOW).append(bio));
		} else {
			drogstylePlayer.drogtor$setBio(null);
			feedback.accept(Text.literal("Your bio has been cleared.").formatted(Formatting.YELLOW));
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
			context.getSource().sendFeedback(Text.literal("Command failed! Check log for details.").formatted(Formatting.RED), false);
			Drogstyle.LOGGER.error("[Drogstyle] Error while executing command: {}", context.getInput(), e);
			return 0;
		}
	}

	private static int reloadConfig(CommandContext<ServerCommandSource> context) {
		if (ConfigManager.loadConfig()) {
			context.getSource().sendFeedback(Text.literal("Reloaded config!"), false);
		} else {
			context.getSource().sendError(Text.literal("Error occurred while reloading config!").formatted(Formatting.RED));
		}
		return 1;
	}

	public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandBuildContext buildContext, CommandManager.RegistrationEnvironment environment) {
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
				.then(CommandManager.literal("reload")
					.requires(src -> src.hasPermissionLevel(3))
					.executes(DrogstyleCommands::reloadConfig))
		);
	}
}
