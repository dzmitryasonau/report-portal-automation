package com.reportportal.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Predicate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reportportal.exceptions.AutomationException;
import com.reportportal.models.User;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class UserDataService
{
    private static final String MODULE_NAME = "/core-module/src/main/resources/test_data/";
    private static final Integer WAIT_AVAILABLE_USER = 1000;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private BlockingDeque<User> users;

    @PostConstruct
    private void init()
    {
        String pathToFile = (System.getProperty("user.dir").contains("tests-module") ? System.getProperty("user.dir")
                .substring(0, System.getProperty("user.dir").lastIndexOf("\\")) : System.getProperty("user.dir"))
                + MODULE_NAME + "users.json";
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
        return getUserByCondition(user -> true);
    }

    public User getUserByLogin(String userLogin)
    {
        return getUserByCondition(user -> user.getLogin().equals(userLogin));
    }

    public synchronized void releaseUser(User user)
    {
        if (users.contains(user))
        {
            return;
        }
        users.addLast(user);
    }

    private User getUserByCondition(Predicate<User> condition)
    {
        User user = null;
        List<User> tempList = new ArrayList<>();

        try
        {
            while (user == null)
            {
                synchronized (this.users)
                {
                    User tempUser;
                    while ((tempUser = users.poll()) != null)
                    {
                        if (condition.test(tempUser))
                        {
                            user = tempUser;
                        }
                        tempList.add(tempUser);

                        if (user != null)
                        {
                            break;
                        }
                    }
                    users.addAll(tempList);
                    tempList.clear();
                }

                if (user == null)
                {
                    try
                    {
                        Thread.sleep(WAIT_AVAILABLE_USER);
                    }
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                        throw new AutomationException("Interrupted while waiting for a user to be free", e);
                    }
                }
            }
        }
        catch (Exception e)
        {
            Thread.currentThread().interrupt();
            throw new AutomationException("Error while getting user", e);
        }

        return user;
    }
}
