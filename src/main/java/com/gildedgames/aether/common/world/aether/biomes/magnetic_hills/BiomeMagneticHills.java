package com.gildedgames.aether.common.world.aether.biomes.magnetic_hills;

import com.gildedgames.aether.api.world.generation.WorldDecoration;
import com.gildedgames.aether.api.world.generation.WorldDecorationSimple;
import com.gildedgames.aether.api.world.islands.IIslandData;
import com.gildedgames.aether.api.world.islands.IIslandGenerator;
import com.gildedgames.aether.common.blocks.BlocksAether;
import com.gildedgames.aether.common.blocks.natural.BlockAetherGrass;
import com.gildedgames.aether.common.registry.content.GenerationAether;
import com.gildedgames.aether.common.world.aether.biomes.BiomeAetherBase;
import com.gildedgames.aether.common.world.aether.island.gen.IslandGenerators;
import com.gildedgames.aether.common.world.templates.TemplateWorldGen;
import com.gildedgames.orbis.api.processing.IBlockAccessExtended;
import com.gildedgames.orbis.api.util.mc.NBT;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class BiomeMagneticHills extends BiomeAetherBase
{

	public BiomeMagneticHills(final BiomeProperties properties, final ResourceLocation registryName)
	{
		super(properties, registryName);

		this.topBlock = BlocksAether.aether_grass.getDefaultState().withProperty(BlockAetherGrass.PROPERTY_VARIANT, BlockAetherGrass.MAGNETIC);
	}

	@Override
	public IBlockState getCoastalBlock()
	{
		return BlocksAether.ferrosite_sand.getDefaultState();
	}

	@Override
	public IIslandGenerator createIslandGenerator(Random rand)
	{
		return IslandGenerators.MAGNETIC_HILLS;
	}

	@Override
	public Collection<NBT> createIslandComponents(final IIslandData islandData)
	{
		final List<NBT> components = Lists.newArrayList();

		final BlockPos center = new BlockPos(islandData.getBounds().getCenterX(), 0, islandData.getBounds().getCenterZ());

		components
				.add(new MagneticHillsData(center, islandData.getSeed(), 200,
						300));

		return components;
	}

	@Override
	public float getRarityWeight()
	{
		return 1.0F;
	}

	@Override
	public void postDecorate(final World world, final Random rand, final BlockPos pos)
	{

	}

	@Override
	public List<WorldDecoration> createBasicDecorations(Random rand)
	{
		List<WorldDecoration> decorations = Lists.newArrayList();

		decorations.add(new WorldDecorationSimple(2, GenerationAether.short_aether_grass));
		decorations.add(new WorldDecorationSimple(1, 0.2F, GenerationAether.skyroot_twigs));

		decorations.add(new WorldDecorationSimple(6, GenerationAether.holystone_rocks)
		{
			@Override
			public BlockPos findPositionToPlace(final IBlockAccessExtended blockAccess, final Random rand, final BlockPos pos)
			{
				final int x = rand.nextInt(16) + 8;
				final int y = rand.nextInt(128);
				final int z = rand.nextInt(16) + 8;

				return pos.add(x, y, z);
			}
		});

		decorations.add(new WorldDecorationSimple(1, 0.06F, GenerationAether.golden_aercloud)
		{
			@Override
			public BlockPos findPositionToPlace(final IBlockAccessExtended blockAccess, final Random rand, final BlockPos pos)
			{
				final int width = 16;
				final int minY = 90;
				final int maxY = 130;

				return pos.add(rand.nextInt(width), minY + rand.nextInt(maxY - minY), rand.nextInt(width));
			}
		});

		return decorations;
	}

	@Override
	public List<WorldDecoration> createTreeDecorations(Random rand)
	{
		List<WorldDecoration> treeDecorations = Lists.newArrayList();

		treeDecorations.add(new WorldDecorationSimple(2, new TemplateWorldGen(GenerationAether.blue_skyroot_tree)));
		treeDecorations.add(new WorldDecorationSimple(2, new TemplateWorldGen(GenerationAether.green_skyroot_tree),
				new TemplateWorldGen(GenerationAether.green_skyroot_small_pine)));
		treeDecorations.add(new WorldDecorationSimple(2, new TemplateWorldGen(GenerationAether.green_skyroot_oak)));
		treeDecorations.add(new WorldDecorationSimple(1, new TemplateWorldGen(GenerationAether.golden_oak)));
		treeDecorations.add(new WorldDecorationSimple(1, new TemplateWorldGen(GenerationAether.skyroot_moa_nest_tree_1)));

		return treeDecorations;
	}

	@Override
	public float createForestTreeCountModifier(Random rand)
	{
		if (rand.nextInt(30) == 0)
		{
			return 0.1F + (rand.nextFloat() * 0.9F);
		}

		return 0.75F + (rand.nextFloat() * 0.25F);
	}

	@Override
	public float createOpenAreaDecorationGenChance(Random rand)
	{
		return 0.15F * rand.nextFloat();
	}

}
