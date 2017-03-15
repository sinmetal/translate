package org.sinmetal.translate.controller.api.v1;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.sinmetal.translate.form.TranslateForm;
import org.sinmetal.translate.logging.TranslateLog;
import org.slim3.controller.Navigation;
import org.slim3.controller.SimpleController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.cloud.RetryParams;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class TranslateController extends SimpleController {

	Logger logger = Logger.getLogger(TranslateController.class.getSimpleName());
	
	final UserService userService = UserServiceFactory.getUserService();

	@Override
	protected Navigation run() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		
		TranslateForm form;
		try {
			form = objectMapper.readValue(request.getInputStream(), TranslateForm.class);
		} catch (Throwable e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		String log = TranslateLog.create(userService.getCurrentUser().getEmail(), form.message);
		logger.info(log);
		
		final String res = translateText(form.message, form.sourceLanguage, form.targetLanguage);

		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().print(String.format("{\"translatedText\": \"%s\"}", res));
		response.flushBuffer();

		return null;
	}

	/**
	 * Translates the source text
	 *
	 * @param sourceText
	 *            source text to be translated
	 * @param out
	 *            print stream
	 */
	public static String translateText(String sourceText, String sourceLanguage, String targetLanguage) {
		Translate translate = createTranslateService();
		Translation translation = translate.translate(sourceText, TranslateOption.sourceLanguage(sourceLanguage),
				TranslateOption.targetLanguage(targetLanguage));
		return translation.getTranslatedText();
	}

	/**
	 * Create Google Translate API Service.
	 *
	 * @return Google Translate Service
	 */
	public static Translate createTranslateService() {
		TranslateOptions translateOption = TranslateOptions.newBuilder().setRetryParams(retryParams())
				.setConnectTimeout(60000).setReadTimeout(60000).build();
		return translateOption.getService();
	}

	/**
	 * Retry params for the Translate API.
	 */
	private static RetryParams retryParams() {
		return RetryParams.newBuilder().setRetryMaxAttempts(3).setMaxRetryDelayMillis(30000)
				.setTotalRetryPeriodMillis(120000).setInitialRetryDelayMillis(250).build();
	}
}
