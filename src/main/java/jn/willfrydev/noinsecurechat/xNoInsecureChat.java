/*
 * xPlugins x xNoInsecureChat - 1.0.0]
 * Copyright (C) 2025 xPlugins x WillfryDev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package jn.willfrydev.noinsecurechat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class xNoInsecureChat extends JavaPlugin implements CommandExecutor {
    private static xNoInsecureChat instance;
    public static xNoInsecureChat getInstance() {
        return instance;
    }

    private PacketAdapter packetAdapter;
    private boolean pluginEnabled = true;

    @Override
    public void onEnable() {

        instance = this;
        getLogger().info(ChatColor.BLUE + "             __       _____                                      ___ _           _");
        getLogger().info(ChatColor.BLUE + "  __  __ /\\ \\ \\___   \\_   \\_ __  ___  ___  ___ _   _ _ __ ___  / __\\ |__   __ _| |_");
        getLogger().info(ChatColor.BLUE + "  \\ \\/ //  \\/ / _ \\   / /\\/ '_ \\/ __|/ _ \\/ __| | | | '__/ _ \\/ /  | '_ \\ / _` | __|");
        getLogger().info(ChatColor.BLUE + "   >  < / /\\  / (_) /\\/ /_ | | | \\__ \\  __/ (__| |_| | | |  __/ /___| | | | (_| | |_");
        getLogger().info(ChatColor.BLUE + "  /_/\\_\\_\\ \\/ \\___/\\____/ |_| |_|___/\\___|\\___|\\__,_|_|  \\___\\____/|_| |_|\\__,_|\\__|");
        getLogger().info(ChatColor.BLUE + " ");
        getLogger().info(ChatColor.BLUE + "        xNoInsecureChat by xPlugins ha sido activado.");
        getLogger().info(ChatColor.BLUE + " ");
        saveDefaultConfig();
        loadConfigValues();
        if (!setupProtocolLib()) {
            return;
        }

        getCommand("noinsecurechat").setExecutor(this);
        getLogger().info("¡Enganchado a ProtocolLib y comando registrado!");
    }

    private boolean setupProtocolLib() {
        if (getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
            getLogger().severe("¡ERROR! ProtocolLib no se encontró.");
            getLogger().severe("Este plugin lo necesita para funcionar.");
            getLogger().severe("Descárgalo desde: https://www.spigotmc.org/resources/protocollib.1997/");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        }

        registerPacketListener();
        return true;
    }

    private void loadConfigValues() {
        reloadConfig();
        this.pluginEnabled = getConfig().getBoolean("enabled", true);

        if (this.pluginEnabled) {
            getLogger().info("Función de bloqueo de chat activada.");
        } else {
            getLogger().info("Función de bloqueo de chat desactivada desde la config.");
        }
    }

    private void registerPacketListener() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        if (this.packetAdapter != null) {
            protocolManager.removePacketListener(this.packetAdapter);
        }

        this.packetAdapter = new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.SYSTEM_CHAT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (!pluginEnabled) {
                    return;
                }

                try {
                    WrappedChatComponent chatComponent = event.getPacket().getChatComponents().read(0);
                    String json = chatComponent.getJson();

                    if (json.contains("chat.disabled.secure") || json.contains("chat.disabled.missingProfileKey")) {
                        event.setCancelled(true);
                    }
                } catch (Exception e) {
                    getLogger().warning("No se pudo leer un paquete de SYSTEM_CHAT: " + e.getMessage());
                }
            }
        };
        protocolManager.addPacketListener(this.packetAdapter);
    }

    @Override
    public void onDisable() {
        getLogger().info("xNoInsecureChat desactivado. ¡Adiós!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("noinsecurechat.reload")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo tienes permiso para usar este comando."));
                return true;
            }
            loadConfigValues();
            registerPacketListener();
            String reloaderName = (sender instanceof Player) ? sender.getName() : "Consola";
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a¡xNoInsecureChat ha sido recargado por " + reloaderName + "!"));
            getLogger().info("Configuración recargada por " + reloaderName + ".");
            return true;
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUsa &a/" + label + " reload &epara recargar la configuración."));
        return true;
    }
    /**
     *     Check if the unsafe chat message filter is enabled.
     *         @return true if enabled, false if not.
     */
    public boolean isFilterEnabled() {
        return this.pluginEnabled;
    }
    /**
     *        Turn the unsafe chat message filter on or off.
     *  @param enabled true to enable the filter, false to disable it.
     */
    public void setFilterEnabled(boolean enabled) {
        this.pluginEnabled = enabled;
        getConfig().set("enabled", enabled);
        saveConfig();
        String status = enabled ? "activado" : "desactivado";
        getLogger().info("El filtro de chat ha sido " + status + " via API.");
    }
}