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

import java.util.regex.Pattern;

@Mixin(value = PlayerEntity.class, priority = 1001)
public class PlayerEntityMixin implements DrogtorPlayer, DrogstylePlayer {
	private static final Pattern COLOR_PATTERN = Pattern.compile("<(color:'?|/?)?(yellow|dark_blue|dark_purple|gold|red|aqua|gray|light_purple|white|dark_gray|green|blue|dark_aqua|dark_green|black)'?>|<color:#[0-9a-fA-f]{6}>|</color>", Pattern.CASE_INSENSITIVE);
	private static final Pattern BIO_PATTERN = Pattern.compile("<hover:'?[^<'>]+'?>|</hover>", Pattern.CASE_INSENSITIVE);

	@SuppressWarnings("DataFlowIssue")
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
		String nickname = getHolder().sn_get();
		if (nickname == null) {
			nickname = ((PlayerEntity) (Object) this).getGameProfile().getName();
		}
		nickname = BIO_PATTERN.matcher(nickname).replaceAll("");
		if (nickname.isEmpty()) throw new IllegalStateException("Nickname would be empty!");
		if (bio != null) {
			nickname = "<hover:'%s'>".formatted(bio.replaceAll("[<'>]", "")) + nickname;
		}
		getHolder().sn_set(nickname, true);
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
		return getHolder().sn_shouldDisplay();
	}

	@Override
	public void drogstyle$setNameColor(@Nullable TextColor color) {
		String nickname = getHolder().sn_get();
		if (nickname == null) {
			nickname = ((PlayerEntity) (Object) this).getGameProfile().getName();
		}
		nickname = COLOR_PATTERN.matcher(nickname).replaceAll("");
		if (nickname.isEmpty()) throw new IllegalStateException("Nickname would be empty!");
		if (color != null) {
			nickname = (color.getName().startsWith("#") ? "<color:%s>" : "<%s>").formatted(color.getName()) + nickname;
		}
		getHolder().sn_set(nickname, true);
	}

	@Override
	public @Nullable TextColor drogstyle$getNameColor() {
		Text nickname = getHolder().sn_getParsed();
		if (nickname == null) return null;
		return nickname.getStyle().getColor();
	}
}
