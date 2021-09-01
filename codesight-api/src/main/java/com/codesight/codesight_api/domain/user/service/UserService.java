package com.codesight.codesight_api.domain.user.service;

import com.codesight.codesight_api.web.dtos.user.UserGetDto;
import com.codesight.codesight_api.web.dtos.user.UserPostDto;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserGetDto> get(Pageable pageable);
    UserGetDto get(Integer id);
    UserGetDto create(UserPostDto userPostDto);
    void delete(Integer id);
    UserGetDto partialUpdate(Integer id, JsonMergePatch patch);

    UserGetDto register(UserPostDto userPostDto);
}
