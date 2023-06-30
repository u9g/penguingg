package net.fabricmc.example.mixin;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(Scoreboard.class)
public interface ScoreboardMixin {
    @Accessor("playerObjectives")
    Map<String, Map<ScoreboardObjective, ScoreboardPlayerScore>> playerObjectives();
}
