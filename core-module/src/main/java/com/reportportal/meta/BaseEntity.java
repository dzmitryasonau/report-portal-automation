package com.reportportal.meta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reportportal.exceptions.TestExecutionException;
import com.reportportal.utils.ObjectFormatUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class BaseEntity {

    @Override
    public String toString() {
        return ObjectFormatUtils.wrapToJsonTreeIfPossible(this);
    }

    @JsonIgnore
    public Set<String> getFields() {
        Set<String> result = new HashSet<>();
        Class clazz = this.getClass();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                result.add(field.getName());
            }
            clazz = clazz.getSuperclass();
        }
        return result;
    }

    public Object getValue(final String elementName) {
        Object value = getValueFromElement(elementName);
        return value == null ? "null" : value;
    }

    public <T extends BaseEntity> T getDto(final String elementName) {
        return getValueFromElement(elementName);
    }

    public Object invoke(final String methodName) {
        return invoke(methodName, new Object[0]);
    }

    public Object invoke(final String methodName, Object... args) {
        Method method = ReflectionUtils.findMethod(this.getClass(), methodName);
        if (Objects.isNull(method)) {
            throw new TestExecutionException("Method '%s' not found in the class '%s'", methodName, this.getClass());
        }
        return ReflectionUtils.invokeMethod(method, this, args);
    }

    /**
     * Get value from element by element name and parametrized Class.
     *
     * @param elementName name of field.
     * @return value from field, throws {@link RuntimeException} if field not found in this class or superclass.
     */
    public <T> T getValueFromElement(final String elementName) {
        Class<?> clazz = this.getClass();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Field requestedField = clazz.getDeclaredField(elementName);
                    return getEntityFromField(requestedField);
                } catch (final NoSuchFieldException e) {
                    //just ignore, field can be in superclass
                }
            }
            clazz = clazz.getSuperclass();
        }
        throw new TestExecutionException(String.format("Field '%s' wasn't found in clazz '%s'", elementName, this.getClass().getName()));

    }

    @SuppressWarnings("unchecked")
    private <T> T getEntityFromField(final Field requestedField) {
        requestedField.setAccessible(true);
        try {
            return (T) requestedField.get(this);
        } catch (IllegalAccessException e) {
            throw new TestExecutionException("Can't get entity from field '%s'", requestedField.getName());
        }
    }

}
