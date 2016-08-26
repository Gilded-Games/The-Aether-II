package com.gildedgames.aether.common;

import com.gildedgames.aether.api.capabilites.AetherCapabilities;
import com.gildedgames.aether.api.capabilites.entity.effects.EntityEffectInstance;
import com.gildedgames.aether.api.capabilites.entity.effects.EntityEffectProcessor;
import com.gildedgames.aether.api.capabilites.entity.effects.IEntityEffectsCapability;
import com.gildedgames.aether.api.capabilites.items.effects.IItemEffectsCapability;
import com.gildedgames.aether.api.capabilites.items.extra_data.IItemExtraDataCapability;
import com.gildedgames.aether.api.capabilites.items.properties.IItemPropertiesCapability;
import com.gildedgames.aether.api.player.IPlayerAetherCapability;
import com.gildedgames.aether.common.capabilities.item.effects.ItemEffects;
import com.gildedgames.aether.common.capabilities.item.effects.ItemEffectsProvider;
import com.gildedgames.aether.common.capabilities.item.extra_data.ItemExtraDataImpl;
import com.gildedgames.aether.common.capabilities.item.extra_data.ItemExtraDataProvider;
import com.gildedgames.aether.common.capabilities.item.properties.ItemPropertiesImpl;
import com.gildedgames.aether.common.capabilities.item.properties.ItemPropertiesProvider;
import com.gildedgames.aether.common.capabilities.player.PlayerAetherImpl;
import com.gildedgames.aether.common.capabilities.player.PlayerAetherProvider;
import com.gildedgames.aether.common.entities.effects.EntityEffects;
import com.gildedgames.aether.common.entities.effects.EntityEffectsProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;

public class AetherCapabilityManager
{

	public static void init()
	{
		MinecraftForge.EVENT_BUS.register(AetherCapabilityManager.class);

		CapabilityManager.INSTANCE.register(IItemEffectsCapability.class, new ItemEffects.Storage(), ItemEffects.class);
		CapabilityManager.INSTANCE.register(IItemPropertiesCapability.class, new ItemPropertiesImpl.Storage(), ItemPropertiesImpl.class);
		CapabilityManager.INSTANCE.register(IPlayerAetherCapability.class, new PlayerAetherImpl.Storage(), PlayerAetherImpl.class);
		CapabilityManager.INSTANCE.register(IEntityEffectsCapability.class, new EntityEffects.Storage(), EntityEffects.class);
		CapabilityManager.INSTANCE.register(IItemExtraDataCapability.class, new ItemExtraDataImpl.Storage(), ItemExtraDataImpl.class);
	}

	@SubscribeEvent
	public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
	{

	}

	@SubscribeEvent
	public static void onPlayerInteract(PlayerInteractEvent event)
	{
		EntityPlayer player = event.getEntityPlayer();

		ItemStack stack = player.getActiveItemStack();

		if (stack != null && stack.hasCapability(AetherCapabilities.ITEM_PROPERTIES, null))
		{
			IItemPropertiesCapability props = stack.getCapability(AetherCapabilities.ITEM_PROPERTIES, null);

			IPlayerAetherCapability aePlayer = PlayerAetherImpl.getPlayer(player);

			if (props != null && props.isEquippable())
			{
				int nextEmptySlot = aePlayer.getEquipmentInventory().getNextEmptySlotForType(props.getEquipmentType());

				if (nextEmptySlot != -1)
				{
					aePlayer.getEquipmentInventory().setInventorySlotContents(nextEmptySlot, stack.copy());

					if (!player.capabilities.isCreativeMode)
					{
						player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityLoad(AttachCapabilitiesEvent.Entity event)
	{
		if (event.getEntity() == null)
		{
			return;
		}

		event.addCapability(AetherCore.getResource("EntityEffects"), new EntityEffectsProvider(new EntityEffects(event.getEntity())));

		if (event.getEntity() instanceof EntityPlayer)
		{
			event.addCapability(AetherCore.getResource("PlayerData"), new PlayerAetherProvider(new PlayerAetherImpl((EntityPlayer) event.getEntity())));
		}
	}

	@SubscribeEvent
    public static void onItemLoad(AttachCapabilitiesEvent.Item event)
    {
    	// TODO: Stop iterating-- do lookups
		for (ItemEffects.RegistrationEntry entry : ItemEffects.getRegistrationEntries())
		{
			if (entry.getItem() == event.getItem())
			{
				List<Pair<EntityEffectProcessor, EntityEffectInstance>> emptyList = Collections.emptyList();

				ItemEffects effects = new ItemEffects(entry.getEffectsProvider() == null ? emptyList : entry.getEffectsProvider().provide());
				event.addCapability(AetherCore.getResource("ItemStackEffects"), new ItemEffectsProvider(effects));

				break;
			}
		}

        event.addCapability(AetherCore.getResource("ItemStackProperties"), new ItemPropertiesProvider(event.getItemStack()));
		event.addCapability(AetherCore.getResource("ItemExtraData"), new ItemExtraDataProvider());
    }

}
