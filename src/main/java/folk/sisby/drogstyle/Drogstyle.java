package folk.sisby.drogstyle;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.ColorArgumentType;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.command.api.ServerArgumentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Drogstyle implements ModInitializer {
	public static final String ID = "drogstyle";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitialize(ModContainer mod) {
		ServerArgumentType.register(
				new Identifier(ID, "text_color"),
				TextColorArgumentType.class,
				new TextColorArgumentType.Info(),
				arg -> StringArgumentType.greedyString(),
				ColorArgumentType.color()::listSuggestions
		);
	}
}
