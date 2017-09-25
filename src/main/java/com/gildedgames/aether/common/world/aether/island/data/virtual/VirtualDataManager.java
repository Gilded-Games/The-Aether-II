package com.gildedgames.aether.common.world.aether.island.data.virtual;

import com.gildedgames.aether.api.world.TemplateInstance;
import com.gildedgames.aether.api.world.generation.TemplateDefinition;
import com.gildedgames.aether.api.world.generation.TemplateLoc;
import com.gildedgames.aether.api.world.islands.IIslandData;
import com.gildedgames.aether.api.world.islands.IVirtualChunk;
import com.gildedgames.aether.api.world.islands.IVirtualDataManager;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.List;

public class VirtualDataManager implements IVirtualDataManager
{

	private final World world;

	private final IIslandData parent;

	private final List<TemplateInstance> templateInstances = Lists.newArrayList();

	private IVirtualChunk[] chunks;

	private boolean isPrepped = false, isPreparing = false;

	public VirtualDataManager(final World world, final IIslandData parent)
	{
		this.world = world;
		this.parent = parent;

		final int chunkWidth = parent.getBounds().getWidth() >> 4;
		final int chunkLength = parent.getBounds().getLength() >> 4;

		this.chunks = new IVirtualChunk[chunkWidth * chunkLength];
	}

	private int getChunkCount()
	{
		return this.chunks.length;
	}

	private int getChunkIndexInner(final int chunkX, final int chunkZ)
	{
		final int minChunkX = this.parent.getBounds().getMinX() / 16;
		final int minChunkZ = this.parent.getBounds().getMinZ() / 16;

		final int indexX = Math.abs(chunkX - minChunkX);
		final int indexZ = Math.abs(chunkZ - minChunkZ);

		final int index = indexX + (indexZ * (this.parent.getBounds().getWidth() >> 4));

		if (index < this.getChunkCount() && index >= 0)
		{
			return index;
		}

		return -1;
	}

	private int getChunkIndex(final int chunkX, final int chunkZ)
	{
		final int index = this.getChunkIndexInner(chunkX, chunkZ);

		if (index == -1)
		{
			throw new ArrayIndexOutOfBoundsException("Tried to access chunk index that isn't in this VirtualDataManager");
		}

		return index;
	}

	private int getChunkIndexFromBlockPos(final int x, final int z)
	{
		return this.getChunkIndex(x >> 4, z >> 4);
	}

	@Override
	public void placeTemplate(final TemplateDefinition def, final TemplateLoc loc)
	{
		final TemplateInstance instance = new TemplateInstance(def, loc);

		this.templateInstances.add(instance);
	}

	@Override
	public boolean dropTemplate(final TemplateInstance templateInstance)
	{
		return this.templateInstances.remove(templateInstance);
	}

	@Override
	public List<TemplateInstance> getPlacedTemplates()
	{
		return this.templateInstances;
	}

	@Override
	public Chunk createRealChunkFromVirtualData(final World world, final int chunkX, final int chunkZ)
	{
		final VirtualChunkFunnel funnel = new VirtualChunkFunnel(this.getChunk(chunkX, chunkZ));

		return new Chunk(world, funnel, chunkX, chunkZ);
	}

	@Override
	public void dropChunk(final int chunkX, final int chunkZ)
	{
		final int chunkIndex = this.getChunkIndexInner(chunkX, chunkZ);

		if (chunkIndex == -1)
		{
			return;
		}

		this.chunks[chunkIndex] = null;
	}

	@Override
	public void dropAllChunks()
	{
		this.chunks = null;
	}

	@Override
	public boolean hasChunk(final int chunkX, final int chunkZ)
	{
		final int chunkIndex = this.getChunkIndexInner(chunkX, chunkZ);

		if (chunkIndex == -1)
		{
			return false;
		}

		final IVirtualChunk chunk = this.chunks[chunkIndex];

		return chunk != null;
	}

	@Override
	@Nullable
	public IVirtualChunk getChunk(final int chunkX, final int chunkZ)
	{
		final int chunkIndex = this.getChunkIndexInner(chunkX, chunkZ);

		if (chunkIndex == -1)
		{
			return null;
		}

		IVirtualChunk chunk = this.chunks[chunkIndex];

		if (chunk == null)
		{
			this.chunks[this.getChunkIndex(chunkX, chunkZ)] = new VirtualChunk(chunkX, chunkZ);

			chunk = this.chunks[this.getChunkIndex(chunkX, chunkZ)];
		}

		return chunk;
	}

	@Nullable
	@Override
	public World getWorld()
	{
		return this.world;
	}

	@Override
	public boolean canAccess(final BlockPos pos)
	{
		return this.canAccess(pos.getX(), pos.getZ()) && pos.getY() < VirtualChunk.HEIGHT && pos.getY() >= 0;
	}

