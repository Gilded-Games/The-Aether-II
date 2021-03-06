package com.gildedgames.aether.common.entities.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

import java.util.List;
import java.util.Random;

public class EntityAIRamAttack extends EntityAIBase
{
	private EntityLiving entity;

	private EntityLivingBase target;

	private double maxDistance, chargeSpeed;

	private final float minCharge, maxCharge;

	private float currentCharge;

	private boolean ramming;

	private Random rand;

	public EntityAIRamAttack(EntityLiving entity, double chargeSpeed, float minChargeTime, float maxChargeTime, double maxDistance)
	{
		this.rand = new Random();
		this.entity = entity;
		this.chargeSpeed = chargeSpeed;
		this.minCharge = minChargeTime;
		this.maxCharge = maxChargeTime;
		this.maxDistance = maxDistance;
	}

	@Override
	public void updateTask()
	{
		if (this.shouldExecute())
		{
			if (this.ramming)
			{
				this.entity.getLookHelper().setLookPositionWithEntity(this.target, (float)this.entity.getHorizontalFaceSpeed(), (float)this.entity.getVerticalFaceSpeed());
				this.entity.faceEntity(this.target, 180f, 180f);

				List<EntityLivingBase> entities = this.entity.world.getEntitiesWithinAABB(EntityLivingBase.class, this.entity.getEntityBoundingBox().expand(1, 1, 1));

				if (entities.contains(this.target))
				{
					this.ramming = false;
					this.entity.attackEntityAsMob(this.target);
					this.target = null;
				}
			}
		}
		else
		{
			this.target = null;
		}
	}

	@Override
	public boolean shouldExecute()
	{
		return this.target != null && this.entity.getDistance(this.target) < this.maxDistance;
	}

	public void setTarget(EntityLivingBase target)
	{
		if (this.target == null || this.currentCharge <= 0)
		{
			this.target = target;
			this.currentCharge = (this.minCharge + (this.maxCharge - this.minCharge) * this.rand.nextFloat()) * 20;
		}
		else if (this.target.equals(target))
		{
			this.currentCharge /= 1.2f;
		}
	}

	public void update()
	{
		if (this.target == null)
		{
			return;
		}

		if (this.ramming)
		{
			EntityLivingBase target = this.target;

			double ang = Math.atan2(target.posZ - this.entity.posZ, target.posX - this.entity.posX);
			this.entity.motionX = (float) Math.cos(ang) * this.chargeSpeed;
			this.entity.motionZ = (float) Math.sin(ang) * this.chargeSpeed;

			this.entity.getLookHelper().setLookPositionWithEntity(this.target, (float)this.entity.getHorizontalFaceSpeed(), (float)this.entity.getVerticalFaceSpeed());
			this.entity.faceEntity(this.target, 360f, 360f);
		}
		else if (this.currentCharge > 0)
		{
			this.currentCharge--;

			if (this.currentCharge <= 0)
			{
				this.ramming = true;
			}
		}
	}

	public void setMaxDistance(double maxDistance)
	{
		this.maxDistance = maxDistance;
	}

}
