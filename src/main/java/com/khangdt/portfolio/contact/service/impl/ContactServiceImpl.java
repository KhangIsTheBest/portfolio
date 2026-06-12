package com.khangdt.portfolio.contact.service.impl;

import com.khangdt.portfolio.common.exception.ResourceNotFoundException;
import com.khangdt.portfolio.contact.dto.request.ContactSubmitRequest;
import com.khangdt.portfolio.contact.dto.response.ContactResponse;
import com.khangdt.portfolio.contact.entity.Contact;
import com.khangdt.portfolio.contact.mapper.ContactMapper;
import com.khangdt.portfolio.contact.repository.ContactRepository;
import com.khangdt.portfolio.contact.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactServiceImpl implements ContactService {

    private static final String RESOURCE_NAME = "Contact";

    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    @Override
    @Transactional
    public ContactResponse submitContact(ContactSubmitRequest request) {
        Contact contact = contactMapper.toEntity(request);
        Contact savedContact = contactRepository.save(contact);
        return contactMapper.toResponse(savedContact);
    }

    @Override
    public Page<ContactResponse> getAllContacts(Pageable pageable) {
        return contactRepository.findAll(pageable)
                .map(contactMapper::toResponse);
    }

    @Override
    public ContactResponse getContactById(Long id) {
        Contact contact = findContactById(id);
        return contactMapper.toResponse(contact);
    }

    @Override
    @Transactional
    public void deleteContact(Long id) {
        Contact contact = findContactById(id);
        contactRepository.delete(contact);
    }

    private Contact findContactById(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }
}
