package com.kh.auth2andkhqr.KHQR;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CheckTransactionByMd5Request {
    @NotBlank(message = "MD5 is required")
    @Size(min = 1, max = 255, message = "MD5 must be between 1 and 255 characters")
    private String md5;
}
