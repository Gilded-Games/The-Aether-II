package com.gildedgames.aether.client.gui.container.guidebook.discovery;

import com.gildedgames.aether.common.AetherCore;
import com.gildedgames.aether.common.capabilities.entity.player.PlayerAether;
import com.gildedgames.aether.common.travellers_guidebook.entries.TGEntryEffectsPage;
import com.gildedgames.orbis.lib.client.gui.util.GuiAbstractButton;
import com.gildedgames.orbis.lib.client.gui.util.GuiTexture;
import com.gildedgames.orbis.lib.client.gui.util.gui_library.GuiElement;
import com.gildedgames.orbis.lib.client.rect.Dim2D;
import com.gildedgames.orbis.lib.client.rect.Pos2D;
import net.minecraft.util.ResourceLocation;

public class EffectsSlot extends GuiAbstractButton
{
    private static final ResourceLocation DARK_OVERLAY = AetherCore.getResource("textures/gui/inventory/dark_slot_overlay.png");

    private final TGEntryEffectsPage page;

    private final ResourceLocation texture;

    private final PlayerAether playerAether;

    private GuiTexture icon;

    public EffectsSlot(final PlayerAether playerAether, final Pos2D pos, final TGEntryEffectsPage page)
    {
        super(Dim2D.build().width(18).height(18).pos(pos).flush(), new GuiTexture(Dim2D.build().width(18).height(18).flush(), DARK_OVERLAY));

        this.playerAether = playerAether;
        this.page = page;
        this.texture = page.getSlotTexture();
    }

    @Override
    public void build()
    {
        super.build();

        final ResourceLocation texture = this.texture;

        this.icon = new GuiTexture(Dim2D.build().width(16).height(16).x(2).y(2).flush(), texture);

        this.context().addChildren(this.icon);
    }

    @Override
    public void onDraw(final GuiElement element)
    {
        super.onDraw(element);

        final ResourceLocation texture = this.texture;

        this.icon.setResourceLocation(texture);
    }

    public TGEntryEffectsPage getPage()
    {
        return this.page;
    }
}
