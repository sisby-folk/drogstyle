package folk.sisby.drogstyle;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class Drogstyle implements ModInitializer {
	public static final String ID = "drogstyle";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(DrogstyleCommands::registerCommands);
	}
}
