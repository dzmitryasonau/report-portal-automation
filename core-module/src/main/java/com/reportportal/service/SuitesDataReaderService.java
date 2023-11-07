package com.reportportal.service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reportportal.models.Suites;

public class SuitesDataReaderService
{
    private static final String FILE_PATH =
            System.getProperty("user.dir") + "/core-module/src/main/resources/test_data/json/launches_suites.json";
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
        return Arrays.stream(suites).filter(suit -> suit.getId() == id).map(Suites::getSuitesName).findFirst()
                .orElse(Collections.emptyList());
    }
}
