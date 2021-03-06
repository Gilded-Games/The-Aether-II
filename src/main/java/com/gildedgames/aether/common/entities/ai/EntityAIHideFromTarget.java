package com.gildedgames.aether.common.entities.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class EntityAIHideFromTarget extends EntityAIBase
{

	static final int maxDist = 12, minDist = 4;

	private final double movementSpeed;

	protected EntityLivingBase hideFrom;

	final EntityCreature entity;

	final Class<? extends EntityLivingBase> hideFromClass;

	private BlockPos hidingPos;

	public EntityAIHideFromTarget(final EntityCreature entity, final Class<? extends EntityLivingBase> clazz, final double movementSpeed)
	{
		this.entity = entity;
		this.hideFromClass = clazz;
		this.movementSpeed = movementSpeed;
	}

	@Override
	public boolean shouldExecute()
	{
		if (this.hidingPos != null)
		{
			return true;
		}

		final List entities = this.entity.world.getEntitiesWithinAABB(this.hideFromClass, this.entity.getEntityBoundingBox().expand(maxDist, maxDist, maxDist));

		if (entities.isEmpty())
		{
			return false;
		}

		EntityLivingBase toHideFrom = null;

		for (final Object o : entities)
		{
			if (o instanceof EntityLivingBase && !(o instanceof EntityPlayer && ((EntityPlayer) o).isCreative()))
			{
				toHideFrom = (EntityLivingBase) o;
			}
		}
		if (toHideFrom == null)
		{
			return false;
		}
		else
		{
			this.hideFrom = toHideFrom;

			if (!this.hideFrom.canEntityBeSeen(this.entity))
			{
				return false;
			}

			final Vec3d spot = this.findHidingSpot();

			if (spot == null)
			{
				return false;
			}

			this.hidingPos = new BlockPos(spot.x, spot.y, spot.z);

			return true;
		}
	}

	@Override
	public boolean shouldContinueExecuting()
	{
		return this.hidingPos != null;
	}

	@Override
	public void startExecuting()
	{
		final Path path = this.entity.getNavigator().getPathToPos(this.hidingPos);

		this.entity.getNavigator().setPath(path, this.movementSpeed);
	}

	@Override
	public void updateTask()
	{
		if (this.entity.getNavigator().noPath() && this.hideFrom.canEntityBeSeen(this.entity))
		{
			this.hidingPos = null;
		}
	}

	protected Vec3d findHidingSpot()
	{
		final Random random = this.entity.getRNG();
		final World world = this.entity.world;

		for (int i = 0; i < 13; ++i)
		{
			final int j = MathHelper.floor(this.entity.posX + random.nextInt(20) - 10.0D);
			final int k = MathHelper.floor(this.entity.getEntityBoundingBox().minY + random.nextInt(6) - 3.0D);
			final int l = MathHelper.floor(this.entity.posZ + random.nextInt(20) - 10.0D);

			final RayTraceResult raytrace = world.rayTraceBlocks(new Vec3d(j, k + this.entity.getEyeHeight(), l), new Vec3d(this.hideFrom.posX,
					this.hideFrom.posY + this.hideFrom.getEyeHeight(), this.hideFrom.posZ));

			if (raytrace != null && raytrace.typeOfHit != null)
			{
				return new Vec3d(j, k, l);
			}
		}

		return null;
	}

}
