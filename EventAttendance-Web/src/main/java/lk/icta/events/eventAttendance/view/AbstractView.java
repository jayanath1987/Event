package lk.icta.events.eventAttendance.view;

import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lk.icta.events.eventAttendance.service.dto.user.UserDetailsDto;
import lk.icta.events.eventAttendance.service.process.utill.ConfigurationUtil;

public abstract class AbstractView implements InterfaceView {
	private UserDetailsDto userDetails;

	protected String selectedLanguage = ConfigurationUtil.LANGUAGES_SINHALA;
	/**
	 * This is common abstract class for all the GUI Classes In here initiate
	 * user details
	 */
	private static final long serialVersionUID = 1L;

	public UserDetailsDto getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetailsDto userDetails) {
		this.userDetails = userDetails;
	}

	@PostConstruct
	private void getCurrentUser() {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		Object language=sessionMap.get(UIVariables.PARAM_SELECTED_LANGUAGE);
		
		if(language!= null && language.equals("si")){
			FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("si"));
			this.selectedLanguage = ConfigurationUtil.LANGUAGES_SINHALA;
		}else if(language!= null && language.equals("ta")){
			FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("ta"));
			this.selectedLanguage = ConfigurationUtil.LANGUAGES_TAMIL;
		}else{
			FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("en"));
			this.selectedLanguage = ConfigurationUtil.LANGUAGES_ENGLISH;
		}
		
		//sessionMap.put("somekey", yourVariable);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			userDetails = (UserDetailsDto) auth.getPrincipal();
		}
	}

	

}
