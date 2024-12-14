package dev.xkmc.gensokyolegacy.content.entity.behavior.combat;

public record TargetKind(boolean initiateAttack, boolean isPrey, boolean noAdditionalEffect) {

	public static final TargetKind NONE = new TargetKind(false, false, false);
	public static final TargetKind PRAY = new TargetKind(false, true, false);
	public static final TargetKind WORTHY = new TargetKind(false, false, true);
	public static final TargetKind ENEMY = new TargetKind(true, false, true);

}
