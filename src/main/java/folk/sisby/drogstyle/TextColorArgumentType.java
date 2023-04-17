package folk.sisby.drogstyle;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandBuildContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.ArgumentTypeInfo;
import net.minecraft.command.argument.ColorArgumentType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class TextColorArgumentType implements ArgumentType<TextColor> {
	private static final Collection<String> EXAMPLES = Arrays.asList("red", "green", "#FFFFFF");

	private TextColorArgumentType() {
	}

	public static TextColorArgumentType color() {
		return new TextColorArgumentType();
	}

	public static TextColor getColor(CommandContext<ServerCommandSource> context, String name) {
		return context.getArgument(name, TextColor.class);
	}

	public static boolean isAllowedInUnquotedHexString(final char c) {
		return c >= '0' && c <= '9'
				|| c >= 'A' && c <= 'Z'
				|| c >= 'a' && c <= 'z'
				|| c == '_' || c == '-'
				|| c == '.' || c == '+' ||
				c == '#';
	}

	public String readUnquotedHexString(StringReader stringReader) {
		final int start = stringReader.getCursor();
		while (stringReader.canRead() && isAllowedInUnquotedHexString(stringReader.peek())) {
			stringReader.skip();
		}
		return stringReader.getString().substring(start, stringReader.getCursor());
	}

	public TextColor parse(StringReader stringReader) throws CommandSyntaxException {
		String string = readUnquotedHexString(stringReader);
		try {
			return TextColor.parse(string);
		} catch (NumberFormatException e) {
			throw ColorArgumentType.INVALID_COLOR_EXCEPTION.create(string);
		}
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContext, SuggestionsBuilder suggestionsBuilder) {
		return CommandSource.suggestMatching(Formatting.getNames(true, false), suggestionsBuilder);
	}

	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	public static final class Info implements ArgumentTypeInfo<TextColorArgumentType, TextColorArgumentType.Info.Template> {
		@Override
		public void serializeToNetwork(TextColorArgumentType.Info.Template type, PacketByteBuf buf) {
		}

		@Override
		public TextColorArgumentType.Info.Template deserializeFromNetwork(PacketByteBuf buf) {
			return new TextColorArgumentType.Info.Template();
		}

		@Override
		public void serializeToJson(TextColorArgumentType.Info.Template type, JsonObject json) {
		}

		@Override
		public TextColorArgumentType.Info.Template unpack(TextColorArgumentType type) {
			return new TextColorArgumentType.Info.Template();
		}

		public final class Template implements ArgumentTypeInfo.Template<TextColorArgumentType> {
			public Template() {
			}

			@Override
			public TextColorArgumentType instantiate(CommandBuildContext context) {
				return new TextColorArgumentType();
			}

			@Override
			public ArgumentTypeInfo<TextColorArgumentType, ?> type() {
				return TextColorArgumentType.Info.this;
			}
		}
	}
}
