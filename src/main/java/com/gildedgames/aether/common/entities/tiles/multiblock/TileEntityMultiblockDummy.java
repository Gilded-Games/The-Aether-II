package com.gildedgames.aether.common.entities.tiles.multiblock;

import com.gildedgames.aether.api.world.IWorldObjectHoverable;
import com.gildedgames.aether.common.AetherCore;
import com.gildedgames.aether.common.entities.tiles.TileEntitySynced;
import com.gildedgames.orbis.lib.util.mc.NBTHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TileEntityMultiblockDummy extends TileEntitySynced implements ITileEntityMultiblock, IWorldObjectHoverable
{
	private BlockPos controllerPosOffset;

	@Override
	public boolean onInteract(final EntityPlayer player)
	{
		BlockPos linked = this.getLinkedController();

		if (linked != null)
		{
			final TileEntity entity = this.world.getTileEntity(linked);

			if (entity instanceof TileEntityMultiblockController)
			{
				((ITileEntityMultiblock) entity).onInteract(player);

				return true;
			}
			else
			{
				AetherCore.LOGGER.warn("TileEntityMultiblockDummy at " + this.pos.toString() + ", is missing it's linked controller at "
						+ linked.toString());
			}
		}

		return false;
	}

	@Override
	public void onDestroyed()
	{
		BlockPos linked = this.getLinkedController();

		if (linked == null)
		{
			return;
		}

		final TileEntity entity = this.world.getTileEntity(linked);

		if (entity instanceof ITileEntityMultiblock)
		{
			((ITileEntityMultiblock) entity).onDestroyed();
		}
		else
		{
			AetherCore.LOGGER.warn("TileEntityMultiblockDummy at " + this.pos.toString() + ", is missing it's linked controller at "
					+ this.getLinkedController().toString());
		}
	}

	@Override
	public ItemStack getPickedStack(final World world, final BlockPos pos, final IBlockState state)
	{
		BlockPos linked = this.getLinkedController();

		if (linked == null)
		{
			return ItemStack.EMPTY;
		}

		final TileEntity entity = this.world.getTileEntity(linked);

		if (entity instanceof ITileEntityMultiblock)
		{
			return ((ITileEntityMultiblock) entity).getPickedStack(world, pos, state);
		}
		else
		{
			AetherCore.LOGGER.warn("TileEntityMultiblockDummy at " + this.pos.toString() + ", is missing it's linked controller at "
					+ this.getLinkedController().toString());
		}

		return ItemStack.EMPTY;
	}

	protected void linkController(final BlockPos controllerPos)
	{
		this.controllerPosOffset = controllerPos.add(-this.pos.getX(), -this.pos.getY(), -this.pos.getZ());
	}

	@Nullable
	protected BlockPos getLinkedController()
	{
		if (this.controllerPosOffset == null)
		{
			return null;
		}

		return this.pos.add(this.controllerPosOffset);
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		if (compound.hasKey("controller"))
		{
			this.controllerPosOffset = NBTHelper.readBlockPos(compound.getCompoundTag("controller"));
		}
		else
		{
			this.invalidate();
		}
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		if (this.controllerPosOffset != null)
		{
			compound.setTag("controller", NBTHelper.writeBlockPos(this.controllerPosOffset));
		}

		return compound;
	}

	@Override
	public ITextComponent getHoverText(World world, RayTraceResult result)
	{
		BlockPos linked = this.getLinkedController();

		if (linked == null)
		{
			return null;
		}

		TileEntity entity = this.world.getTileEntity(linked);

		if (entity instanceof IWorldObjectHoverable)
		{
			return ((IWorldObjectHoverable) entity).getHoverText(world, result);
		}

		return null;
	}
}
