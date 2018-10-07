package clairvoyance.clairvoyance.particles;


import java.util.HashMap;
import java.util.UUID;

public class ParticlesTracker {
    private HashMap<UUID, UUID> activePlayerParticleTaskUUIDs = new HashMap<>();
    private static ParticlesTracker instance = new ParticlesTracker();

    public static ParticlesTracker getInstance() {
        return instance;
    }

    public HashMap<UUID, UUID> getActivePlayerParticleTaskUUIDs() {
        return activePlayerParticleTaskUUIDs;
    }

    public void addActivePlayerParticleTaskUUIDs(UUID activePlayerUUID, UUID activeTaskUUID) {
        activePlayerParticleTaskUUIDs.put(activePlayerUUID, activeTaskUUID);
    }
}
