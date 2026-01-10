package dev.xkmc.gensokyolegacy.content.attachment.chunk;

import java.util.List;

public interface IFixableHomeHolder extends IHomeHolder {

	boolean isBroken();

	int doFix(int count, FixStage stage);

	List<BlockFix> popFix(int count, FixStage stage);

}
