package com.gildedgames.aether.client.gui;

import com.gildedgames.aether.api.entity.effects.IAetherStatusEffectPool;
import com.gildedgames.aether.api.entity.effects.IAetherStatusEffects;
import com.gildedgames.aether.api.registrar.CapabilitiesAether;
import com.gildedgames.aether.common.AetherCore;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class EffectSystemOverlay extends Gui
{
	private static final ResourceLocation BAR_OUTLINE = AetherCore.getResource("textures/gui/overlay/effects/bar_outline.png");
	private static final ResourceLocation BAR_HIGHLIGHT = AetherCore.getResource("textures/gui/overlay/effects/bar_highlight.png");
	private static final ResourceLocation BAR_BUILDUP = AetherCore.getResource("textures/gui/overlay/effects/buildup_bar.png");

	private static final ResourceLocation AMBROSIUM_ICON = AetherCore.getResource("textures/gui/overlay/effects/ambrosium_poisoning.png");
	private static final ResourceLocation BLEED_ICON = AetherCore.getResource("textures/gui/overlay/effects/bleed.png");
	private static final ResourceLocation COCKATRICE_VENOM_ICON = AetherCore.getResource("textures/gui/overlay/effects/cockatrice_venom.png");
	private static final ResourceLocation FRACTURE_ICON = AetherCore.getResource("textures/gui/overlay/effects/fracture.png");
	private static final ResourceLocation FUNGAL_ROT_ICON = AetherCore.getResource("textures/gui/overlay/effects/fungal_rot.png");
	private static final ResourceLocation STUN_ICON = AetherCore.getResource("textures/gui/overlay/effects/stun.png");
	private static final ResourceLocation TOXIN_ICON = AetherCore.getResource("textures/gui/overlay/effects/toxin.png");
	private static final ResourceLocation FREEZE_ICON = AetherCore.getResource("textures/gui/overlay/effects/freeze.png");
	private static final ResourceLocation WEBBING_ICON = AetherCore.getResource("textures/gui/overlay/effects/webbing.png");
	private static final ResourceLocation IRRADIATION_ICON = AetherCore.getResource("textures/gui/overlay/effects/irradiation.png");

	private static final ResourceLocation SATURATION_BOOST_ICON = AetherCore.getResource("textures/gui/overlay/effects/teas/saturation_boost.png");

	private static final ResourceLocation GUARD_BREAK_ICON = AetherCore.getResource("textures/gui/overlay/effects/guard_break.png");

	private final int BAR_OUTLINE_TEXTURE_WIDTH = 22;
	private final int BAR_OUTLINE_TEXTURE_HEIGHT = 5;

	private final int BAR_HIGHLIGHT_TEXTURE_WIDTH = 24;
	private final int BAR_HIGHLIGHT_TEXTURE_HEIGHT = 7;

	private final int BAR_TEXTURE_WIDTH = 20;
	private final int BAR_TEXTURE_HEIGHT = 3;

	private float highlightAlpha = 0.0f;
	private boolean increasing = true;

	private Map<IAetherStatusEffects.effectTypes, Integer> buildup = Maps.newHashMap();
	private Map<IAetherStatusEffects.effectTypes, Integer> barBuildup = Maps.newHashMap();

	public void render(Minecraft mc)
	{
		ScaledResolution res = new ScaledResolution(mc);

		IAetherStatusEffectPool statusEffectPool = mc.player.getCapability(CapabilitiesAether.STATUS_EFFECT_POOL, null);

		if (statusEffectPool != null)
		{
			int numOfEffectsApplied = 0;

			for (IAetherStatusEffects effect : statusEffectPool.getPool().values())
			{
				if (effect != null)
				{
					if (effect.getBuildup() > 0)
					{
						numOfEffectsApplied++;
					}
				}
			}

			int numOfTextsActive = 0;

			for (IAetherStatusEffects effect : statusEffectPool.getPool().values())
			{
				if (effect != null)
				{
					if (effect.getTimer() < 60 && effect.getTimer() > 0)
					{
						numOfTextsActive++;
					}
				}
			}

			float xPos = (res.getScaledWidth() / 2.0F) - (12.f * numOfEffectsApplied) + 4f;
			float yPos = 2f;
			int barWidth = 0;
			float yPosShift = 6.0F;

			int i = 0;

			for (IAetherStatusEffects effect : statusEffectPool.getPool().values())
			{
				if (effect == null)
				{
					continue;
				}

				if (effect.getBuildup() > 0)
				{
					if (!effect.getIsEffectApplied())
					{
						this.renderBar(mc, BAR_OUTLINE, 22, 5, this.BAR_OUTLINE_TEXTURE_WIDTH, this.BAR_OUTLINE_TEXTURE_HEIGHT, xPos + (i * 25.f), yPos,
								false, effect);

						barWidth = effect.getBuildup() / 5;

						this.renderBar(mc, BAR_BUILDUP, barWidth, 3, this.BAR_TEXTURE_WIDTH, this.BAR_TEXTURE_HEIGHT, (xPos + 1F) + (i * 25.f), (yPos + 1F),
								true, effect);

						this.highlightAlpha = 0.0f;
						this.increasing = true;

						yPosShift = 6.0F;
					}
					else
					{
						this.renderBar(mc, BAR_OUTLINE, 22, 5, this.BAR_OUTLINE_TEXTURE_WIDTH, this.BAR_OUTLINE_TEXTURE_HEIGHT, xPos + (i * 25.f), yPos,
								false, effect);

						barWidth = 20 - (int) (effect.getTimer() / (effect.getActiveEffectTime() * effect.getActiveEffectTimeModifier()));

						this.renderBar(mc, BAR_BUILDUP, barWidth, 3, this.BAR_TEXTURE_WIDTH, this.BAR_TEXTURE_HEIGHT, (xPos + 1F) + (i * 25.f), (yPos + 1F),
								true, effect);

						this.renderBar(mc, BAR_HIGHLIGHT, 24, 7, this.BAR_HIGHLIGHT_TEXTURE_WIDTH, this.BAR_HIGHLIGHT_TEXTURE_HEIGHT, (xPos - 1) + (i * 25.f), (yPos - 1),
								true, effect);

						if (this.increasing)
						{
							this.highlightAlpha += 0.02f;

							if (this.highlightAlpha >= 1f)
							{
								this.increasing = false;
							}
						}
						else
						{
							this.highlightAlpha -= 0.02f;

							if (this.highlightAlpha <= 0f)
							{
								this.increasing = true;
							}
						}

						this.renderHighlight(mc, BAR_HIGHLIGHT, 24, 7, this.BAR_HIGHLIGHT_TEXTURE_WIDTH, this.BAR_HIGHLIGHT_TEXTURE_HEIGHT, (xPos - 1) + (i * 25.f), (yPos - 1),
								true, effect, this.highlightAlpha);

						float yTextPos = 0.0f;

						if (numOfTextsActive > 1)
						{
							yTextPos = yPos + ((i + 1) * 10) + 12.f;
						}
						else
						{
							yTextPos = yPos + 22.f;
						}

						if (effect.getTimer() < 60 && effect.getTimer() >= 0)
						{
							this.renderText(mc, (xPos + 11) + (i * 25.f), yTextPos, effect, effect.getEffectTextAlpha());
						}

						yPosShift = 6.0F;
					}

					this.renderIcon(mc, this.getEffectIconFromType(effect.getEffectType()), 16,16,16,16,xPos + 3f + (i * 25.f),yPos+yPosShift);
					i++;
				}
			}
		}
	}

	private void renderText(Minecraft mc, float x, float y, IAetherStatusEffects effect, float alpha)
	{
		GlStateManager.pushMatrix();

		float r = 0, g = 0, b = 0, a = 0 ;

		r = Color.getColorFromEffect(effect.getEffectType()).r / 255.F;
		g = Color.getColorFromEffect(effect.getEffectType()).g / 255.F;
		b = Color.getColorFromEffect(effect.getEffectType()).b / 255.F;
		a = alpha;

		if (alpha > 0)
		{
			int rgb = new java.awt.Color(r, g, b, a).getRGB();

			this.drawCenteredString(mc.fontRenderer, I18n.format(effect.getEffectName()), (int) x, (int) y, rgb);
		}

		GlStateManager.color(1,1,1, 1);

		GlStateManager.popMatrix();
	}

	private void renderHighlight(Minecraft mc, ResourceLocation texture, int width, int height, int textureWidth, int textureHeight, float x, float y, boolean doAlpha, IAetherStatusEffects effect,
								 float alpha)
	{
		GlStateManager.pushMatrix();

		GlStateManager.enableBlend();
		GlStateManager.disableRescaleNormal();

		mc.getTextureManager().bindTexture(texture);
		GlStateManager.translate(x,y,0.0f);

		if (doAlpha)
		{
			Color color = Color.getColorFromEffect(effect.getEffectType());
			float a = 0 ;

			a = alpha;

			GlStateManager.color(1,1,1, a);
		}

		Gui.drawModalRectWithCustomSizedTexture(0,0,0, 0, width, height, textureWidth, textureHeight);

		GlStateManager.color(1,1,1, 1);

		GlStateManager.popMatrix();
	}

	private void renderBar(Minecraft mc, ResourceLocation texture, int width, int height, int textureWidth, int textureHeight, float x, float y, boolean doColor, IAetherStatusEffects effect)
	{
		GlStateManager.pushMatrix();

		GlStateManager.enableBlend();
		GlStateManager.disableRescaleNormal();

		mc.getTextureManager().bindTexture(texture);
		GlStateManager.translate(x,y,0.0f);

		if (doColor)
		{
			Color color = Color.getColorFromEffect(effect.getEffectType());
			float r = 0, g = 0, b = 0, a = 0 ;

			r = Color.getColorFromEffect(effect.getEffectType()).r / 255.F;
			g = Color.getColorFromEffect(effect.getEffectType()).g / 255.F;
			b = Color.getColorFromEffect(effect.getEffectType()).b / 255.F;
			a = 1.0f;

			GlStateManager.color(r,g,b,a);
		}

		Gui.drawModalRectWithCustomSizedTexture(0,0,0, 0, width, height, textureWidth, textureHeight);

		GlStateManager.color(1,1,1, 1);

		GlStateManager.popMatrix();
	}

	private void renderIcon(Minecraft mc, ResourceLocation texture, int width, int height, int textureWidth, int textureHeight, float x, float y)
	{
		GlStateManager.pushMatrix();

		GlStateManager.enableBlend();
		GlStateManager.disableRescaleNormal();

		mc.getTextureManager().bindTexture(texture);
		GlStateManager.translate(x,y,0.0f);

		Gui.drawModalRectWithCustomSizedTexture(0,0,0, 0, width, height, textureWidth, textureHeight);

		GlStateManager.popMatrix();
	}

	private ResourceLocation getEffectIconFromType(IAetherStatusEffects.effectTypes effectType)
	{
		switch(effectType)
		{
			case AMBROSIUM_POISONING:
				return AMBROSIUM_ICON;
			case BLEED:
				return BLEED_ICON;
			case FRACTURE:
				return FRACTURE_ICON;
			case COCKATRICE_VENOM:
				return COCKATRICE_VENOM_ICON;
			case FUNGAL_ROT:
				return FUNGAL_ROT_ICON;
			case STUN:
				return STUN_ICON;
			case TOXIN:
				return TOXIN_ICON;
			case FREEZE:
				return FREEZE_ICON;
			case WEBBING:
				return WEBBING_ICON;
			case SATURATION_BOOST:
				return SATURATION_BOOST_ICON;
			case GUARD_BREAK:
				return GUARD_BREAK_ICON;
			case IRRADIATION:
				return IRRADIATION_ICON;
		}

		return STUN_ICON;
	}

	public enum Color {
		AMBROSIUM_POISONING(219,198,66),
		BLEED(148,21,12),
		COCKATRICE_VENOM(111,57,153),
		FRACTURE(214,210,180),
		FUNGAL_ROT(163,130,87),
		STUN(255,255,255),
		TOXIN(69,122,65),
		FREEZE(174,203,209),
		WEBBING(199, 197, 187),
		SATURATION_BOOST(212, 200, 121),
		GUARD_BREAK(163, 199, 135),
		IRRADIATION(219,198,66),
		DEFAULT_COLOR(1,1,1);

		public final int r;
		public final int g;
		public final int b;
		public final int a = 255;

		Color(int r, int g, int b)
		{
			this.r = r;
			this.g = g;
			this.b = b;
		}

		public static Color getColorFromEffect(IAetherStatusEffects.effectTypes effectType)
		{
			switch(effectType)
			{
				case AMBROSIUM_POISONING:
					return AMBROSIUM_POISONING;
				case BLEED:
					return BLEED;
				case FRACTURE:
					return FRACTURE;
				case COCKATRICE_VENOM:
					return COCKATRICE_VENOM;
				case FUNGAL_ROT:
					return FUNGAL_ROT;
				case STUN:
					return STUN;
				case TOXIN:
					return TOXIN;
				case FREEZE:
					return FREEZE;
				case WEBBING:
					return WEBBING;
				case SATURATION_BOOST:
					return SATURATION_BOOST;
				case GUARD_BREAK:
					return GUARD_BREAK;
				case IRRADIATION:
					return IRRADIATION;
					default:
						return DEFAULT_COLOR;
			}
		}
	}
}
