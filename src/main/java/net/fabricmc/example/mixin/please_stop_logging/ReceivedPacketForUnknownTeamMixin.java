package net.fabricmc.example.mixin.please_stop_logging;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPlayNetworkHandler.class)
public class ReceivedPacketForUnknownTeamMixin {
    // stop printing: "Received packet for unknown team §r*el_dormo11: team action: REMOVE, player action: null"
    @WrapWithCondition(method = "onTeam", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;[Ljava/lang/Object;)V", remap = false))
    private boolean printWarnLog(Logger instance, String format, Object... arguments) {
        return false;
    }
}
