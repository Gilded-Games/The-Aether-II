package com.gildedgames.orbis.client.player.godmode;

import com.gildedgames.aether.api.orbis.IWorldRenderer;
import com.gildedgames.aether.common.AetherCore;
import com.gildedgames.orbis.client.gui.util.GuiTexture;
import com.gildedgames.orbis.client.util.rect.Dim2D;
import com.gildedgames.orbis.common.player.PlayerOrbisModule;
import com.gildedgames.orbis.common.player.godmode.GodPowerReplace;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class GodPowerReplaceClient implements IGodPowerClient
{
	private static final ResourceLocation TEXTURE = AetherCore.getResource("orbis/godmode/power_icons/replace_icon.png");

	private static final int SHAPE_COLOR = 0x4179e6;

	private final GodPowerReplace server;

	private final GuiTexture icon;

	public GodPowerReplaceClient(final GodPowerReplace server)
	{
		this.server = server;

		this.icon = new GuiTexture(Dim2D.build().width(14).height(14).flush(), TEXTURE);
	}

	@Override
	public boolean openGuiScreen()
	{
		return false;
	}

	@Override
	public String displayName()
	{
		return "Replace";
	}

	@Override
	public GuiTexture getIcon()
	{
		return this.icon;
	}

	@Override
	public boolean has3DCursor(final PlayerOrbisModule module)
	{
		final ItemStack held = module.getEntity().getHeldItemMainhand();

		return held.getItem() instanceof ItemBlock;
	}

	@Override
	public float minFade3DCursor(final PlayerOrbisModule module)
	{
		return 0.0F;
	}

	@Override
	public int getShapeColor(final PlayerOrbisModule module)
	{
		return SHAPE_COLOR;
	}

	@Override
	public List<IWorldRenderer> getActiveRenderers(final PlayerOrbisModule module, final World world)
	{
		return Collections.emptyList();
	}
}