package com.gildedgames.aether.common.init;

import com.gildedgames.aether.common.AetherCore;
import com.gildedgames.orbis.lib.OrbisLib;
import com.gildedgames.orbis.lib.data.blueprint.BlueprintData;
import com.gildedgames.orbis.lib.data.management.IProject;
import com.gildedgames.orbis.lib.data.management.IProjectManager;
import net.minecraft.util.ResourceLocation;

import java.util.Optional;
import java.util.UUID;

public class BlueprintsAether
{

	public static BlueprintData OUTPOST_A;

	public static BlueprintData OUTPOST_B;

	public static BlueprintData NECROMANCER_TOWER;

	public static BlueprintData ABAND_ANGEL_STOREROOM_1A;

	public static BlueprintData ABAND_ANGEL_WATCHTOWER_1A;

	public static BlueprintData ABAND_CAMPSITE_1A;

	public static BlueprintData ABAND_HUMAN_HOUSE_1A;

	public static BlueprintData ABAND_HUMAN_HOUSE_1B;

	public static BlueprintData SKYROOT_WATCHTOWER_1A;

	public static BlueprintData SKYROOT_WATCHTOWER_1B;

	public static BlueprintData SKYROOT_WATCHTOWER_2A;

	public static BlueprintData SKYROOT_WATCHTOWER_2B;

	public static BlueprintData SKYROOT_WATCHTOWER_3A;

	public static BlueprintData SKYROOT_WATCHTOWER_3B;

	public static BlueprintData WELL_1A;

	public static BlueprintData WELL_1B;

	public static BlueprintData GREATROOT_TREE;

	public static BlueprintData SKYROOT_OAK_GREEN, SKYROOT_OAK_BLUE, SKYROOT_OAK_DARK_BLUE;

	public static BlueprintData AMBEROOT_TREE;

	public static BlueprintData WISPROOT_GREEN, WISPROOT_BLUE, WISPROOT_DARK_BLUE;

	public static BlueprintData CRAZY_MUTANT_TREE;

	private static IProject project;

	private BlueprintsAether()
	{

	}

	private static BlueprintData loadData(String name)
	{
		Optional<UUID> id = project.getCache().getDataId(name + ".blueprint");

		if (id.isPresent())
		{
			Optional<BlueprintData> data = project.getCache().getData(id.get());

			if (data.isPresent())
			{
				return data.get();
			}
		}

		throw new RuntimeException("Failed to load Blueprint Data: " + name + ".blueprint");
	}

	public static void load(IProjectManager projectManager)
	{
		ResourceLocation location = AetherCore.getResource("aetherii");

		project = OrbisLib.services().loadProject(null, location, AetherCore.INSTANCE, "aether_ii");

		projectManager.cacheProject(location.getPath(), project);

		OUTPOST_A = loadData("outpost_a");
		OUTPOST_B = loadData("outpost_b");
		NECROMANCER_TOWER = loadData("necromancer_tower");
		ABAND_ANGEL_STOREROOM_1A = loadData("aband_angel_storeroom_1a");
		ABAND_ANGEL_WATCHTOWER_1A = loadData("aband_angel_watchtower_1a");
		ABAND_CAMPSITE_1A = loadData("aband_campsite_1a");
		ABAND_HUMAN_HOUSE_1A = loadData("aband_human_house_1a");
		ABAND_HUMAN_HOUSE_1B = loadData("aband_human_house_1b");
		SKYROOT_WATCHTOWER_1A = loadData("skyroot_watchtower_1a");
		SKYROOT_WATCHTOWER_1B = loadData("skyroot_watchtower_1b");
		SKYROOT_WATCHTOWER_2A = loadData("skyroot_watchtower_2a");
		SKYROOT_WATCHTOWER_2B = loadData("skyroot_watchtower_2b");
		SKYROOT_WATCHTOWER_3A = loadData("skyroot_watchtower_3a");
		SKYROOT_WATCHTOWER_3B = loadData("skyroot_watchtower_3b");
		WELL_1A = loadData("well_1a");
		WELL_1B = loadData("well_1b");
		GREATROOT_TREE = loadData("greatroot_1");
		SKYROOT_OAK_GREEN = loadData("trees/skyroot/skyroot_oak_green");
		SKYROOT_OAK_BLUE = loadData("trees/skyroot/skyroot_oak_blue");
		SKYROOT_OAK_DARK_BLUE = loadData("trees/skyroot/skyroot_oak_dark_blue");
		AMBEROOT_TREE = loadData("trees/amberoot/amberoot_tree");
		WISPROOT_GREEN = loadData("trees/wisproot/wisproot_green");
		WISPROOT_BLUE = loadData("trees/wisproot/wisproot_blue");
		WISPROOT_DARK_BLUE = loadData("trees/wisproot/wisproot_dark_blue");

		CRAZY_MUTANT_TREE = loadData("trees/skyroot/mutated_tree");
	}
}