	@Override
	public boolean canAccess(final int x, final int z)
	{
		return this.getChunkIndexInner(x >> 4, z >> 4) != -1;
	}

	@Override
	public BlockPos getTopPos(final BlockPos pos)
	{
		return new BlockPos(pos.getX(), this.getTopY(pos.getX(), pos.getZ()), pos.getZ());
	}

	@Override
	public int getTopY(final int x, final int z)
	{
		return this.getChunkFromBlockPos(x, z).getHeightValue(x & 15, z & 15);
	}

	private IVirtualChunk getChunkFromBlockPos(final int x, final int z)
	{
		IVirtualChunk chunk = this.chunks[this.getChunkIndexFromBlockPos(x, z)];

		if (chunk == null)
		{
			chunk = this.chunks[this.getChunkIndexFromBlockPos(x, z)] = new VirtualChunk(x >> 4, z >> 4);
		}

		return chunk;
	}

	@Override
	public IBlockState getBlock(final int x, final int y, final int z)
	{
		if (y > VirtualChunk.HEIGHT || !this.canAccess(x, z))
		{
			throw new ArrayIndexOutOfBoundsException("Tried to access block outside of this island's bounds");
		}

		final IVirtualChunk chunk = this.getChunkFromBlockPos(x, z);

		return chunk.getState(x & 15, y, z & 15);
	}

	@Override
	public boolean setBlock(final int x, final int y, final int z, final IBlockState state)
	{
		if (y > VirtualChunk.HEIGHT || !this.canAccess(x, z))
		{
			throw new ArrayIndexOutOfBoundsException("Tried to set block outside of the world height");
		}

		final IVirtualChunk chunk = this.getChunkFromBlockPos(x, z);

		final int posX = x % 16;
		final int posZ = z % 16;

		chunk.setState(posX < 0 ? 16 + posX : posX, y, posZ < 0 ? 16 + posZ : posZ, state);

		return true;
	}

	@Override
	public boolean isPrepped()
	{
		return this.isPrepped;
	}

	@Override
	public void markPrepared()
	{
		this.isPrepped = true;
	}

	@Override
	public boolean isPreparing()
	{
		return this.isPreparing;
	}

	@Override
	public void setPreparing(final boolean preparing)
	{
		this.isPreparing = preparing;
	}

	@Override
	public void write(final NBTTagCompound tag)
	{
		tag.setBoolean("isPrepped", this.isPrepped);
		tag.setBoolean("isPreparing", this.isPreparing);

		final NBTTagList templateInstances = new NBTTagList();

		for (final TemplateInstance instance : this.templateInstances)
		{
			final NBTTagCompound data = new NBTTagCompound();

			instance.write(data);

			templateInstances.appendTag(data);
		}

		tag.setTag("templateInstances", templateInstances);
	}

	@Override
	public void read(final NBTTagCompound tag)
	{
		this.isPrepped = tag.getBoolean("isPrepped");
		this.isPreparing = tag.getBoolean("isPreparing");

		final NBTTagList templateInstances = tag.getTagList("templateInstances", 10);

		if (this.templateInstances.isEmpty())
		{
			for (int i = 0; i < templateInstances.tagCount(); i++)
			{
				final NBTTagCompound data = templateInstances.getCompoundTagAt(i);

				this.templateInstances.add(new TemplateInstance(data));
			}
		}
	}

	@Nullable
	@Override
	public TileEntity getTileEntity(final BlockPos pos)
	{
		return null;
	}

	@Override
	public int getCombinedLight(final BlockPos pos, final int lightValue)
	{
		return 0;
	}

	@Override
	public IBlockState getBlockState(final BlockPos pos)
	{
		return this.getBlock(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public boolean isAirBlock(final BlockPos pos)
	{
		return this.getBlockState(pos).getBlock().isAir(this.getBlockState(pos), this, pos);
	}

	@Override
	public Biome getBiome(final BlockPos pos)
	{
		return null;
	}

	@Override
	public int getStrongPower(final BlockPos pos, final EnumFacing direction)
	{
		return 0;
	}

	@Override
	public WorldType getWorldType()
	{
		return null;
	}

	@Override
	public boolean isSideSolid(final BlockPos pos, final EnumFacing side, final boolean _default)
	{
		return false;
	}

	@Override
	public void setBlockToAir(final BlockPos pos)
	{
		this.setBlockState(pos, Blocks.AIR.getDefaultState());
	}

	@Override
	public boolean setBlockState(final BlockPos pos, final IBlockState state)
	{
		return this.setBlock(pos.getX(), pos.getY(), pos.getZ(), state);
	}

	/**
	 * Flags are not considered in this implementation since they're not necessary.
	 * @param pos The position that the state will be placed.
	 * @param state The block state that will be set.
	 * @param flags The flags for this state's placement.
	 */
	@Override
	public boolean setBlockState(final BlockPos pos, final IBlockState state, final int flags)
	{
		return this.setBlockState(pos, state);
	}
}