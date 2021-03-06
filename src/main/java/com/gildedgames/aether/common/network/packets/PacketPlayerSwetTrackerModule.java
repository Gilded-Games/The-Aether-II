package com.gildedgames.aether.common.network.packets;

import com.gildedgames.aether.common.capabilities.entity.player.PlayerAether;
import com.gildedgames.aether.common.capabilities.entity.player.modules.PlayerSwetTrackerModule;
import com.gildedgames.aether.common.network.MessageHandlerClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketPlayerSwetTrackerModule  implements IMessage
{

	private PlayerSwetTrackerModule module;

	private NBTTagCompound tag;

	public PacketPlayerSwetTrackerModule()
	{

	}

	public PacketPlayerSwetTrackerModule(PlayerSwetTrackerModule module)
	{
		this.module = module;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.tag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		NBTTagCompound tag = new NBTTagCompound();

		this.module.write(tag);

		ByteBufUtils.writeTag(buf, tag);
	}

	public static class HandlerClient extends MessageHandlerClient<PacketPlayerSwetTrackerModule, IMessage>
	{
		@Override
		public IMessage onMessage(final PacketPlayerSwetTrackerModule message, final EntityPlayer player)
		{
			if (player == null || player.world == null)
			{
				return null;
			}

			final PlayerAether playerAether = PlayerAether.getPlayer(player);
			playerAether.getModule(PlayerSwetTrackerModule.class).read(message.tag);

			return null;
		}
	}
}
