package com.gildedgames.aether.client.renderer.entities.living;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;

public abstract class RenderLivingLOD<T extends EntityLiving> extends RenderLiving<T>
{
	private final ModelBase lowDetailModel;

	protected boolean isLowDetail;

	public RenderLivingLOD(RenderManager rendermanagerIn, ModelBase highDetailModel, ModelBase lowDetailModel, float shadowsizeIn)
	{
		super(rendermanagerIn, highDetailModel, shadowsizeIn);

		this.lowDetailModel = lowDetailModel;
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		boolean forceLowDetail = false;
		boolean lowDetail =  forceLowDetail || Minecraft.getMinecraft().player.getDistanceSq(entity) > this.getHighLODMinDistanceSq();

		this.isLowDetail = lowDetail;

		ModelBase prev = this.mainModel;

		if (lowDetail)
		{
			this.mainModel = this.lowDetailModel;
		}

		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		this.mainModel = prev;

		this.isLowDetail = false;
	}

	/**
	 * @return The minimum distance needed to use the high level of detail model.
	 */
	protected double getHighLODMinDistanceSq()
	{
		return 30.0 * 30.0;
	}
}
