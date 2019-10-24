package lk.icta.events.eventAttendance.view;

import java.util.Locale;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("view")
public class MasterBean implements InterfaceView {
	private final static Logger logger = Logger.getLogger(MasterBean.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 5411132867192790989L;

	public void changeLanguageToSinhala() {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put(UIVariables.PARAM_SELECTED_LANGUAGE, "si");
		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("si"));
		logger.info("Change Language to Sinhala");
	}

	public void changeLanguageToTamil() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();

		sessionMap.put(UIVariables.PARAM_SELECTED_LANGUAGE, "ta");

		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("ta"));
		logger.info("Change Language to Sinhala");
	}

	public void changeLanguageToEnglish() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put(UIVariables.PARAM_SELECTED_LANGUAGE, "en");

		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("en"));
		logger.info("Change Language to English");
	}

}
