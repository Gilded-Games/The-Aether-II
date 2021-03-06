package com.gildedgames.aether.common.entities.effects;

import com.gildedgames.aether.api.entity.effects.EEffectIntensity;
import com.gildedgames.aether.common.capabilities.entity.effects.EffectsDamageSource;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.text.TextFormatting;

import java.util.Collection;

public class StatusEffectFungalRot extends StatusEffect
{
	private EntityLivingBase affectedEntity;

	public StatusEffectFungalRot(EntityLivingBase livingBase)
	{
		super(effectTypes.FUNGAL_ROT, new AttributeModifier("aether.statusEffectFungalRot", -0.15, 1).setSaved(false), livingBase);

		this.affectedEntity = livingBase;
	}

	@Override
	public void applyEffect(EntityLivingBase livingBase, int timer)
	{
		IAttributeInstance iAttributeInstance;

		if (this.isEffectApplied)
		{
			iAttributeInstance = livingBase.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
			if (iAttributeInstance != null && !iAttributeInstance.hasModifier(this.getAttributeModifier()))
			{
				iAttributeInstance.applyModifier(this.getAttributeModifier());
			}

			if (this.effectTimer % (TICKS_PER_SECOND * 2) == 0)
			{
				livingBase.attackEntityFrom(EffectsDamageSource.FUNGAL_ROT, 1f);
			}
		}
		else
		{
			iAttributeInstance = livingBase.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
			if (iAttributeInstance != null && iAttributeInstance.getModifier(this.getAttributeModifier().getID()) != null)
			{
				iAttributeInstance.removeModifier(this.getAttributeModifier());
			}
		}
	}

	@Override
	public void onEffectEnd()
	{
		EntityLivingBase livingBase = this.affectedEntity;

		IAttributeInstance iAttributeInstance;

		iAttributeInstance = livingBase.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		if (iAttributeInstance != null && iAttributeInstance.getModifier(this.getAttributeModifier().getID()) != null)
		{
			iAttributeInstance.removeModifier(this.getAttributeModifier());
		}
	}

	@Override
	public int getBuildupFromIntensity(EEffectIntensity intensity)
	{
		switch (intensity)
		{
			case MINOR:
				return 5;
			case ORDINARY:
				return 15;
			case MAJOR:
				return 40;
		}

		return 0;
	}

	@Override
	public void addInformation(Collection<String> label)
	{
		label.add(TextFormatting.DARK_GREEN.toString() + TextFormatting.ITALIC.toString() + I18n.format("effect.aether.fungal_rot"));
	}
}
