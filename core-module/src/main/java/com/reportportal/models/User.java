package com.reportportal.models;

import com.reportportal.meta.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity
{
    private String login;
    private String password;
    private String apiKey;

    public User(String login, String password)
    {
        this.login = login;
        this.password = password;
    }
}
