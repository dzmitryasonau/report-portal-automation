package com.reportportal.ui.components;

import java.util.HashMap;
import java.util.Map;

public final class GlobalVariablesService
{
    private static final ThreadLocal<GlobalVariablesService> instance = new ThreadLocal<>();

    public Map<String, Object> variables;

    private GlobalVariablesService()
    {
        variables = new HashMap<>();
    }

    public static synchronized GlobalVariablesService getInstance()
    {
        if (instance.get() == null)
        {
            instance.set(new GlobalVariablesService());
        }
        return instance.get();
    }

    public void addVariable(String key, Object value)
    {
        variables.put(key, value);
    }

    public Object getVariable(String key)
    {
        return variables.get(key);
    }

    public void removeVariable(String key)
    {
        variables.remove(key);
    }
}
