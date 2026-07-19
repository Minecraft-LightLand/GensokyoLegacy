package dev.xkmc.gensokyolegacy.content.block.portal;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class GapPortalItem extends BlockItem {

	public GapPortalItem(Block block, Properties properties) {
		super(block, properties.stacksTo(1));
	}

}
