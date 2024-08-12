package net.dzikoysk.funnyguilds;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.dzikoysk.funnyguilds.shared.bukkit.NmsUtils;
import org.bukkit.Bukkit;
import panda.std.stream.PandaStream;

public class FunnyGuildsLogger {

    protected final Logger rootLogger;

    public FunnyGuildsLogger(Logger rootLogger) {
        this.rootLogger = rootLogger;
    }

    public void debug(String content) {
        this.info("[Debug] > " + content);
    }

    public void update(String content) {
        this.info("[Updater] > " + content);
    }

    public void deserialize(String content) {
        this.error("[Deserialize] > " + content);
    }

    public void parser(String content) {
        this.warning("[Parser] > " + content);
    }

    public void info(String content) {
        this.rootLogger.info(content);
    }

    public void warning(String content) {
        this.rootLogger.warning(content);
    }

    public void error(String content) {
        this.rootLogger.severe(content);
    }

    public void error(String content, Throwable cause) {
        this.error("");
        this.error(content);
        this.error("");

        StringWriter errorDump = new StringWriter();
        cause.printStackTrace(new PrintWriter(errorDump));

        PandaStream.of(errorDump.toString().split("\n")).forEach(this::error);
    }

    static class DefaultLogger extends FunnyGuildsLogger {

        private final FunnyGuilds plugin;

        DefaultLogger(FunnyGuilds plugin) {
            super(plugin.getLogger());
            this.plugin = plugin;
        }

        @Override
        public void debug(String content) {
            if (!this.plugin.getPluginConfiguration().debugMode) {
                return;
            }

            super.debug(content);
        }

        @Override
        public void error(String content, Throwable cause) {
            String loadedPlugins = Arrays.stream(Bukkit.getPluginManager().getPlugins())
                    .filter(plugin -> !plugin.getName().contains("FunnyGuilds"))
                    .map(plugin -> plugin.getName() + " " + plugin.getDescription().getVersion())
                    .collect(Collectors.joining(", "));

            if (loadedPlugins.isEmpty()) {
                loadedPlugins = "none";
            }

            super.error(content, cause);

            this.error("");
            this.error("Server Information:");
            this.error("  FunnyGuilds: " + this.plugin.getVersion().getFullVersion());
            this.error("  Bukkit: " + Bukkit.getBukkitVersion());
            this.error("  Java: " + System.getProperty("java.version"));
            this.error("  Thread: " + Thread.currentThread());
            this.error("  Loaded plugins: " + loadedPlugins);
            this.error("  Reload count: " + NmsUtils.getReloadCount());
            this.error("");
        }

    }

}