package com.gildedgames.aether.common.world.spawning.conditions;

import com.gildedgames.aether.api.world.spawn.conditions.IConditionPosition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class CheckBiome implements IConditionPosition
{

	private final Biome biomeToCheckFor;

	public CheckBiome(Biome biomeToCheckFor)
	{
		this.biomeToCheckFor = biomeToCheckFor;
	}

	@Override
	public boolean isMet(World world, BlockPos spawnAt, BlockPos underneath)
	{
		return world.getBiome(spawnAt) == this.biomeToCheckFor;
	}

}
