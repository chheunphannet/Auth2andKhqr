package com.kh.auth2andkhqr.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.auth2andkhqr.dto.IndividualInfoDTO;
import com.kh.auth2andkhqr.qrservice.QrGeneratedService;

import kh.org.nbc.bakong_khqr.model.KHQRData;
import kh.org.nbc.bakong_khqr.model.KHQRResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class KhqrController {
	
	private final QrGeneratedService generatedService;
	
	@PostMapping("/getqr")
	public ResponseEntity<KHQRResponse<KHQRData>> getQrData(@RequestBody IndividualInfoDTO dto){
		KHQRResponse<KHQRData> qrData = generatedService.createQr(dto);
		if(qrData.getKHQRStatus().getCode() == 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body(qrData);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(qrData);
	}
	
}
