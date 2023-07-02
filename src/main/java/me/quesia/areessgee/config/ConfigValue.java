package me.quesia.areessgee.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.quesia.areessgee.AreEssGee;
import org.apache.commons.lang3.ClassUtils;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.ArrayList;
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
            T temp = null;
            if (element == null || element.isJsonNull()) {
                JsonObject config = SpeedrunConfig.get();
                Method addMethod = this.getAddMethod(type, config);
                if (addMethod != null) {
                    try {
                        addMethod.invoke(config, this.key, this.def);
                        SpeedrunConfig.write(config);
                        temp = this.def;
                    } catch (Exception e) { AreEssGee.LOGGER.error("Occurrence 1", e); }
                }
            }
            if (temp == null && element != null) {
                Method elementMethod = this.getElementMethod(type, element);
                if (elementMethod != null) {
                    try { temp = (T) elementMethod.invoke(element); }
                    catch (Exception e) { AreEssGee.LOGGER.error("Occurrence 2", e); }
                }
            }
            if (temp != null) { this.value = temp; }
        } else {
            if (element == null || element.isJsonNull()) {
                JsonObject config = SpeedrunConfig.get();
                JsonArray array = new JsonArray();
                List<?> list = (List<?>) this.def;
                if (!list.isEmpty()) {
                    type = list.get(0).getClass();
                    for (Method method : JsonArray.class.getDeclaredMethods()) {
                        if (method.getName().equalsIgnoreCase("add")) {
                            if (method.getParameterCount() > 0 && method.getParameterTypes()[0].isAssignableFrom(type)) {
                                for (Object value : list) {
                                    try { method.invoke(array, value); }
                                    catch (Exception e) { AreEssGee.LOGGER.error("Occurrence 3", e); }
                                }
                                break;
                            }
                        }
                    }
                }
                config.add(this.key, array);
                SpeedrunConfig.write(config);
                this.value = this.def;
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

    private Method getAddMethod(Class<?> type, JsonObject object) {
        for (Method method : JsonObject.class.getDeclaredMethods()) {
            if (method.getName().equalsIgnoreCase("addProperty")) {
                if (method.canAccess(object) && method.getParameterCount() >= 2) {
                    if (method.getParameterTypes()[1].isAssignableFrom(type)) {
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
        return !this.getDefault().equals(this.value);
    }

    private JsonElement getElement() {
        JsonObject config = SpeedrunConfig.get();
        return config.get(this.key);
    }
}
