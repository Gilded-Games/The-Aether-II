package com.gildedgames.aether.common.blocks.containers;

import com.gildedgames.aether.common.AetherCore;
import com.gildedgames.aether.common.entities.tiles.TileEntityIncubator;
import com.gildedgames.aether.common.entities.tiles.TileEntityMasonryBench;
import com.gildedgames.aether.common.network.AetherGuiHandler;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockIncubator extends BlockContainer
{

	public static final PropertyBool PROPERTY_IS_LIT = PropertyBool.create("is_lit");

	public static final PropertyDirection PROPERTY_FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public static final int UNLIT_META = 0, LIT_META = 1;

	protected static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.8D, 1.0D);

	public BlockIncubator()
	{
		super(Material.ROCK);

		this.setHardness(3.5f);
		this.setResistance(17.5f);

		this.setSoundType(SoundType.STONE);

		this.setDefaultState(this.getBlockState().getBaseState().withProperty(PROPERTY_IS_LIT, Boolean.FALSE).withProperty(PROPERTY_FACING, EnumFacing.NORTH));
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileEntityIncubator)
		{
			InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileEntity);
		}

		super.breakBlock(world, pos, state);
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
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BOUNDS;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return BOUNDS;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side,
			float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			player.openGui(AetherCore.INSTANCE, AetherGuiHandler.INCUBATOR_ID, world, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return state.getValue(PROPERTY_IS_LIT) ? 13 : 0;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
											EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(PROPERTY_FACING, placer.getHorizontalFacing());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		worldIn.setBlockState(pos, state.withProperty(PROPERTY_FACING, placer.getHorizontalFacing()));

		if (stack.hasDisplayName())
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityIncubator)
			{
				((TileEntityIncubator)tileentity).setCustomInventoryName(stack.getDisplayName());
			}
		}
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		int meta = state.getValue(PROPERTY_FACING).getIndex();

		if (state.getValue(PROPERTY_IS_LIT))
		{
			meta |= 8;
		}

		return meta;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		boolean isLit = (meta & 8) == 8;

		return this.getDefaultState().withProperty(PROPERTY_FACING, EnumFacing.byHorizontalIndex(meta)).withProperty(PROPERTY_IS_LIT, isLit);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PROPERTY_IS_LIT, PROPERTY_FACING);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityIncubator();
	}

}
