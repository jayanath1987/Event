package lk.icta.events.eventAttendance.view.invitation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lk.icta.events.eventAttendance.service.dto.benefiary.BenefiarySearchDto;
import lk.icta.events.eventAttendance.service.dto.districts.DistrictDto;
import lk.icta.events.eventAttendance.service.dto.divinagumaZone.DivinagumaZoneDto;
import lk.icta.events.eventAttendance.service.dto.divisionalSecDivision.DivisionalSecDivisionDto;
import lk.icta.events.eventAttendance.service.dto.gnd.GNDDto;
import lk.icta.events.eventAttendance.service.dto.province.ProvinceDto;
import lk.icta.events.eventAttendance.service.dto.registration.InviteeDto;
import lk.icta.events.eventAttendance.service.dto.user.UserSettingsDto;
import lk.icta.events.eventAttendance.service.process.benefiary.BenefiaryProcess;
import lk.icta.events.eventAttendance.service.process.benefiary.SocialogicalInfoProcess;
import lk.icta.events.eventAttendance.service.process.invitee.InviteeProcess;
import lk.icta.events.eventAttendance.service.process.master.MasterDataProcess;
import lk.icta.events.eventAttendance.service.process.user.MessageException;
import lk.icta.events.eventAttendance.service.process.user.UserDetailsProcess;
import lk.icta.events.eventAttendance.service.process.utill.ConfigurationUtil;
import lk.icta.events.eventAttendance.view.AbstractView;
import lk.icta.events.eventAttendance.view.UIVariables;

@Component
@Scope("view")
public class SendInvitaionsView extends AbstractView {

	private final static Logger logger = Logger.getLogger(SendInvitaionsView.class);
	private static final long serialVersionUID = 1L;

	@Autowired
	private InviteeProcess inviteeProcess;

	@Autowired
	private UserDetailsProcess userDetailsProcess;

	private Collection<InviteeDto> inviteeList = new ArrayList();
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

	public SendInvitaionsView() {

	}

	public void changeLanguage() {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put(UIVariables.PARAM_SELECTED_LANGUAGE, "si");

		this.selectedLanguage = ConfigurationUtil.LANGUAGES_SINHALA;

		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("si"));
		logger.info("Change Language isto Sinhala");
	}

	public void populateAll() {
		try {
			inviteeList = inviteeProcess.getByStatus(0);

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

	public void sendInvitations(InviteeDto inviteeDto) {
		try {

			// Map<String, String> params =
			// FacesContext.getCurrentInstance().getExternalContext()
			// .getRequestParameterMap();

			// long action = Long.parseLong(params.get("id"));

			inviteeProcess.invite(inviteeDto.getId());
			inviteeList.remove(inviteeDto);
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Invitaions Sending", "Sent Invitaion Successfully"));

		} catch (MessageException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Load Error", e.getMessage()));
			e.printStackTrace();
		}
	}

}
