package folk.sisby.drogstyle.mixin;

import com.unascribed.drogtor.DrogtorPlayer;
import eu.pb4.stylednicknames.NicknameHolder;
import folk.sisby.drogstyle.DrogstylePlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(value = PlayerEntity.class, priority = 1001)
public class PlayerEntityMixin implements DrogtorPlayer, DrogstylePlayer {
	@Unique private static final Pattern COLOR_PATTERN = Pattern.compile("(<(color:'?|/?)?(yellow|dark_blue|dark_purple|gold|red|aqua|gray|light_purple|white|dark_gray|green|blue|dark_aqua|dark_green|black)'?>|<color:#[0-9a-fA-f]{6}>|</color>)", Pattern.CASE_INSENSITIVE);
	@Unique private static final Pattern BIO_PATTERN = Pattern.compile("(<hover:'?[^<'>]+'?>)|</hover>", Pattern.CASE_INSENSITIVE);

	@Override
	public void drogtor$setNickname(@Nullable String nickname) {
		String newName = nickname;
		if (nickname != null) {
			String oldName = NicknameHolder.of(this).sn_get();
			if (oldName != null) {
				Matcher oldColor = COLOR_PATTERN.matcher(oldName);
				if (oldColor.find() && oldColor.group(1) != null && !oldColor.group(1).contains("/")) {
					newName = oldColor.group(1) + newName;
				}
				Matcher oldBio = BIO_PATTERN.matcher(oldName);
				if (oldBio.find() && oldBio.group(1) != null && !oldBio.group(1).contains("/")) {
					newName = oldBio.group(1) + newName;
				}
			}
		}
		NicknameHolder.of(this).styledNicknames$set(newName, true);
	}

	@Override
	public void drogtor$setNameColor(@Nullable Formatting fmt) {
		drogstyle$setNameColor(TextColor.fromFormatting(fmt));
	}

	@Override
	public void drogtor$setBio(@Nullable String bio) {
		String nickname = NicknameHolder.of(this).styledNicknames$get();
		if (nickname == null) {
			nickname = ((PlayerEntity) (Object) this).getGameProfile().getName();
		}
		nickname = BIO_PATTERN.matcher(nickname).replaceAll("");
		if (nickname.isEmpty()) throw new IllegalStateException("Nickname would be empty!");
		if (bio != null) {
			nickname = "<hover:'%s'>".formatted(bio.replaceAll("[<'>]", "")) + nickname;
		}
		NicknameHolder.of(this).styledNicknames$set(nickname, true);
	}

	@Override
	public @Nullable String drogtor$getNickname() {
		Text nickname = NicknameHolder.of(this).styledNicknames$getParsed();
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
		Text nickname = NicknameHolder.of(this).styledNicknames$getParsed();
		if (nickname == null) return null;
		HoverEvent hoverEvent = nickname.getStyle().getHoverEvent();
		if (hoverEvent == null) return null;
		Text hoverText = hoverEvent.getValue(HoverEvent.Action.SHOW_TEXT);
		if (hoverText == null) return null;
		return hoverText.getString();
	}

	@Override
	public boolean drogtor$isActive() {
		return NicknameHolder.of(this).styledNicknames$shouldDisplay();
	}

	@Override
	public void drogstyle$setNameColor(@Nullable TextColor color) {
		String nickname = NicknameHolder.of(this).styledNicknames$get();
		if (nickname == null) {
			nickname = ((PlayerEntity) (Object) this).getGameProfile().getName();
		}
		nickname = COLOR_PATTERN.matcher(nickname).replaceAll("");
		if (nickname.isEmpty()) throw new IllegalStateException("Nickname would be empty!");
		if (color != null) {
			nickname = (color.getName().startsWith("#") ? "<color:%s>" : "<%s>").formatted(color.getName()) + nickname;
		}
		NicknameHolder.of(this).styledNicknames$set(nickname, true);
	}

	@Override
	public @Nullable TextColor drogstyle$getNameColor() {
		Text nickname = NicknameHolder.of(this).styledNicknames$getParsed();
		if (nickname == null) return null;
		return nickname.getStyle().getColor();
	}
}
