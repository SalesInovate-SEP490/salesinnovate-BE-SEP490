package fpt.capstone.SalesInnovate.iLead.controller;

import fpt.capstone.SalesInnovate.iLead.service.LogCallLeadsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/log-call-lead")
public class LogCallLeadController {
    private LogCallLeadsService logCallLeadsService;
//    @PostMapping("/create-log-call-lead")
//    public ResponseData<LeadDTO> createLead(@RequestBody LogCallLeadDTO logCallLeadDTO) {
//        try {
//            this.logCallLeadsService.createLogCallLeads(logCallLeadDTO);
//            return new ResponseData<>(HttpStatus.CREATED.value(), "Create logCallLeadDTO success", 1);
//        } catch (Exception e) {
//            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Create logCallLeadDTO fail");
//        }
//    }
//    @DeleteMapping("/delete-log-call-lead/{id}")
//    public ResponseData<String> deleteLead(@PathVariable(name = "id") int id) {
//        try {
//            this.logCallLeadsService.deleteLogCallLeadById(id);
//            return new ResponseData<>(HttpStatus.OK.value(), "Delete log call Leads success", 1);
//        } catch (Exception e) {
//            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Delete log call Leads fail");
//        }
//    }
//    @PutMapping("/update-log-call-leads/{id}")
//    public ResponseData<LeadDTO> updateLead(@RequestBody LogCallLeadDTO logCallLeadDTO,@PathVariable(name = "id") int id) {
//        try {
//            this.logCallLeadsService.updateLogCallLead(logCallLeadDTO, id);
//            return new ResponseData<>(HttpStatus.OK.value(), "Update log call Leads success", 1);
//        } catch (Exception e) {
//            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Update log call Leads fail");
//        }
//    }
}
