package com.reportportal.services;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reportportal.exceptions.AutomationException;
import com.reportportal.models.User;

import org.springframework.stereotype.Service;

@Service
public class UserDataService
{
    private static final String MODULE_NAME = "/domain-module/src/main/resources/test_data/";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private BlockingDeque<User> users;

    @PostConstruct
    private void init()
    {
        String pathToFile = System.getProperty("user.dir") + MODULE_NAME + "users.json";
        try (FileReader file = new FileReader(pathToFile))
        {
            this.users = new LinkedBlockingDeque<>(Arrays.asList(objectMapper.readValue(file, User[].class)));
        }
        catch (IOException e)
        {
            throw new AutomationException("Error while loading users data", e);
        }
    }

    public User getUser()
    {
        try
        {
            return users.take();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            throw new AutomationException("Error while getting user", e);
        }
    }

    public void releaseUser(User user)
    {
        users.addLast(user);
    }
}
