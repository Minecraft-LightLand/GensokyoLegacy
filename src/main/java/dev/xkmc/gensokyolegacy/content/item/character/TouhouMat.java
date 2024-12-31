package dev.xkmc.gensokyolegacy.content.item.character;

import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public enum TouhouMat {
	SUWAKO_HAT(60, 10, 2, 3, () -> Ingredient.of(ModItems.CANVAS.get()), SoundEvents.ARMOR_EQUIP_LEATHER),
	STRAW_HAT(10, 1, 0, 0, () -> Ingredient.of(ModItems.CANVAS.get()), SoundEvents.ARMOR_EQUIP_LEATHER),
	KOISHI_HAT(60, 10, 4, 3, () -> Ingredient.of(Items.IRON_INGOT), SoundEvents.ARMOR_EQUIP_IRON),
	RUMIA_HAIRBAND(60, 10, 0, 0, () -> Ingredient.of(ModItems.CANVAS.get()), SoundEvents.ARMOR_EQUIP_LEATHER),
	REIMU_HAIRBAND(60, 10, 2, 0, () -> Ingredient.of(ModItems.CANVAS.get()), SoundEvents.ARMOR_EQUIP_LEATHER),
	CIRNO_HAIRBAND(60, 10, 0, 0, () -> Ingredient.of(ModItems.CANVAS.get()), SoundEvents.ARMOR_EQUIP_LEATHER),
	;

	private static final int[] DURABILITY = {13, 15, 16, 11};

	private final int durability;
	private final Holder<ArmorMaterial> mat;

	TouhouMat(int durability, int enchantment, int defense, int tough, Supplier<Ingredient> repair, Holder<SoundEvent> sound) {
		this.durability = durability;
		mat = new SimpleEntry<>(GensokyoLegacy.REGISTRATE.simple(
				name().toLowerCase(Locale.ROOT), Registries.ARMOR_MATERIAL, () -> new ArmorMaterial(
						Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
							map.put(ArmorItem.Type.BOOTS, 1 + defense);
							map.put(ArmorItem.Type.LEGGINGS, 2 + defense);
							map.put(ArmorItem.Type.CHESTPLATE, 3 + defense);
							map.put(ArmorItem.Type.HELMET, 1 + defense);
							map.put(ArmorItem.Type.BODY, 3 + defense);
						}),
						enchantment, sound, repair,
						List.of(new ArmorMaterial.Layer(GensokyoLegacy.loc("suwako_hat"))),
						tough, 0)
		));
	}

	public int getDurabilityForType(ArmorItem.Type type) {
		return DURABILITY[type.getSlot().getIndex()] * durability;
	}

	public Holder<ArmorMaterial> holder() {
		return mat;
	}

	public static void register() {
	}

}
