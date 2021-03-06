package com.gildedgames.aether.client.renderer.entities.living;

import com.gildedgames.aether.client.models.entities.living.ModelAerbunny;
import com.gildedgames.aether.client.renderer.entities.living.layers.LayerAerbunnyCollar;
import com.gildedgames.aether.common.AetherCore;
import com.gildedgames.aether.common.entities.animals.EntityAerbunny;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerWolfCollar;
import net.minecraft.util.ResourceLocation;

public class RenderAerbunny extends RenderLiving<EntityAerbunny>
{
	private static final ResourceLocation texture = AetherCore.getResource("textures/entities/aerbunny/aerbunny.png");

	public RenderAerbunny(final RenderManager renderManager)
	{
		super(renderManager, new ModelAerbunny(), 0.4f);
		this.addLayer(new LayerAerbunnyCollar(this));
	}

	protected void rotateAerbunny(final EntityAerbunny entity)
	{
		if (!entity.onGround && entity.isRiding())
		{
			if (entity.motionY > 0.5D)
			{
				GlStateManager.rotate(15F, -1F, 0.0F, 0.0F);
			}
			else if (entity.motionY < -0.5D)
			{
				GlStateManager.rotate(-15F, -1F, 0.0F, 0.0F);
			}
			else
			{
				GlStateManager.rotate((float) (entity.motionY * 30D), -1F, 0.0F, 0.0F);
			}
		}
	}

	@Override
	protected void preRenderCallback(final EntityAerbunny entity, final float partialTicks)
	{
		this.rotateAerbunny(entity);
	}

	@Override
	protected ResourceLocation getEntityTexture(final EntityAerbunny entity)
	{
		return texture;
	}
}
