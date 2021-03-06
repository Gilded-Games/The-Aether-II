package com.gildedgames.aether.client.gui;

import com.gildedgames.aether.common.AetherCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PerformanceIngame extends Gui
{
	private static final ResourceLocation SERVER_STALL_ICON = AetherCore.getResource("textures/gui/overlay/server_stall.png");

	private static final int ANIMATION_FRAMES = 4;

	private float iconAlpha = -1.0f;

	private float iconFrame;

	private long visibleUntil;

	private long lastRenderSystemTime;

	private long timeSinceLastTick;

	private boolean isDisabled;

	public void renderIcon()
	{
		Minecraft mc = Minecraft.getMinecraft();

		this.update(mc);

		if (!this.isDisabled && AetherCore.CONFIG.displayPerformanceIndicator)
		{
			this.renderIcon(mc);
		}
	}

	private void renderIcon(Minecraft mc)
	{
		ScaledResolution res = new ScaledResolution(mc);

		long now = System.currentTimeMillis();

		if (now < this.visibleUntil)
		{
			this.iconAlpha += 0.02f * mc.getRenderPartialTicks();

			if (this.iconAlpha > 1.0f)
			{
				this.iconAlpha = 1.0f;
			}
		}
		else
		{
			this.iconAlpha -= 0.02f * mc.getRenderPartialTicks();

			if (this.iconAlpha < 0.0f)
			{
				this.iconAlpha = 0.0f;
			}
		}

		if (this.iconAlpha > 0.0f)
		{
			GlStateManager.enableBlend();

			if (this.timeSinceLastTick > 800)
			{
				GlStateManager.color(255 / 255.0f, 78 / 255.0f, 41 / 255.0f, this.iconAlpha);
			}
			else if (this.timeSinceLastTick > 400)
			{
				GlStateManager.color(255 / 255.0f, 137 / 255.0f, 41 / 255.0f, this.iconAlpha);
			}
			else
			{
				GlStateManager.color(255 / 255.0f, 198 / 255.0f, 41 / 255.0f, this.iconAlpha);
			}

			mc.getTextureManager().bindTexture(SERVER_STALL_ICON);

			this.iconFrame += ((now - this.lastRenderSystemTime) / 400.0f);

			if (this.iconFrame > ANIMATION_FRAMES)
			{
				this.iconFrame = 0.0f;
			}

			GlStateManager.pushMatrix();
			GlStateManager.enableRescaleNormal();

			GlStateManager.translate(res.getScaledWidth() - 24.0f, 8.0f, 0.0f);
			GlStateManager.scale(0.25f, 0.25f, 0.25f);

			this.drawTexturedModalRect(0, 0, 0, 64 * (int) this.iconFrame, 64, 64);

			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			GlStateManager.popMatrix();
		}

		this.lastRenderSystemTime = now;
	}

	private void update(Minecraft mc)
	{
		// No server to measure...
		if (FMLCommonHandler.instance().getMinecraftServerInstance() == null)
		{
			this.isDisabled = true;

			return;
		}

		this.isDisabled = false;

		this.timeSinceLastTick = System.currentTimeMillis() - FMLCommonHandler.instance().getMinecraftServerInstance().currentTime;

		if (this.timeSinceLastTick > 200)
		{
			this.visibleUntil = System.currentTimeMillis() + 1000;
		}
	}
}
