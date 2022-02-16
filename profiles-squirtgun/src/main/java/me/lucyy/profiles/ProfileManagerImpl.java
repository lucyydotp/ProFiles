package me.lucyy.profiles;

import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.ProfileFieldParameter;
import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.profiles.field.SimpleProfileField;
import me.lucyy.profiles.storage.Storage;
import org.apache.commons.lang.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Pattern;

public class ProfileManagerImpl implements ProfileManager {
    private final ProFilesPlatform platform;
    private final Pattern keyValidPattern = Pattern.compile("^[A-Za-z0-9\\-_]+$");
    private final Map<String, Class<? extends ProfileField>> typeMap = new HashMap<>();
    private final Map<String, ProfileField> fieldMap = new HashMap<>();

    public ProfileManagerImpl(ProFilesPlatform platform) {
        this.platform = platform;
    }

    public Storage getStorage() {
        return platform.getStorage();
    }

    private void assertTrue(boolean condition, String message) throws InvalidConfigurationException {
        if (!condition) throw new InvalidConfigurationException(message);
    }

    private void populateFields(ProfileField field, Map<String, Object> parameters) throws InvalidConfigurationException {
        List<Field> fields = new ArrayList<>();
        Class<?> clazz = field.getClass();

        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        for (Field parameter : fields) {
            ProfileFieldParameter annotation = parameter.getAnnotation(ProfileFieldParameter.class);
            if (annotation == null) continue;

            Object value = parameters.get(parameter.getName());
            if (value == null) {
                if (annotation.required()) {
                    throw new InvalidConfigurationException(
                            MessageFormat.format("Required parameter {0} is missing", parameter)
                    );
                }
                continue;
            }

            Class<?> expectedType = parameter.getType();

            if (!ClassUtils.isAssignable(value.getClass(), expectedType, true)) {
                throw new InvalidConfigurationException(
                        MessageFormat.format("Parameter ''{0}'' is of type {1} but was expecting {2}",
                                parameter.getName(), value.getClass().getName(), expectedType.getName())
                );
            }

            parameter.setAccessible(true);
            try {
                parameter.set(field, value);
            } catch (IllegalAccessException e) {
                // this is a bug if thrown - we make it accessible above
                e.printStackTrace();
            }
        }
    }

    // this should only be called after server init (scheduler)
    public void loadFields() throws InvalidConfigurationException {
        Map<String, Map<String, Object>> fieldsSection = platform.getConfig().fields();
        assertTrue(fieldsSection != null, "Fields config section not found");

        fieldMap.clear();

        for (Map.Entry<String, Map<String, Object>> entry : fieldsSection.entrySet()) {
            String key = entry.getKey();
            assertTrue(!key.equals("subtitle"),
                    "The key 'subtitle' is reserved for internal use and cannot be used as a field name");

            assertTrue(!fieldMap.containsKey(key), "Key '" + key + "' is a duplicate");

            Map<String, Object> params = entry.getValue();

            String type = params.get("type").toString();
            Class<? extends ProfileField> typeClass = typeMap.get(type);

            if (typeClass == null) {
                platform.getLogger().warning("Field type '" + type + "' is unknown, the field '" + key
                        + "' will be ignored");
                continue;
            }

            try {
                ProfileField field = typeClass.getConstructor(String.class).newInstance(key);
                populateFields(field, params);
                fieldMap.put(key, field);
            } catch (InvalidConfigurationException e) {
                platform.getLogger().warning("While loading field '" + key + "': " + e.getMessage());
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }

            if (platform.getConfig().subtitleEnabled()) {
                ProfileField subtitle = new SimpleProfileField("subtitle");
                populateFields(subtitle, Map.of(
                        "displayName", "Subtitle",
                        "order", -1,
                        "allowColour", false
                ));
                fieldMap.put("subtitle", subtitle);
            }
        }
    }

    @Override
    public Collection<ProfileField> getFields() {
        return fieldMap.values();
    }

    @Override
    public ProfileField getField(String key) {
        return fieldMap.get(key);
    }

    @Override
    public void register(String key, Class<? extends ProfileField> factory) {
        if (!keyValidPattern.matcher(key).find()) throw new IllegalArgumentException("Key contains invalid characters");
        if (typeMap.containsKey(key)) throw new IllegalStateException("Key is already registered");
        platform.getLogger().info("Registered handler for '" + key + "'");
        typeMap.put(key, factory);
    }
}
