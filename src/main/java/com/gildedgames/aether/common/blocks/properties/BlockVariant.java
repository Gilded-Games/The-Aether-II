package com.gildedgames.aether.common.blocks.properties;

import java.util.Objects;

public class BlockVariant implements Comparable<BlockVariant>
{
	private final int meta;

	private final String name;

	public BlockVariant(int meta, String name)
	{
		this.meta = meta;
		this.name = name;
	}

	public int getMeta()
	{
		return this.meta;
	}

	public String getName()
	{
		return this.name;
	}

	@Override
	public int compareTo(BlockVariant variant)
	{
		return Integer.compare(this.meta, variant.meta);
	}

	@Override
	public String toString()
	{
		return this.getName();
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}

		if (o == null || this.getClass() != o.getClass())
		{
			return false;
		}

		BlockVariant that = (BlockVariant) o;

		return this.meta == that.meta && Objects.equals(this.name, that.name);
	}

	@Override
	public int hashCode()
	{
		int hash = 1;
		hash = 31 * hash + this.name.hashCode();
		hash = 31 * hash + this.meta;

		return hash;
	}
}
