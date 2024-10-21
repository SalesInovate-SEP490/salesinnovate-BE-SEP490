package fpt.capstone.iUser.service.impl;

import fpt.capstone.iUser.dto.Converter;
import fpt.capstone.iUser.dto.request.UsersDTO;
import fpt.capstone.iUser.dto.response.RoleResponse;
import fpt.capstone.iUser.dto.response.UserResponse;
import fpt.capstone.iUser.model.Users;
import fpt.capstone.iUser.repository.UsersRepository;
import fpt.capstone.iUser.service.UserService;
import fpt.capstone.iUser.util.KeycloakSecurityUtil;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final KeycloakSecurityUtil keycloakUtil ;
    @Autowired
    private final Converter converter;
    private final UsersRepository usersRepository;


    @Override
    public List<UserResponse> getAllUsers() {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        List<UserRepresentation> userRepresentations =
                keycloak.realm("master").users().list();
        return userRepresentations.stream()
                .map(user -> {
                    List<RoleRepresentation> roles = keycloak.realm("master").users().get(user.getId()).roles().realmLevel().listAll();
                    return converter.mapUser(user, roles);
                })
                .collect(Collectors.toList());
    }


    public List<UserResponse> getAllUsersByRole(String roleName) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        List<UserRepresentation> userRepresentations = keycloak.realm("master")
                .roles()
                .get(roleName)
                .getUserMembers();
        return userRepresentations.stream()
                .map(user -> {
                    List<RoleRepresentation> roles = keycloak.realm("master").users().get(user.getId()).roles().realmLevel().listAll();
                    return converter.mapUser(user, roles);
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUsersById(String id) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        UserRepresentation userRepresentation = keycloak.realm("master").users().get(id).toRepresentation();
        List<RoleRepresentation> roles = keycloak.realm("master").users().get(userRepresentation.getId()).roles().realmLevel().listAll();
		return converter.mapUser(userRepresentation,roles);
    }

    @Override
    public String createUser(UsersDTO usersDTO) {
        try {
            UserRepresentation userRep = converter.mapUserRep(usersDTO);
            Keycloak keycloak = keycloakUtil.getKeycloakInstance();
            Response res = keycloak.realm("master").users().create(userRep);

            if (res.getStatus() == 201) {
                String userId = res.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                Users users = Users.builder()
                        .userId(userId)
                        .userName(usersDTO.getUserName())
                        .firstName(usersDTO.getFirstName())
                        .lastName(usersDTO.getLastName())
                        .email(usersDTO.getEmail())
                        .passWord(usersDTO.getPassWord())
                        .createDate(LocalDateTime.now())
                        .build();
                usersRepository.save(users);
                return userId;
            } else {
                throw new RuntimeException("Failed to create user");
            }
        }catch (Exception e){
            log.info(e.getMessage(),e.getCause());
            throw(e);
        }
    }

    @Override
    public Boolean updateUser(String userId, UsersDTO usersDTO) {
        try {
            UserRepresentation userRep = converter.mapUserRep(usersDTO);
            Keycloak keycloak = keycloakUtil.getKeycloakInstance();
            keycloak.realm("master").users().get(userId).update(userRep);
            return true ;
        }catch (Exception e){
            log.info(e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean updatePassword(String userId, String newPassword) {
        try {
            Keycloak keycloak = keycloakUtil.getKeycloakInstance();
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(newPassword);
            credential.setTemporary(false);
            keycloak.realm("master").users().get(userId).resetPassword(credential);
            return true;
        }catch (Exception e){
            log.info(e.getMessage());
            throw new RuntimeException("Failed to update password user");
        }
    }

    @Override
    public boolean addRoleToUser(String id, String roleName) {
        try{
            Keycloak keycloak = keycloakUtil.getKeycloakInstance();
		RoleRepresentation role = keycloak.realm("master").roles().get(roleName).toRepresentation();
		keycloak.realm("master").users().get(id).roles().realmLevel().add(Arrays.asList(role));
            return true ;
        }catch (Exception e){
            log.info(e.getMessage());
            return false;
        }
    }

    @Override
    public List<RoleResponse> getUserRoles(String id) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        List<RoleRepresentation> roles = keycloak.realm("master").users().get(id).roles().realmLevel().listAll();
        return converter.mapRoles(roles);
    }

}
