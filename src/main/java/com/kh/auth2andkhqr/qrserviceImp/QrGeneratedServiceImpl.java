package com.kh.auth2andkhqr.qrserviceImp;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kh.auth2andkhqr.dto.IndividualInfoDTO;
import com.kh.auth2andkhqr.qrservice.QrGeneratedService;

import jakarta.annotation.PostConstruct;
import kh.org.nbc.bakong_khqr.BakongKHQR;
import kh.org.nbc.bakong_khqr.model.IndividualInfo;
import kh.org.nbc.bakong_khqr.model.KHQRCurrency;
import kh.org.nbc.bakong_khqr.model.KHQRData;
import kh.org.nbc.bakong_khqr.model.KHQRResponse;

@Service
public class QrGeneratedServiceImpl implements QrGeneratedService{
	
	@Value("${BAKONG_TOKEN}")
	private String token;
	
	@PostConstruct
	private String getBakongToken() {
		return this.token;
	}
	
	@Override
	public KHQRResponse<KHQRData> createQr(IndividualInfoDTO dto) {
		return BakongKHQR.generateIndividual(toIndividualInfo(dto));
	}
	
	private IndividualInfo toIndividualInfo(IndividualInfoDTO dto) {
		if(dto == null) return null;
		
		IndividualInfo info = new IndividualInfo();
		
		info.setBakongAccountId(dto.getBakongAccountId());
		if(dto.getCurrency() != null) info.setCurrency(toKHQRCurrency(dto.getCurrency()));
		info.setAmount(dto.getAmount());
		info.setMerchantName(dto.getMerchantName());
		info.setMerchantCity("Phnom Penh");
		info.setBillNumber(generatedBillNumber(dto.getStoreLabel()));
		info.setStoreLabel(dto.getStoreLabel());
		info.setTerminalLabel(dto.getTerminalLabel());
		info.setMobileNumber(dto.getMobileNumber());
		info.setAccountInformation(dto.getMobileNumber());
		info.setAcquiringBank(dto.getAcquiringBank());
		
		return info;
	}
	
	private String generatedBillNumber(String head) {
		return head.replace(" ", "") + RandomStringUtils.randomAlphanumeric(10);
	}
	
	private KHQRCurrency toKHQRCurrency(String value) {
		value.trim();
		KHQRCurrency currency = KHQRCurrency.KHR;
		
		if(value.equals("USD")) {
			currency = KHQRCurrency.USD;
		}
		return currency;
	}
	
}
