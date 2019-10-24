package lk.icta.events.eventAttendance.view.registration;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lk.icta.events.eventAttendance.service.dto.registration.InviteeDto;
import lk.icta.events.eventAttendance.service.dto.user.UserSettingsDto;
import lk.icta.events.eventAttendance.service.process.invitee.InviteeProcess;
import lk.icta.events.eventAttendance.service.process.user.MessageException;
import lk.icta.events.eventAttendance.service.process.user.UserDetailsProcess;
import lk.icta.events.eventAttendance.service.process.utill.ConfigurationUtil;
import lk.icta.events.eventAttendance.view.AbstractView;
import lk.icta.events.eventAttendance.view.UIVariables;

@Component
@Scope("view")
public class RegistrationAttendanceView  extends AbstractView { 
	protected String selectedLanguage = ConfigurationUtil.LANGUAGES_SINHALA;
	
	/**
	 * User Login Management
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(RegistrationAttendanceView.class);
	private InviteeDto inviteeDto = new InviteeDto();
	
	private String uiMode = UIVariables.UI_MODES[0];

	private UserSettingsDto defaultSettings = new UserSettingsDto();

	@PostConstruct
	public void init() {
		try {
			UserSettingsDto userSettings = userDetailsProcess.findDefaultSeByID(getUserDetails().getUsername(),
					selectedLanguage);
			if (userSettings != null) {
				defaultSettings = userSettings;
			} else {
				defaultSettings.setUserDetails(getUserDetails());
				logger.info("User Settings Not Exsists");
			}
		} catch (NumberFormatException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Load Error", e.getMessage()));
			logger.error("Error Loading Details ", e);
		}
	}

	@Autowired
	private UserDetailsProcess userDetailsProcess;


	public String getUiMode() {
		return uiMode;
	}

	public void setUiMode(String uiMode) {
		this.uiMode = uiMode;
	}

	@Autowired
	private InviteeProcess inviteeProcess;

	public RegistrationAttendanceView() {

	}

	public void saveAndNew() {
		
		try {
			inviteeProcess.registerAttendace(inviteeDto);
			
			inviteeDto = new InviteeDto();
			uiMode = UIVariables.UI_MODES[0];
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Thank you !",
					"Participant's Details has been successfuly registered. Please enter new participant's details"));

		} catch (MessageException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save Error", e.getMessage()));
		}
	}

	public InviteeDto getInviteeDto() {
		return inviteeDto;
	}

	public void setInviteeDto(InviteeDto inviteeDto) {
		this.inviteeDto = inviteeDto;
	}
	
	

}
