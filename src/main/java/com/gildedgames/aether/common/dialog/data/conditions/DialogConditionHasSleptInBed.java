package com.gildedgames.aether.common.dialog.data.conditions;

import com.gildedgames.aether.api.dialog.IDialogCondition;
import com.gildedgames.aether.api.dialog.IDialogController;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Type;

public class DialogConditionHasSleptInBed implements IDialogCondition
{
	private final boolean validate;

	private DialogConditionHasSleptInBed(boolean validate)
	{
		this.validate = validate;
	}

	@Override
	public boolean isMet(IDialogController controller)
	{
		if (controller.getDialogPlayer().world.isRemote)
		{
			return false;
		}

		EntityPlayer player = controller.getDialogPlayer();

		BlockPos bedPos = player.getBedLocation(((EntityPlayerMP) player).dimension);
		final EntityPlayerMP mp = (EntityPlayerMP) player;

		if (bedPos != null)
		{
			bedPos = EntityPlayer.getBedSpawnLocation(mp.getServerWorld(), bedPos, mp.isSpawnForced(mp.dimension));
		}

		return bedPos != null == this.validate;
	}

	public static class Deserializer implements JsonDeserializer<DialogConditionHasSleptInBed>
	{
		@Override
		public DialogConditionHasSleptInBed deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			return new DialogConditionHasSleptInBed(json.getAsJsonObject().get("validate").getAsBoolean());
		}
	}
}
