package lk.icta.events.eventAttendance.view.registration;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lk.icta.events.eventAttendance.service.dto.registration.InviteeDto;
import lk.icta.events.eventAttendance.service.process.invitee.InviteeProcess;
import lk.icta.events.eventAttendance.service.process.user.MessageException;
import lk.icta.events.eventAttendance.view.InterfaceView;
import lk.icta.events.eventAttendance.view.UIVariables;

@Component
@Scope("view")
public class RegistrationView implements InterfaceView {
	/**
	 * User Login Management
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(RegistrationView.class);
	private InviteeDto inviteeDto = new InviteeDto();
	
	private int requestCount=0;
	private String uiMode = UIVariables.UI_MODES[0];

	public int getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(int requestCount) {
		this.requestCount = requestCount;
	}

	public String getUiMode() {
		return uiMode;
	}

	public void setUiMode(String uiMode) {
		this.uiMode = uiMode;
	}

	@Autowired
	private InviteeProcess inviteeProcess;

	public RegistrationView() {

	}

	public void save() {
		try {
			inviteeProcess.create(inviteeDto);
			uiMode = UIVariables.UI_MODES[3];
			requestCount++;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Thank you !",
					"Your Request has been successfuly registered"));

		} catch (MessageException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save Error", e.getMessage()));
		}
	}

	public void subscribeNew() {
		
		try {
			inviteeProcess.create(inviteeDto);
			requestCount++;
			
			
			InviteeDto tempObj = new InviteeDto();
			tempObj.setOrganization(inviteeDto.getOrganization());
			tempObj.setOfficeAddress1(inviteeDto.getOfficeAddress1());
			tempObj.setOfficeAddress2(inviteeDto.getOfficeAddress2());
			tempObj.setOfficeAddress3(inviteeDto.getOfficeAddress3());
			tempObj.setWorkPhone(inviteeDto.getWorkPhone());
			inviteeDto = tempObj;
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
