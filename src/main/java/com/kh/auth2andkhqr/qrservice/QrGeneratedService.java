package com.kh.auth2andkhqr.qrservice;

import com.kh.auth2andkhqr.dto.IndividualInfoDTO;

import kh.org.nbc.bakong_khqr.model.KHQRData;
import kh.org.nbc.bakong_khqr.model.KHQRResponse;

public interface QrGeneratedService {
	public KHQRResponse<KHQRData> createQr(IndividualInfoDTO dto);
}
