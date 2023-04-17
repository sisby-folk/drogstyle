package folk.sisby.drogstyle.mixin;

import com.unascribed.drogtor.DrogtorPlayer;
import eu.pb4.stylednicknames.NicknameHolder;
import folk.sisby.drogstyle.DrogstylePlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = PlayerEntity.class, priority = 1001)
public class PlayerEntityMixin implements DrogtorPlayer, DrogstylePlayer {
	private NicknameHolder getHolder() {
		return (NicknameHolder) ((ServerPlayerEntity) (Object) this).networkHandler;
	}

	@Override
	public void drogtor$setNickname(@Nullable String nickname) {
		getHolder().sn_set(nickname, true);
	}

	@Override
	public void drogtor$setNameColor(@Nullable Formatting fmt) {
		drogstyle$setNameColor(TextColor.fromFormatting(fmt));
	}

	@Override
	public void drogtor$setBio(@Nullable String bio) {

	}

	@Override
	public @Nullable String drogtor$getNickname() {
		Text nickname = getHolder().sn_getParsed();
		if (nickname == null) return null;
		return nickname.getString();
	}

	/**
	 * @return null if the colour isn't a formatting colour.
	 */
	@Override
	public @Nullable Formatting drogtor$getNameColor() {
		TextColor color = drogstyle$getNameColor();
		if (color == null) return null;
		return Formatting.byName(color.getName());
	}

	@Override
	public @Nullable String drogtor$getBio() {
		Text nickname = getHolder().sn_getParsed();
		if (nickname == null) return null;
		HoverEvent hoverEvent = nickname.getStyle().getHoverEvent();
		if (hoverEvent == null) return null;
		Text hoverText = hoverEvent.getValue(HoverEvent.Action.SHOW_TEXT);
		if (hoverText == null) return null;
		return hoverText.getString();
	}

	@Override
	public boolean drogtor$isActive() {
		return getHolder().sn_get() != null;
	}

	@Override
	public void drogstyle$setNameColor(@Nullable TextColor color) {

	}

	@Override
	public @Nullable TextColor drogstyle$getNameColor() {
		Text nickname = getHolder().sn_getParsed();
		if (nickname == null) return null;
		return nickname.getStyle().getColor();
	}
}
