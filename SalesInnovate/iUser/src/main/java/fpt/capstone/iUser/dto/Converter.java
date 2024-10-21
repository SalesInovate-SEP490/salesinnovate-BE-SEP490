package fpt.capstone.iUser.dto;

import fpt.capstone.iUser.dto.request.EmailTemplateDTO;
import fpt.capstone.iUser.dto.request.FileManagerDTO;
import fpt.capstone.iUser.dto.request.FileShareDTO;
import fpt.capstone.iUser.dto.request.UsersDTO;
import fpt.capstone.iUser.dto.response.EmailTemplateResponse;
import fpt.capstone.iUser.dto.response.*;
import fpt.capstone.iUser.dto.response.PageResponse;
import fpt.capstone.iUser.model.*;
import fpt.capstone.iUser.repository.FileManagerRepository;
import fpt.capstone.iUser.repository.UsersRepository;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Converter {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private FileManagerRepository fileManagerRepository;

    public EmailTemplateDTO entityToEmailTemplateDTO(EmailTemplate emailTemplate) {
        return EmailTemplateDTO.builder()
                .sendTo(emailTemplate.getSendTo())
                .mailSubject(emailTemplate.getMailSubject())
                .htmlContent(emailTemplate.getHtmlContent())
                .userId(emailTemplate.getUsers().getUserId())
                .build();
    }

    public EmailTemplate DTOToEmailTemplate(EmailTemplateDTO emailTemplateDTO) {
        return EmailTemplate.builder()
                .sendTo(emailTemplateDTO.getSendTo())
                .mailSubject(emailTemplateDTO.getMailSubject())
                .htmlContent(emailTemplateDTO.getHtmlContent())
                .users(usersRepository.findById(emailTemplateDTO.getUserId()).orElse(null))
                .build();
    }

    public EmailTemplate UpdateEmailTemplateFromDTO(EmailTemplateDTO emailTemplateDTO, EmailTemplate emailTemplate) {
        emailTemplate.setSendTo(emailTemplateDTO.getSendTo());
        emailTemplate.setMailSubject(emailTemplateDTO.getMailSubject());
        emailTemplate.setHtmlContent(emailTemplateDTO.getHtmlContent());
        return emailTemplate;
    }

    public EmailTemplateResponse entityToEmailTemplateReource(EmailTemplate emailTemplate) {
        return EmailTemplateResponse.builder()
                .emailTemplateId(emailTemplate.getEmailTemplateId())
                .sendTo(emailTemplate.getSendTo())
                .mailSubject(emailTemplate.getMailSubject())
                .htmlContent(emailTemplate.getHtmlContent())
                .isDeleted(emailTemplate.getIsDeleted())
                .deleteDate(emailTemplate.getDeleteDate())
                .build();
    }

    public PageResponse<?> convertToPageResponse(Page<EmailTemplate> emailTemplate, Pageable pageable) {
        List<EmailTemplateResponse> response = emailTemplate.stream().map(this::entityToEmailTemplateReource).toList();
        return PageResponse.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .total(emailTemplate.getTotalPages())
                .items(response)
                .build();
    }

    public FileManager DTOtoFileManager(FileManagerDTO fileManagerDTO) {
        return FileManager.builder()
                .fileCloudId(fileManagerDTO.getFileCloudId())
                .fileName(fileManagerDTO.getFileName())
                .build();
    }

    public FileResponse entityToResponse(FileManager fileManager) {
        return FileResponse.builder()
                .id(fileManager.getFileId())
                .fileCloudId(fileManager.getFileCloudId())
                .fileName(fileManager.getFileName())
                .createdDate(fileManager.getCreatedDate())
                .build();
    }

    public FileManagerDTO saveDataFileToDTO(String fileName,String fileCloudId) {
        return FileManagerDTO.builder()
                .fileCloudId(fileCloudId)
                .fileName(fileName)
                .build();
    }

    public FileShare DTOtoFileShare(FileShareDTO fileShareDTO) {
        return FileShare.builder()
                .users(usersRepository.findById(fileShareDTO.getUserId()).orElse(null))
                .fileManager(fileManagerRepository.findById(fileShareDTO.getFileId()).orElse(null))
                .build();
    }

    public FileShareDTO saveDataFileShareToDTO(String UserId,Long fileId) {
        return FileShareDTO.builder()
                .userId(UserId)
                .fileId(fileId)
                .build();
    }

    public FileResponse convertEntityToFileReponse(FileManager fileManager
    ) {
        return FileResponse.builder()
                .id(fileManager.getFileId())
                .fileCloudId(fileManager.getFileCloudId())
                .fileName(fileManager.getFileName())
                .createdDate(fileManager.getCreatedDate())
                .build();
    }

    public PageResponse<?> convertToPageFileReponse(Page<FileManager> fileManager, Pageable pageable) {
        List<FileResponse> response = fileManager.stream().map(this::convertEntityToFileReponse).toList();
        return PageResponse.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .total(fileManager.getTotalPages())
                .items(response)
                .build();
    }

    public List<UserResponse> mapUsers(List<UserRepresentation> userRepresentations, List<RoleRepresentation> roles) {
        List<UserResponse> users = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(userRepresentations)) {
            userRepresentations.forEach(userRep -> {
                users.add(mapUser(userRep, roles));
            });
        }
        return users;
    }

    public UserResponse mapUser(UserRepresentation userRep, List<RoleRepresentation> roles) {
        UserResponse user = new UserResponse();
        user.setUserId(userRep.getId());
        user.setEmail(userRep.getEmail());
        user.setUserName(userRep.getUsername());
        user.setRoles(mapRoles(roles));
        return user;
    }

    public UserRepresentation mapUserRep(UsersDTO user) {
        UserRepresentation userRep = new UserRepresentation();
        userRep.setId(user.getUserId());
        userRep.setUsername(user.getUserName());
        userRep.setFirstName(user.getFirstName());
        userRep.setLastName(user.getLastName());
        userRep.setEmail(user.getEmail());
        userRep.setEnabled(true);
        userRep.setEmailVerified(true);
        List<CredentialRepresentation> creds = new ArrayList<>();
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setValue(user.getPassWord());
        creds.add(cred);
        userRep.setCredentials(creds);
        return userRep;
    }

    public RoleResponse mapRole(RoleRepresentation roleRep) {
        RoleResponse role = new RoleResponse();
        role.setId(roleRep.getId());
        role.setName(roleRep.getName());
        return role;
    }

    public List<RoleResponse> mapRoles(List<RoleRepresentation> representations) {
        List<RoleResponse> roles = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(representations)) {
            representations.forEach(roleRep -> roles.add(mapRole(roleRep)));
        }
        return roles;
    }
}
