package fpt.capstone.iUser.service;

import fpt.capstone.iUser.dto.request.UsersDTO;
import fpt.capstone.iUser.dto.response.RoleResponse;
import fpt.capstone.iUser.dto.response.UserResponse;
import fpt.capstone.iUser.model.Users;

import java.util.List;

public interface UserService {
    public List<UserResponse> getAllUsers();

    public List<UserResponse> getAllUsersByRole(String roleName);

    public UserResponse getUsersById(String id);

    public String createUser (UsersDTO usersDTO);

    public Boolean updateUser(String userId , UsersDTO usersDTO);

    public Boolean updatePassword(String userId , String newPassword);

    boolean addRoleToUser(String id, String roleName);

    List<RoleResponse> getUserRoles(String id);
}
