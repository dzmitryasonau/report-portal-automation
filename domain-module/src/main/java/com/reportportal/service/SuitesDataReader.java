package com.reportportal.service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reportportal.models.Suites;

public class SuitesDataReader
{
    private static final String FILE_PATH =
            System.getProperty("user.dir") + "/domain-module/src/main/resources/test_data/json/launches_suites.json";
    private static Suites[] suites;

    static
    {
        try
        {
            suites = new ObjectMapper().readValue(Paths.get(FILE_PATH).toFile(), Suites[].class);
        }
        catch (IOException ex)
        {
            suites = new Suites[0];
        }
    }

    public List<String> getSuitesName(int id)
    {
        for (Suites suit : suites)
        {
            if (suit.getId() == id)
            {
                return new ArrayList<>(suit.getSuitesName());
            }
        }
        return new ArrayList<>();
    }
}
