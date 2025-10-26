package com.kh.auth2andkhqr.KHQR;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckTransactionResponse {
    private Integer responseCode;
    private String responseMessage;
    private Integer errorCode;
    private TransactionData data;
}
