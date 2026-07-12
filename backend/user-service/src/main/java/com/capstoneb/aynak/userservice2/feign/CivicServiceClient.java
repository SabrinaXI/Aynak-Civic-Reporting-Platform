package com.capstoneb.aynak.userservice2.feign;

import com.capstoneb.aynak.userservice2.dto.ReportCountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("CIVIC-SERVICE2")
public interface CivicServiceClient {

	@GetMapping("/internal/reports/approved-counts")
	List<ReportCountDTO> getApprovedCounts();
}
