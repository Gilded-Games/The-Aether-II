package com.gildedgames.aether.common.entities.monsters;

import com.gildedgames.aether.api.entity.damage.DamageTypeAttributes;
import com.gildedgames.aether.api.entity.effects.EEffectIntensity;
import com.gildedgames.aether.api.entity.effects.IAetherStatusEffectIntensity;
import com.gildedgames.aether.api.entity.effects.IAetherStatusEffectPool;
import com.gildedgames.aether.api.entity.effects.IAetherStatusEffects;
import com.gildedgames.aether.api.registrar.CapabilitiesAether;
import com.gildedgames.aether.api.registrar.SoundsAether;
import com.gildedgames.aether.common.entities.ai.EntityAIUnstuckBlueAercloud;
import com.gildedgames.aether.common.entities.ai.cockatrice.EntityAICockatriceHide;
import com.gildedgames.aether.common.entities.ai.cockatrice.EntityAICockatriceSneakAttack;
import com.gildedgames.aether.common.entities.ai.cockatrice.EntityAICockatriceWander;
import com.gildedgames.aether.common.entities.effects.StatusEffectCockatriceVenom;
import com.gildedgames.aether.common.init.LootTablesAether;
import com.gildedgames.aether.common.util.helpers.EntityUtil;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import java.util.Map;

public class EntityCockatrice extends EntityAetherMob
{
	protected Map<String, Float> defenseMap = Maps.newHashMap();
	{{
		this.defenseMap.put("Very Weak", 4.0F);
		this.defenseMap.put("Weak", 2.0F);
		this.defenseMap.put("Average", 0.0F);
		this.defenseMap.put("Strong", -2.0F);
		this.defenseMap.put("Very Strong", -4.0F);
	}}

	private static final DataParameter<Boolean> IS_HIDING = new DataParameter<>(16, DataSerializers.BOOLEAN);

	private static final DataParameter<Boolean> IS_HIDDEN = new DataParameter<>(17, DataSerializers.BOOLEAN);

	private static final DataParameter<Boolean> IS_ATTACKING = new DataParameter<>(18, DataSerializers.BOOLEAN);

	public EntityCockatrice(final World world)
	{
		super(world);

		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(0, new EntityAIUnstuckBlueAercloud(this));
		this.tasks.addTask(1, new EntityAICockatriceHide(this, EntityPlayer.class, 0.9D));
		this.tasks.addTask(2, new EntityAICockatriceWander(this, 0.35D));
		this.tasks.addTask(7, new EntityAILookIdle(this));

		this.targetTasks.addTask(0, new EntityAICockatriceSneakAttack(this, EntityPlayer.class));

		this.jumpHelper = new JumpHelperDisable(this);

		this.setSize(1.0F, 2.5F);
		this.stepHeight = 1.0F;

		this.experienceValue = 7;

		this.getResistances().put(IAetherStatusEffects.effectTypes.COCKATRICE_VENOM, 1.0D);
	}

	@Override
	protected void jump()
	{

	}

	@Override
	protected void entityInit()
	{
		super.entityInit();

		this.dataManager.register(EntityCockatrice.IS_HIDING, Boolean.FALSE);
		this.dataManager.register(EntityCockatrice.IS_HIDDEN, Boolean.FALSE);
		this.dataManager.register(EntityCockatrice.IS_ATTACKING, Boolean.FALSE);
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();

		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);

		this.getEntityAttribute(DamageTypeAttributes.SLASH_DEFENSE_LEVEL).setBaseValue(-4.0f);
		this.getEntityAttribute(DamageTypeAttributes.IMPACT_DEFENSE_LEVEL).setBaseValue(0.0f);
		this.getEntityAttribute(DamageTypeAttributes.PIERCE_DEFENSE_LEVEL).setBaseValue(2.0f);
	}

	@Override
	public boolean isPotionApplicable(final PotionEffect potionEffect)
	{
		return potionEffect.getPotion() != MobEffects.POISON && super.isPotionApplicable(potionEffect);
	}

	@Override
	public boolean attackEntityAsMob(final Entity entity)
	{
		final boolean flag = super.attackEntityAsMob(entity);

		if (flag && entity instanceof EntityLivingBase)
		{
			final EntityLivingBase living = (EntityLivingBase) entity;

			this.applyStatusEffectOnAttack(entity);
		}

		return flag;
	}

	@Override
	protected void applyStatusEffectOnAttack(final Entity target)
	{
		if (target instanceof EntityLivingBase)
		{
			final EntityLivingBase living = (EntityLivingBase) target;

			if (!living.isActiveItemStackBlocking())
			{
				IAetherStatusEffectPool statusEffectPool = living.getCapability(CapabilitiesAether.STATUS_EFFECT_POOL, null);

				if (statusEffectPool != null)
				{
					if (!statusEffectPool.effectExists(IAetherStatusEffects.effectTypes.COCKATRICE_VENOM))
					{
						statusEffectPool.applyStatusEffect(IAetherStatusEffects.effectTypes.COCKATRICE_VENOM, 55);
					}
					else
					{
						statusEffectPool.modifyActiveEffectBuildup(IAetherStatusEffects.effectTypes.COCKATRICE_VENOM,
								statusEffectPool.getBuildupFromEffect(IAetherStatusEffects.effectTypes.COCKATRICE_VENOM) + 55);
					}
				}
			}
		}
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		EntityUtil.despawnEntityDuringDaytime(this);
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundsAether.cockatrice_ambient;
	}

	@Override
	protected SoundEvent getHurtSound(final DamageSource damageSourceIn)
	{
		return SoundsAether.cockatrice_hurt;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundsAether.cockatrice_death;
	}

	public boolean isHiding()
	{
		return this.dataManager.get(EntityCockatrice.IS_HIDING);
	}

	public void setHiding(final boolean isHiding)
	{
		this.dataManager.set(EntityCockatrice.IS_HIDING, isHiding);
	}

	public boolean isHidden()
	{
		return this.dataManager.get(EntityCockatrice.IS_HIDDEN);
	}

	public void setHidden(final boolean isHidden)
	{
		this.dataManager.set(EntityCockatrice.IS_HIDDEN, isHidden);
	}

	public boolean isAttacking()
	{
		return this.dataManager.get(EntityCockatrice.IS_ATTACKING);
	}

	public void setAttacking(final boolean isAttacking)
	{
		this.dataManager.set(EntityCockatrice.IS_ATTACKING, isAttacking);
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		return LootTablesAether.ENTITY_COCKATRICE;
	}

	public static class JumpHelperDisable extends EntityJumpHelper
	{

		public JumpHelperDisable(final EntityLiving entityIn)
		{
			super(entityIn);
		}

		@Override
		public void setJumping()
		{

		}

		@Override
		public void doJump()
		{

		}

	}

}
