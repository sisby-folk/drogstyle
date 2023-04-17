package folk.sisby.drogstyle.mixin.styled_nicknames;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = eu.pb4.stylednicknames.command.Commands.class, remap = false)
public class CommandsMixin {
	/**
	 * @author Sisby folk
	 * @reason Intended to completely removes styled nicknames command registration for drogstyle.
	 * Allowing only info and /reload was too hard.
	 */
	@Overwrite
	public static void register() {}
}
