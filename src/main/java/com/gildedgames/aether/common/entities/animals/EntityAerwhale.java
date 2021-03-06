package com.gildedgames.aether.common.entities.animals;

import com.gildedgames.aether.api.registrar.SoundsAether;
import com.gildedgames.aether.common.entities.ai.EntityAIForcedWander;
import com.gildedgames.aether.common.entities.flying.EntityFlying;
import com.gildedgames.aether.common.entities.flying.PathNavigateFlyer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityAerwhale extends EntityFlying
{

	public EntityAerwhale(final World world)
	{
		super(world);

		this.setSize(3.0F, 3.0F);
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();

		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(250);
	}

	@Override
	protected PathNavigate createNavigator(final World worldIn)
	{
		PathNavigateFlyer navigateFlyer = new PathNavigateFlyer(this, worldIn);

		navigateFlyer.setAvoidGround(true);

		return navigateFlyer;
	}

	@Override
	protected void initEntityAI()
	{
		final EntityAIMoveTowardsRestriction moveTowardsRestriction = new EntityAIMoveTowardsRestriction(this, 0.4D);
		final EntityAIForcedWander wander = new EntityAIForcedWander(this, 0.4D, 10, 12, 7);

		wander.setMutexBits(3);
		moveTowardsRestriction.setMutexBits(3);

		this.tasks.addTask(1, moveTowardsRestriction);
		this.tasks.addTask(2, wander);
	}

	@Override
	public int getTalkInterval()
	{
		return 2000;
	}

	@Override
	protected float getSoundVolume()
	{
		return 10.0F;
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundsAether.aerwhale_ambient;
	}

	@Override
	protected SoundEvent getHurtSound(final DamageSource src)
	{
		return SoundsAether.aerwhale_ambient;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundsAether.aerwhale_death;
	}

	@Override
	protected boolean canDespawn()
	{
		return false;
	}
}
