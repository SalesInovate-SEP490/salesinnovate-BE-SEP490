package fpt.capstone.iAccount.service;

import fpt.capstone.iAccount.dto.Converter;
import fpt.capstone.iAccount.dto.request.AccountExportDTO;
import fpt.capstone.iAccount.dto.request.AccountImportDTO;
import fpt.capstone.iAccount.model.Account;
import fpt.capstone.iAccount.model.AccountType;
import fpt.capstone.iAccount.model.AddressInformation;
import fpt.capstone.iAccount.model.Industry;
import fpt.capstone.iAccount.repository.AccountTypeRepository;
import fpt.capstone.iAccount.repository.AddressInformationRepository;
import fpt.capstone.iAccount.repository.IndustryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.checkerframework.checker.units.qual.A;
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
    private Converter converter;
    private final IndustryRepository industryRepository;
    private AddressInformationRepository addressInformationRepository;
    private AccountTypeRepository accountTypeRepository;
    private static final String[] HEADER = {
            "Id","Account Name","Phone", "Website","Description",
            "Employee No", "Parent Account Id", "IndustryStatusName",
            "Account Type Name", "Street", "City", "Province", "PostalCode", "Country"
    };
    private static final String SHEET_NAME = "Accounts";
    public  boolean isValidExcelFile(MultipartFile file){
        return Objects.equals(file.getContentType(),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
    @Transactional
    public List<Account> getLeadDataFromExcel(InputStream inputStream, String userId){
        List<Account> listAccount = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("account");
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
                AccountImportDTO accountImportDTO = new AccountImportDTO();
                while (cellIterator.hasNext()){
                    log.info("Add data from file excel in column :" + cellIndex);
                    Cell cell = cellIterator.next();
                    if (cell == null || cell.getCellType() == CellType.BLANK) {
                        cellIndex++;
                        continue;
                    }
                    switch (cellIndex){
                        case 0 -> accountImportDTO.setAccountName(cell.getStringCellValue());
                        case 1 -> accountImportDTO.setWebsite(cell.getStringCellValue());
                        case 2 -> accountImportDTO.setPhone(Double.toString(cell.getNumericCellValue()));
                        case 3 -> accountImportDTO.setDescription(cell.getStringCellValue());
                        case 4 -> accountImportDTO.setNoEmployee((int)cell.getNumericCellValue());
                        case 5 -> accountImportDTO.setParentAccountId((long)cell.getColumnIndex());
                        case 6 -> accountImportDTO.setIndustryStatusName(cell.getStringCellValue());
                        case 7 -> accountImportDTO.setAccountTypeName(cell.getStringCellValue());
                        case 8 -> accountImportDTO.setStreet(cell.getStringCellValue());
                        case 9 ->accountImportDTO.setCity(cell.getStringCellValue());
                        case 10 -> accountImportDTO.setProvince(cell.getStringCellValue());
                        case 11 -> accountImportDTO.setPostalCode(String.valueOf(cell.getNumericCellValue()));
                        case 12 -> accountImportDTO.setCountry(cell.getStringCellValue());

                        default -> {
                            log.warn("Unexpected column index: " + cellIndex);
                        }
                    }
                    cellIndex++;
                }
                AddressInformation addressInformation =
                        converter.DataToAddressInformation(accountImportDTO.getStreet(),
                                accountImportDTO.getCity(),accountImportDTO.getProvince(),
                                accountImportDTO.getPostalCode(), accountImportDTO.getCountry());
                AddressInformation savedAddressInformation = addressInformationRepository.save(addressInformation);

                Account account = converter.
                        convertFileImportToAccount(accountImportDTO,savedAddressInformation,
                                userId);
                listAccount.add(account);
            }
            log.info("Add data from file excel success");
            return listAccount;
        } catch (IOException e) {
            e.getStackTrace();
            return null;
        }
    }
    public ByteArrayInputStream dataToExecel(List<Account> list) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Sheet sheet = workbook.createSheet(SHEET_NAME);
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < HEADER.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADER[i]);
            }

            List<AccountExportDTO> accountExportDTOs = converter.AccountEntityListToLeadExportDTOList(list);
            int rowIndex = 1;

            for (AccountExportDTO accountExportDTO : accountExportDTOs) {
                Row dataRow = sheet.createRow(rowIndex++);

                AddressInformation addressInformation = accountExportDTO.getAddressInformationId() != null ?
                        addressInformationRepository.findById(accountExportDTO.getAddressInformationId()).orElse(null) : null;
                String industryStatusName = accountExportDTO.getIndustryId() != null ?
                        industryRepository.findById(accountExportDTO.getIndustryId())
                                .map(Industry::getIndustryStatusName).orElse("") : "";
                String accountTypeName = accountExportDTO.getAccountTypeId() != null ?
                        accountTypeRepository.findById(accountExportDTO.getAccountTypeId())
                                .map(AccountType::getAccountTypeName).orElse("") : "";

                dataRow.createCell(0).setCellValue(accountExportDTO.getAccountId());
                dataRow.createCell(1).setCellValue(Optional.ofNullable(accountExportDTO.getAccountName()).orElse(""));
                dataRow.createCell(2).setCellValue(Optional.ofNullable(accountExportDTO.getPhone()).orElse(""));
                dataRow.createCell(3).setCellValue(Optional.ofNullable(accountExportDTO.getWebsite()).orElse(""));
                dataRow.createCell(4).setCellValue(Optional.ofNullable(accountExportDTO.getDescription()).orElse(""));
                dataRow.createCell(5).setCellValue(Optional.ofNullable(accountExportDTO.getNoEmployee()).orElse(0));
                dataRow.createCell(6).setCellValue(Optional.ofNullable(accountExportDTO.getParentAccountId()).orElse(0L));
                dataRow.createCell(7).setCellValue(Optional.ofNullable(industryStatusName).orElse(""));
                dataRow.createCell(8).setCellValue(Optional.ofNullable(accountTypeName).orElse(""));
                dataRow.createCell(9).setCellValue(Optional.ofNullable(addressInformation).map(AddressInformation::getStreet).orElse(""));
                dataRow.createCell(10).setCellValue(Optional.ofNullable(addressInformation).map(AddressInformation::getCity).orElse(""));
                dataRow.createCell(11).setCellValue(Optional.ofNullable(addressInformation).map(AddressInformation::getProvince).orElse(""));
                dataRow.createCell(12).setCellValue(Optional.ofNullable(addressInformation).map(AddressInformation::getPostalCode).orElse(""));
                dataRow.createCell(13).setCellValue(Optional.ofNullable(addressInformation).map(AddressInformation::getCountry).orElse(""));
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
