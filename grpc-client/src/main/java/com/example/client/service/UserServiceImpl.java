package com.example.client.service;

import com.example.client.dto.UserDto;
import com.example.server.grpc.*;
import com.google.protobuf.Empty;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @GrpcClient("user-service")
    UserGrpc.UserBlockingStub userBlockingStub;

    public static UserDto mapToUserDto(UserObj userObj) {
        return new UserDto(
                userObj.getId(),
                userObj.getName(),
                userObj.getSurname(),
                userObj.getAge()
        );
    }

    @Override
    public List<UserDto> getUsers() {
        UsersResponse response = userBlockingStub.getUsers(Empty.newBuilder().build());
        return response.getUsersList().stream()
                .map(UserServiceImpl::mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Long id) {
        GetUserRequest request = GetUserRequest.newBuilder().setId(id).build();
        UserResponse response = userBlockingStub.getUser(request);
        return mapToUserDto(response.getUser());
    }

}
