 package fpt.capstone.SalesInnovate.iLead.service;

import fpt.capstone.SalesInnovate.iLead.dto.Converter;
import fpt.capstone.SalesInnovate.iLead.dto.request.AddressInformationDTO;
import fpt.capstone.SalesInnovate.iLead.dto.request.LeadDTO;
import fpt.capstone.SalesInnovate.iLead.dto.request.LeadExportDTO;
import fpt.capstone.SalesInnovate.iLead.dto.request.LeadImportFileDTO;
import fpt.capstone.SalesInnovate.iLead.model.*;
import fpt.capstone.SalesInnovate.iLead.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
 @Slf4j
 @Service
 @AllArgsConstructor
public class ExcelUploadService {
     private Converter leadConverter;
     private AddressInformationRepository addressInformationRepository;
     private final LeadsRepository leadsRepository;
     private final LeadStatusRepository leadStatusRepository;
     private final IndustryRepository industryRepository;
     private final LeadSourceRepository leadSourceRepository;
     private final SearchRepository searchRepository;
     private final LeadRatingRepository leadRatingRepository;
     private final LeadSalutionRepository leadSalutionRepository;
     private static final String[] HEADER = {
             "id","LeadSalutionName", "First Name","Middle Name", "Last Name", "Gender", "Title", "Email",
             "Phone", "Website","Company", "Employee No", "Lead Source Name", "IndustryStatusName",
             "LeadStatusName","LeadRatingName", "Street", "City", "Province", "PostalCode", "Country"
     };
     private static final String SHEET_NAME = "Leads";

