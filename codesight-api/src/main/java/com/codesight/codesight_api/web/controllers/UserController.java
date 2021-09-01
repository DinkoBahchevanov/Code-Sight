package com.codesight.codesight_api.web.controllers;

import com.codesight.codesight_api.domain.user.service.UserService;
import com.codesight.codesight_api.web.dtos.user.UserGetDto;
import com.codesight.codesight_api.web.dtos.user.UserPostDto;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "User-Controller")
@Controller
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get list of Users")
    @ApiResponse(code = 200, message = "Success|OK")
    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<UserGetDto>> get(Pageable pageable){
        return ResponseEntity.ok(userService.get(pageable));
    }

    @ApiOperation(value = "Get User by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User found|OK"),
            @ApiResponse(code = 404, message = "User NOT found|NOT_FOUND")
    })
    @GetMapping(value ="/{id}", produces = "application/json")
    public ResponseEntity<UserGetDto> get(@PathVariable Integer id){
        return ResponseEntity.ok(userService.get(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create User")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created|CREATED"),
            @ApiResponse(code = 400, message = "Bad request|BAD_REQUEST")
    })
    @PostMapping(produces = "application/json")
    public ResponseEntity<UserGetDto> create(@RequestBody @Valid UserPostDto userPostDto){
        return new ResponseEntity<>(userService.create(userPostDto), new HttpHeaders(), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete User by id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "User deleted|NO_CONTENT"),
            @ApiResponse(code = 404, message = "User NOT found|NOT_FOUND")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Partial Update User by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User UPDATED|OK"),
            @ApiResponse(code = 400, message = "Bad request|BAD_REQUEST"),
            @ApiResponse(code = 404, message = "User NOT found|NOT_FOUND")
    })
    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json", produces = "application/json")
    public ResponseEntity<UserGetDto> partialUpdate(@PathVariable Integer id, @RequestBody JsonMergePatch patch){
        return ResponseEntity.ok(userService.partialUpdate(id, patch));
    }
}
