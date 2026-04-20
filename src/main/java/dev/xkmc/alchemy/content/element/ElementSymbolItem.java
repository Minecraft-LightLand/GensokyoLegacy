package dev.xkmc.alchemy.content.element;

import net.minecraft.world.item.Item;

public class ElementSymbolItem extends Item {

	private final AlchemyElement element;

	public ElementSymbolItem(Properties properties, AlchemyElement element) {
		super(properties);
		this.element = element;
	}

}
