package lk.icta.events.eventAttendance.view.invitation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.aspectj.weaver.tools.cache.AsynchronousFileCacheBacking.RemoveCommand;
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
public class AttendanceView extends AbstractView {

	private final static Logger logger = Logger.getLogger(AttendanceView.class);
	private static final long serialVersionUID = 1L;

	@Autowired
	private InviteeProcess inviteeProcess;
	
	@Autowired
	private UserDetailsProcess userDetailsProcess;

	private Collection<InviteeDto> inviteeList = new ArrayList();
	
	private InviteeDto selectedAttendace = new InviteeDto();
	private Collection<InviteeDto> filterdInviteeList = new ArrayList();
	private UserSettingsDto defaultSettings = new UserSettingsDto();

	public Collection<InviteeDto> getFilterdInviteeList() {
		return filterdInviteeList;
	}

	public void setFilterdInviteeList(Collection<InviteeDto> filterdInviteeList) {
		this.filterdInviteeList = filterdInviteeList;
	}

	@PostConstruct
	public void init() {
		try {

			populateAll();
			UserSettingsDto userSettings = userDetailsProcess.findDefaultSeByID(getUserDetails().getUsername(),
					selectedLanguage);
			if (userSettings != null) {
				logger.info("User Settings Exsists. Populate GND");
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

	public AttendanceView() {

	}
	public void markAttendace() {
		try {
			
			if(selectedAttendace.getId()!=0){
				inviteeProcess.attendance(selectedAttendace.getId());
				inviteeList.remove(selectedAttendace);
				selectedAttendace= new InviteeDto(); 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Thank you !",
						"Attendance Details Successfuly Updated"));
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Participant's",
						"Please Select Participant"));
			}
			
			
			

		} catch (MessageException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save Error", e.getMessage()));
		}
	}

	public void changeLanguage() {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put(UIVariables.PARAM_SELECTED_LANGUAGE, "si");

		this.selectedLanguage = ConfigurationUtil.LANGUAGES_SINHALA;

		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("si"));
		logger.info("Change Language to Sinhala");
	}

	public void populateAll() {
		try {
			inviteeList = inviteeProcess.getByStatus(1);

		} catch (MessageException e) {
			logger.error(e);
		}
	}

	public Collection<InviteeDto> getInviteeList() {
		return inviteeList;
	}

	public void setInviteeList(Collection<InviteeDto> inviteeList) {
		this.inviteeList = inviteeList;
	}

	public InviteeDto getSelectedAttendace() {
		return selectedAttendace;
	}

	public void setSelectedAttendace(InviteeDto selectedAttendace) {
		this.selectedAttendace = selectedAttendace;
	}

	
	
}
