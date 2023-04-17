package folk.sisby.drogstyle;

import net.minecraft.text.TextColor;
import org.jetbrains.annotations.Nullable;

public interface DrogstylePlayer {
	void drogstyle$setNameColor(@Nullable TextColor color);
	@Nullable TextColor drogstyle$getNameColor();
}
