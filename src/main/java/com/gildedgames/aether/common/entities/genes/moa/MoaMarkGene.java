package com.gildedgames.aether.common.entities.genes.moa;

import com.gildedgames.aether.api.entity.genes.Gene;
import com.gildedgames.aether.api.entity.genes.Inheritance;
import com.gildedgames.aether.api.entity.genes.Mutation;
import com.gildedgames.aether.common.AetherCore;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MoaMarkGene implements Gene
{

	private final String name;

	private final String resourceName;

	private final Inheritance inheritance;

	private final Mutation[] potentialMutations;

	private final ResourceLocation back;

	private final ResourceLocation head;

	private final ResourceLocation tail;

	private final ResourceLocation wing;

	private final ResourceLocation back_egg;

	private final ResourceLocation head_egg;

	public MoaMarkGene(String name, String resourceName, Inheritance inheritance, Mutation... potentialMutations)
	{
		this.name = name;
		this.resourceName = resourceName;

		this.inheritance = inheritance;

		this.potentialMutations = potentialMutations;

		this.back = AetherCore.getResource("textures/entities/moa/back/" + resourceName + ".png");
		this.head = AetherCore.getResource("textures/entities/moa/head/" + resourceName + ".png");
		this.tail = AetherCore.getResource("textures/entities/moa/tail/" + resourceName + ".png");
		this.wing = AetherCore.getResource("textures/entities/moa/wing/" + resourceName + ".png");

		this.back_egg = AetherCore.getResource("textures/tile_entities/moa_egg/back/" + resourceName + ".png");
		this.head_egg = AetherCore.getResource("textures/tile_entities/moa_egg/head/" + resourceName + ".png");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String localizedName()
	{
		return I18n.format(this.name);
	}

	@Override
	public String unlocalizedName()
	{
		return this.name;
	}

	@Override
	public Mutation[] potentialMutations()
	{
		return this.potentialMutations;
	}

	@Override
	public Inheritance inheritance()
	{
		return this.inheritance;
	}

	public ResourceLocation getEggBack()
	{
		return this.back_egg;
	}

	public ResourceLocation getEggHead()
	{
		return this.head_egg;
	}

	public ResourceLocation getBack()
	{
		return this.back;
	}

	public ResourceLocation getHead()
	{
		return this.head;
	}

	public ResourceLocation getTail()
	{
		return this.tail;
	}

	public ResourceLocation getWing()
	{
		return this.wing;
	}

	public String getResourceName()
	{
		return this.resourceName;
	}

}
