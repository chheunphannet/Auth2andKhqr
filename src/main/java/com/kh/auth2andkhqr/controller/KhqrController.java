package com.kh.auth2andkhqr.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.auth2andkhqr.KHQR.CheckTransactionByMd5Request;
import com.kh.auth2andkhqr.KHQR.CheckTransactionResponse;
import com.kh.auth2andkhqr.KHQR.TransactionService;
import com.kh.auth2andkhqr.dto.IndividualInfoDTO;
import com.kh.auth2andkhqr.qrservice.QrGeneratedService;

import jakarta.validation.Valid;
import kh.org.nbc.bakong_khqr.model.CRCValidation;
import kh.org.nbc.bakong_khqr.model.KHQRData;
import kh.org.nbc.bakong_khqr.model.KHQRResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class KhqrController {
	
	private final QrGeneratedService generatedService;
	private final TransactionService transactionService;

	@PostMapping("/getqr")
	public ResponseEntity<KHQRResponse<KHQRData>> getQrData(@RequestBody IndividualInfoDTO dto){
		KHQRResponse<KHQRData> qrData = generatedService.createQr(dto);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(qrData);
	}
	
	@GetMapping("/validateqr")
	public ResponseEntity<KHQRResponse<CRCValidation>> isValidate(@RequestBody String qr){
		KHQRResponse<CRCValidation> validKHQR = generatedService.validKHQR(qr);
		return ResponseEntity.status(HttpStatus.OK).body(validKHQR);
	}
	
   
    @PostMapping("/check_transaction_by_md5")
    public ResponseEntity<CheckTransactionResponse> checkTransactionByMd5(
//    		@RequestHeader("Authorization") String authorization,
            @Valid @RequestBody CheckTransactionByMd5Request request) {	
        
        CheckTransactionResponse response = transactionService.checkTransactionByMd5(request);
        return ResponseEntity.ok(response);
    }
	
}
