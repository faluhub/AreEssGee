package me.quesia.areessgee.config;

import com.google.gson.*;
import me.quesia.areessgee.AreEssGee;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SpeedrunConfig {
    private static File FILE = FabricLoader.getInstance().getConfigDir().resolve(AreEssGee.MOD_NAME + ".json").toFile();

    public static void init() {
        if (!FILE.exists()) {
            try { boolean ignored = FILE.createNewFile(); }
            catch (IOException e) { throw new RuntimeException(e); }
        }
        JsonObject config = get(false);
        if (config.has("file")) {
            File tempFile = new File(config.get("file").getAsString());
            if (tempFile.exists()) {
                FILE = tempFile;
                init();
            }
        }
    }

    protected static JsonObject get() {
        return get(true);
    }

    protected static JsonObject get(boolean initialise) {
        if (initialise) { init(); }
        try {
            FileReader reader = new FileReader(FILE);
            JsonParser parser = new JsonParser();

            Object obj = parser.parse(reader);
            reader.close();

            return JsonNull.INSTANCE.equals(obj) ? new JsonObject() : (JsonObject) obj;
        } catch (IOException ignored) {}

        return new JsonObject();
    }

    protected static void write(JsonObject config) {
        try {
            FileWriter writer = new FileWriter(FILE);

            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(config));
            writer.flush();
            writer.close();
        } catch (IOException ignored) {}
    }
}
