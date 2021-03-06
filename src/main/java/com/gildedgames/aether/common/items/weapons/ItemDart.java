package com.gildedgames.aether.common.items.weapons;

import com.gildedgames.aether.common.items.IDropOnDeath;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemDart extends Item implements IDropOnDeath
{
	public static final ItemDartType[] ITEM_VARIANTS = new ItemDartType[] { ItemDartType.GOLDEN, ItemDartType.ENCHANTED,
			ItemDartType.POISON };

	public ItemDart()
	{
		this.setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> subItems)
	{
		if (!this.isInCreativeTab(tab))
		{
			return;
		}

		for (final ItemDartType type : ITEM_VARIANTS)
		{
			subItems.add(new ItemStack(this, 1, type.ordinal()));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack stack, final World world, final List<String> tooltip, final ITooltipFlag flag)
	{
		final ItemDartType type = ITEM_VARIANTS[stack.getItemDamage()];

		final int slashDamageLevel = type.getSlashDamageLevel();
		final int pierceDamageLevel = type.getPierceDamageLevel();
		final int impactDamageLevel = type.getImpactDamageLevel();

		tooltip.add(TextFormatting.GRAY + I18n.format("item.aether.dart.desc"));

		if (slashDamageLevel > 0)
		{
			this.addDamageLevel("slash", slashDamageLevel, tooltip);
		}

		if (pierceDamageLevel > 0)
		{
			this.addDamageLevel("pierce", pierceDamageLevel, tooltip);
		}

		if (impactDamageLevel > 0)
		{
			this.addDamageLevel("impact", impactDamageLevel, tooltip);
		}
	}

	private void addDamageLevel(String damageType, int damageLevel, final List<String> tooltip)
	{
		tooltip.add(TextFormatting.GRAY + String.valueOf(damageLevel) + " " + I18n.format("item.aether.bolt." + damageType));
	}

	@Override
	public String getTranslationKey(final ItemStack stack)
	{
		return super.getTranslationKey(stack) + "." + ItemDartType.fromOrdinal(stack.getMetadata()).getID();
	}
}
