package com.unascribed.drogtor;

import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

public interface DrogtorPlayer {
	void drogtor$setNickname(@Nullable String nickname);

	void drogtor$setNameColor(@Nullable Formatting fmt);

	void drogtor$setBio(@Nullable String bio);

	@Nullable
	String drogtor$getNickname();

	@Nullable
	Formatting drogtor$getNameColor();

	@Nullable
	String drogtor$getBio();

	boolean drogtor$isActive();
}
