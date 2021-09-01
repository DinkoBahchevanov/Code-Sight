package com.codesight.codesight_api.domain.user.service;

import com.codesight.codesight_api.domain.user.entity.ApplicationUserRole;
import com.codesight.codesight_api.domain.user.entity.User;
import com.codesight.codesight_api.domain.user.repository.UserRepository;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.shared.IncorrectJsonMergePatchProcessingException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.shared.IdCannotBeChangedException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.users.UserAlreadyExistsException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.users.UserNotFoundException;
import com.codesight.codesight_api.web.dtos.user.UserGetDto;
import com.codesight.codesight_api.web.dtos.user.UserPostDto;
import com.codesight.codesight_api.web.mappers.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, ObjectMapper objectMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.objectMapper = objectMapper;
        this.bCryptPasswordEncoder= bCryptPasswordEncoder;
    }

    @Override
    public Page<UserGetDto> get(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::userToUserGetDto);
    }

    @Override
    public UserGetDto get(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.userToUserGetDto(user);
    }

    @Override
    public UserGetDto create(UserPostDto userPostDto) {
        if(userRepository.existsByEmail(userPostDto.getEmail())) throw new UserAlreadyExistsException(userPostDto.getEmail());

        User user = userMapper.userPostDtoToUser(userPostDto);
        return userMapper.userToUserGetDto(userRepository.save(user));
    }

    @Override
    public void delete(Integer id) {
        if(!userRepository.existsById(id)) throw new UserNotFoundException(id);
        userRepository.deleteById(id);
    }

    @Override
    public UserGetDto partialUpdate(Integer id, JsonMergePatch patch) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        try {
            User patchedUser = applyPatchToUser(patch, user);
            if(!user.getId().equals(patchedUser.getId())) throw new IdCannotBeChangedException();

            if(userRepository.existsByEmail(patchedUser.getEmail()) && !patchedUser.getEmail().equalsIgnoreCase(user.getEmail()))
                throw new UserAlreadyExistsException(patchedUser.getEmail());

            return userMapper.userToUserGetDto(userRepository.save(patchedUser));
        } catch (JsonPatchException | JsonProcessingException e) {
            e.printStackTrace();
            throw new IncorrectJsonMergePatchProcessingException("There's a problem with the format of the request, check if the passed enum exists");
        }
    }

    @Override
    public UserGetDto register(UserPostDto userGetDto) {
        User user = userMapper.userPostDtoToUser(userGetDto);

        if (userRepository.count() == 0) {
            user.setRole(ApplicationUserRole.ADMIN);
        } else user.setRole(ApplicationUserRole.COMPETITOR);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
        }catch (DataIntegrityViolationException exception) {
            throw new UserAlreadyExistsException(user.getEmail());
        }
        return userMapper.userToUserGetDto(user);
    }

    private User applyPatchToUser(JsonMergePatch patch, User targetUser) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(targetUser, JsonNode.class));
        return objectMapper.treeToValue(patched, User.class);
    }
}
