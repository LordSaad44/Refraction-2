package com.teamwizardry.refraction.api.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by Demoniaque.
 */
public class BlockUtils {

	private static UUID uuid = UUID.randomUUID();
	private static GameProfile breaker = new GameProfile(uuid, "Refraction Block Breaker");
	private static GameProfile placer = new GameProfile(uuid, "Refraction Block Placer");

	/**
	 * Tries placing a block safely and fires an event for it.
	 *
	 * @return Whether the block was successfully placed
	 */
	public static boolean placeBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nullable EntityPlayerMP player) {
		if (!world.isBlockLoaded(pos)) return false;

		EntityPlayerMP playerMP;
		if (player == null) {
			playerMP = new FakePlayer((WorldServer) world, placer);
			playerMP.setPosition(pos.getX(), pos.getY(), pos.getZ());
		} else playerMP = player;

		if (!hasEditPermission(pos, playerMP)) return false;

		BlockEvent.PlaceEvent event = new BlockEvent.PlaceEvent(BlockSnapshot.getBlockSnapshot(world, pos), Blocks.AIR.getDefaultState(), playerMP, playerMP.getActiveHand());
		MinecraftForge.EVENT_BUS.post(event);
		return !event.isCanceled() && world.setBlockState(pos, state);
	}

	/**
	 * Tries breaking a block safely and fires an event for it.
	 *
	 * @return Whether the block was successfully broken
	 */
	public static boolean breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nullable IBlockState oldState, @Nullable EntityPlayerMP player, boolean drop) {
		if (!world.isBlockLoaded(pos)) return false;

		EntityPlayerMP playerMP;
		if (player == null) {
			playerMP = new FakePlayer((WorldServer) world, breaker);
			playerMP.setPosition(pos.getX(), pos.getY(), pos.getZ());
		} else playerMP = player;

		if (!hasBreakPermission(world, pos, playerMP)) return false;

		if (oldState == null) oldState = world.getBlockState(pos);

		BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, oldState, playerMP);

		MinecraftForge.EVENT_BUS.post(event);
		return !event.isCanceled() && (drop ? world.destroyBlock(pos, true) : world.setBlockToAir(pos));
	}

	public static boolean hasBreakPermission(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EntityPlayerMP player) {
		return hasEditPermission(pos, player) && !(ForgeHooks.onBlockBreakEvent(world, player.interactionManager.getGameType(), player, pos) == -1);
	}

	public static boolean hasEditPermission(@Nonnull BlockPos pos, @Nonnull EntityPlayerMP player) {
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isBlockProtected(player.getEntityWorld(), pos, player))
			return false;

		for (EnumFacing e : EnumFacing.VALUES)
			if (!player.canPlayerEdit(pos, e, player.getHeldItemMainhand()))
				return false;
		return true;
	}
}
