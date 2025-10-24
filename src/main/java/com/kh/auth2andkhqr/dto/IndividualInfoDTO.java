package com.kh.auth2andkhqr.dto;

import kh.org.nbc.bakong_khqr.model.KHQRCurrency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndividualInfoDTO {
	
	private String bakongAccountId;
    private String currency = "KHR";
    private Double amount;
    private String merchantName;
//    private String merchantCity;
//    private String billNumber;
    private String storeLabel;
    private String terminalLabel;
    private String mobileNumber;
//    private String accountInformation;
    private String acquiringBank;
    
}
