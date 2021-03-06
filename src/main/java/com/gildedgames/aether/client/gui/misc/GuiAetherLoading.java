package com.gildedgames.aether.client.gui.misc;

import com.gildedgames.aether.client.events.listeners.gui.GuiLoadingListener;
import com.gildedgames.aether.common.AetherCore;
import com.gildedgames.aether.common.util.helpers.MathUtil;
import com.gildedgames.orbis.lib.client.PartialTicks;
import com.gildedgames.orbis.lib.client.gui.data.Text;
import com.gildedgames.orbis.lib.client.gui.util.GuiText;
import com.gildedgames.orbis.lib.client.gui.util.GuiTexture;
import com.gildedgames.orbis.lib.client.gui.util.gui_library.GuiElement;
import com.gildedgames.orbis.lib.client.gui.util.gui_library.GuiViewer;
import com.gildedgames.orbis.lib.client.gui.util.gui_library.IGuiContext;
import com.gildedgames.orbis.lib.client.rect.Dim2D;
import com.gildedgames.orbis.lib.client.rect.Pos2D;
import com.gildedgames.orbis.lib.util.InputHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import org.lwjgl.opengl.GL11;

public class GuiAetherLoading extends GuiViewer implements CustomLoadingRenderer.ICustomLoading
{
	private static final String[] SPINNER_STATES = new String[] { "0oo", "o0o", "oo0" };

	private static final ResourceLocation HIGHLANDS = AetherCore.getResource("textures/gui/intro/highlands.png");

	private static final ResourceLocation HUE_BACKGROUND = AetherCore.getResource("textures/gui/intro/hue_background.png");

	public static float PERCENT = 0.0F;

	private GuiTexture highlands;

	private GuiText loading, spinner;

	private float lastPercent;

	private long millis;

	public GuiAetherLoading()
	{
		super(new GuiElement(Dim2D.flush(), false));
	}

	@Override
	public void build(IGuiContext context)
	{
		this.getViewing().dim().mod().width(this.width).height(this.height).flush();

		final Pos2D center = InputHelper.getCenter();

		this.highlands = new GuiTexture(Dim2D.build().scale(0.5F).width(512).height(235).center(true).pos(center).flush(), HIGHLANDS);

		this.loading = new GuiText(Dim2D.build().pos(InputHelper.getBottomLeft()).addY(-16).addX(8).flush(),
				new Text(new TextComponentTranslation("gui.aether.loading.indeterminate"), 1.0F));

		this.spinner = new GuiText(Dim2D.build().pos(InputHelper.getBottomRight()).addY(-16).addX(-24).flush(),
				new Text(new TextComponentString(SPINNER_STATES[0]), 1.0f));

		context.addChildren(this.highlands, this.loading, this.spinner);

		this.millis = System.currentTimeMillis();
	}

	@Override
	public void drawElements()
	{
		preventInnerTyping();

		GlStateManager.pushMatrix();

		GlStateManager.disableDepth();

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager
				.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
						GlStateManager.SourceFactor.ZERO,
						GlStateManager.DestFactor.ONE);

		GL11.glEnable(GL11.GL_ALPHA_TEST);

		GlStateManager.enableAlpha();

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(HUE_BACKGROUND);

		drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width,
				this.height,
				this.width, this.height);

		GlStateManager.enableDepth();

		GlStateManager.enableBlend();

		GlStateManager.popMatrix();

		if (this.mc.world != null)
		{
			if (!MathUtil.epsilonEquals(PERCENT, this.lastPercent) && PERCENT > 0.0F)
			{
				this.lastPercent = PERCENT;

				String percentString = String.valueOf(MathHelper.floor(PERCENT));

				this.loading.setText(new Text(new TextComponentTranslation("gui.aether.loading.progress", percentString), 1.0F));
			}
		}

		int spinnerStateIndex = (int) ((System.currentTimeMillis() - this.millis) / 150) % SPINNER_STATES.length;

		this.spinner.setText(new Text(new TextComponentString(SPINNER_STATES[spinnerStateIndex]), 1.0F));

		super.drawElements();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);

		GuiLoadingListener.drawFade(false);
	}

	@Override
	public void drawCustomLoading()
	{
		this.drawScreen(InputHelper.getMouseX(), InputHelper.getMouseY(), PartialTicks.get());
	}
}
