package com.xiaoyu.config;

import net.fabricmc.loader.api.FabricLoader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * 模组配置文件管理类
 */
public class ModConfig {
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir()
            .resolve("everything_totem.properties").toFile();
    private static final Properties CONFIG = new Properties();

    public static class General {
        public static boolean consumeItems = true;
        public static boolean checkMainHandFirst = true;
        public static boolean checkOffHandFirst = true;
        public static boolean checkInventory = true;
    }

    public static final General GENERAL = new General();

    public static void register() {
        loadConfig();
        Runtime.getRuntime().addShutdownHook(new Thread(ModConfig::saveConfig));
    }

    private static void loadConfig() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                CONFIG.load(reader);
                General.consumeItems = Boolean.parseBoolean(CONFIG.getProperty("consumeItems", "true"));
                General.checkMainHandFirst = Boolean.parseBoolean(CONFIG.getProperty("checkMainHandFirst", "true"));
                General.checkOffHandFirst = Boolean.parseBoolean(CONFIG.getProperty("checkOffHandFirst", "true"));
                General.checkInventory = Boolean.parseBoolean(CONFIG.getProperty("checkInventory", "true"));
            } catch (IOException e) {}
        } else {
            saveConfig();
        }
    }

    private static void saveConfig() {
        try {
            if (!CONFIG_FILE.exists()) {
                CONFIG_FILE.getParentFile().mkdirs();
                CONFIG_FILE.createNewFile();
            }

            CONFIG.setProperty("consumeItems", String.valueOf(General.consumeItems));
            CONFIG.setProperty("checkMainHandFirst", String.valueOf(General.checkMainHandFirst));
            CONFIG.setProperty("checkOffHandFirst", String.valueOf(General.checkOffHandFirst));
            CONFIG.setProperty("checkInventory", String.valueOf(General.checkInventory));

            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                CONFIG.store(writer, "万物皆可图腾配置文件");
            }
        } catch (IOException e) {}
    }
}