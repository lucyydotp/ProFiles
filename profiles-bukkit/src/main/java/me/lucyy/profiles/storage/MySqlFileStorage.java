/*
 * Copyright (C) 2021 Lucy Poulton https://lucyy.me
 * This file is part of ProFiles.
 *
 * ProFiles is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProFiles is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProFiles.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.lucyy.profiles.storage;

import com.zaxxer.hikari.HikariDataSource;
import me.lucyy.profiles.ProFiles;
import me.lucyy.profiles.config.SqlInfoContainer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.*;
import java.util.*;

public class MySqlFileStorage implements Storage, Listener {

    private final HikariDataSource ds = new HikariDataSource();
    private final ProFiles plugin;
    private final HashMap<UUID, HashMap<String, String>> cache = new HashMap<>();

    public MySqlFileStorage(ProFiles plugin) throws MysqlConnectionException {
        this.plugin = plugin;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            plugin.getLogger().severe("MySQL driver not found! Unable to continue!");
            throw new MysqlConnectionException();
        }
        SqlInfoContainer sqlData = plugin.getConfigHandler().getSqlConnectionData();

        ds.setJdbcUrl("jdbc:mysql://" + sqlData.host + ":" + sqlData.port + "/"
                + sqlData.database + "?useSSL=false");
        ds.setUsername(sqlData.username);
        ds.setPassword(sqlData.password);
        ds.addDataSourceProperty("cachePrepStmts", "true");
        ds.addDataSourceProperty("prepStmtCacheSize", "250");
        ds.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds.addDataSourceProperty("useServerPrepStmts ", "true");

        try (Connection connection = ds.getConnection()) {
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS profiles_fields ( " +
                    "playerUuid VARCHAR(36), field VARCHAR(255), value TEXT," +
                    "PRIMARY KEY (playerUuid, field))");
            plugin.getLogger().info("Connected to MySQL.");
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to MySQL! | " + e.getMessage());
            throw new MysqlConnectionException();
        }

    }

    @EventHandler
    public void onPlayerConnect(PlayerJoinEvent e) {
        runAsync(() -> {
            try (Connection connection = ds.getConnection()) {
                PreparedStatement stmt = connection.prepareStatement(
                        "SELECT field, value FROM profiles_fields WHERE playerUuid=?");
                stmt.setString(1, e.getPlayer().getUniqueId().toString());

                ResultSet set = stmt.executeQuery();
                HashMap<String, String> resultsMap = new HashMap<>();
                while (set.next()) {
                    resultsMap.put(set.getString("field"), set.getString("value"));
                }
                cache.put(e.getPlayer().getUniqueId(), resultsMap);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        cache.remove(e.getPlayer().getUniqueId());
    }

    private void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    private HashMap<String, String> getUserCache(UUID uuid) {
        HashMap<String, String> map = cache.get(uuid);
        if (map != null) return map;

        map = new HashMap<>();
        cache.put(uuid, map);
        return map;
    }

    @Override
    public void setField(UUID uuid, String key, String value) {
        getUserCache(uuid).put(key, value);
        runAsync(() -> {
            try (Connection connection = ds.getConnection()) {
                PreparedStatement stmt = connection.prepareStatement(
                        "INSERT INTO profiles_fields VALUES (?,?,?) ON DUPLICATE KEY UPDATE value=value");
                stmt.setString(1, uuid.toString());
                stmt.setString(2, key);
                stmt.setString(3, value);
                stmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public String getField(UUID uuid, String key) {
        HashMap<String, String> userCache = getUserCache(uuid);
        return userCache.getOrDefault(key, "Unset");
    }

    @Override
    public void clearField(UUID uuid, String key) {
        runAsync(() -> {
            try (Connection connection = ds.getConnection()) {
                PreparedStatement stmt = connection.prepareStatement(
                        "DELETE FROM profiles_fields WHERE playerUuid=? AND field=?");
                stmt.setString(1, uuid.toString());
                stmt.setString(2, key);
                stmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void close() {
        ds.close();
    }
}