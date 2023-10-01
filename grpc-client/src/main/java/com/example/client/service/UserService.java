package com.example.client.service;

import com.example.client.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();

    UserDto getUser(Long id);
}
