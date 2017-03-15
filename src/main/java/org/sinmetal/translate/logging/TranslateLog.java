package org.sinmetal.translate.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TranslateLog {

	public String email;

	public String message;

	public TranslateLog(String email, String message) {
		this.email = email;
		this.message = message;
	}

	public static String create(String email, String message) throws JsonProcessingException {
		TranslateLog log = new TranslateLog(email, message);
		LogContainer container = new LogContainer("__TRANSLATE_LOG__", log);

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(container);
	}
}
