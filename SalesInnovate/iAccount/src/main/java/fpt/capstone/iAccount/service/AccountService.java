package fpt.capstone.iAccount.service;

import fpt.capstone.iAccount.dto.request.AccountDTO;
import fpt.capstone.iAccount.dto.request.AccountTypeDTO;
import fpt.capstone.iAccount.dto.response.AccountResponse;
import fpt.capstone.iAccount.dto.response.PageResponse;
import fpt.capstone.iAccount.dto.response.ResponseData;
import fpt.capstone.iAccount.model.Account;
import fpt.capstone.iAccount.model.AccountType;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface AccountService {

    PageResponse<?> getListAccount(int pageNo, int pageSize);
    AccountResponse getDetailAccount(Long accountId);
    Long convertNewAccount(long leadId, String accountName);
    boolean patchAccount(AccountDTO accountDTO, long id);
    Long createAccount(AccountDTO accountDTO);
    boolean deleteAccount(Long id);
    boolean patchListAccount(Long[] id, AccountDTO accountDTO);
    PageResponse<?> filterAccount(Pageable pageable, String[] search);
    List<AccountType> getListType();
    void importFileAccount(MultipartFile file, String userId);
    ByteArrayInputStream getExportFileData() throws IOException;
}
