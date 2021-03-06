package com.gildedgames.aether.common.entities.ai.moa;

import com.gildedgames.aether.api.registrar.BlocksAether;
import com.gildedgames.aether.common.blocks.natural.plants.BlockWovenSticks;
import com.gildedgames.aether.common.entities.animals.EntityMoa;
import com.gildedgames.aether.common.entities.genes.GeneUtil;
import com.gildedgames.aether.common.entities.genes.moa.MoaGenePool;
import com.gildedgames.aether.common.entities.genes.moa.MoaNest;
import com.gildedgames.aether.common.entities.tiles.TileEntityMoaEgg;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.World;

public class AIMoaLayEgg extends EntityAIBase
{

	public final World world;

	public final EntityMoa mother;

	public final float moveSpeed;

	public boolean hasLayedEgg;

	private final int motherSeed;

	private final int fatherSeed;

	public AIMoaLayEgg(EntityMoa mother, int motherSeed, int fatherSeed, float moveSpeed)
	{
		this.world = mother.world;

		this.mother = mother;

		this.motherSeed = motherSeed;
		this.fatherSeed = fatherSeed;

		this.moveSpeed = moveSpeed;
	}

	@Override
	public boolean isInterruptible()
	{
		return false;
	}

	@Override
	public boolean shouldExecute()
	{
		if (this.hasLayedEgg)
		{
			return false;
		}

		if (this.mother == null || this.world == null)
		{
			return false;
		}

		MoaNest nest = this.mother.getFamilyNest();

		if (nest != null)
		{
			if (nest.pos == null)
			{
				return true;
			}

			if (this.world.getBlockState(nest.pos.add(0, 1, 0)).getBlock() == BlocksAether.moa_egg)
			{
				return false;
			}

			return this.world.getBlockState(nest.pos).getBlock() instanceof BlockWovenSticks;
		}

		return true;
	}

	@Override
	public void updateTask()
	{
		super.updateTask();

		MoaNest nest = this.mother.getFamilyNest();

		if (nest == null || !nest.hasInitialized)
		{
			this.world.setBlockState(this.mother.getPosition(), BlocksAether.moa_egg.getDefaultState());

			TileEntityMoaEgg egg = (TileEntityMoaEgg) this.world.getTileEntity(this.mother.getPosition());

			if (egg != null)
			{
				MoaGenePool teGenes = egg.getGenePool();

				teGenes.transformFromParents(GeneUtil.getRandomSeed(this.mother.getEntityWorld()), this.fatherSeed, this.motherSeed);
			}

			this.mother.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F,
					(this.mother.getRNG().nextFloat() - this.mother.getRNG().nextFloat()) * 0.2F + 1.0F);

			this.hasLayedEgg = true;

			return;
		}

		this.mother.getNavigator().tryMoveToXYZ(nest.pos.getX(), nest.pos.getY() + 1, nest.pos.getZ(), this.moveSpeed);

		if (this.mother.getNavigator().getPath() != null && this.mother.getNavigator().getPath().isFinished())
		{
			this.world.setBlockState(nest.pos.add(0, 1, 0), BlocksAether.moa_egg.getDefaultState());

			TileEntityMoaEgg egg = (TileEntityMoaEgg) this.world.getTileEntity(nest.pos.add(0, 1, 0));

			if (egg != null)
			{
				MoaGenePool teGenes = egg.getGenePool();

				teGenes.transformFromParents(GeneUtil.getRandomSeed(this.mother.getEntityWorld()), this.fatherSeed, this.motherSeed);
			}

			this.mother.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F,
					(this.mother.getRNG().nextFloat() - this.mother.getRNG().nextFloat()) * 0.2F + 1.0F);

			this.hasLayedEgg = true;
		}
	}

}
