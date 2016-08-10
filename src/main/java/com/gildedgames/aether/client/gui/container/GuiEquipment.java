package com.gildedgames.aether.client.gui.container;

import com.gildedgames.aether.client.renderer.entities.companions.RenderCompanion;
import com.gildedgames.aether.common.containers.ContainerEquipment;
import com.gildedgames.aether.common.containers.slots.SlotEquipment;
import com.gildedgames.aether.api.player.IPlayerAetherCapability;
import com.gildedgames.aether.api.items.properties.ItemEquipmentType;
import com.gildedgames.aether.common.entities.companions.EntityCompanion;
import com.gildedgames.aether.common.player.PlayerAether;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Collections;

public class GuiEquipment extends GuiContainer
{
	// TODO: COINBAR AND COMPANIONS-- SEE TRELLO

	private static final ResourceLocation textureAccessories = new ResourceLocation("aether", "textures/gui/inventory/accessories/equipment.png");

	private static final ResourceLocation textureAccessoriesPattern = new ResourceLocation("aether", "textures/gui/inventory/accessories/equipment_pattern.png");

	private static final ResourceLocation textureBackpack = new ResourceLocation("aether", "textures/gui/inventory/accessories/backpack.png");

	private static final ResourceLocation textureBackpackCreative = new ResourceLocation("aether", "textures/gui/inventory/accessories/backpack_creative.png");

	private static final ResourceLocation textureBackpackPattern = new ResourceLocation("aether", "textures/gui/inventory/accessories/backpack_pattern.png");

	private static final ResourceLocation textureBackpackCreativePattern = new ResourceLocation("aether", "textures/gui/inventory/accessories/backpack_creative_pattern.png");

	//	private static final ResourceLocation TEXTURE_COINBAR = new ResourceLocation("aether", "textures/gui/coinbar.png");

	private final PlayerAether aePlayer;

	private static final PatternButton patternButton = new PatternButton(0, 0, 0, true);

