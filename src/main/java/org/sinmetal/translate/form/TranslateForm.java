package org.sinmetal.translate.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TranslateForm {
	public String message;
	public String sourceLanguage;
	public String targetLanguage;
}
