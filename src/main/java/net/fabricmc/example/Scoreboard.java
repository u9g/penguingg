package net.fabricmc.example;

import net.fabricmc.example.mixin.ScoreboardMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.Team;

public class Scoreboard {
    public static float getSkycoins() {
        for (var val : ((ScoreboardMixin) MinecraftClient.getInstance().player.getScoreboard()).playerObjectives().values()) {
            var iter = val.values().iterator();
            if (!iter.hasNext()) continue; // why did we get here?
            var x = iter.next();
            if (x == null) continue; // why did we get here?

            Team team = x.getScoreboard().getPlayerTeam(x.getPlayerName());
            if (team != null) {
                String sidebarPrefix = team.getPrefix().getString();
                if (sidebarPrefix != null && sidebarPrefix.startsWith(" • Skycoins ")) {
                    // " • Skycoins 21,560.01"
                    String[] parts = sidebarPrefix.split("Skycoins ");
                    if (parts.length == 2) {
                        try {
                            return Float.parseFloat(parts[1].replace(",", ""));
                        } catch (Exception e) {}
                    }
                }
            }
        }
        return -1;
    }

    public static int getXP() {
        for (var val : ((ScoreboardMixin) MinecraftClient.getInstance().player.getScoreboard()).playerObjectives().values()) {
            var iter = val.values().iterator();
            if (!iter.hasNext()) continue; // why did we get here?
            var x = iter.next();
            if (x == null) continue; // why did we get here?

            Team team = x.getScoreboard().getPlayerTeam(x.getPlayerName());
            if (team != null) {
                String sidebarPrefix = team.getPrefix().getString();
                if (sidebarPrefix != null && sidebarPrefix.startsWith(" • XP ")) {
                    String[] parts = sidebarPrefix.split("XP ");
                    if (parts.length == 2) {
                        try {
                            return Integer.parseInt(parts[1].replace(",", ""));
                        } catch (Exception e) {}
                    }
                }
            }
        }
        return -1;
    }
}
