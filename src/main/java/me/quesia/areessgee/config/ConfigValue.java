package me.quesia.areessgee.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.quesia.areessgee.AreEssGee;
import org.apache.commons.lang3.ClassUtils;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigValue<T> {
    private final String key;
    private final T def;
    private T value = null;

    @SuppressWarnings("unchecked")
    public ConfigValue(String key, T def) {
        this.key = key;
        this.def = def;
        JsonElement element = this.getElement();

        Class<?> type = this.def.getClass();
        if (!(def instanceof List)) {
            Method elementMethod = this.getElementMethod(type, element);
            if (elementMethod != null) {
                T temp = null;
                if (element == null || element.isJsonNull()) {
                    JsonObject config = SpeedrunConfig.get();
                    Method propertyMethod = this.getPropertyMethod(type, config);
                    if (propertyMethod != null) {
                        try {
                            propertyMethod.invoke(config, this.key, this.def);
                            SpeedrunConfig.write(config);
                            temp = this.def;
                        } catch (Exception e) { AreEssGee.LOGGER.error("Occurrence 1", e); }
                    }
                }
                if (temp == null) {
                    try { temp = (T) elementMethod.invoke(element); }
                    catch (Exception e) { AreEssGee.LOGGER.error("Occurrence 2", e); }
                }
                if (temp != null) { this.value = temp; }
            }
        } else {
            if (element == null || element.isJsonNull()) {
                JsonObject config = SpeedrunConfig.get();
                JsonArray array = new JsonArray();
                for (Method method : JsonArray.class.getDeclaredMethods()) {
                    if (method.getName().equalsIgnoreCase("add")) {
                        if (method.getParameterCount() > 0 && method.getParameterTypes()[0].isInstance(type)) {
                            for (T value : (ArrayList<T>) this.def) {
                                try { method.invoke(array, value); }
                                catch (Exception e) { AreEssGee.LOGGER.error("Occurrence 3", e); }
                            }
                            config.add(this.key, array);
                            SpeedrunConfig.write(config);
                            this.value = this.def;
                            return;
                        }
                    }
                }
                this.value = null;
                return;
            }
            try {
                List<?> values = (List<?>) this.def;
                if (!values.isEmpty()) {
                    type = values.get(0).getClass();
                    ArrayList<Object> list = new ArrayList<>();
                    for (JsonElement element1 : element.getAsJsonArray()) {
                        Method elementMethod = this.getElementMethod(type, element1);
                        if (elementMethod != null) {
                            try { list.add(elementMethod.invoke(element1)); }
                            catch (Exception e) { AreEssGee.LOGGER.error("Occurrence 5", e); }
                        }
                    }
                    this.value = (T) list;
                } else {
                    this.value = (T) new ArrayList<>();
                }
            } catch (Exception e) { AreEssGee.LOGGER.error("Occurrence 4", e); }
        }
    }

    private Class<?> getPrimitiveType(Class<?> type) {
        try {
            if (ClassUtils.getAllInterfaces(type).contains(Serializable.class)) {
                Field field = type.getDeclaredField("value");
                return field.getType();
            }
        } catch (NoSuchFieldException ignored) {
        } catch (Exception e) { AreEssGee.LOGGER.error("While getting primitive type (" + this.key + ")", e); }
        return null;
    }

    private Method getElementMethod(Class<?> type, JsonElement element) {
        for (Method method : JsonElement.class.getDeclaredMethods()) {
            if (method.getName().startsWith("getAs") && method.canAccess(element) && method.getParameterCount() == 0) {
                if (method.getReturnType().equals(type) || method.getReturnType().equals(this.getPrimitiveType(type))) {
                    return method;
                }
            }
        }
        return null;
    }

    private Method getPropertyMethod(Class<?> type, JsonObject object) {
        for (Method method : JsonObject.class.getDeclaredMethods()) {
            if (method.getName().equalsIgnoreCase("addProperty")) {
                if (method.canAccess(object) && method.getParameterCount() >= 2) {
                    if (method.getParameterTypes()[1].isInstance(type)) {
                        return method;
                    }
                }
            }
        }
        return null;
    }

    public T getValue() {
        return this.value;
    }

    public T getDefault() {
        return this.def;
    }

    public boolean hasChanged() {
        return !this.def.equals(this.value);
    }

    private JsonElement getElement() {
        JsonObject config = SpeedrunConfig.get();
        return config.get(this.key);
    }
}
