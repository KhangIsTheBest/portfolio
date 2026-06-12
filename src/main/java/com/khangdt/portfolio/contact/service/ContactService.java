package com.khangdt.portfolio.contact.service;

import com.khangdt.portfolio.contact.dto.request.ContactSubmitRequest;
import com.khangdt.portfolio.contact.dto.response.ContactResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactService {
    ContactResponse submitContact(ContactSubmitRequest request);
    Page<ContactResponse> getAllContacts(Pageable pageable);
    ContactResponse getContactById(Long id);
    void deleteContact(Long id);
}
