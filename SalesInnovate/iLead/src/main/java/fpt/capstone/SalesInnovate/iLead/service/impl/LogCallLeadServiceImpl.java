package fpt.capstone.SalesInnovate.iLead.service.impl;

import fpt.capstone.SalesInnovate.iLead.dto.Converter;
import fpt.capstone.SalesInnovate.iLead.dto.LogCallLeadDTO;
import fpt.capstone.SalesInnovate.iLead.model.Log_Call_Leads;
import fpt.capstone.SalesInnovate.iLead.repository.LogCallLeadRepository;
import fpt.capstone.SalesInnovate.iLead.service.LogCallLeadsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class LogCallLeadServiceImpl implements LogCallLeadsService {
//    private LogCallLeadRepository logCallLeadRepository;
//    private Converter converter;
//    @Override
//    public Log_Call_Leads createLogCallLeads(LogCallLeadDTO logCallLeadDTO) {
//        Log_Call_Leads log_call_leads = this.converter.LogCallLeadDTOToEntity(logCallLeadDTO);
//        return this.logCallLeadRepository.save(log_call_leads);
//    }
//
//
//    @Override
//    public LogCallLeadDTO updateLogCallLead(LogCallLeadDTO logCallLeadDTO, int id) {
//        Log_Call_Leads log_call_leads = this.logCallLeadRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Cannot find Leads with id: " + id));
//        log.info("There exists a lead containing that ID: " + id);
//        this.logCallLeadRepository.save(this.converter
//                .updateLogCallLeadFromLogCallLeadDTO(log_call_leads,logCallLeadDTO));
//        log.info("Finish Update LogCallLead!");
//        LogCallLeadDTO logCallLeadDTOAfterSave = this.converter.LogCallLeadEntityToDTO(log_call_leads);
//        return logCallLeadDTOAfterSave;
//    }

//    @Override
//    public void deleteLogCallLeadById(int id) {
//        Log_Call_Leads log_call_leads = this.logCallLeadRepository.findById(id).orElse(null);
//        if(log_call_leads != null){
//            log.info("There exists a log_call_leads containing that ID: " + id);
//            this.logCallLeadRepository.delete(log_call_leads);
//            log.info("Delete Lead success!");
//        }else {
//            log.info("There are not exists a log_call_leads containing that ID: " + id);
//        }
//    }
}
