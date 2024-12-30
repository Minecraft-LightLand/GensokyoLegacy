package dev.xkmc.gensokyolegacy.content.item.character;

import com.google.common.base.Suppliers;
import dev.xkmc.danmakuapi.api.IDanmakuEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TouhouHatItem extends ArmorItem {

	private final Supplier<ItemAttributeModifiers> defaultModifiers;

	public TouhouHatItem(Properties properties, TouhouMat mat) {
		super(mat.holder(), Type.HELMET, properties.durability(mat.getDurabilityForType(Type.HELMET)));
		this.defaultModifiers = Suppliers.memoize(() -> {
			ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
			EquipmentSlotGroup group = EquipmentSlotGroup.bySlot(type.getSlot());
			ResourceLocation id = ResourceLocation.withDefaultNamespace("armor." + type.getName());
			builder.add(Attributes.ARMOR, new AttributeModifier(id, material.value().getDefense(type),
					AttributeModifier.Operation.ADD_VALUE), group);
			builder.add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(id, material.value().toughness(),
					AttributeModifier.Operation.ADD_VALUE), group);
			float res = material.value().knockbackResistance();
			if (res > 0.0F) {
				builder.add(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(id, res, AttributeModifier.Operation.ADD_VALUE), group);
			}
			addModifiers(builder);
			return builder.build();
		});
	}

	protected void addModifiers(ItemAttributeModifiers.Builder builder) {
	}

	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers() {
		return this.defaultModifiers.get();
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
		return Math.min(amount, 1);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player && slotId == 36) tick(stack, level, player);
	}

	protected void tick(ItemStack stack, Level level, Player player) {
	}

	public boolean support(DyeColor color) {
		return false;
	}

	public DamageSource modifyDamageType(ItemStack stack, LivingEntity le, IDanmakuEntity danmaku, DamageSource type) {
		return type;
	}

	public void onHurtTarget(ItemStack head, DamageSource source, LivingEntity target) {
	}

}
