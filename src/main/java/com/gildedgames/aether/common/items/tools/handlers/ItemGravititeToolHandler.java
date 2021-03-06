package com.gildedgames.aether.common.items.tools.handlers;

import com.gildedgames.aether.common.capabilities.entity.player.PlayerAether;
import com.gildedgames.aether.common.capabilities.entity.player.modules.PlayerBlockLevitateModule;
import com.gildedgames.aether.common.items.armor.ItemAetherShield;
import com.gildedgames.aether.common.items.tools.ItemAetherAxe;
import com.gildedgames.aether.common.items.tools.ItemAetherPickaxe;
import com.gildedgames.aether.common.items.tools.ItemAetherShovel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemGravititeToolHandler implements IToolEventHandler
{
	@Override
	public void onHarvestBlock(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityPlayer entity, List<ItemStack> drops)
	{

	}

	private boolean canMine(IBlockState state, ItemStack stack)
	{
		if (stack.getItem() instanceof ItemAetherAxe)
		{
			return ((ItemAetherAxe) stack.getItem()).canMine(state);
		}
		else if (stack.getItem() instanceof ItemAetherPickaxe)
		{
			return ((ItemAetherPickaxe) stack.getItem()).canMine(state);
		}
		else if (stack.getItem() instanceof ItemAetherShovel)
		{
			return ((ItemAetherShovel) stack.getItem()).canMine(state);
		}

		return false;
	}

	@Override
	public boolean onRightClickBlock(World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing)
	{
		if (hand != EnumHand.MAIN_HAND)
		{
			return false;
		}

		PlayerAether aePlayer = PlayerAether.getPlayer(player);

		if (!aePlayer.getEntity().capabilities.allowEdit)
		{
			return false;
		}

		ItemStack stack = player.getHeldItem(hand);

		if (aePlayer.getModule(PlayerBlockLevitateModule.class).getHeldBlock() == null && player.isSneaking())
		{
			IBlockState state = world.getBlockState(pos);

			if (this.canMine(state, stack))
			{
				if (state.getBlock().hasTileEntity(state))
				{
					return false;
				}

				if (!state.isFullBlock() || state.getBlockHardness(world, pos) < 0.0f)
				{
					return false;
				}

				if (!world.isRemote)
				{
					if (aePlayer.getModule(PlayerBlockLevitateModule.class).pickupBlock(pos, world))
					{
						stack.damageItem(2, player);
					}
				}
				else
				{
					for (int i = 0; i < 15; i++)
					{
						world.spawnParticle(EnumParticleTypes.BLOCK_DUST,
								pos.getX() + (world.rand.nextDouble() * 1.2D),
								pos.getY() + (world.rand.nextDouble()),
								pos.getZ() + (world.rand.nextDouble() * 1.2D), 0.0D, 0.0D, 0.0D,
								Block.getStateId(state));
					}
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public void onRightClickItem(EntityPlayer player, EnumHand hand)
	{
		if (hand == EnumHand.MAIN_HAND && !player.world.isRemote)
		{
			PlayerAether aePlayer = PlayerAether.getPlayer(player);
			PlayerBlockLevitateModule blockLevitateModule = aePlayer.getModule(PlayerBlockLevitateModule.class);

			if (blockLevitateModule.getHeldBlock() != null && blockLevitateModule.getHeldBlock().ticksExisted > 1)
			{
				blockLevitateModule.dropHeldBlock();
			}
		}
	}

	@Override
	public void addInformation(ItemStack stack, List<String> tooltip)
	{
		tooltip.add(1, String.format("%s: %s",
				TextFormatting.DARK_AQUA + I18n.format("item.aether.tooltip.use"),
				TextFormatting.WHITE + I18n.format("item.aether.tool.gravitite.use.desc")));

		tooltip.add(1, String.format("%s: %s",
				TextFormatting.BLUE + I18n.format("item.aether.tooltip.ability"),
				TextFormatting.WHITE + I18n.format("item.aether.tool.gravitite.ability.desc")));
	}

	@Override
	public void onEntityHit(ItemStack stack, Entity target, EntityLivingBase attacker)
	{

	}

	@Override
	public float getBreakSpeed(ItemStack stack, IBlockState state, float original)
	{
		return original;
	}
}
