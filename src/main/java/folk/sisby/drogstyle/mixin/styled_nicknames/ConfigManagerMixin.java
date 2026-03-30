package folk.sisby.drogstyle.mixin.styled_nicknames;

import eu.pb4.placeholders.api.parsers.TagParser;
import eu.pb4.stylednicknames.config.ConfigManager;
import eu.pb4.stylednicknames.config.data.ConfigData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

/**
 * Modifies Styled Nicknames to allow nicknames to be self-assigned by any player, with colour and tooltips.
 *
 * @author Sisby folk
 */
@Mixin(value = ConfigManager.class, remap = false)
public abstract class ConfigManagerMixin {
	@ModifyVariable(method = "loadConfig", at = @At(value = "INVOKE", target = "Ljava/io/BufferedWriter;<init>(Ljava/io/Writer;)V"), name = "config")
	private static ConfigData forceDefaultEnabledColorHover(ConfigData config) {
		config.allowByDefault = true;
		TagParser.DEFAULT_SAFE.tagRegistry().getTags().stream().filter(t -> List.of("color", "hover_event").contains(t.type())).forEach(tag -> config.defaultEnabledFormatting.put(tag.name(), true));
		return config;
	}
}
