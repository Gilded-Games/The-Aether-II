package com.gildedgames.aether.common.entities.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class EntityAIMoveToBlockYSensitive extends EntityAIBase
{
	private final EntityCreature creature;

	private final double movementSpeed;

	private final int searchLength;

	/** Controls task execution delay */
	protected int runDelay;

	/** Block to move to */
	protected BlockPos destinationBlock = BlockPos.ORIGIN;

	private int timeoutCounter;

	private int maxStayTicks;

	private boolean isAboveDestination;

	public EntityAIMoveToBlockYSensitive(final EntityCreature creature, final double speedIn, final int length)
	{
		this.creature = creature;
		this.movementSpeed = speedIn;
		this.searchLength = length;
		this.setMutexBits(5);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		if (this.runDelay > 0)
		{
			--this.runDelay;
			return false;
		}
		else
		{
			this.runDelay = 200 + this.creature.getRNG().nextInt(200);
			return this.searchForDestination();
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting()
	{
		return this.timeoutCounter >= -this.maxStayTicks && this.timeoutCounter <= 1200 && this.shouldMoveTo(this.creature.world, this.destinationBlock);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		this.creature.getNavigator().tryMoveToXYZ((double) ((float) this.destinationBlock.getX()) + 0.5D, (double) (this.destinationBlock.getY() + 1),
				(double) ((float) this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
		this.timeoutCounter = 0;
		this.maxStayTicks = this.creature.getRNG().nextInt(this.creature.getRNG().nextInt(1200) + 1200) + 1200;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	@Override
	public void updateTask()
	{
		if (this.creature.getDistanceSqToCenter(this.destinationBlock.up()) > 1.0D)
		{
			this.isAboveDestination = false;
			++this.timeoutCounter;

			if (this.timeoutCounter % 40 == 0)
			{
				this.creature.getNavigator().tryMoveToXYZ((double) ((float) this.destinationBlock.getX()) + 0.5D, (double) (this.destinationBlock.getY() + 1),
						(double) ((float) this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
			}
		}
		else
		{
			this.isAboveDestination = true;
			--this.timeoutCounter;
		}
	}

	protected boolean getIsAboveDestination()
	{
		return this.isAboveDestination;
	}

	/**
	 * Searches and sets new destination block and returns true if a suitable block (specified in {@link
	 * net.minecraft.entity.ai.EntityAIMoveToBlock#shouldMoveTo(World, BlockPos) EntityAIMoveToBlockYSensitive#shouldMoveTo(World,
	 * BlockPos)}) can be found.
	 */
	private boolean searchForDestination()
	{
		final int i = this.searchLength;
		final int j = 1;
		final BlockPos blockpos = new BlockPos(this.creature);

		final int heightSearch = 4;

		for (int k = -heightSearch; k < heightSearch; k++)
		{
			for (int l = 0; l < i; ++l)
			{
				for (int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1)
				{
					for (int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1)
					{
						final BlockPos blockpos1 = blockpos.add(i1, k, j1);

						if (this.creature.isWithinHomeDistanceFromPosition(blockpos1) && this.shouldMoveTo(this.creature.world, blockpos1))
						{
							this.destinationBlock = blockpos1;
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * Return true to set given position as destination
	 */
	protected abstract boolean shouldMoveTo(World worldIn, BlockPos pos);
}