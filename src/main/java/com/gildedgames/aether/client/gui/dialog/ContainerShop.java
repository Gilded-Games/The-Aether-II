package com.gildedgames.aether.client.gui.dialog;

import com.gildedgames.aether.api.AetherAPI;
import com.gildedgames.aether.api.dialog.IDialogSlide;
import com.gildedgames.aether.api.dialog.IDialogSlideRenderer;
import com.gildedgames.aether.api.shop.*;
import com.gildedgames.aether.client.gui.container.IExtendedContainer;
import com.gildedgames.aether.client.gui.util.GuiItemStack;
import com.gildedgames.aether.common.AetherCelebrations;
import com.gildedgames.aether.common.AetherCore;
import com.gildedgames.aether.common.capabilities.entity.player.PlayerAether;
import com.gildedgames.aether.common.network.NetworkingAether;
import com.gildedgames.aether.common.network.packets.PacketShopBack;
import com.gildedgames.aether.common.network.packets.PacketShopBuy;
import com.gildedgames.aether.common.network.packets.PacketShopSell;
import com.gildedgames.aether.common.util.helpers.ItemHelper;
import com.gildedgames.aether.common.util.helpers.MathUtil;
import com.gildedgames.orbis.lib.client.gui.data.Text;
import com.gildedgames.orbis.lib.client.gui.util.GuiAbstractButton;
import com.gildedgames.orbis.lib.client.gui.util.GuiTexture;
import com.gildedgames.orbis.lib.client.gui.util.gui_library.GuiElement;
import com.gildedgames.orbis.lib.client.gui.util.gui_library.GuiViewer;
import com.gildedgames.orbis.lib.client.gui.util.gui_library.IGuiContext;
import com.gildedgames.orbis.lib.client.gui.util.vanilla.GuiButtonVanilla;
import com.gildedgames.orbis.lib.client.rect.Dim2D;
import com.gildedgames.orbis.lib.client.rect.Pos2D;
import com.gildedgames.orbis.lib.util.InputHelper;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class ContainerShop extends GuiViewer implements ICurrencyListener, IExtendedContainer
{
	private static final ResourceLocation INVENTORY = AetherCore.getResource("textures/gui/shop/inventory.png");

	private static final ResourceLocation STOCK = AetherCore.getResource("textures/gui/shop/stock.png");

	private static final ResourceLocation UP_ARROW = AetherCore.getResource("textures/gui/shop/up_button.png");

	private static final ResourceLocation DOWN_ARROW = AetherCore.getResource("textures/gui/shop/down_button.png");

	private static final ResourceLocation UP_ARROW_HOVER = AetherCore.getResource("textures/gui/shop/up_button_hover.png");

	private static final ResourceLocation DOWN_ARROW_HOVER = AetherCore.getResource("textures/gui/shop/down_button_hover.png");

	private static final ResourceLocation LOCK = AetherCore.getResource("textures/gui/shop/lock.png");

	private static final ResourceLocation UNLOCK = AetherCore.getResource("textures/gui/shop/unlock.png");

	private static final ResourceLocation GILT_BAG = AetherCore.getResource("textures/gui/shop/gilt_bag.png");

	private static final ResourceLocation HOLIDAY_NOTICE = AetherCore.getResource("textures/gui/shop/shop_notice.png");

	private static final ResourceLocation HOLIDAY_ICON = AetherCore.getResource("textures/gui/shop/holiday_icon.png");

	private static int buyCount = 1, prevBuyCount = 1;

	private static boolean isCountLocked;

	private final IDialogSlide slide;

	private final IDialogSlideRenderer renderer;

	private final IShopInstance shopInstance;

	private final List<GuiShopBuy> buys = Lists.newArrayList();

	private final com.gildedgames.aether.common.containers.ContainerShop container;

	private final PlayerAether playerAether;

	private final int shopIndex;

	private int buyCountUnlocked = 1, prevBuyCountUnlocked = 1;

	private GuiButtonVanilla sell, buy;

	private IGuiCurrencyValue playerCoins, sellCoins, buyCoins;

	private ItemStack lastSellStack;

	private int lastSellStackCount;

	private GuiItemStack stackGui;

	private com.gildedgames.orbis.lib.client.gui.util.GuiTextBox buyTitle;

	private GuiTextBox npcDialogue, npcGreeting;

	private int selectedBuy = -1, prevBuy = -1;

	private GuiButtonVanilla back;

	private GuiAbstractButton upArrow, downArrow;

	private GuiButtonVanilla lockButton;

	private GuiTexture lock, unlock, giltBag;

	private boolean upArrowHeld, downArrowHeld, pressLongEnough;

	private long lastBuyCountChangeTime;

	private List<String> hoverDescription;

	private ItemStack hoveredStack;

	private GuiTexture holidayNotice, holidayIcon;

	private com.gildedgames.orbis.lib.client.gui.util.GuiTextBox holidayNoticeText;

	public ContainerShop(GuiViewer prevViewer, EntityPlayer player, IDialogSlide slide, IDialogSlideRenderer renderer, IShopInstance shopInstance, int shopIndex)
	{
		super(new GuiElement(Dim2D.flush(), false), prevViewer, new com.gildedgames.aether.common.containers.ContainerShop(player.inventory, shopInstance));

		this.shopIndex = shopIndex;

		this.setDrawDefaultBackground(false);

		this.container = (com.gildedgames.aether.common.containers.ContainerShop) this.inventorySlots;

		this.slide = slide;
		this.renderer = renderer;
		this.shopInstance = shopInstance;

		this.playerAether = PlayerAether.getPlayer(player);
	}

	private IShopBuy getSelectedBuy()
	{
		return this.selectedBuy == -1 ? null : this.shopInstance.getStock().get(this.selectedBuy);
	}

	@Override
	public void initContainerSize()
	{
		Pos2D center = InputHelper.getCenter().clone().addX(15).flush();

		this.guiLeft = (int) center.x() - 189 - 7;
		this.guiTop = this.height - 198 - 14;

		this.xSize = 385;
		this.ySize = 180;
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();

		this.shopInstance.getCurrencyType().unlistenForCurrency(this.playerAether, this);
	}

	@Override
	public void build(IGuiContext context)
	{
		this.getViewing().dim().mod().width(300).height(300).flush();

		Pos2D center = InputHelper.getCenter().clone().addX(17).flush();

		this.holidayNotice = new GuiTexture(Dim2D.build().width(110).height(50).flush(), HOLIDAY_NOTICE);

		this.holidayIcon = new GuiTexture(Dim2D.build().width(14).height(16).x(8).y(18).flush(), HOLIDAY_ICON);

		this.holidayNoticeText = new com.gildedgames.orbis.lib.client.gui.util.GuiTextBox(Dim2D.build().width(80).height(40).x(27).y(17).flush(), false,
				new Text(new TextComponentTranslation("edison.shop.holiday_notice"), 1.0F));

		this.sellCoins = this.shopInstance.getCurrencyType()
				.createSellItemCurrencyValueGui(Dim2D.build().center(true).pos(center).y(this.height).addX(23).addY(-197).flush());
		this.sellCoins.state().setVisible(false);

		this.buyCoins = this.shopInstance.getCurrencyType()
				.createBuyItemCurrencyValueGui(Dim2D.build().center(true).pos(center).y(this.height).addX(-167).addY(-157).flush());
		this.buyCoins.state().setVisible(false);

		this.playerCoins = this.shopInstance.getCurrencyType()
				.createCurrencyValueGui(Dim2D.build().centerY(true).pos(center).y(this.height).addX(168).addY(-118).flush());
		this.playerCoins.state().setVisible(true);

		GuiTexture inventory = new GuiTexture(Dim2D.build().center(true).width(176).height(120).pos(center).y(this.height).addX(75).addY(-157F).flush(),
				INVENTORY);
		GuiTexture stock = new GuiTexture(Dim2D.build().center(true).width(176).height(80).pos(center).y(this.height).addX(-115).addY(-137).flush(), STOCK);

		this.sell = new GuiButtonVanilla(Dim2D.build().width(72).height(20).pos(center).y(this.height).addX(84).addY(-207).flush());

		this.sell.getInner().displayString = I18n.format("aether.shop.sell");
		this.sell.getInner().enabled = false;

		if (AetherCelebrations.isEdisonNewYearsSale(this.shopInstance))
		{
			context.addChildren(this.holidayNotice, this.holidayNoticeText, this.holidayIcon);
		}

		context.addChildren(stock, inventory, this.sell);

		this.stackGui = new GuiItemStack(Dim2D.build().pos(center).y(this.height).addX(-132).addY(-166).scale(1.0F).flush());
		this.buy = new GuiButtonVanilla(Dim2D.build().width(44).height(20).pos(center).y(this.height).addX(-106).addY(-167).flush());

		this.buy.getInner().displayString = I18n.format("aether.shop.buy", isCountLocked ? buyCount : this.buyCountUnlocked);
		this.buy.state().setEnabled(false);

		this.back = new GuiButtonVanilla(Dim2D.build().width(20).height(20).pos(center).y(this.height).addX(-236).addY(-125).flush());

		this.back.getInner().displayString = "<";

		context.addChildren(this.stackGui, this.buy, this.back);

		int baseBoxSize = 350;
		final boolean resize = this.width - 40 > baseBoxSize;

		this.npcDialogue = new GuiTextBox(0, resize ? (this.width / 2) - (baseBoxSize / 2) : 20, this.height - 85, baseBoxSize, 70);

		int greetingBoxSize = 350;
		final boolean greetingResize = this.width - 40 > greetingBoxSize;

		this.npcGreeting = new GuiTextBox(1, greetingResize ? (this.width / 2) - (greetingBoxSize / 2) : 20, this.height - 85, greetingBoxSize, 70);

		String greeting = MathUtil.getRandomElement(this.shopInstance.getUnlocalizedGreetings(), new Random());

		this.npcGreeting.setText(new TextComponentTranslation(greeting));

		this.buyTitle = new com.gildedgames.orbis.lib.client.gui.util.GuiTextBox(
				Dim2D.build().centerX(true).pos(center).width(60).height(50).y(this.height).addX(-178).addY(-40).flush(), true);

		this.buttonList.add(this.npcDialogue);
		this.buttonList.add(this.npcGreeting);

		//this.addChildren(this.buyTitle);

		for (int i = 0; i < this.shopInstance.getStock().size(); i++)
		{
			GuiShopBuy gui = new GuiShopBuy(
					Dim2D.build().width(18).height(18).pos(center).y(this.height).addX(-196 + (i * 18) - ((i / 9) * 162)).addY(-140F + ((i / 9) * 18)).flush(),
					i,
					this.shopInstance);
			this.buys.add(gui);

			context.addChildren(gui);
		}

		this.upArrow = new GuiAbstractButton(Dim2D.build().width(15).height(10).pos(center).y(this.height).addX(-49).addY(-167).flush(),
				new GuiTexture(Dim2D.build().width(15).height(10).flush(), UP_ARROW),
				new GuiTexture(Dim2D.build().width(15).height(10).flush(), UP_ARROW_HOVER),
				new GuiTexture(Dim2D.build().width(15).height(10).flush(), UP_ARROW));

		this.downArrow = new GuiAbstractButton(Dim2D.build().width(15).height(10).pos(center).y(this.height).addX(-49).addY(-157).flush(),
				new GuiTexture(Dim2D.build().width(15).height(10).flush(), DOWN_ARROW),
				new GuiTexture(Dim2D.build().width(15).height(10).flush(), DOWN_ARROW_HOVER),
				new GuiTexture(Dim2D.build().width(15).height(10).flush(), DOWN_ARROW));

		this.lockButton = new GuiButtonVanilla(Dim2D.build().width(14).height(20).pos(center).y(this.height).addX(-63).addY(-167).flush());

		this.lock = new GuiTexture(Dim2D.build().center(true).height(16).width(16).pos(center).y(this.height).addX(-65 + 9).addY(-167 + 10).flush(), LOCK);
		this.unlock = new GuiTexture(Dim2D.build().center(true).height(16).width(16).pos(center).y(this.height).addX(-65 + 9).addY(-167 + 10).flush(), UNLOCK);

		context.addChildren(this.playerCoins);

		this.giltBag = new GuiTexture(
				Dim2D.build().center(true).height(16).width(16).pos(center).y(this.height)
						.x(this.playerCoins.state().dim().x() + (this.playerCoins.state().dim().width() / 2) - 1)
						.addY(-138).scale(1.5F).flush(),
				GILT_BAG);

		this.playerCoins.setCurrencyValue(this.shopInstance.getCurrencyType().getValue(this.playerAether));

		context.addChildren(this.upArrow, this.downArrow, this.sellCoins, this.buyCoins, this.lockButton, this.lock, this.unlock, this.giltBag);
	}

	public void addBuyCount(int buyCount)
	{
		if (isCountLocked)
		{
			ContainerShop.buyCount = MathHelper.clamp(ContainerShop.buyCount + buyCount, 1, 64);

			if (this.getSelectedBuy() != null)
			{
				int count = (int) Math
						.min(this.getSelectedBuy().getStock(), Math.min(ContainerShop.buyCount, Math.min(this.getSelectedBuy().getItemStack().getMaxStackSize(),
								this.shopInstance.getCurrencyType().getValue(this.playerAether) / ShopUtil
										.getFilteredPrice(this.shopInstance, this.getSelectedBuy()))));

				if (count <= 0)
				{
					count = 1;
				}

				if (this.stackGui.getItemStack() != null)
				{
					this.stackGui.getItemStack().setCount(count);
				}

				int value = ShopUtil.getFilteredPrice(this.shopInstance, this.getSelectedBuy()) * count;

				this.buyCoins.setCurrencyValue(value);
				this.buyCoins.setNonFilteredCurrencyValue(this.getSelectedBuy().getPrice() * count);
				this.buyCoins.state().setVisible(true);
			}
		}
		else
		{
			if (this.getSelectedBuy() != null)
			{
				int max = (int) Math.min(this.getSelectedBuy().getStock(), Math.min(this.getSelectedBuy().getItemStack().getMaxStackSize(),
						this.shopInstance.getCurrencyType().getValue(this.playerAether) / ShopUtil.getFilteredPrice(this.shopInstance, this.getSelectedBuy())));

				if (max <= 0)
				{
					max = 1;
				}

				this.buyCountUnlocked = MathHelper.clamp(this.buyCountUnlocked + buyCount, 1, max);

				if (this.stackGui.getItemStack() != null)
				{
					this.stackGui.getItemStack().setCount(this.buyCountUnlocked);
				}

				int value = ShopUtil.getFilteredPrice(this.shopInstance, this.getSelectedBuy()) * this.buyCountUnlocked;

				this.buyCoins.setCurrencyValue(value);
				this.buyCoins.setNonFilteredCurrencyValue(this.getSelectedBuy().getPrice() * this.buyCountUnlocked);
				this.buyCoins.state().setVisible(true);
			}
		}
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks)
	{
		this.shopInstance.getCurrencyType().listenForCurrency(this.playerAether, this);
		this.shopInstance.getCurrencyType().update(this.playerAether);

		if (InputHelper.isHovered(this.back))
		{
			this.setHoveredDescription(null, Lists.newArrayList(I18n.format("aether.shop.back")));
		}

		this.stackGui.setDrawCount(isCountLocked);

		this.lock.state().setVisible(isCountLocked);
		this.unlock.state().setVisible(!isCountLocked);

		if ((this.upArrowHeld || this.downArrowHeld) && !this.pressLongEnough && System.currentTimeMillis() - this.lastBuyCountChangeTime >= 300L)
		{
			this.lastBuyCountChangeTime = System.currentTimeMillis();

			this.pressLongEnough = true;
		}

		if (this.pressLongEnough)
		{
			if (System.currentTimeMillis() - this.lastBuyCountChangeTime >= 50L)
			{
				this.lastBuyCountChangeTime = System.currentTimeMillis();

				if (this.upArrowHeld)
				{
					this.addBuyCount(1);
				}
				else if (this.downArrowHeld)
				{
					this.addBuyCount(-1);
				}
			}
		}

		this.drawWorldBackground(0);
		MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.BackgroundDrawnEvent(this));

		GlStateManager.pushMatrix();

		GlStateManager.disableDepth();

		GlStateManager.translate(0, 0, 100F);
		GlStateManager.color(1.0F, 1.0F, 1.0F);

		if (this.slide != null && this.renderer != null)
		{
			GlStateManager.pushMatrix();

			GlStateManager.translate(-100F, 0F, 0F);

			this.renderer.draw(this.slide, this.width, this.height, mouseX, mouseY, partialTicks);
			GlStateManager.popMatrix();
		}

		GlStateManager.translate(0, 0, 100F);

		Gui.drawRect(0, this.height - 90, this.width, this.height, Integer.MIN_VALUE);

		super.drawScreen(mouseX, mouseY, partialTicks);

		GlStateManager.enableDepth();

		GlStateManager.popMatrix();

		if (this.hoverDescription != null && this.hoverDescription.size() > 0)
		{
			if (this.hoveredStack != null)
			{
				GuiUtils.preItemToolTip(this.hoveredStack);
				GuiUtils.drawHoveringText(this.hoverDescription, mouseX, mouseY, width, height, -1,
						Minecraft.getMinecraft().fontRenderer);
				GuiUtils.postItemToolTip();
			}
			else
			{
				GuiUtils.drawHoveringText(this.hoverDescription, mouseX, mouseY, width, height, -1,
						Minecraft.getMinecraft().fontRenderer);
			}
		}

		if (InputHelper.isHovered(this.lockButton))
		{
			GuiUtils
					.drawHoveringText(Lists.newArrayList(I18n.format("aether.shop.lockTooltip")), mouseX, mouseY, this.width, this.height, 120,
							Minecraft.getMinecraft().fontRenderer);
		}

		this.hoverDescription = null;

		this.renderHoveredToolTip(mouseX, mouseY);

		ItemStack stack = this.container.getSlot(0).getStack();

		if (stack != this.lastSellStack || stack.getCount() != this.lastSellStackCount)
		{
			int hash = ItemHelper.getKeyForItemStack(stack);
			IShopBuy shopBuy = null;

			for (IShopBuy buy : this.shopInstance.getStock())
			{
				int buyHash = ItemHelper.getKeyForItemStack(buy.getItemStack());

				if (buyHash == hash)
				{
					shopBuy = buy;
					break;
				}
			}

			double value;

			if (shopBuy != null)
			{
				value = ShopUtil.getFilteredPrice(this.shopInstance, stack, shopBuy.getSellingPrice()) * (double) stack.getCount();
				this.sellCoins.setNonFilteredCurrencyValue(shopBuy.getSellingPrice() * stack.getCount());
			}
			else
			{
				double originalValue = AetherAPI.content().currency().getValue(stack, this.shopInstance.getCurrencyType().getClass());

				value = ShopUtil.getFilteredPrice(this.shopInstance, stack, originalValue);
				this.sellCoins.setNonFilteredCurrencyValue(originalValue);
			}

			this.sellCoins.setCurrencyValue(value);

			this.lastSellStack = stack;
			this.lastSellStackCount = stack.getCount();

			this.sell.state().setEnabled(value >= 1);
			this.sellCoins.state().setVisible(true);
		}

		if (this.getSelectedBuy() != null)
		{
			this.npcGreeting.visible = false;

			int maxAllowedWithHeldStack = this.getSelectedBuy().getItemStack().getMaxStackSize() - this.mc.player.inventory.getItemStack().getCount();

			int amount = Math.min(maxAllowedWithHeldStack, Math.min(buyCount, this.getSelectedBuy().getStock()));

			boolean canAfford =
					this.shopInstance.getCurrencyType().getValue(this.playerAether) >= ShopUtil.getFilteredPrice(this.shopInstance, this.getSelectedBuy());
			boolean isHandFree = this.mc.player.inventory.getItemStack().isEmpty();
			boolean isBuyItem = ItemHelper.getKeyForItemStack(this.mc.player.inventory.getItemStack()) == ItemHelper
					.getKeyForItemStack(this.getSelectedBuy().getItemStack());
			boolean canStack = this.mc.player.inventory.getItemStack().isStackable();
			boolean isAtStackLimit = this.mc.player.inventory.getItemStack().getCount() >= this.mc.player.inventory.getItemStack().getMaxStackSize();
			boolean hasStock = this.getSelectedBuy().getStock() > 0 && amount > 0;

			this.buy.state()
					.setEnabled(hasStock && !isAtStackLimit && canStack && canAfford && (isHandFree || isBuyItem) && this.getSelectedBuy().getStock() > 0);
		}
		else
		{
			this.npcGreeting.visible = true;
		}

		if (this.getSelectedBuy() != null && this.prevBuy != this.selectedBuy)
		{
			this.stackGui.setItemStack(this.getSelectedBuy().getItemStack());
			//this.buyTitle.setText(new Text(new TextComponentTranslation(this.getSelectedBuy().getItemStack().getDisplayName()), 1.0F));
			//this.buyTitle.init();

			String chosenDesc = MathUtil.getRandomElement(this.getSelectedBuy().getUnlocalizedDescriptions(), new Random());

			this.npcDialogue.setText(new TextComponentTranslation(chosenDesc));

			this.prevBuy = this.selectedBuy;

			int count = (int) Math
					.min(isCountLocked ? ContainerShop.buyCount : this.buyCountUnlocked, Math.min(this.getSelectedBuy().getItemStack().getMaxStackSize(),
							this.shopInstance.getCurrencyType().getValue(this.playerAether) / ShopUtil
									.getFilteredPrice(this.shopInstance, this.getSelectedBuy())));

			if (count <= 0)
			{
				count = 1;
			}

			int value = ShopUtil.getFilteredPrice(this.shopInstance, this.getSelectedBuy()) * count;

			this.buyCoins.setCurrencyValue(value);
			this.buyCoins.setNonFilteredCurrencyValue(this.getSelectedBuy().getPrice() * count);
			this.buyCoins.state().setVisible(true);
		}

		if (buyCount != prevBuyCount && isCountLocked)
		{
			prevBuyCount = buyCount;

			this.buy.getInner().displayString = I18n.format("aether.shop.buy", buyCount);
		}

		if (this.buyCountUnlocked != this.prevBuyCountUnlocked && !isCountLocked)
		{
			this.prevBuyCountUnlocked = this.buyCountUnlocked;

			this.buy.getInner().displayString = I18n.format("aether.shop.buy", this.buyCountUnlocked);
		}
	}

	@Override
	public void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY)
	{

	}

	@Override
	public void drawDefaultBackground()
	{

	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state)
	{
		super.mouseReleased(mouseX, mouseY, state);

		this.upArrowHeld = false;
		this.downArrowHeld = false;
		this.lastBuyCountChangeTime = 0;
		this.pressLongEnough = false;
	}

	@Override
	protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (InputHelper.isHovered(this.lockButton))
		{
			isCountLocked = !isCountLocked;

			if (isCountLocked)
			{
				this.buy.getInner().displayString = I18n.format("aether.shop.buy", buyCount);

				if (this.getSelectedBuy() != null)
				{
					int count = (int) Math
							.min(this.getSelectedBuy().getStock(), Math.min(ContainerShop.buyCount, Math.min(this.getSelectedBuy().getItemStack().getMaxStackSize(),
									this.shopInstance.getCurrencyType().getValue(this.playerAether) / ShopUtil
											.getFilteredPrice(this.shopInstance, this.getSelectedBuy()))));

					if (this.stackGui.getItemStack() != null)
					{
						this.stackGui.getItemStack().setCount(count);
					}

					int value = ShopUtil.getFilteredPrice(this.shopInstance, this.getSelectedBuy()) * count;

					this.buyCoins.setCurrencyValue(value);
					this.buyCoins.setNonFilteredCurrencyValue(this.getSelectedBuy().getPrice() * count);
					this.buyCoins.state().setVisible(true);
				}

				this.addBuyCount(0);
			}
			else
			{
				this.buy.getInner().displayString = I18n.format("aether.shop.buy", this.buyCountUnlocked);

				if (this.stackGui.getItemStack() != null)
				{
					this.stackGui.getItemStack().setCount(this.buyCountUnlocked);
				}

				if (this.getSelectedBuy() != null)
				{
					int value = ShopUtil.getFilteredPrice(this.shopInstance, this.getSelectedBuy()) * this.buyCountUnlocked;

					this.buyCoins.setCurrencyValue(value);
					this.buyCoins.setNonFilteredCurrencyValue(this.getSelectedBuy().getPrice() * this.buyCountUnlocked);
					this.buyCoins.state().setVisible(true);
				}
			}
		}

		if (InputHelper.isHovered(this.upArrow))
		{
			this.upArrowHeld = true;
			this.addBuyCount(1);
			this.lastBuyCountChangeTime = System.currentTimeMillis();

			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
			{
				this.addBuyCount(64);
			}
		}

		if (InputHelper.isHovered(this.downArrow))
		{
			this.downArrowHeld = true;
			this.addBuyCount(-1);
			this.lastBuyCountChangeTime = System.currentTimeMillis();

			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
			{
				this.addBuyCount(-64);
			}
		}

		if (InputHelper.isHovered(this.back))
		{
			NetworkingAether.sendPacketToServer(new PacketShopBack());
		}

		if (InputHelper.isHovered(this.buy) && this.buy.state().isEnabled())
		{
			int index = 0;

			for (int i = 0; i < this.shopInstance.getStock().size(); i++)
			{
				IShopBuy shopBuy = this.shopInstance.getStock().get(i);

				if (shopBuy == this.getSelectedBuy())
				{
					index = i;
					break;
				}
			}

			NetworkingAether.sendPacketToServer(new PacketShopBuy(index, isCountLocked ? buyCount : this.buyCountUnlocked, this.shopIndex));

			int maxAllowedWithHeldStack = this.getSelectedBuy().getItemStack().getMaxStackSize() - this.mc.player.inventory.getItemStack().getCount();

			int amount = Math.min(maxAllowedWithHeldStack, Math.min(isCountLocked ? buyCount : this.buyCountUnlocked, this.getSelectedBuy().getStock()));

			boolean isHandFree = this.mc.player.inventory.getItemStack().isEmpty();
			boolean isBuyItem = ItemHelper.getKeyForItemStack(this.mc.player.inventory.getItemStack()) == ItemHelper
					.getKeyForItemStack(this.getSelectedBuy().getItemStack());

			if (isHandFree)
			{
				this.getSelectedBuy().addStock(-amount);

				ItemStack stack = this.getSelectedBuy().getItemStack().copy();
				stack.setCount(amount);

				this.mc.player.inventory.setItemStack(stack);
			}
			else if (isBuyItem)
			{
				this.getSelectedBuy().addStock(-amount);

				this.mc.player.inventory.getItemStack().setCount(this.mc.player.inventory.getItemStack().getCount() + amount);
			}

			this.addBuyCount(0);
		}

		if (InputHelper.isHovered(this.sell) && this.sell.state().isEnabled())
		{
			ItemStack stack = this.container.getSlot(0).getStack();
			double singleValue = ShopUtil
					.getFilteredPrice(this.shopInstance, stack,
							AetherAPI.content().currency().getSingleValue(stack, this.shopInstance.getCurrencyType().getClass()));

			if (singleValue < 1)
			{
				double wholeValue = ShopUtil
						.getFilteredPrice(this.shopInstance, stack,
								AetherAPI.content().currency().getValue(stack, this.shopInstance.getCurrencyType().getClass()));
				double decimals = wholeValue - MathHelper.floor(wholeValue);

				double howManyTimesDivInto = decimals / singleValue;

				int leftover = MathHelper.floor(howManyTimesDivInto);

				this.container.getSlot(0).getStack().setCount(leftover);
			}
			else
			{
				this.container.getSlot(0).putStack(ItemStack.EMPTY);
			}

			NetworkingAether.sendPacketToServer(new PacketShopSell(this.shopIndex));
		}

		for (GuiShopBuy b : this.buys)
		{
			if (InputHelper.isHovered(b))
			{
				this.buyCountUnlocked = 1;
				this.selectedBuy = b.getBuyIndex();

				break;
			}
		}
	}

	@Override
	public void onCurrencyChange(long prevCurrency, long newCurrency)
	{
		this.playerCoins.setCurrencyValue(newCurrency);

		this.giltBag.dim().mod().x(this.playerCoins.dim().x() + (this.playerCoins.dim().width() / 2) - 1).flush();

		this.addBuyCount(0);
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();

		int scroll = MathHelper.clamp(Mouse.getEventDWheel(), -1, 1);

		if (scroll != 0 && (InputHelper.isHovered(this.buy) || InputHelper.isHovered(this.upArrow) || InputHelper.isHovered(this.downArrow) || InputHelper
				.isHovered(this.lockButton)))
		{
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
			{
				this.addBuyCount(scroll * 64);
			}
			else
			{
				this.addBuyCount(scroll);
			}
		}
	}

	@Override
	public void setHoveredDescription(ItemStack stack, List<String> desc)
	{
		this.hoveredStack = stack;
		this.hoverDescription = desc;
	}
}
