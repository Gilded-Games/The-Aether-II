package com.gildedgames.aether.common.items.irradiated;

import com.gildedgames.aether.common.items.IDropOnDeath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemIrradiatedVisuals extends Item implements IDropOnDeath
{
	private static ResourceLocation RES_ITEM_GLINT;

	static
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
		}
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		final RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();

		Minecraft mc = Minecraft.getMinecraft();

		GlStateManager.pushMatrix();

		GlStateManager.disableLighting();

		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		IBakedModel model = itemRenderer.getItemModelMesher().getItemModel(stack);

		this.renderEffect(model);

		GlStateManager.enableLighting();

		GlStateManager.popMatrix();

		return false;
	}

	@SideOnly(Side.CLIENT)
	private void renderEffect(IBakedModel model)
	{
		Minecraft mc = Minecraft.getMinecraft();

		GlStateManager.depthMask(false);
		GlStateManager.depthFunc(514);
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);

		mc.getTextureManager().bindTexture(RES_ITEM_GLINT);

		GlStateManager.matrixMode(5890);
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);

		float f = (float) (Minecraft.getSystemTime() % 10000L) / 10000.0F / 8.0F;

		GlStateManager.translate(f, 0.0F, 0.0F);
		GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);

		mc.getRenderItem().renderModel(model, -999999992);

		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);

		float f1 = (float) (Minecraft.getSystemTime() % 12873L) / 12873.0F / 8.0F;

		GlStateManager.translate(-f1, 0.0F, 0.0F);
		GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);

		mc.getRenderItem().renderModel(model, -999999992);

		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableLighting();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);

		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}

}
