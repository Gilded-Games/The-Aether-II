package com.gildedgames.aether.common.world.biomes.arctic_peaks;

import com.gildedgames.aether.api.registrar.BlocksAether;
import com.gildedgames.aether.api.world.decoration.WorldDecoration;
import com.gildedgames.aether.api.world.decoration.WorldDecorationSimple;
import com.gildedgames.aether.api.world.generation.BlueprintWorldGen;
import com.gildedgames.aether.api.world.generation.positioners.PositionerLevels;
import com.gildedgames.aether.api.world.generation.positioners.PositionerSurface;
import com.gildedgames.aether.api.world.islands.IIslandBounds;
import com.gildedgames.aether.api.world.islands.IIslandData;
import com.gildedgames.aether.api.world.islands.IIslandGenerator;
import com.gildedgames.aether.api.world.noise.IChunkNoiseBuffer2D;
import com.gildedgames.aether.common.blocks.IBlockSnowy;
import com.gildedgames.aether.common.blocks.natural.BlockAetherGrass;
import com.gildedgames.aether.common.init.GenerationAether;
import com.gildedgames.aether.common.util.helpers.IslandHelper;
import com.gildedgames.aether.common.world.WorldProviderAether;
import com.gildedgames.aether.common.world.biomes.BiomeAetherBase;
import com.gildedgames.aether.common.world.biomes.ISnowyBiome;
import com.gildedgames.aether.common.world.island.IslandVariables;
import com.gildedgames.aether.common.world.island.generators.IslandGeneratorHighlands;
import com.gildedgames.aether.common.world.noise.ChunkDataGenerator2DSingle;
import com.gildedgames.aether.common.world.noise.impl.NoiseGeneratorIslandTerrain;
import com.gildedgames.aether.common.world.util.GenUtil;
import com.gildedgames.orbis.lib.core.BlueprintDefinition;
import com.gildedgames.orbis.lib.util.mc.NBT;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BiomeArcticPeaks extends BiomeAetherBase implements ISnowyBiome
{

	public BiomeArcticPeaks(final BiomeProperties properties, final ResourceLocation registryName)
	{
		super(properties, registryName);

		this.topBlock = BlocksAether.aether_grass.getDefaultState().withProperty(BlockAetherGrass.PROPERTY_VARIANT, BlockAetherGrass.ARCTIC);
	}

	@Override
	public IBlockState getCoastalBlock()
	{
		return BlocksAether.highlands_packed_ice.getDefaultState();
	}

	@Override
	public IIslandGenerator createIslandGenerator(Random rand, IIslandData islandData)
	{
		int coastHeight = 1 + rand.nextInt(3);
		double coastSpread = 0.15;

		if (coastHeight == 0)
		{
			coastSpread = 0.0;
		}

		double mountainAmplitude = 3.75;

		boolean hasTerraces = rand.nextInt(30) == 0;

		int maxTerrainHeight = 40 + rand.nextInt(35);

		return new IslandGeneratorHighlands(IslandVariables.build()
				.coastHeight(coastHeight)
				.coastSpread(coastSpread)
				.lakeBlendRange(0.05 + (rand.nextDouble() * 0.5))
				.lakeDepth(rand.nextInt(40) + 5)
				.lakeScale(40.0D + (rand.nextDouble() * 30.0D))
				.lakeThreshold(rand.nextDouble() * 0.3)
				.maxTerrainHeight(maxTerrainHeight)
				.terraces(hasTerraces)
				.lakeConcentrationModifier(0.5 + (rand.nextDouble() * -2.5))
				.heightSampleFilter((heightSample) -> Math.min(1.1, Math.pow(heightSample, mountainAmplitude) * 0.55))
				.snowCaps(!hasTerraces)
				.maxTerrainHeight(20 + rand.nextInt(25))
				.maxYFilter((bottomMaxY, filteredSample, cutoffPoint) -> bottomMaxY + ((filteredSample - (hasTerraces ? cutoffPoint : 0.0))
						* maxTerrainHeight))
				.lakeBottomValueFilter((lakeBottomValue) -> 0.0));
	}

	@Override
	public Collection<NBT> createIslandComponents(final IIslandData islandData)
	{
		return Collections.emptyList();
	}

	@Override
	public float getRarityWeight()
	{
		return 1.0F;
	}

	@Override
	public void postDecorate(final World world, final Random rand, final BlockPos pos)
	{
		final int chunkX = pos.getX() >> 4;
		final int chunkZ = pos.getZ() >> 4;

		IIslandData island = IslandHelper.get(world, chunkX, chunkZ);

		if (island == null)
		{
			return;
		}

		WorldProviderAether provider = WorldProviderAether.get(world);

		IIslandBounds bounds  = island.getBounds();

		final IChunkNoiseBuffer2D samples = new ChunkDataGenerator2DSingle(new NoiseGeneratorIslandTerrain(provider.getNoise(), bounds), 4)
				.generate(chunkX, chunkZ)
				.createInterpolatedNoiseBuffer();

		final int posX = pos.getX() + 8;
		final int posZ = pos.getZ() + 8;

		final double centerX = island.getBounds().getCenterX();
		final double centerZ = island.getBounds().getCenterZ();

		final double radiusX = island.getBounds().getWidth() / 2.0;
		final double radiusZ = island.getBounds().getLength() / 2.0;

		for (int x = 0; x < 16; x++)
		{
			for (int z = 0; z < 16; z++)
			{
				final BlockPos p = new BlockPos(posX + x, 0, posZ + z);

				if (world.isBlockLoaded(p))
				{
					final int worldX = posX + x;
					final int worldZ = posZ + z;

					final double distX = Math.abs((centerX - worldX) * (1.0 / radiusX));
					final double distZ = Math.abs((centerZ - worldZ) * (1.0 / radiusZ));

					// Get distance from center of Island
					final double dist = Math.sqrt(distX * distX + distZ * distZ) / 1.0D;

					final double sample = samples.get(x, z);
					final double heightSample = sample + 1.0 - dist;

					final BlockPos blockpos1 = p.add(0, world.getHeight(posX + x, posZ + z), 0);
					final BlockPos blockpos2 = blockpos1.down();

					if (world.canBlockFreezeWater(blockpos2))
					{
						world.setBlockState(blockpos2, BlocksAether.highlands_ice.getDefaultState(), 2 | 16);
					}

					if (heightSample > 0.5)
					{
						final IBlockState state = world.getBlockState(blockpos1);
						final Block block = state.getBlock();

						if (world.canSnowAt(blockpos1, true))
						{
							world.setBlockState(blockpos1, BlocksAether.highlands_snow_layer.getDefaultState(), 2 | 16);
						}
						else if (block instanceof IBlockSnowy)
						{
							final IBlockState newState = state.withProperty(IBlockSnowy.PROPERTY_SNOWY, Boolean.TRUE);

							world.setBlockState(blockpos1, newState, 2 | 16);
						}
					}
				}
			}
		}
	}

	@Override
	public List<WorldDecoration> createBasicDecorations(Random rand)
	{
		List<WorldDecoration> decorations = Lists.newArrayList();

		decorations.add(new WorldDecorationSimple(2, 0.0F, EventType.GRASS, new PositionerSurface(), GenerationAether.short_aether_grass));
		decorations.add(new WorldDecorationSimple(1, 0.2F, EventType.GRASS, new PositionerSurface(), GenerationAether.skyroot_twigs));

		List<IBlockState> flowers = Lists.newArrayList();

		flowers.addAll(GenUtil.GENERAL_FLOWER_STATES);

		decorations.add(GenUtil.createFlowerDecorations(rand, flowers, Lists.newArrayList(BlocksAether.arctic_spikespring.getDefaultState())));
		decorations.add(GenUtil.createShroomDecorations(GenUtil.SHROOM_STATES));

		decorations.add(new WorldDecorationSimple(6, 0.0F, EventType.GRASS, new PositionerLevels(0, 128), GenerationAether.holystone_rocks));

		decorations.add(new WorldDecorationSimple(2, 0.5f, EventType.CUSTOM, new PositionerLevels(26, 90), GenerationAether.ice_crystals));

		return decorations;
	}

	@Override
	public List<WorldDecoration> createTreeDecorations(Random rand)
	{
		List<WorldDecoration> treeDecorations = Lists.newArrayList();

		BlueprintDefinition[] forest = new BlueprintDefinition[]
				{
						GenerationAether.SKYROOT_OAK_DARK_BLUE, GenerationAether.SKYROOT_OAK_BLUE
				};

		BlueprintDefinition[] neopolitan = new BlueprintDefinition[]
				{
						GenerationAether.SKYROOT_OAK_DARK_BLUE, GenerationAether.SKYROOT_OAK_BLUE,
						GenerationAether.WISPROOT_BLUE, GenerationAether.WISPROOT_DARK_BLUE
				};

		BlueprintDefinition[] chosen = rand.nextBoolean() ? neopolitan : forest;

		int amountOfTreeTypes = 2 + rand.nextInt(4);

		for (int i = 0; i < amountOfTreeTypes; i++)
		{
			treeDecorations.add(new WorldDecorationSimple(15, 0.0F, EventType.TREE, new PositionerSurface(),
					new BlueprintWorldGen(chosen[rand.nextInt(chosen.length)])));
		}

		treeDecorations.add(new WorldDecorationSimple(20, 0.0F, EventType.TREE, new PositionerSurface(), new BlueprintWorldGen(GenerationAether.GREATROOT_TREE)));

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

	@Override
	@SideOnly(Side.CLIENT)
	public int getSkyColorByTemp(final float currentTemperature)
	{
		return 0xACBAE6;
	}

}
