package com.khangdt.portfolio.contact.service.impl;

import com.khangdt.portfolio.common.exception.ResourceNotFoundException;
import com.khangdt.portfolio.contact.dto.request.ContactSubmitRequest;
import com.khangdt.portfolio.contact.dto.response.ContactResponse;
import com.khangdt.portfolio.contact.entity.Contact;
import com.khangdt.portfolio.contact.mapper.ContactMapper;
import com.khangdt.portfolio.contact.repository.ContactRepository;
import com.khangdt.portfolio.contact.service.ContactService;
import com.khangdt.portfolio.common.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final EmailService emailService;

    @Value("${spring.mail.username:}")
    private String adminEmail;

    @Override
    @Transactional
    public ContactResponse submitContact(ContactSubmitRequest request) {
        Contact contact = contactMapper.toEntity(request);
        Contact savedContact = contactRepository.save(contact);
        
        try {
            String subject = "[Portfolio CLI] Tin nhắn mới từ: " + request.getName();
            String body = String.format(
                "Bạn nhận được một tin nhắn liên hệ mới:\n\n" +
                "Họ và tên: %s\n" +
                "Email: %s\n" +
                "Chủ đề: %s\n\n" +
                "Nội dung:\n%s",
                request.getName(),
                request.getEmail(),
                request.getSubject(),
                request.getMessage()
            );
            emailService.sendSimpleEmail(adminEmail, subject, body);
        } catch (Exception e) {
            System.err.println("Failed to send contact notification email: " + e.getMessage());
        }
        
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
