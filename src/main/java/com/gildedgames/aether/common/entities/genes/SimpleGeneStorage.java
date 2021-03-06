package com.gildedgames.aether.common.entities.genes;

import com.gildedgames.aether.api.entity.genes.IGeneStorage;
import net.minecraft.nbt.NBTTagCompound;

public class SimpleGeneStorage implements IGeneStorage
{

	private int seed, fatherSeed, motherSeed;

	public SimpleGeneStorage()
	{

	}

	@Override
	public int getSeed()
	{
		return this.seed;
	}

	@Override
	public void setSeed(int seed)
	{
		this.seed = seed;
	}

	@Override
	public int getFatherSeed()
	{
		return this.fatherSeed;
	}

	@Override
	public void setFatherSeed(int seed)
	{
		this.fatherSeed = seed;
	}

	@Override
	public int getMotherSeed()
	{
		return this.motherSeed;
	}

	@Override
	public void setMotherSeed(int seed)
	{
		this.motherSeed = seed;
	}

	@Override
	public void setShouldRetransform(boolean flag)
	{

	}

	@Override
	public void write(NBTTagCompound tag)
	{
		tag.setInteger("seed", this.getSeed());
		tag.setInteger("fatherSeed", this.getFatherSeed());
		tag.setInteger("motherSeed", this.getMotherSeed());
	}

	@Override
	public void read(NBTTagCompound tag)
	{
		this.setSeed(tag.getInteger("seed"));
		this.setFatherSeed(tag.getInteger("fatherSeed"));
		this.setMotherSeed(tag.getInteger("motherSeed"));
	}
}
