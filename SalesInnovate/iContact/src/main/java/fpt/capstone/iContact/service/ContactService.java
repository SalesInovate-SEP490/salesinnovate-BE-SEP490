package fpt.capstone.iContact.service;

import fpt.capstone.iContact.dto.request.ContactDTO;
import fpt.capstone.iContact.dto.response.ContactResponse;
import fpt.capstone.iContact.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface ContactService {

    Long createFromLead (long leadId,long accountId,long salution,String firstName,String middleName,String lastName);

    Long existingFromLead(long contactId, long  accountId);

    PageResponse<?> getAllContactsWithSortByDefault(int pageNo, int pageSize);

    PageResponse<?> getAllContactByAccount(int pageNo, int pageSize, long id);

    ContactResponse getContactDetail(long id);

    boolean deleteContact(long id);

    Long createContact(ContactDTO contactDTO);

    boolean patchContact(ContactDTO contactDTO,long id);

    PageResponse<?> filterContact(Pageable pageable, String[] search);
    ByteArrayInputStream getExportFileData() throws IOException;
}
