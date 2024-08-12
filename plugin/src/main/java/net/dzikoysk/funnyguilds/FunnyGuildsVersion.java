package net.dzikoysk.funnyguilds;

public final class FunnyGuildsVersion {

    private final String fullVersion;

    public FunnyGuildsVersion(FunnyGuilds funnyGuilds) {
        this.fullVersion = funnyGuilds.getDescription().getVersion();
    }

    public String getFullVersion() {
        return this.fullVersion;
    }
}
