package com.teamwizardry.refraction.common.block;

import com.teamwizardry.refraction.client.render.blocks.RenderMirror;
import com.teamwizardry.refraction.common.tile.TileMirror;
import com.teamwizardry.refraction.common.tile.TileMirrorBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Demoniaque
 */
public class BlockMirror extends BlockMirrorBase {

	public BlockMirror() {
		super("mirror", Material.GLASS);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(@NotNull World world, @NotNull IBlockState state) {
		return new TileMirror();
	}

	@NotNull
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}
