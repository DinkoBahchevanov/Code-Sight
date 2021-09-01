package com.codesight.codesight_api.web.mappers;

import com.codesight.codesight_api.domain.user.entity.User;
import com.codesight.codesight_api.web.dtos.user.UserGetDto;
import com.codesight.codesight_api.web.dtos.user.UserPostDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserGetDto userToUserGetDto(User user);
    List<UserGetDto> usersToUserGetDtos(List<User> users);
    User userPostDtoToUser(UserPostDto userPostDto);
}
