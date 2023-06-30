package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.example.mixin.ScoreboardMixin;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Team;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExampleMod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	private static final Pattern SUCCESS_FAILURE_DESTROY = Pattern.compile(" \\* Success: (\\d+)% - Failure: \\d+% - Destroy: \\d+%");

	private void clickSlotWithName(GenericContainerScreen gcs, String name) {
		for (Slot slot : gcs.getScreenHandler().slots) {
			if (slot == null) continue;
			ItemStack stack = slot.getStack();
			if (stack == null) continue;
			if (stack.getName().getString().equals(name)) {
				MinecraftClient.getInstance().interactionManager.clickSlot(
						gcs.getScreenHandler().syncId, slot.id, 0, SlotActionType.PICKUP, MinecraftClient.getInstance().player);
				break;
			}
		}
	}

	@Override
	public void onInitialize() {
		boolean[] doingRerolls = { false };
		ClientMessageEvents.MESSAGE_RECEIVED.register((client, message) -> {
			Matcher m = SUCCESS_FAILURE_DESTROY.matcher(message.getString());
			if (m.matches()) {
				if (Integer.parseInt(m.group(1)) < 90 && Scoreboard.getXP() > 20_000) {
					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							System.out.println("starting!!");
							MinecraftClient.getInstance().executeSync(() -> MinecraftClient.getInstance().player.sendCommand("enchant"));
							doingRerolls[0] = true;
						}
					}, 250);
				}
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
			if (minecraftClient.currentScreen instanceof GenericContainerScreen gcs) {
				switch (gcs.getTitle().getString()) {
					case "PENGUIN.GG » Mob Grinding Captcha": {
						clickSlotWithName(gcs, "5");
						break;
					}
					case "PENGUIN.GG » Enchants": {
						if (!doingRerolls[0]) return;
						clickSlotWithName(gcs, "Item Shop");
						break;
					}
					case "PENGUIN.GG » Enchant Item Shop": {
						if (!doingRerolls[0]) return;
						clickSlotWithName(gcs, "Reroll Book Chances");
						break;
					}
					case "PENGUIN.GG » Enchant Reroll": {
						if (!doingRerolls[0]) return;
						clickSlotWithName(gcs, "Reroll");
						doingRerolls[0] = false;
						break;
					}
				}
			}
		});

		// 1ce every 5 minutes
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				MinecraftClient.getInstance().execute(() -> {
					if (MinecraftClient.getInstance().player == null || MinecraftClient.getInstance().player.getScoreboard() == null ||
							MinecraftClient.getInstance().player.getEntityName().equals("U9G")) return;

					if (MinecraftClient.getInstance().world.getPlayers().stream().filter(c -> c.getEntityName().equals("U9G")).count() == 0)
						return;

					float skycoins = Scoreboard.getSkycoins();
					if (skycoins > 1) {
						String cmd = "skycoins pay U9G " + (int) Math.floor(skycoins);
						MinecraftClient.getInstance().player.sendCommand(cmd);
					}
				});
			}
		}, 1_000 * 30, 1_000/*millisec*/ * 60 /*sec*/ * 5/*min*/);
	}
}
