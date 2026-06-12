package com.khangdt.portfolio.file.dto.response;

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
public class UploadFileResponse {
    private String fileName;
    private String fileUrl;
    private String fileType;
    private long size;
}
