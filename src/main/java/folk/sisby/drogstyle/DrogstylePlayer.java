package folk.sisby.drogstyle;

import com.unascribed.drogtor.DrogtorPlayer;
import net.minecraft.text.TextColor;
import org.jetbrains.annotations.Nullable;

public interface DrogstylePlayer extends DrogtorPlayer {
	void drogstyle$setNameColor(@Nullable TextColor color);
	@Nullable TextColor drogstyle$getNameColor();
}
