package io.github.kmaba.vLobbyConnect;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReloadCommand implements SimpleCommand {

    private final ProxyServer server;
    private final Logger logger;
    private final Map<String, List<RegisteredServer>> versionLobbies;

    public ReloadCommand(ProxyServer server, Logger logger, Map<String, List<RegisteredServer>> versionLobbies) {
        this.server = server;
        this.logger = logger;
        this.versionLobbies = versionLobbies;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();

        if (!source.hasPermission("vlobbyconnect.reload")) {
            source.sendMessage(Component.text("You do not have permission to use this command."));
            return;
        }

        try {
            Yaml yaml = new Yaml();
            File configFile = new File("plugins/vLobbyConnect/config.yml");
            if (!configFile.exists()) {
                source.sendMessage(Component.text("Configuration file not found."));
                return;
            }

            Map<String, Object> config = yaml.load(Files.newInputStream(configFile.toPath()));
            Map<String, String> lobbies = (Map<String, String>) config.get("lobbies");
            if (lobbies == null) {
                source.sendMessage(Component.text("Failed to load valid lobby settings from config file."));
                return;
            }

            versionLobbies.clear();
            Pattern pattern = Pattern.compile("^(\\d+\\.\\d+)lobby(\\d+)$");
            for (Map.Entry<String, String> entry : lobbies.entrySet()) {
                Matcher matcher = pattern.matcher(entry.getKey());
                if (matcher.matches()) {
                    String version = matcher.group(1);
                    String lobbyName = entry.getValue();
                    Optional<RegisteredServer> serverOpt = server.getServer(lobbyName);
                    if (serverOpt.isPresent()) {
                        versionLobbies.computeIfAbsent(version, k -> new ArrayList<>()).add(serverOpt.get());
                    } else {
                        logger.warn("Lobby server '{}' not found in Velocity configuration.", lobbyName);
                    }
                } else {
                    logger.warn("Invalid lobby configuration key: {}", entry.getKey());
                }
            }

            source.sendMessage(Component.text("vLobbyConnect configuration reloaded successfully."));
        } catch (IOException e) {
            logger.error("Error loading config.yml", e);
            source.sendMessage(Component.text("An error occurred while reloading the configuration."));
        }
    }
}
