package folk.sisby.drogstyle.mixin;

import com.unascribed.drogtor.DrogtorPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = PlayerEntity.class, priority = 1001)
public class PlayerEntityMixin implements DrogtorPlayer {
	@Override
	public void drogtor$setNickname(@Nullable String nickname) {

	}

	@Override
	public void drogtor$setNameColor(@Nullable Formatting fmt) {

	}

	@Override
	public void drogtor$setBio(@Nullable String bio) {

	}

	@Override
	public @Nullable String drogtor$getNickname() {
		return null;
	}

	@Override
	public @Nullable Formatting drogtor$getNameColor() {
		return null;
	}

	@Override
	public @Nullable String drogtor$getBio() {
		return null;
	}

	@Override
	public boolean drogtor$isActive() {
		return false;
	}
}
