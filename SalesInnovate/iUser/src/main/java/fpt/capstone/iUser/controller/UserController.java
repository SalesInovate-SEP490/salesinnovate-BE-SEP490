package fpt.capstone.iUser.controller;

import fpt.capstone.iUser.dto.request.UsersDTO;
import fpt.capstone.iUser.dto.response.ResponseData;
import fpt.capstone.iUser.dto.response.ResponseError;
import fpt.capstone.iUser.service.UserService;
import fpt.capstone.iUser.util.KeycloakSecurityUtil;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    @Autowired
    KeycloakSecurityUtil keycloakUtil ;

    @GetMapping("/all-user")
    public ResponseData<?> getAllUser() {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all users success", userService.getAllUsers(), 1);
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Get all users fail");
        }
    }

    @GetMapping("/role")
    public ResponseData<?> getAllUserByRole(@RequestParam String roleName) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all users by role success", userService.getAllUsersByRole(roleName), 1);
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Get all users by role fail");
        }
    }

    @GetMapping("/user")
    public ResponseData<?> getUserById(@RequestParam String id) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all users by role success", userService.getUsersById(id), 1);
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Get all users by role fail");
        }
    }

    @GetMapping("/get-roles")
    public ResponseData<?> getRoles() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();
            Keycloak keycloak = keycloakUtil.getKeycloakInstance();
            List<RoleRepresentation> roles = keycloak.realm("master")
                    .users().get(userId).roles().realmLevel().listAll();
            return new ResponseData<>(HttpStatus.OK.value(), "Get roles success", roles, 1);
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Get roles fail");
        }
    }

    @PutMapping("/user/{id}")
    public ResponseData<?> updateUser(@PathVariable String id, @RequestBody UsersDTO usersDTO) {
        return userService.updateUser(id, usersDTO) ?
                new ResponseData<>(HttpStatus.CREATED.value(), "Update user success"
                        , 1) :
                new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Update user success");
    }


    @PostMapping("/user")
    public ResponseData<?> createUser(@RequestBody UsersDTO usersDTO) {
        try {
            return new ResponseData<>(HttpStatus.CREATED.value(), "Create user success", userService.createUser(usersDTO), 1);
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Create user fail");
        }
    }

    @PostMapping(value = "/users/{id}/roles/{roleName}")
    public ResponseData<?> addRoleToUser(@PathVariable("id") String id, @PathVariable("roleName") String roleName) {
        return userService.addRoleToUser(id, roleName) ?
                new ResponseData<>(HttpStatus.CREATED.value(), "Update user success"
                        , 1) :
                new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Add role to user fail");
    }

    @PutMapping("/users/{userId}/password")
    public ResponseData<?> changePassword(@PathVariable String userId, @RequestParam String newPassword) {
        try {
            return userService.updatePassword(userId,newPassword)?
                    new ResponseData<>(HttpStatus.CREATED.value(), "Update user password success",1):
                    new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Update user password fail");
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Update user password fail");
        }
    }
}