    public  boolean isValidExcelFile(MultipartFile file){

        return Objects.equals(file.getContentType(),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
    @Transactional
    public List<Leads> getLeadDataFromExcel(InputStream inputStream, String userId){
        List<Leads> listLeads = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("leads");
            log.info("Read file excel");
            int rowIndex =0;
            for (Row row : sheet){
                if (rowIndex ==0){
                    rowIndex++;
                    continue;
                }
                boolean isEmptyRow = true;
                Iterator<Cell> cellIterator = row.iterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (cell != null && cell.getCellType() != CellType.BLANK) {
                        isEmptyRow = false;
                        break;
                    }
                }

                if (isEmptyRow) {
                    break;
                }
                cellIterator = row.iterator();
                int cellIndex = 0;
                LeadImportFileDTO leadImportFileDTO = new LeadImportFileDTO();
                while (cellIterator.hasNext()){
                    log.info("Add data from file excel in column :" + cellIndex);
                    Cell cell = cellIterator.next();
                    if (cell == null || cell.getCellType() == CellType.BLANK) {
                        cellIndex++;
                        continue;
                    }
                    switch (cellIndex){
                        case 0 -> leadImportFileDTO.setFirstName(cell.getStringCellValue());
                        case 1 -> leadImportFileDTO.setLastName(cell.getStringCellValue());
                        case 2 -> leadImportFileDTO.setGender(cell.getStringCellValue());
                        case 3 -> leadImportFileDTO.setTitle(cell.getStringCellValue());
                        case 4 -> leadImportFileDTO.setEmail(cell.getStringCellValue());
                        case 5 -> leadImportFileDTO.setPhone(Double.toString(cell.getNumericCellValue()));
                        case 6 -> leadImportFileDTO.setWebsite(cell.getStringCellValue());
                        case 7 -> leadImportFileDTO.setCompany(cell.getStringCellValue());
                        case 8 -> leadImportFileDTO.setNoEmployee((int)cell.getNumericCellValue());
                        case 9 -> leadImportFileDTO.setLeadSourceName(cell.getStringCellValue());
                        case 10 -> leadImportFileDTO.setIndustryStatusName(cell.getStringCellValue());
                        case 11 -> leadImportFileDTO.setLeadStatusName(cell.getStringCellValue());
                        case 12 -> leadImportFileDTO.setLeadRatingName(cell.getStringCellValue());
                        case 13 -> leadImportFileDTO.setStreet(cell.getStringCellValue());
                        case 14 -> leadImportFileDTO.setCity(cell.getStringCellValue());
                        case 15 -> leadImportFileDTO.setProvince(cell.getStringCellValue());
                        case 16 -> leadImportFileDTO.setPostalCode(String.valueOf(cell.getNumericCellValue()));
                        case 17 -> leadImportFileDTO.setCountry(cell.getStringCellValue());
                        case 18 -> leadImportFileDTO.setLeadSalutionName(cell.getStringCellValue());
                        default -> {
                            log.warn("Unexpected column index: " + cellIndex);
                        }
                    }
                    cellIndex++;
                }
                AddressInformation addressInformation =
                        leadConverter.DataToAddressInformation(leadImportFileDTO.getStreet(),
                                leadImportFileDTO.getCity(),leadImportFileDTO.getProvince(),
                                leadImportFileDTO.getPostalCode(), leadImportFileDTO.getCountry());
                AddressInformation savedAddressInformation = addressInformationRepository.save(addressInformation);

                Leads lead = leadConverter.
                        convertFileImportToLeads(leadImportFileDTO,savedAddressInformation,
                                userId);
                listLeads.add(lead);
            }
            log.info("Add data from file excel success");
            return listLeads;
        } catch (IOException  e) {
            e.getStackTrace();
            return null;
        }
    }
     public ByteArrayInputStream dataToExecel(List<Leads> list) throws IOException {
         Workbook workbook = new XSSFWorkbook();
         ByteArrayOutputStream out = new ByteArrayOutputStream();

         try {
             Sheet sheet = workbook.createSheet(SHEET_NAME);
             Row headerRow = sheet.createRow(0);

             for (int i = 0; i < HEADER.length; i++) {
                 Cell cell = headerRow.createCell(i);
                 cell.setCellValue(HEADER[i]);
             }

             List<LeadExportDTO> leadExportDTOs = leadConverter.LeadEntityListToLeadExportDTOList(list);
             int rowIndex = 1;

             for (LeadExportDTO lead : leadExportDTOs) {
                 Row dataRow = sheet.createRow(rowIndex++);

                 AddressInformation addressInformation = lead.getAddressInformationId() != null ?
                         addressInformationRepository.findById(lead.getAddressInformationId()).orElse(null) : null;
                 String leadSalutionName = lead.getLeadSalutionId() != null ?
                         leadSalutionRepository.findById(lead.getLeadSalutionId()).map(LeadSalution::getLeadSalutionName).orElse("") : "";
                 String leadSourceName = lead.getLeadSourceId() != null ?
                         leadSourceRepository.findById(lead.getLeadSourceId()).map(LeadSource::getLeadSourceName).orElse("") : "";
                 String industryStatusName = lead.getIndustryId() != null ?
                         industryRepository.findById(lead.getIndustryId()).map(Industry::getIndustryStatusName).orElse("") : "";
                 String leadStatusName = lead.getLeadStatusID() != null ?
                         leadStatusRepository.findById(lead.getLeadStatusID()).map(LeadStatus::getLeadStatusName).orElse("") : "";

                 dataRow.createCell(0).setCellValue(lead.getLeadId());
                 dataRow.createCell(1).setCellValue(Optional.ofNullable(leadSalutionName).orElse(""));
                 dataRow.createCell(2).setCellValue(Optional.ofNullable(lead.getFirstName()).orElse(""));
                 dataRow.createCell(3).setCellValue(Optional.ofNullable(lead.getMiddleName()).orElse(""));
                 dataRow.createCell(4).setCellValue(Optional.ofNullable(lead.getLastName()).orElse(""));
                 dataRow.createCell(5).setCellValue(Optional.ofNullable(lead.getGender()).orElse(0));
                 dataRow.createCell(6).setCellValue(Optional.ofNullable(lead.getTitle()).orElse(""));
                 dataRow.createCell(7).setCellValue(Optional.ofNullable(lead.getEmail()).orElse(""));
                 dataRow.createCell(8).setCellValue(Optional.ofNullable(lead.getPhone()).orElse(""));
                 dataRow.createCell(9).setCellValue(Optional.ofNullable(lead.getWebsite()).orElse(""));
                 dataRow.createCell(10).setCellValue(Optional.ofNullable(lead.getCompany()).orElse(""));
                 dataRow.createCell(11).setCellValue(Optional.ofNullable(lead.getNoEmployee()).orElse(0));
                 dataRow.createCell(12).setCellValue(Optional.ofNullable(leadSourceName).orElse(""));
                 dataRow.createCell(13).setCellValue(Optional.ofNullable(industryStatusName).orElse(""));
                 dataRow.createCell(14).setCellValue(Optional.ofNullable(leadStatusName).orElse(""));
                 dataRow.createCell(15).setCellValue(Optional.ofNullable(addressInformation).map(AddressInformation::getStreet).orElse(""));
                 dataRow.createCell(16).setCellValue(Optional.ofNullable(addressInformation).map(AddressInformation::getCity).orElse(""));
                 dataRow.createCell(17).setCellValue(Optional.ofNullable(addressInformation).map(AddressInformation::getProvince).orElse(""));
                 dataRow.createCell(18).setCellValue(Optional.ofNullable(addressInformation).map(AddressInformation::getPostalCode).orElse(""));
                 dataRow.createCell(19).setCellValue(Optional.ofNullable(addressInformation).map(AddressInformation::getCountry).orElse(""));
             }

             workbook.write(out);
             return new ByteArrayInputStream(out.toByteArray());
         } catch (IOException e) {
             e.printStackTrace();
             System.out.println("fail to export File");
             return null;
         } finally {
             workbook.close();
             out.close();
         }
     }
}