	public GuiEquipment(PlayerAether aePlayer)
	{
		super(new ContainerEquipment(aePlayer));

		this.allowUserInput = true;
		this.aePlayer = aePlayer;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		this.guiLeft = this.width / 2 - 90 - (176 / 2);
		this.guiTop = this.height / 2 - (147 / 2);

		this.xSize = 179 * 2;
		this.ySize = 169;

		this.patternButton.xPosition = this.width / 2 + 179 - 26;
		this.patternButton.yPosition = this.height / 2 - (166 / 2) + 7;

		this.addButton(this.patternButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick)
	{
		this.drawWorldBackground(0);

		this.mc.renderEngine.bindTexture(textureAccessories);

		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		this.drawTexturedModalRect(this.width / 2 - 90 - 179 / 2, this.height / 2 - 169 / 2, 0, 0, 179, 169);

		if (this.patternButton.pressedDown)
		{
			this.mc.renderEngine.bindTexture(textureAccessoriesPattern);

			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			this.drawTexturedModalRect(this.width / 2 - 90 - 179 / 2, this.height / 2 - 169 / 2, 0, 0, 179, 169);
		}

		this.mc.renderEngine.bindTexture(aePlayer.getPlayer().capabilities.isCreativeMode ? textureBackpackCreative : textureBackpack);

		this.drawTexturedModalRect(this.width / 2 + 90 - 176 / 2, this.height / 2 - 166 / 2, 0, 0, 176, 166);

		this.fontRendererObj.drawString(I18n.format("container.crafting"), this.width / 2 + (this.aePlayer.getPlayer().capabilities.isCreativeMode ? 70 : 51), this.height / 2 - 135 / 2, 4210752);

		if (this.patternButton.pressedDown)
		{
			this.mc.renderEngine.bindTexture(aePlayer.getPlayer().capabilities.isCreativeMode ? textureBackpackCreativePattern : textureBackpackPattern);

			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			this.drawTexturedModalRect(this.width / 2 + 90 - 176 / 2, this.height / 2 - 166 / 2, 0, 0, 176, 166);
		}

		this.drawPlayer(mouseX, mouseY);

		super.drawScreen(mouseX, mouseY, partialTick);

		// this.drawCoinCounter();

		this.drawSlotName(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) { }

	@Override
	public void drawDefaultBackground() { }

	private boolean isMouseOverSlot(Slot slot, int mouseX, int mouseY)
	{
		return this.isPointInRegion(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, mouseX, mouseY);
	}

	private void drawSlotName(int mouseX, int mouseY)
	{
		String unlocalizedTooltip = null;

		for (Slot slot : this.inventorySlots.inventorySlots)
		{
			if (slot.canBeHovered() && !slot.getHasStack())
			{
				if (this.isMouseOverSlot(slot, mouseX, mouseY))
				{
					if (slot instanceof SlotEquipment)
					{
						ItemEquipmentType type = ((SlotEquipment) slot).getEquipmentType();

						unlocalizedTooltip = type.getUnlocalizedName();
					}

					final int dif = this.aePlayer.getPlayer().inventory.getSizeInventory() - 2;

					if (slot.getSlotIndex() == dif + 1)
					{
						unlocalizedTooltip = "Off-Hand";
					}

					if (slot.getSlotIndex() == dif)
					{
						unlocalizedTooltip = "Helmet";
					}

					if (slot.getSlotIndex() == dif - 1)
					{
						unlocalizedTooltip = "Chestplate";
					}

					if (slot.getSlotIndex() == dif - 2)
					{
						unlocalizedTooltip = "Leggings";
					}

					if (slot.getSlotIndex() == dif - 3)
					{
						unlocalizedTooltip = "Boots";
					}

					break;
				}
			}
		}

		if (unlocalizedTooltip != null)
		{
			this.drawHoveringText(Collections.singletonList(I18n.format(unlocalizedTooltip)), mouseX, mouseY, this.mc.fontRendererObj);
		}
	}

	private void drawPlayer(int mouseX, int mouseY)
	{
		//		boolean hasCompanion = player.currentCompanion != null;
		//		GuiInventory.drawEntityOnScreen(this.width / 2 - (hasCompanion ? 100 : 90), this.height / 2 + 40, 45, (this.guiLeft + 88) - mouseX, (this.guiTop + 42) - mouseY, this.mc.thePlayer);

		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		GuiInventory.drawEntityOnScreen(this.width / 2 - 48, this.height / 2 + 10, 37, (this.guiLeft + 88) - mouseX, (this.guiTop + 42) - mouseY, this.mc.thePlayer);

		//		if (player.currentCompanion != null)
		//		{
		//			EntityCompanion.invRender = true;
		//			GL11.glPushMatrix();
		//			GL11.glScalef(0.5F, 0.5F, 0.5F);
		//			GuiInventory.drawEntityOnScreen(this.width - 140, this.height + 90, 45, (this.guiLeft + 88) - mouseX, (this.guiTop + 42) - mouseY, player.currentCompanion);
		//			GL11.glPopMatrix();
		//			EntityCompanion.invRender = false;
		//		}
	}

	//	private void drawCoinCounter(/* ??? */)
	//	{
	//		int coinX = this.width / 2 - 89;
	//		int coinY = this.height / 2 + 58;
	//
	//		int coinAmount = aePlayer.getAetherCoins();
	//
	//		this.mc.renderEngine.bindTexture(TEXTURE_COINBAR);
	//
	//		//drawTexturedModalRect(width / 2 - (71 / 2), dynamicY, 0, 0, 71, 15);
	//		this.drawTexturedModalRect(coinX - ((this.mc.fontRendererObj.getStringWidth("x" + String.valueOf(coinAmount)) / 2) + 3) - (10 / 2), coinY + 1, 0, 15, 10, 10);
	//
	//		this.mc.fontRendererObj.drawStringWithShadow("x", coinX - ((this.mc.fontRendererObj.getStringWidth("x" + String.valueOf(coinAmount)) / 2) + 2) + 6, coinY + 1, 0xffffffff);
	//		this.mc.fontRendererObj.drawStringWithShadow(String.valueOf(coinAmount), coinX - ((this.mc.fontRendererObj.getStringWidth("x" + String.valueOf(coinAmount)) / 2) + 2) + 13, coinY + 2, 0xffffffff);
	//	}

	private static class PatternButton extends GuiButton
	{

		private static final ResourceLocation PRESSED = new ResourceLocation("aether", "textures/gui/inventory/accessories/patternButtonPressed.png");

		private static final ResourceLocation UNPRESSED = new ResourceLocation("aether", "textures/gui/inventory/accessories/patternButtonUnpressed.png");

		private static final ResourceLocation PRESSED_HOVERED = new ResourceLocation("aether", "textures/gui/inventory/accessories/patternButtonPressedHovered.png");

		private static final ResourceLocation UNPRESSED_HOVERED = new ResourceLocation("aether", "textures/gui/inventory/accessories/patternButtonUnpressedHovered.png");

		private static final ResourceLocation SYMBOL = new ResourceLocation("aether", "textures/gui/inventory/accessories/patternSymbol.png");

		private static final ResourceLocation SYMBOL_HOVERED = new ResourceLocation("aether", "textures/gui/inventory/accessories/patternSymbolHovered.png");

		private boolean pressedDown;

		public PatternButton(int buttonId, int x, int y, boolean pressedDown)
		{
			super(buttonId, x, y, 18, 14, "");

			this.pressedDown = pressedDown;
		}

		@Override
		public void mouseReleased(int mouseX, int mouseY)
		{
			this.pressedDown = !this.pressedDown;
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY)
		{
			if (this.visible)
			{
				this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

				if (pressedDown)
				{
					if (this.hovered)
					{
						mc.getTextureManager().bindTexture(PRESSED_HOVERED);
					}
					else
					{
						mc.getTextureManager().bindTexture(PRESSED);
					}
				}
				else
				{
					if (this.hovered)
					{
						mc.getTextureManager().bindTexture(UNPRESSED_HOVERED);
					}
					else
					{
						mc.getTextureManager().bindTexture(UNPRESSED);
					}
				}

				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

				this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0, this.width, this.height);

				GlStateManager.popMatrix();

				this.mouseDragged(mc, mouseX, mouseY);

				if (this.hovered)
				{
					mc.getTextureManager().bindTexture(SYMBOL_HOVERED);
				}
				else
				{
					mc.getTextureManager().bindTexture(SYMBOL);
				}

				this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0, this.width, this.height);
			}
		}

	}

}
