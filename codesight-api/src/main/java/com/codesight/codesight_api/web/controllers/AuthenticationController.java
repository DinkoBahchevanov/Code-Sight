package com.codesight.codesight_api.web.controllers;

import com.codesight.codesight_api.domain.challenge.service.ChallengeService;
import com.codesight.codesight_api.domain.user.service.UserService;
import com.codesight.codesight_api.web.dtos.challenge.ChallengeGetDto;
import com.codesight.codesight_api.web.dtos.user.UserGetDto;
import com.codesight.codesight_api.web.dtos.user.UserPostDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
@Api(value = "Authenticate-Controller")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get list of Challenges")
    @ApiResponse(code = 200, message = "Success|OK")
    @PostMapping(produces = "application/json")
    ResponseEntity<UserGetDto> register(@RequestBody UserPostDto postDto) {
        return ResponseEntity.ok(userService.register(postDto));
    }
}
