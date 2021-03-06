package com.gildedgames.aether.common.blocks.natural;

import com.gildedgames.aether.common.blocks.IBlockMultiName;
import com.gildedgames.aether.common.blocks.properties.BlockVariant;
import com.gildedgames.aether.common.blocks.properties.PropertyVariant;
import com.gildedgames.aether.common.blocks.util.BlockBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockIceCrystal extends BlockBuilder implements IBlockMultiName
{
	public static final int CRYSTAL_META = 0;
	public static final int CRYSTAL_A_META = 1;
	public static final int CRYSTAL_B_META = 2;

	public static final BlockVariant
			STALAGMITE = new BlockVariant(0, "stalagmite"),
			STALAGMITE_A = new BlockVariant(1, "stalagmite_a"),
			STALAGMITE_B = new BlockVariant(2, "stalagmite_b"),
			STALACTITE = new BlockVariant(3, "stalactite"),
			STALACTITE_A = new BlockVariant(4, "stalactite_a"),
			STALACTITE_B = new BlockVariant(5, "stalactite_b");

	public static final PropertyVariant PROPERTY_VARIANT = PropertyVariant.create("variant",
			STALAGMITE, STALAGMITE_A, STALAGMITE_B, STALACTITE, STALACTITE_A, STALACTITE_B);

	private static final AxisAlignedBB STALACTITE_BB = new AxisAlignedBB(0.25D, 0.25D, 0.25D, 0.75D, 1D, 0.75D);

	private static final AxisAlignedBB STALACTITE_SHORT_BB = new AxisAlignedBB(0.25D, 0.5D, 0.25F, 0.75D, 1D, 0.75D);

	private static final AxisAlignedBB STALAGMITE_BB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1D, 0.75D);

	private static final AxisAlignedBB STALAGMITE_SHORT_BB = new AxisAlignedBB(0.25D, 0.0D, 0.25F, 0.75D, 0.5D, 0.75D);

	public BlockIceCrystal()
	{
		super(Material.GLASS);
		this.setSoundType(SoundType.GLASS);
		this.setHardness(0.25f);
		this.setResistance(1f);
		this.setTickRandomly(true);
	}

	@Override
	public int damageDropped(final IBlockState state)
	{
		return STALAGMITE.getMeta();
	}

	@Override
	public IBlockState getStateFromMeta(final int meta)
	{
		final BlockVariant variant = PROPERTY_VARIANT.fromMeta(meta);
		return this.getDefaultState().withProperty(PROPERTY_VARIANT, variant);
	}

	@Override
	public int getMetaFromState(final IBlockState state)
	{
		return state.getValue(PROPERTY_VARIANT).getMeta();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PROPERTY_VARIANT);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) > 11 - this.getDefaultState().getLightOpacity(worldIn, pos))
		{
			worldIn.setBlockToAir(pos);
		}
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.005F);

		if (this.canSilkHarvest(worldIn, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0)
		{
			java.util.List<ItemStack> items = new java.util.ArrayList<ItemStack>();
			ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this), 1, 0);

			if (!itemstack.isEmpty())
			{
				items.add(itemstack);
			}

			net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true, player);
			for (ItemStack item : items)
			{
				spawnAsEntity(worldIn, pos, item);
			}
		}
		else
		{
			harvesters.set(player);
			int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			this.dropBlockAsItem(worldIn, pos, state, i);
			harvesters.set(null);
		}
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
	}

	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
	{
		if (entityIn instanceof EntityPlayer)
		{
			worldIn.getBlockState(pos).getBlock().removedByPlayer(worldIn.getBlockState(pos), worldIn, pos, (EntityPlayer) entityIn, false);
			if (entityIn.world.isRemote)
			{
				entityIn.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 1f);

				for (int i = 0; i < 50; i++)
				{
					final double x = pos.getX() + worldIn.rand.nextDouble();
					final double y = pos.getY() + 1.0D;
					final double z = pos.getZ() + worldIn.rand.nextDouble();

					worldIn.spawnParticle(EnumParticleTypes.WATER_SPLASH, x, y, z, 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (playerIn.isCreative())
		{
			worldIn.setBlockState(pos, state.cycleProperty(PROPERTY_VARIANT));
			return true;
		}
		return false;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{

		if (state.getValue(PROPERTY_VARIANT) == STALACTITE || state.getValue(PROPERTY_VARIANT) == STALACTITE_A || state.getValue(PROPERTY_VARIANT) == STALACTITE_B)
		{
			if (worldIn.isAirBlock(pos.up()) || worldIn.getBlockState(pos.up()).getBlock() == Blocks.WATER)
			{
				worldIn.setBlockToAir(pos);
			}
		}
		if (state.getValue(PROPERTY_VARIANT) == STALAGMITE || state.getValue(PROPERTY_VARIANT) == STALAGMITE_A || state.getValue(PROPERTY_VARIANT) == STALAGMITE_B)
		{
			if (worldIn.isAirBlock(pos.down()) || worldIn.getBlockState(pos.down()).getBlock() == Blocks.WATER)
			{
				worldIn.setBlockToAir(pos);
			}
		}
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer)
	{
		if (hitY == 0)
		{
			return this.getDefaultState().withProperty(PROPERTY_VARIANT, PROPERTY_VARIANT.fromMeta(meta+3));
		}

		return this.getDefaultState().withProperty(PROPERTY_VARIANT, PROPERTY_VARIANT.fromMeta(meta));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		int meta = state.getValue(PROPERTY_VARIANT).getMeta();

		if (meta == STALAGMITE.getMeta() || meta == STALAGMITE_A.getMeta() || meta == STALAGMITE_B.getMeta())
		{
			if (meta == STALAGMITE_B.getMeta()) {
				return STALAGMITE_SHORT_BB;
			}
			return STALAGMITE_BB;
		}

		if (meta == STALACTITE_B.getMeta()) {
			return STALACTITE_SHORT_BB;
		}
		return STALACTITE_BB;
	}

	@Override
	public String getTranslationKey(ItemStack stack)
	{
		return PROPERTY_VARIANT.fromMeta(CRYSTAL_META).getName();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isTranslucent(IBlockState state)
	{
		return true;
	}

	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Block.EnumOffsetType getOffsetType()
	{
		return EnumOffsetType.XZ;
	}

	@Override
	protected boolean canSilkHarvest()
	{
		return true;
	}
}
