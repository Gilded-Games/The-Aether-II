package com.gildedgames.aether.common.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class MessageHandlerServer<REQ extends IMessage, RES extends IMessage> extends MessageHandler<REQ, RES>
{
	private final boolean executesOnGameThread;

	public MessageHandlerServer()
	{
		this(true);
	}

	/**
	 * Creates a message handler for the server-side.
	 * @param safety True if this packet should process on the main game thread.
	 *               You almost always want this unless you need to reply instantly
	 *               to the packet.
	 */
	public MessageHandlerServer(final boolean safety)
	{
		this.executesOnGameThread = safety;
	}

	@Override
	public RES onMessage(final REQ message, final MessageContext ctx)
	{
		if (this.executesOnGameThread)
		{
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> this.onMessage(message, ctx.getServerHandler().player));

			return null;
		}

		return this.onMessage(message, ctx.getServerHandler().player);
	}

	@Override
	public abstract RES onMessage(REQ message, EntityPlayer player);
}
