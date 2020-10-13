package com.idiotBox.service;

import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import com.idiotBox.pojos.Video;

public interface VideoService {
	HttpEntity<MultiValueMap<String, Object>> fileToResource(MultipartFile file, Video video) throws IOException;
}