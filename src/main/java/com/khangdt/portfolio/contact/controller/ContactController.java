package com.khangdt.portfolio.contact.controller;

import com.khangdt.portfolio.common.response.ApiResponse;
import com.khangdt.portfolio.common.response.PagedResponse;
import com.khangdt.portfolio.contact.dto.request.ContactSubmitRequest;
import com.khangdt.portfolio.contact.dto.response.ContactResponse;
import com.khangdt.portfolio.contact.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Contacts", description = "Contact form and messages APIs")
public class ContactController {

    private static final String BEARER_AUTH = "Bearer Authentication";

    private final ContactService contactService;

    @Operation(
            summary = "Submit contact form",
            description = "Allows visitors to send a message to the portfolio owner (Public)."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "210",
                    description = "Contact message sent successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid validation inputs"
            )
    })
    @PostMapping("/api/v1/contacts")
    public ResponseEntity<ApiResponse<ContactResponse>> submitContact(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Contact submission payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ContactSubmitRequest.class))
            )
            @Valid @RequestBody ContactSubmitRequest request
    ) {
        ContactResponse response = contactService.submitContact(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Your message was sent successfully!", response));
    }

    @Operation(
            summary = "Get all contact messages",
            description = "Returns a paginated list of all contact submissions. Requires admin JWT."
    )
    @SecurityRequirement(name = BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Contact list retrieved successfully"
            )
    })
    @GetMapping("/api/v1/admin/contacts")
    public ResponseEntity<ApiResponse<PagedResponse<ContactResponse>>> getContacts(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ContactResponse> contacts = contactService.getAllContacts(pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.from(contacts)));
    }

    @Operation(
            summary = "Get contact message by ID",
            description = "Retrieves a specific contact message details. Requires admin JWT."
    )
    @SecurityRequirement(name = BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Contact message details retrieved successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Contact message not found"
                )
    })
    @GetMapping("/api/v1/admin/contacts/{id}")
    public ResponseEntity<ApiResponse<ContactResponse>> getContactById(
            @Parameter(description = "Contact message ID", example = "1")
            @PathVariable Long id
    ) {
        ContactResponse response = contactService.getContactById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Delete contact message by ID",
            description = "Deletes a specific contact message. Requires admin JWT."
    )
    @SecurityRequirement(name = BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Contact message deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Contact message not found"
            )
    })
    @DeleteMapping("/api/v1/admin/contacts/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteContact(
            @Parameter(description = "Contact message ID", example = "1")
            @PathVariable Long id
    ) {
        contactService.deleteContact(id);
        return ResponseEntity.ok(ApiResponse.success("Contact message deleted successfully", null));
    }
}
