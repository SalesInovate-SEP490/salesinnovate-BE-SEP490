package fpt.capstone.SalesInnovate.iLead.controller;


import fpt.capstone.SalesInnovate.iLead.dto.request.LeadDTO;
import fpt.capstone.SalesInnovate.iLead.dto.response.ResponseData;
import fpt.capstone.SalesInnovate.iLead.dto.response.ResponseError;
import fpt.capstone.SalesInnovate.iLead.service.LeadsService;
import fpt.capstone.SalesInnovate.iLead.util.KeycloakSecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/leads")
public class LeadsController {
    //    private LeadServiceImpl leadsService;
    private final LeadsService leadsService;
    @Autowired
    KeycloakSecurityUtil keycloakUtil ;

    @PostMapping("/upload-leads-data")
    public ResponseData<?> uploadCustomersData(@RequestParam("file") MultipartFile file,
                                               @RequestParam("userId") String userId) {
        try {
            leadsService.saveLeadsToDatabase(file, userId);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Import file success", 1);
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Add user fail");
        }
    }

    @GetMapping("/leads-list")
    public ResponseData<?> getLeads(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                    @RequestParam(defaultValue = "20", required = false) int pageSize) {
        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            String userId = authentication.getName();
            return new ResponseData<>(1, HttpStatus.OK.value(), leadsService.getAllLeadsWithSortByDefault(pageNo, pageSize));
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/{leadId}")
    public ResponseData<?> getLeadDetail(@PathVariable Long leadId) {
        try {
            if (leadsService.getLeadDetail(leadId) == null) {
                return new ResponseData<>(0, HttpStatus.NO_CONTENT.value(), leadsService.getLeadDetail(leadId));
            }
            return new ResponseData<>(1, HttpStatus.OK.value(), leadsService.getLeadDetail(leadId));
        } catch (Exception e) {

            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "list Leads fail");
        }
    }

    @PostMapping("/create-leads")
    public ResponseData<?> createLead(@RequestBody LeadDTO leadDTO) {
        try {
            long leadId = leadsService.createLead(leadDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Create Leads success", leadId, 1);
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PatchMapping("/patch-leads/{id}")
    public ResponseData<?> patchLead(@RequestBody LeadDTO leadDTO, @PathVariable(name = "id") long id) {
        return leadsService.patchLead(leadDTO, id) ?
                new ResponseData<>(HttpStatus.OK.value(), "Update Leads success", 1)
                : new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Update Leads fail");

    }

    @DeleteMapping("/delete-leads/{id}")
    public ResponseData<?> deleteLead(@PathVariable(name = "id") Long id) {
        return leadsService.deleteLeadById(id) ?
                new ResponseData<>(HttpStatus.OK.value(), "Delete Leads success", 1) :
                new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Delete Leads fail");

    }

    @GetMapping("/industry-list")
    public ResponseData<?> getAllIndustry() {
        try {
            return new ResponseData<>(1, HttpStatus.OK.value(), leadsService.getAllIndustry());
        } catch (Exception e) {

            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "list industry fail");
        }
    }

    @GetMapping("/status-list")
    public ResponseData<?> getAllLeadStatus() {
        try {
            return new ResponseData<>(1, HttpStatus.OK.value(), leadsService.geLeadStatus());
        } catch (Exception e) {

            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "list status fail");
        }
    }

    @GetMapping("/salution-list")
    public ResponseData<?> getAllLeadSalution() {
        try {
            return new ResponseData<>(1, HttpStatus.OK.value(), leadsService.geLeadSalution());
        } catch (Exception e) {

            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "list status fail");
        }
    }

    @GetMapping("/source-list")
    public ResponseData<?> getAllLeadSource() {
        try {
            return new ResponseData<>(1, HttpStatus.OK.value(), leadsService.getAllLeadSource());
        } catch (Exception e) {

            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "list source fail");
        }
    }

    @GetMapping("/filterSearch")
    public ResponseData<?> filterLeadsWithSpecifications(Pageable pageable,
                                                         @RequestParam(required = false) String[] search) {
        try {
            return new ResponseData<>(1, HttpStatus.OK.value(),
                    leadsService.filterLeadsWithSpecifications(pageable, search));
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "list source fail");
        }
    }

    @GetMapping("/leads-by-status")
    public ResponseData<?> getLeadsByStatus(@RequestParam long id) {
        try {
            return new ResponseData<>(1, HttpStatus.OK.value(),
                    leadsService.getLeadsByStatus(id));
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PatchMapping("/patch-list-lead")
    public ResponseData<?> patchListLead(@RequestParam Long[] id, @RequestBody LeadDTO leadDTO) {
        return leadsService.patchListLead(id, leadDTO) ?
                new ResponseData<>(1, HttpStatus.OK.value(), "Update Lead success") :
                new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Update leads fail");
    }

    @GetMapping("/export-file")
    public ResponseEntity<Resource> getFileExport() throws IOException {
        String filename = "leads.xlsx";
        ByteArrayInputStream fileExport = leadsService.getExportFileData();
        InputStreamResource file = new InputStreamResource(fileExport);

        ResponseEntity<Resource> response = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);

        // Debugging headers
        HttpHeaders headers = response.getHeaders();
        System.out.println("Content-Disposition: " + headers.get(HttpHeaders.CONTENT_DISPOSITION));
        System.out.println("Content-Type: " + headers.get(HttpHeaders.CONTENT_TYPE));

        return response;
    }

}