package net.fabricmc.example;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.profiler.Profiler;

public class ClientMessageEvents {
    public static final Event<MessageReceived> MESSAGE_RECEIVED = EventFactory.createArrayBacked(ClientMessageEvents.MessageReceived.class, callbacks -> (client, message) -> {
        if (EventFactory.isProfilingEnabled()) {
            final Profiler profiler = client.getProfiler();
            profiler.push("fabricEndMessageReceived");

            for (MessageReceived event : callbacks) {
                profiler.push(EventFactory.getHandlerName(event));
                event.onMessageReceived(client, message);
                profiler.pop();
            }

            profiler.pop();
        } else {
            for (MessageReceived event : callbacks) {
                event.onMessageReceived(client, message);
            }
        }
    });

    @FunctionalInterface
    public interface MessageReceived {
        void onMessageReceived(MinecraftClient client, Text message);
    }
}
