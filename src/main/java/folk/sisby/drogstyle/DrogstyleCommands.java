package folk.sisby.drogstyle;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandBuildContext;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
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
			feedback.accept(Text.literal("Your display name is now ").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)).append(player.getDisplayName().copy().setStyle(Style.EMPTY.withColor(Formatting.WHITE))));
		} else {
			drogstylePlayer.drogtor$setNickname(null);
			feedback.accept(Text.literal("Your display name has been cleared.").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)));
		}
		Drogstyle.LOGGER.info("[Drogstyle] Player Nickname Change: '" + oldDn.getString() + "' -> '" + player.getDisplayName().getString() + "' [" + player.getGameProfile().getName() + "]");
		return 1;
	}

	private static int setColor(ServerPlayerEntity player, DrogstylePlayer drogstylePlayer, String colorString, Consumer<Text> feedback) {
		if (colorString != null) {
			TextColor color = TextColor.parse(colorString);
			if (color == null) {
				feedback.accept(Text.literal("Invalid color! must be a color name or # followed by a 6-digit hex code.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
				return -1;
			}
			drogstylePlayer.drogstyle$setNameColor(color);
			feedback.accept(Text.literal("Your display name is now ").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)).append(player.getDisplayName().copy().setStyle(Style.EMPTY.withColor(Formatting.WHITE))));
		} else {
			drogstylePlayer.drogstyle$setNameColor(null);
			feedback.accept(Text.literal("Your color has been cleared.").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)));
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
			feedback.accept(Text.literal("Your bio is now:\n").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)).append(bio));
		} else {
			drogstylePlayer.drogtor$setBio(null);
			feedback.accept(Text.literal("Your bio has been cleared.").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)));
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
			context.getSource().sendFeedback(Text.literal("Command failed! Check log for details.").setStyle(Style.EMPTY.withColor(Formatting.RED)), false);
			Drogstyle.LOGGER.error("[Drogstyle] Error while executing command: {}", context.getInput(), e);
			return 0;
		}
	}

	public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandBuildContext buildContext, CommandManager.RegistrationEnvironment environment) {
		dispatcher.register(
			LiteralArgumentBuilder.<ServerCommandSource>literal("nick")
				.then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("nick", StringArgumentType.greedyString())
					.executes((c) -> execute(c, "nick", DrogstyleCommands::setNick))).executes((c) -> execute(c, null, DrogstyleCommands::setNick)));
		dispatcher.register(
			LiteralArgumentBuilder.<ServerCommandSource>literal("color")
				.then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("color", StringArgumentType.greedyString())
					.suggests((c, s) -> CommandSource.suggestMatching(Formatting.getNames(true, false), s))
					.executes((c) -> execute(c, "color", DrogstyleCommands::setColor)))
				.executes((c) -> execute(c, null, DrogstyleCommands::setColor)));
		dispatcher.register(
			LiteralArgumentBuilder.<ServerCommandSource>literal("bio")
				.then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("bio", StringArgumentType.greedyString())
					.executes((c) -> execute(c, "bio", DrogstyleCommands::setBio)))
				.executes((c) -> execute(c, null, DrogstyleCommands::setBio)));
	}
}
