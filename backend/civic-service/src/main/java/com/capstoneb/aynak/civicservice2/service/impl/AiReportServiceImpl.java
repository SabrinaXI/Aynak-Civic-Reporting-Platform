package com.capstoneb.aynak.civicservice2.service.impl;

import com.capstoneb.aynak.civicservice2.dto.AiReportAnalysisDTO;
import com.capstoneb.aynak.civicservice2.service.AiReportService;

import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class AiReportServiceImpl implements AiReportService {

	@Value("${gemini.api.key}")
	private String geminiApiKey;

	@Value("${gemini.model}")
	private String geminiModel;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public AiReportAnalysisDTO analyzeReportImage(MultipartFile photo) throws IOException {

		AiReportAnalysisDTO aiResult = analyzeImageWithGemini(photo);
		extractGpsFromImage(photo, aiResult);
		return aiResult;
	}

	private AiReportAnalysisDTO analyzeImageWithGemini(MultipartFile photo) throws IOException {

		String base64Image = Base64.getEncoder().encodeToString(photo.getBytes());
		String prompt = """
				You are helping a UAE civic incident reporting app called Aynak.

				Analyze this image and return ONLY valid JSON.
				Do not include markdown.
				Do not include explanations.

				Choose category only from:
				Road Damage,
				Waste / Cleanliness,
				Street Light,
				Water Leak,
				Public Safety,
				Other

				JSON format:
				{
				  "title": "short incident title",
				  "description": "clear incident description",
				  "category": "one category from the list"
				}
				""";

		Map<String, Object> textPart = Map.of("text", prompt);
		Map<String, Object> imagePart = Map.of("inline_data", Map.of("mime_type", photo.getContentType(), "data", base64Image));
		Map<String, Object> content = Map.of("parts", List.of(textPart, imagePart));
		Map<String, Object> requestBody = Map.of("contents", List.of(content));

		String url = "https://generativelanguage.googleapis.com/v1beta/models/"
				+ geminiModel
				+ ":generateContent?key="
				+ geminiApiKey;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

		return parseGeminiResponse(response.getBody());
	}

	private AiReportAnalysisDTO parseGeminiResponse(String responseBody) throws IOException {

		JsonNode root = objectMapper.readTree(responseBody);

		String text = root
				.path("candidates")
				.get(0)
				.path("content")
				.path("parts")
				.get(0)
				.path("text")
				.asText();

		text = text
				.replace("```json", "")
				.replace("```", "")
				.trim();

		AiReportAnalysisDTO dto = objectMapper.readValue(text, AiReportAnalysisDTO.class);

		dto.setLocationDetected(false);

		return dto;
	}

	private void extractGpsFromImage(MultipartFile photo, AiReportAnalysisDTO dto) {

		try {
			Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(photo.getBytes()));

			GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);

			if (gpsDirectory != null) {
				GeoLocation geoLocation = gpsDirectory.getGeoLocation();

				if (geoLocation != null && !geoLocation.isZero()) {
					dto.setLatitude(geoLocation.getLatitude());
					dto.setLongitude(geoLocation.getLongitude());
					dto.setLocationDetected(true);
					dto.setLocationName("Location detected from image metadata");
				}
			}

		} catch (Exception error) {
			dto.setLocationDetected(false);
		}
	}
}
