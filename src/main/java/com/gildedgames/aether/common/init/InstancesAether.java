package com.gildedgames.aether.common.init;

import com.gildedgames.aether.common.world.instances.necromancer_tower.NecromancerTowerInstanceFactory;
import com.gildedgames.aether.common.world.instances.necromancer_tower.NecromancerTowerInstanceHelper;
import com.gildedgames.orbis.lib.OrbisLib;

public class InstancesAether
{
	public static NecromancerTowerInstanceHelper NECROMANCER_TOWER_HANDLER;

	public static void postInit()
	{
		final NecromancerTowerInstanceFactory factory = new NecromancerTowerInstanceFactory(DimensionsAether.NECROMANCER_TOWER);

		NECROMANCER_TOWER_HANDLER = new NecromancerTowerInstanceHelper(OrbisLib.instances().createInstanceHandler(factory));
	}
}
