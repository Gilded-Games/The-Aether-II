package com.gildedgames.aether.common.items.other;

import com.gildedgames.aether.common.items.IDropOnDeath;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.List;

public class ItemMoaFeather extends Item implements IDropOnDeath
{

	public ItemMoaFeather()
	{
		this.setHasSubtypes(true);
	}

	public static void setColor(final ItemStack stack, final String colorName, final int color)
	{
		if (stack.getTagCompound() == null)
		{
			stack.setTagCompound(new NBTTagCompound());
		}

		final NBTTagCompound tag = new NBTTagCompound();

		tag.setString("colorName", colorName);
		tag.setInteger("color", color);

		stack.getTagCompound().setTag("featherColor", tag);
	}

	public static int getColor(final ItemStack stack)
	{
		if (stack.getTagCompound() == null)
		{
			ItemMoaFeather.setColor(stack, "", 0xFFFFFF);
		}

		final NBTTagCompound tag = stack.getTagCompound().getCompoundTag("featherColor");

		return tag.getInteger("color");
	}

	@SideOnly(Side.CLIENT)
	public static String getColorName(final ItemStack stack)
	{
		if (stack.getTagCompound() == null)
		{
			ItemMoaFeather.setColor(stack, "", 0xFFFFFF);
		}

		final NBTTagCompound tag = stack.getTagCompound().getCompoundTag("featherColor");

		return tag.getString("colorName");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> subItems)
	{
		if (!this.isInCreativeTab(tab))
		{
			return;
		}

		final ItemStack feather = new ItemStack(this);
		ItemMoaFeather.setColor(feather, "moa.feathers.blue", new Color(0x83c4e2).getRGB());

		subItems.add(feather);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack stack, final World world, final List<String> tooltip, final ITooltipFlag flag)
	{
		final String colorName = ItemMoaFeather.getColorName(stack);

		if (!colorName.isEmpty())
		{
			tooltip.add(TextFormatting.GRAY + "" + TextFormatting.ITALIC + "Color: " + I18n.format(colorName));
		}
	}

	@Override
	public boolean getShareTag()
	{
		return true;
	}

}
