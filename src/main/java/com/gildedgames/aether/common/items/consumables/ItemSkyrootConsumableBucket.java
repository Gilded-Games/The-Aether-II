package com.gildedgames.aether.common.items.consumables;

import com.gildedgames.aether.api.entity.effects.IAetherStatusEffectPool;
import com.gildedgames.aether.api.entity.effects.IAetherStatusEffects;
import com.gildedgames.aether.api.registrar.CapabilitiesAether;
import com.gildedgames.aether.api.registrar.ItemsAether;
import com.gildedgames.aether.common.init.CreativeTabsAether;
import com.gildedgames.aether.common.items.IDropOnDeath;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.Objects;

public class ItemSkyrootConsumableBucket extends Item implements IDropOnDeath
{
	public ItemSkyrootConsumableBucket()
	{
		this.setMaxStackSize(1);

		this.setCreativeTab(CreativeTabsAether.TAB_MISCELLANEOUS);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		playerIn.setActiveHand(hand);

		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving)
	{
		EntityPlayer entityPlayer = entityLiving instanceof EntityPlayer ? (EntityPlayer)entityLiving : null;

		if (entityPlayer instanceof EntityPlayerMP)
		{
			CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityPlayer, stack);
		}

		if (entityPlayer != null)
		{
			entityPlayer.addStat(Objects.requireNonNull(StatList.getObjectUseStats(this)));

			if (!world.isRemote)
			{
				if (stack.getItem() == ItemsAether.skyroot_milk_bucket)
				{
					entityPlayer.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
				}
			}
		}

		if (entityPlayer == null || !entityPlayer.capabilities.isCreativeMode)
		{
			IAetherStatusEffectPool statusEffectPool = (entityPlayer != null)
					? entityPlayer.getCapability(CapabilitiesAether.STATUS_EFFECT_POOL, null)
					: entityLiving.getCapability(CapabilitiesAether.STATUS_EFFECT_POOL, null);

			if (!world.isRemote)
			{
				if (statusEffectPool != null)
				{
					if (stack.getItem() == ItemsAether.skyroot_poison_bucket)
					{
						if (!statusEffectPool.effectExists(IAetherStatusEffects.effectTypes.TOXIN))
						{
							statusEffectPool.applyStatusEffect(IAetherStatusEffects.effectTypes.TOXIN, 50);
						}
						else
						{
							statusEffectPool.modifyActiveEffectBuildup(IAetherStatusEffects.effectTypes.TOXIN,
									statusEffectPool.getBuildupFromEffect(IAetherStatusEffects.effectTypes.TOXIN) + 50);
						}
					}
				}
			}

			stack.shrink(1);
		}

		return stack.getCount() <= 0 ? new ItemStack(ItemsAether.skyroot_bucket) : stack;
	}

	private void applyEffect(ItemStack stack, World world, EntityLivingBase player)
	{
		if (stack.getItem() == ItemsAether.skyroot_milk_bucket)
		{
			player.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
		}
		else if (stack.getItem() == ItemsAether.skyroot_poison_bucket)
		{
			player.addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 3));
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.DRINK;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 32;
	}
}
