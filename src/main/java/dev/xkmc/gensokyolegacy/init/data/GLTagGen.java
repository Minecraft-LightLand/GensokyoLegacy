package dev.xkmc.gensokyolegacy.init.data;

import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.structure.GLStructureTagGen;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class GLTagGen {

	public static final TagKey<Item> INCENSE_ITEM = item("incense_item");
	public static final TagKey<Item> RAW_FLESH = item("raw_flesh");
	public static final TagKey<Item> FLESH_FOOD = item("flesh_food");
	public static final TagKey<Item> APPARENT_FLESH_FOOD = item("apparent_flesh_food");

	public static final TagKey<Item> TOUHOU_HAT = item("touhou_hat");
	public static final TagKey<Item> TOUHOU_WINGS = item("touhou_wings");

	public static final TagKey<Block> TOTEMS = block("ritual_totem");

	public static final TagKey<EntityType<?>> FLESH_SOURCE = entity("flesh_source");
	public static final TagKey<EntityType<?>> YOUKAI_IGNORE = entity("youkai_ignore");

	public static final TagKey<EntityType<?>> SKULL_SOURCE = entity("drops_skeleton_skull");
	public static final TagKey<EntityType<?>> WITHER_SOURCE = entity("drops_wither_skull");
	public static final TagKey<EntityType<?>> ZOMBIE_SOURCE = entity("drops_zombie_head");
	public static final TagKey<EntityType<?>> CREEPER_SOURCE = entity("drops_creeper_head");
	public static final TagKey<EntityType<?>> PIGLIN_SOURCE = entity("drops_piglin_head");


	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		GLStructureTagGen.genBlockTag(pvd);
	}

	public static void onItemTagGen(RegistrateItemTagsProvider pvd) {

	}

	public static void onEntityTagGen(RegistrateTagsProvider.IntrinsicImpl<EntityType<?>> pvd) {
		pvd.addTag(FLESH_SOURCE).add(EntityType.EVOKER, EntityType.PILLAGER, EntityType.VINDICATOR, EntityType.ILLUSIONER, EntityType.WITCH,
				EntityType.VILLAGER, EntityType.WANDERING_TRADER, EntityType.PLAYER);

		pvd.addTag(SKULL_SOURCE).add(EntityType.SKELETON, EntityType.STRAY);
		pvd.addTag(WITHER_SOURCE).add(EntityType.WITHER_SKELETON, EntityType.WITHER);
		pvd.addTag(ZOMBIE_SOURCE).add(EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.HUSK, EntityType.DROWNED);
		pvd.addTag(CREEPER_SOURCE).add(EntityType.CREEPER);
		pvd.addTag(PIGLIN_SOURCE).add(EntityType.PIGLIN, EntityType.PIGLIN_BRUTE);

		pvd.addTag(YOUKAI_IGNORE).add(EntityType.ENDER_DRAGON);
	}

	public static TagKey<Item> item(String id) {
		return ItemTags.create(GensokyoLegacy.loc(id));
	}

	public static TagKey<Block> block(String id) {
		return BlockTags.create(GensokyoLegacy.loc(id));
	}

	public static TagKey<EntityType<?>> entity(String id) {
		return TagKey.create(Registries.ENTITY_TYPE, GensokyoLegacy.loc(id));
	}

}
