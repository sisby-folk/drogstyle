package folk.sisby.drogstyle.mixin;

import com.unascribed.drogtor.DrogtorPlayer;
import eu.pb4.stylednicknames.NicknameHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = PlayerEntity.class, priority = 1001)
public class PlayerEntityMixin implements DrogtorPlayer {
	@Shadow
	public ServerPlayNetworkHandler networkHandler;

	@Override
	public void drogtor$setNickname(@Nullable String nickname) {
		((NicknameHolder) networkHandler).sn_set(nickname, true);
	}

	@Override
	public void drogtor$setNameColor(@Nullable Formatting fmt) {

	}

	@Override
	public void drogtor$setBio(@Nullable String bio) {

	}

	@Override
	public @Nullable String drogtor$getNickname() {
		Text nickname = ((NicknameHolder) networkHandler).sn_getParsed();
		if (nickname == null) return null;
		return nickname.getString();
	}

	/**
	 * @return null if the colour isn't a formatting colour.
	 */
	@Override
	public @Nullable Formatting drogtor$getNameColor() {
		Text nickname = ((NicknameHolder) networkHandler).sn_getParsed();
		if (nickname == null) return null;
		if (nickname.getStyle().getColor() == null) return null;
		return Formatting.byName(nickname.getStyle().getColor().getName());
	}

	@Override
	public @Nullable String drogtor$getBio() {
		Text nickname = ((NicknameHolder) networkHandler).sn_getParsed();
		if (nickname == null) return null;
		HoverEvent hoverEvent = nickname.getStyle().getHoverEvent();
		if (hoverEvent == null) return null;
		Text hoverText = hoverEvent.getValue(HoverEvent.Action.SHOW_TEXT);
		if (hoverText == null) return null;
		return hoverText.getString();
	}

	@Override
	public boolean drogtor$isActive() {
		return ((NicknameHolder) networkHandler).sn_get() != null;
	}
}
