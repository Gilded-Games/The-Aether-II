package com.gildedgames.aether.common.blocks.containers;

import com.gildedgames.aether.api.AetherAPI;
import com.gildedgames.aether.api.registrar.ItemsAether;
import com.gildedgames.aether.common.entities.tiles.TileEntityAltar;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAltar extends Block implements ITileEntityProvider
{
	public static final PropertyDirection PROPERTY_FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockAltar()
	{
		super(Material.ROCK);

		this.setHardness(3.5f);
		this.setResistance(17.5f);

		this.setSoundType(SoundType.STONE);

		this.setDefaultState(this.getBlockState().getBaseState().withProperty(PROPERTY_FACING, EnumFacing.NORTH));
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntityAltar altar = (TileEntityAltar) world.getTileEntity(pos);
		altar.dropContents();

		super.breakBlock(world, pos, state);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(PROPERTY_FACING, placer.getHorizontalFacing());
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(PROPERTY_FACING, EnumFacing.byHorizontalIndex(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(PROPERTY_FACING).getIndex();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side,
			float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			TileEntityAltar altar = (TileEntityAltar) world.getTileEntity(pos);

			ItemStack heldStack = player.inventory.getCurrentItem();

			if (heldStack.isEmpty() || heldStack.isItemEqual(altar.getStackOnAltar()))
			{
				this.dropNextItem(altar, world);
			}
			else if (heldStack.getItem() == ItemsAether.ambrosium_shard)
			{
				if (altar.getAmbrosiumCount() < 16)
				{
					if (!player.capabilities.isCreativeMode)
					{
						heldStack.shrink(1);
					}

					altar.addAmbrosiumShard();
				}
			}
			else if (AetherAPI.content().altar().getMatchingRecipeFromInput(heldStack) != null)
			{
				ItemStack stack = heldStack.copy();
				stack.setCount(1);

				if (!altar.getStackOnAltar().isEmpty())
				{
					world.spawnEntity(altar.createEntityItemAboveAltar(altar.getStackOnAltar()));
				}

				altar.setStackOnAltar(stack);

				heldStack.shrink(1);
			}

			altar.attemptCrafting();
		}

		return true;
	}

	private void dropNextItem(TileEntityAltar altar, World world)
	{
		ItemStack stack = null;

		if (!altar.getStackOnAltar().isEmpty())
		{
			stack = altar.getStackOnAltar();

			altar.setStackOnAltar(ItemStack.EMPTY);
		}
		else if (altar.getAmbrosiumCount() > 0)
		{
			stack = new ItemStack(ItemsAether.ambrosium_shard, 1);

			altar.removeAmbrosiumShard();
		}

		if (stack != null)
		{
			world.spawnEntity(altar.createEntityItemAboveAltar(stack));
		}
	}

	@SideOnly(Side.CLIENT)
	public boolean hasCustomBreakingProgress(IBlockState state)
	{
		return true;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityAltar();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PROPERTY_FACING);
	}
}
