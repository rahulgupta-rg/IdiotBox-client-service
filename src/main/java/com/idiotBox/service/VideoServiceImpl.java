package com.idiotBox.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import com.idiotBox.pojos.Video;

@Service
public class VideoServiceImpl implements VideoService {

	@Override
	public HttpEntity<MultiValueMap<String, Object>> fileToResource(MultipartFile file, Video video) throws IOException {
		
		String contentType = file.getContentType();
		contentType = contentType.split("/")[1];
		
		// MultipartFile to Resource
		Path tempFile = Files.createTempFile(video.getVideoName(), "." + contentType);
		Files.write(tempFile, file.getBytes());
		FileSystemResource theFile = new FileSystemResource(tempFile.toFile());

		// Header
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		// MultiValueMap
		MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
		requestBody.add("file", theFile);

		// HttpEntity
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
		return requestEntity;
	}

}
