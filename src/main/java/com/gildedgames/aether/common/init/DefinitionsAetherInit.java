package com.gildedgames.aether.common.init;

import com.gildedgames.aether.api.AetherAPI;
import com.gildedgames.aether.common.AetherCore;
import net.minecraft.util.ResourceLocation;

public class DefinitionsAetherInit
{
    public static void preInit()
    {
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:aechor_plant"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:aerbunny"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:burrukai"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:carrion_sprout"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:cockatrice"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:glactrix"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:glitterwing"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:kirrid"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:moa"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:sheepuff"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:skyroot_lizard"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:swet"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:taegore"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:tempest"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:varanys"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:zephyr"));

        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:ambrosium_poisoning"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:bleed"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:cockatrice_venom"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:fracture"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:freeze"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:guard_break"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:irradiation"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:saturation_boost"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:stun"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:toxin"));
        AetherAPI.content().tgManager().registerEntry(new ResourceLocation("aether:webbing"));

        if (AetherCore.isServer())
        {
            AetherAPI.content().tgManager().load();
        }
    }
}
