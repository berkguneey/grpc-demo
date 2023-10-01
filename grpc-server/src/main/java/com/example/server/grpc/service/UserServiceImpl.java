package com.example.server.grpc.service;


import com.example.server.dto.UserDto;
import com.example.server.grpc.*;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@GrpcService
public class UserServiceImpl extends UserGrpc.UserImplBase {

    private static List<UserDto> userList = new ArrayList<>();

    static {
        userList.add(new UserDto(1L, "Alice", "Doe", 27));
        userList.add(new UserDto(2L, "Bob", "Smith", 30));
        userList.add(new UserDto(3L, "John", "Haynes", 32));
    }

    public static UserObj mapToUserObj(UserDto userDto) {
        return UserObj.newBuilder()
                .setId(userDto.getId())
                .setName(userDto.getName())
                .setSurname(userDto.getSurname())
                .setAge(userDto.getAge())
                .build();
    }

    public void getUsers(Empty request, StreamObserver<UsersResponse> responseObserver) {
        List<UserObj> userObjs = userList.stream()
                .map(UserServiceImpl::mapToUserObj).collect(Collectors.toList());
        UsersResponse response = UsersResponse.newBuilder().addAllUsers(userObjs).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void getUser(GetUserRequest request, StreamObserver<UserResponse> responseObserver) {
        Optional<UserDto> userDtoOpt = userList.stream().filter(user -> user.getId().equals(request.getId())).findFirst();
        if (userDtoOpt.isPresent()) {
            UserResponse response = UserResponse.newBuilder().setUser(mapToUserObj(userDtoOpt.get())).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Kullanıcı bulunamadı.").asException());
        }
    }

}