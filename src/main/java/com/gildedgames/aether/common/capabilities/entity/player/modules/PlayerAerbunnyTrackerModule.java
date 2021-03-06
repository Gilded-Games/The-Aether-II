package com.gildedgames.aether.common.capabilities.entity.player.modules;

import com.gildedgames.aether.api.player.IPlayerAetherModule;
import com.gildedgames.aether.common.AetherCore;
import com.gildedgames.aether.common.capabilities.entity.player.PlayerAether;
import com.gildedgames.aether.common.capabilities.entity.player.PlayerAetherModule;
import com.gildedgames.aether.common.entities.animals.EntityAerbunny;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

/**
 * This Module is only used to recover Data on the Server side to respawn/reattach Aerbunnys after saving/logout. It is not synced to the Client.
 */
public class PlayerAerbunnyTrackerModule extends PlayerAetherModule implements IPlayerAetherModule.Serializable
{
	private EntityAerbunny aerbunny;

	private NBTTagCompound bunnyData;

	public PlayerAerbunnyTrackerModule(PlayerAether playerAether)
	{
		super(playerAether);
	}

	public void attachAerbunny(EntityAerbunny aerbunny)
	{
		this.aerbunny = aerbunny;
	}

	public void detachAerbunny()
	{
		this.aerbunny = null;
	}

	public void onLoggedOut()
	{
		if (this.aerbunny != null)
		{
			this.aerbunny.setDead();
		}
	}

	public void onLoggedIn()
	{
		if (this.bunnyData != null)
		{
			this.aerbunny = new EntityAerbunny(this.getWorld());
			this.aerbunny.readFromNBT(this.bunnyData);
			this.getWorld().spawnEntity(this.aerbunny);

			this.aerbunny.startRiding(this.getEntity());
		}
	}

	@Override
	public void onDeath(LivingDeathEvent event)
	{
		this.detachAerbunny();
	}

	@Override
	public void write(NBTTagCompound tag)
	{
		if (this.aerbunny != null)
		{
			this.aerbunny.writeToNBT(tag);
		}
	}

	@Override
	public void read(NBTTagCompound tag)
	{
		this.bunnyData = tag.getSize() <= 0 ? null : tag;
	}

	@Override
	public ResourceLocation getIdentifier()
	{
		return AetherCore.getResource("aerbunny");
	}
}
