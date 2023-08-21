package folk.sisby.drogstyle;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.unascribed.drogtor.DrogtorPlayer;
import net.minecraft.command.CommandBuildContext;
import net.minecraft.command.argument.ColorArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;

import java.util.regex.Pattern;

public class DrogstyleCommands implements CommandRegistrationCallback {
	private static final Pattern ESCAPE_PATTERN = Pattern.compile("\\\\.");

	@Override
	public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandBuildContext buildContext, CommandManager.RegistrationEnvironment environment) {
		dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("nick")
				.then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("nick", StringArgumentType.greedyString())
						.executes((c) -> {
							Text oldDn = c.getSource().getPlayer().getDisplayName();
							String newDn = c.getArgument("nick", String.class).replace("§", "");
							((DrogtorPlayer) c.getSource().getPlayer( )).drogtor$setNickname(newDn);
							informDisplayName(c.getSource().getPlayer(), oldDn);
							return 1;
						}))
				.executes((c) -> {
					Text oldDn = c.getSource().getPlayer().getDisplayName();
					((DrogtorPlayer) c.getSource().getPlayer()).drogtor$setNickname(null);
					informDisplayName(c.getSource().getPlayer(), oldDn);
					return 1;
				}));
		dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("color")
				.then(RequiredArgumentBuilder.<ServerCommandSource, TextColor>argument("color", TextColorArgumentType.color())
						.suggests(ColorArgumentType.color()::listSuggestions)
						.executes((c) -> {
							((DrogstylePlayer) c.getSource().getPlayer()).drogstyle$setNameColor(TextColorArgumentType.getColor(c, "color"));
							informDisplayName(c.getSource().getPlayer(), null);
							return 1;
						}))
				.executes((c) -> {
					((DrogtorPlayer) c.getSource().getPlayer()).drogtor$setNameColor(null);
					informDisplayName(c.getSource().getPlayer(), null);
					return 1;
				}));
		dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("bio")
				.then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("bio", StringArgumentType.greedyString())
						.executes((c) -> {
							String bio = c.getArgument("bio", String.class).replace("§", "");
							bio = ESCAPE_PATTERN.matcher(bio).replaceAll(res -> {
								String s = res.group().substring(1);
								if (s.equals("n")) {
									return "\n";
								} else if (s.equals("\\")) {
									return "\\\\";
								}
								return s;
							});
							((DrogtorPlayer) c.getSource().getPlayer()).drogtor$setBio(bio);
							informBio(c.getSource().getPlayer());
							return 1;
						}))
				.executes((c) -> {
					((DrogtorPlayer) c.getSource().getPlayer()).drogtor$setBio(null);
					informBio(c.getSource().getPlayer());
					return 1;
				}));
	}

	private void informDisplayName(ServerPlayerEntity player, Text oldDn) {
		player.sendMessage(Text.literal("Your display name is now ").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)).append(player.getDisplayName().copy().setStyle(Style.EMPTY.withColor(Formatting.WHITE))), false);
		if (oldDn != null) {
			Drogstyle.LOGGER.info("[Drogstyle] Player Nickname Change: '" + oldDn.getString() + "' -> '" + player.getDisplayName().getString() + "' [" + player.getGameProfile().getName() + "]");
		} else {
			player.sendMessage(Text.literal("Your display name has been cleared.").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)), false);
		}
	}

	private void informBio(ServerPlayerEntity player) {
		String bio = ((DrogtorPlayer)player).drogtor$getBio();
		if (bio != null) {
			player.sendMessage(Text.literal("Your bio is now:\n").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)).append(bio), false);
		} else {
			player.sendMessage(Text.literal("Your bio has been cleared.").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)), false);
		}
	}
}
