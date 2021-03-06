package com.gildedgames.aether.common.blocks.decorative;

import com.gildedgames.aether.common.blocks.properties.BlockVariant;
import com.gildedgames.aether.common.blocks.properties.PropertyVariant;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockHellfirestoneDecorative extends BlockDecorative
{
	public static final BlockVariant BASE_BRICKS = new BlockVariant(0, "base_bricks"),
			BASE_PILLAR = new BlockVariant(1, "base_pillar"),
			CAPSTONE_BRICKS = new BlockVariant(2, "capstone_bricks"),
			CAPSTONE_PILLAR = new BlockVariant(3, "capstone_pillar"),
			FLAGSTONES = new BlockVariant(4, "flagstones"),
			KEYSTONE = new BlockVariant(5, "keystone");

	public static final PropertyVariant PROPERTY_VARIANT = PropertyVariant
			.create("variant", BASE_BRICKS, BASE_PILLAR, CAPSTONE_BRICKS, CAPSTONE_PILLAR, FLAGSTONES, KEYSTONE);

	public BlockHellfirestoneDecorative()
	{
		super(Material.ROCK);

		this.setHardness(1.5F);
		this.setResistance(10.0F);

		this.setSoundType(SoundType.STONE);

		this.setHarvestLevel("pickaxe", 0);
	}

	@Override
	protected PropertyVariant getVariantProperty()
	{
		return PROPERTY_VARIANT;
	}
}
