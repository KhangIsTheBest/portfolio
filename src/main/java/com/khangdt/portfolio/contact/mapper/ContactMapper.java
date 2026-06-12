package com.khangdt.portfolio.contact.mapper;

import com.khangdt.portfolio.contact.dto.request.ContactSubmitRequest;
import com.khangdt.portfolio.contact.dto.response.ContactResponse;
import com.khangdt.portfolio.contact.entity.Contact;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ContactMapper {

    public Contact toEntity(ContactSubmitRequest request) {
        if (request == null) {
            return null;
        }

        return Contact.builder()
                .name(normalizeText(request.getName()))
                .email(normalizeText(request.getEmail()))
                .subject(normalizeText(request.getSubject()))
                .message(normalizeText(request.getMessage()))
                .build();
    }

    public ContactResponse toResponse(Contact contact) {
        if (contact == null) {
            return null;
        }

        return ContactResponse.builder()
                .id(contact.getId())
                .name(contact.getName())
                .email(contact.getEmail())
                .subject(contact.getSubject())
                .message(contact.getMessage())
                .createdAt(contact.getCreatedAt())
                .build();
    }

    public List<ContactResponse> toResponseList(List<Contact> contacts) {
        if (contacts == null || contacts.isEmpty()) {
            return Collections.emptyList();
        }

        return contacts.stream()
                .map(this::toResponse)
                .toList();
    }

    private String normalizeText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
