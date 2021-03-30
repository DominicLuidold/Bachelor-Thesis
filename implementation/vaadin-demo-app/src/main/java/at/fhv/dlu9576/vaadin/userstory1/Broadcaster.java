package at.fhv.dlu9576.vaadin.userstory1;

import com.vaadin.flow.shared.Registration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Source code inspired by https://vaadin.com/docs/latest/flow/advanced/server-push/#push.broadcaster
 *
 * @see <a href="https://vaadin.com/docs/latest/flow/advanced/server-push/#push.broadcaster">
 * Server Push | Advanced Topics | Flow | Vaadin Docs
 * </a>
 */
public class Broadcaster {
    static Executor executor = Executors.newSingleThreadExecutor();
    static Map<UUID, Consumer<Void>> listeners = new HashMap<>();

    /**
     * Allows registering a {@link Consumer<Void>} which listens to broadcasts.
     *
     * @param source   The source of the listener
     * @param listener The listener accepting broadcasts
     *
     * @return Registration allowing to remove the registration
     */
    public static synchronized Registration register(UUID source, Consumer<Void> listener) {
        listeners.put(source, listener);

        return () -> {
            synchronized (Broadcaster.class) {
                listeners.remove(source);
            }
        };
    }

    /**
     * Broadcasts to all {@link Broadcaster#listeners} except for the sender itself.
     *
     * @param sender The sender which should not receive the message
     */
    public static synchronized void broadcast(UUID sender) {
        listeners.forEach((uuid, listener) -> {
            if (!uuid.equals(sender)) {
                executor.execute(() -> listener.accept(null));
            }
        });
    }
}
