package com.gildedgames.aether.common.items.consumables;

import com.gildedgames.aether.api.registrar.ItemsAether;
import com.gildedgames.aether.common.capabilities.entity.player.PlayerAether;
import com.gildedgames.aether.common.capabilities.entity.player.modules.PlayerParachuteModule;
import com.gildedgames.aether.common.entities.blocks.EntityParachute;
import com.gildedgames.aether.common.items.IDropOnDeath;
import com.gildedgames.aether.common.util.helpers.ItemHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCloudParachute extends Item implements IDropOnDeath
{
	public ItemCloudParachute()
	{
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
	}

	@Override
	public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> subItems)
	{
		if (this.isInCreativeTab(tab)) {
			for(int i = 0; i < 3; ++i) {
				subItems.add(new ItemStack(this, 1, i));
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack stack, @Nullable final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn)
	{
		tooltip.add(I18n.format("cloudParachute.ability") + " " + "\247r" + I18n.format(
				"cloudParachute.ability." + EntityParachute.Type.fromOrdinal(stack.getMetadata()).name));
		tooltip.add(I18n.format("cloudParachute.ability.rightClick"));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand)
	{
		final ItemStack stack = player.getHeldItem(hand);

		final EntityParachute parachute = new EntityParachute(world, player, EntityParachute.Type.fromOrdinal(stack.getMetadata()));

		final PlayerAether playerAether = PlayerAether.getPlayer(player);

		if (playerAether.getModule(PlayerParachuteModule.class).isParachuting())
		{
			if (playerAether.getModule(PlayerParachuteModule.class).getParachuteType() == EntityParachute.Type.COLD)
			{
				if (playerAether.getModule(PlayerParachuteModule.class).getParachuteType().ordinal() != stack.getMetadata())
				{
					playerAether.getModule(PlayerParachuteModule.class).parachuteEquipped(true);

					spawnParachute(playerAether, world, stack, parachute, hand);
				}
				else if (playerAether.getModule(PlayerParachuteModule.class).getParachuteType().ordinal() == stack.getMetadata())
				{
					playerAether.getModule(PlayerParachuteModule.class).setParachuting(false, playerAether.getModule(PlayerParachuteModule.class).getParachuteType());
				}
			}
		}
		else
		{
			spawnParachute(playerAether, world, stack, parachute, hand);
		}

		return super.onItemRightClick(world, player, hand);
	}

	public void spawnParachute(PlayerAether playerAether, World world, ItemStack stack, EntityParachute parachute, EnumHand hand)
	{
		ItemStack coldParachute = new ItemStack(ItemsAether.cloud_parachute);

		playerAether.getModule(PlayerParachuteModule.class).setParachuting(true, EntityParachute.Type.fromOrdinal(stack.getMetadata()));

		world.spawnEntity(parachute);

		stack.shrink(1);

		playerAether.getModule(PlayerParachuteModule.class).parachuteItem(coldParachute);

		ItemStack parachuteStack = playerAether.getModule(PlayerParachuteModule.class).getParachuteItem();

		if (!playerAether.getEntity().inventory.addItemStackToInventory(parachuteStack))
		{
			playerAether.getEntity().dropItem(parachuteStack, false);
		}

		playerAether.getModule(PlayerParachuteModule.class).parachuteItemSlot(playerAether.getEntity().inventory.currentItem);
	}

	@Override
	public String getTranslationKey(final ItemStack stack)
	{
		return "item.aether." + EntityParachute.Type.fromOrdinal(stack.getMetadata()).name + "_cloud_parachute";
	}

}
