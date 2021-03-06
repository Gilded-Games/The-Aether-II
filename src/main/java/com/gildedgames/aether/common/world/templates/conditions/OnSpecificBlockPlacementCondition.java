package com.gildedgames.aether.common.world.templates.conditions;

import com.gildedgames.aether.api.world.templates.PlacementConditionTemplate;
import com.gildedgames.orbis.lib.processing.IBlockAccessExtended;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.template.Template;

import java.util.List;

public class OnSpecificBlockPlacementCondition implements PlacementConditionTemplate
{

	private final Block[] blocks;

	public OnSpecificBlockPlacementCondition(final Block... blocks)
	{
		this.blocks = blocks;
	}

	@Override
	public boolean canPlace(final Template template, final IBlockAccessExtended world, final BlockPos placedAt, final Template.BlockInfo block)
	{
		if (block.pos.getY() == placedAt.getY() && block.blockState.getBlock() != Blocks.AIR
				&& block.blockState.getBlock() != Blocks.STRUCTURE_VOID)
		{
			final BlockPos down = block.pos.down();

			if (!world.canAccess(down))
			{
				return false;
			}

			final Block blockDown = world.getBlockState(down).getBlock();

			for (final Block s : this.blocks)
			{
				if (s == blockDown)
				{
					return true;
				}
			}

			return false;
		}

		return true;
	}

	@Override
	public boolean canPlaceCheckAll(final Template template, final IBlockAccessExtended world, final BlockPos placedAt, final List<Template.BlockInfo> blocks)
	{
		return true;
	}

}
