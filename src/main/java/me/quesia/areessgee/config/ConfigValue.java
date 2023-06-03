package me.quesia.areessgee.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.quesia.areessgee.config.list.StringList;

public class ConfigValue<T> {
    private final String key;
    private final T def;
    private final JsonElement element;
    private final T value;

    @SuppressWarnings("unchecked")
    public ConfigValue(String key, T def) {
        this.key = key;
        this.def = def;
        this.element = this.getElement();
        Class<T> type = (Class<T>) this.def.getClass();
        if (type.equals(Integer.class)) { this.value = (T) this.getIntValue(); }
        else if (type.equals(Float.class)) { this.value = (T) this.getFloatValue(); }
        else if (type.equals(String.class)) { this.value = (T) this.getStringValue(); }
        else if (type.equals(Boolean.class)) { this.value = (T) this.getBooleanValue(); }
        else if (type.equals(StringList.class)) { this.value = (T) this.getStringListValue(); }
        else { this.value = null; }
    }

    public T getValue() {
        return this.value;
    }

    private JsonElement getElement() {
        JsonObject config = SpeedrunConfig.get();
        return config.get(this.key);
    }

    private Integer getIntValue() {
        if (this.element == null || this.element.isJsonNull()) {
            JsonObject config = SpeedrunConfig.get();
            config.addProperty(this.key, (Integer) this.def);
            SpeedrunConfig.write(config);
            return (Integer) this.def;
        }
        return this.element.getAsInt();
    }

    private Float getFloatValue() {
        if (this.element == null || this.element.isJsonNull()) {
            JsonObject config = SpeedrunConfig.get();
            config.addProperty(this.key, (Float) this.def);
            SpeedrunConfig.write(config);
            return (Float) this.def;
        }
        return this.element.getAsFloat();
    }

    private String getStringValue() {
        if (this.element == null || this.element.isJsonNull()) {
            JsonObject config = SpeedrunConfig.get();
            config.addProperty(this.key, (String) this.def);
            SpeedrunConfig.write(config);
            return (String) this.def;
        }
        return this.element.getAsString();
    }

    private Boolean getBooleanValue() {
        if (this.element == null || this.element.isJsonNull()) {
            JsonObject config = SpeedrunConfig.get();
            config.addProperty(this.key, (Boolean) this.def);
            SpeedrunConfig.write(config);
            return (Boolean) this.def;
        }
        return this.element.getAsBoolean();
    }

    private StringList getStringListValue() {
        if (this.element == null || this.element.isJsonNull()) {
            JsonObject config = SpeedrunConfig.get();
            JsonArray array = new JsonArray();
            for (String def : (StringList) this.def) {
                array.add(def);
            }
            config.add(this.key, array);
            SpeedrunConfig.write(config);
            return (StringList) this.def;
        }
        StringList list = new StringList();
        for (JsonElement element1 : this.element.getAsJsonArray()) {
            list.add(element1.getAsString());
        }
        return list;
    }
}
