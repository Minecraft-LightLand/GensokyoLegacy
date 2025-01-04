package dev.xkmc.gensokyolegacy.content.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.gensokyolegacy.init.registrate.GLMechanics;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;

public class GLRoleCommands extends GLCommands {

	public static ArgumentBuilder<CommandSourceStack, ?> build() {
		return argument("player", EntityArgument.player())
				.then(literal("clear").requires(e -> e.hasPermission(2))
						.executes(GLRoleCommands::clearRoles))
				.then(literal("set").requires(e -> e.hasPermission(2))
						.then(argument("role", ResourceKeyArgument.key(GLMechanics.ROLES.key()))
								.then(argument("point", IntegerArgumentType.integer(0, 2000))
										.executes(GLRoleCommands::setRolePoints))))
				.then(literal("gain").requires(e -> e.hasPermission(2))
						.then(argument("role", ResourceKeyArgument.key(GLMechanics.ROLES.key()))
								.then(argument("point", IntegerArgumentType.integer(0, 2000))
										.executes(GLRoleCommands::gainRolePoints))));
	}

	private static int clearRoles(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		var e = ctx.getArgument("player", EntitySelector.class);
		var player = e.findSinglePlayer(ctx.getSource());
		var data = GLMeta.ABILITY.type().getOrCreate(player);
		data.commandClearRoles();
		return 0;
	}

	private static int setRolePoints(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		var e = ctx.getArgument("player", EntitySelector.class);
		var role = resolveKey(ctx, "role", GLMechanics.ROLES.key(), ERR_INVALID_NAME);
		int point = ctx.getArgument("point", Integer.class);
		var player = e.findSinglePlayer(ctx.getSource());
		var data = GLMeta.ABILITY.type().getOrCreate(player);
		data.commandSetRolePoints(role.value(), point);
		ctx.getSource().sendSystemMessage(GLLang.COMMAND$SUCCESS.get());
		return 0;
	}

	private static int gainRolePoints(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		var e = ctx.getArgument("player", EntitySelector.class);
		var role = resolveKey(ctx, "role", GLMechanics.ROLES.key(), ERR_INVALID_NAME);
		int point = ctx.getArgument("point", Integer.class);
		var player = e.findSinglePlayer(ctx.getSource());
		role.value().startOrAdvance(player, 2000, point);
		ctx.getSource().sendSystemMessage(GLLang.COMMAND$SUCCESS.get());
		return 0;
	}


	private static final DynamicCommandExceptionType ERR_INVALID_NAME =
			new DynamicCommandExceptionType(GLLang.COMMAND$INVALID_ROLE::get);


	private static <T> ResourceKey<T> getRegistryKey(
			CommandContext<CommandSourceStack> ctx, String name,
			ResourceKey<Registry<T>> reg, DynamicCommandExceptionType err
	) throws CommandSyntaxException {
		ResourceKey<?> ans = ctx.getArgument(name, ResourceKey.class);
		Optional<ResourceKey<T>> optional = ans.cast(reg);
		return optional.orElseThrow(() -> err.create(ans));
	}

	private static <T> Registry<T> getRegistry(CommandContext<CommandSourceStack> ctx, ResourceKey<? extends Registry<T>> reg) {
		return ctx.getSource().getServer().registryAccess().registryOrThrow(reg);
	}

	private static <T> Holder.Reference<T> resolveKey(
			CommandContext<CommandSourceStack> ctx, String name,
			ResourceKey<Registry<T>> reg, DynamicCommandExceptionType err
	) throws CommandSyntaxException {
		ResourceKey<T> ans = getRegistryKey(ctx, name, reg, err);
		return getRegistry(ctx, reg).getHolder(ans).orElseThrow(() -> err.create(ans.location()));
	}

}
