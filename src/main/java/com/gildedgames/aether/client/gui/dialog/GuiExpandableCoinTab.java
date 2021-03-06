package com.gildedgames.aether.client.gui.dialog;

import com.gildedgames.aether.common.AetherCore;
import com.gildedgames.orbis.lib.client.gui.util.GuiTexture;
import com.gildedgames.orbis.lib.client.gui.util.gui_library.GuiElement;
import com.gildedgames.orbis.lib.client.rect.Dim2D;
import com.gildedgames.orbis.lib.client.rect.Pos2D;
import com.gildedgames.orbis.lib.client.rect.Rect;
import com.gildedgames.orbis.lib.util.InputHelper;
import net.minecraft.util.ResourceLocation;

public class GuiExpandableCoinTab extends GuiElement
{

	private static final ResourceLocation GILT_BAG = AetherCore.getResource("textures/gui/shop/gilt_bag.png");

	private static final ResourceLocation GILT_GROW = AetherCore.getResource("textures/gui/trade/gilt_grow.png");

	private static final ResourceLocation GILT_GROW_LEFT = AetherCore.getResource("textures/gui/trade/gilt_grow_left.png");

	private static final ResourceLocation GILT_GROW_RIGHT = AetherCore.getResource("textures/gui/trade/gilt_grow_right.png");

	private GuiCoins coinsCount;

	private GuiTexture giltBag, shape, extent;

	private boolean left, inactive;

	private float x, y, width;

	public GuiExpandableCoinTab(Rect rect, boolean shouldDisplayAlways, boolean left, boolean inactive, float x, float y)
	{
		super(rect, shouldDisplayAlways);

		this.left = left;
		this.inactive = inactive;
		this.x = x;
		this.y = y;
	}

	@Override
	public void build()
	{
		this.giltBag = new GuiTexture(Dim2D.build().width(16).height(16).flush(),
			GILT_BAG);

		this.coinsCount = new GuiCoins(Dim2D.build().flush(), true);

		this.extent = new GuiTexture(Dim2D.build().width(1).height(23).flush(),
				GILT_GROW);

		if (this.left)
		{
			this.shape = new GuiTexture(Dim2D.build().width(3).height(23).flush(),
					GILT_GROW_LEFT);
		}
		else
		{
			this.shape = new GuiTexture(Dim2D.build().width(3).height(23).flush(),
					GILT_GROW_RIGHT);
		}

		this.update();
		this.context().addChildren(this.shape, this.extent, this.coinsCount, this.giltBag);
	}

	private void update()
	{
		Pos2D center = InputHelper.getCenter().clone().flush();

		float off = this.coinsCount.dim().width();

		this.dim().mod().pos(center).addX(this.x - (this.left ? off : 0)).y(this.y).flush();

		this.giltBag.dim().mod().pos((this.left ? -18 : off + 2), 4).flush();

		this.coinsCount.dim().mod().pos(0, 4).flush();

		this.extent.dim().mod().width(off + 21).pos((this.left ? -19 : -2), 0).flush();

		if (this.left)
		{
			this.shape.dim().mod().pos(-22, 0).flush();
		}
		else
		{
			this.shape.dim().mod().pos(off + 19, 0).flush();
		}

		this.width = off + 27;
	}

	public boolean intersected(int mouseX, int mouseY)
	{
		Pos2D center = InputHelper.getCenter().clone().flush();
		float off = (this.left ? this.width - 12 : 0);

		return mouseX > center.x() - 7 + this.x - off && mouseX < center.x() - 7 + this.x - off + this.width && mouseY > this.y && mouseY < this.y + 21;
	}

	public void setCurrencyValue(double value)
	{
		this.coinsCount.setCurrencyValue(value);
		this.update();
	}
}
