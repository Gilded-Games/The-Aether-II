package com.gildedgames.aether.common.entities.ai;

import com.gildedgames.aether.common.entities.projectiles.EntityDart;
import com.gildedgames.aether.common.items.weapons.ItemDartType;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.util.math.MathHelper;

public class EntityAIAechorPlantAttack extends EntityAITarget
{
	private int ticksUntilAttack = 3;

	public EntityAIAechorPlantAttack(final EntityCreature creature)
	{
		super(creature, true);
	}

	@Override
	public boolean shouldExecute()
	{
		return this.hasTarget();
	}

	@Override
	public boolean shouldContinueExecuting()
	{
		final EntityLivingBase target = this.taskOwner.getAttackTarget();

		if (target == null || !target.isEntityAlive())
		{
			this.ticksUntilAttack = 20;

			return false;
		}

		return true;
	}

	@Override
	public void updateTask()
	{
		if (this.ticksUntilAttack <= 0)
		{
			this.ticksUntilAttack = 45;

			final EntityLivingBase prey = this.taskOwner.getAttackTarget();

			if (prey == null)
			{
				return;
			}

			final EntityCreature predator = this.taskOwner;

			if (!predator.world.isRemote)
			{
				final EntityDart dart = new EntityDart(predator.world, predator);
				dart.shoot(predator.posX, predator.posY + 1.0F, predator.posZ, 0.6F, 1.0F);

				final double motionX = prey.posX - predator.posX;
				final double motionY = prey.getEntityBoundingBox().minY + (double) (prey.height / 3.0F) - dart.posY;
				final double motionZ = prey.posZ - predator.posZ;

				final double accel = (double) MathHelper.sqrt(motionX * motionX + motionZ * motionZ);

				dart.shoot(motionX, motionY + accel * 0.2D, motionZ, 1.6F, 0.5f);
				dart.setDamage(0.5f);

				dart.setDartType(ItemDartType.POISON);

				dart.world.spawnEntity(dart);
			}
		}

		this.ticksUntilAttack--;
	}

	public boolean hasTarget()
	{
		final EntityCreature predator = this.taskOwner;

		final double maxDistance = this.getTargetDistance();

		return predator.getAttackTarget() != null && predator.isEntityAlive()
				&& predator.getDistanceSq(predator.getAttackTarget()) < (maxDistance * maxDistance);
	}
}
