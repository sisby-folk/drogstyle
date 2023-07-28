package folk.sisby.drogstyle.mixin.styled_nicknames;

import eu.pb4.placeholders.api.parsers.TextParserV1;
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
	@ModifyVariable(method = "loadConfig", at = @At(value = "INVOKE", target = "Ljava/io/BufferedWriter;<init>(Ljava/io/Writer;)V"))
	private static ConfigData forceDefaultEnabledColorHover(ConfigData configData) {
		configData.allowByDefault = true;
		TextParserV1.SAFE.getTags().stream().filter(t -> List.of("color", "hover_event").contains(t.type())).forEach(tag -> configData.defaultEnabledFormatting.put(tag.name(), true));
		return configData;
	}
}
