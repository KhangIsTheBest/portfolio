package com.khangdt.portfolio.technology.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnologyUpdateRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 255, message = "Icon URL must not exceed 255 characters")
    @Pattern(
            regexp = "^(|[a-zA-Z0-9_-]+|https?://\\S+)$",
            message = "Icon URL must be empty, a preset key, or a valid http(s) URL"
    )
    private String iconUrl;
}
