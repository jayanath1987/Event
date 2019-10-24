package lk.icta.events.eventAttendance.view.readQR;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lk.icta.events.eventAttendance.service.dto.registration.InviteeDto;
import lk.icta.events.eventAttendance.service.dto.user.UserSettingsDto;
import lk.icta.events.eventAttendance.service.entities.Invitee;
import lk.icta.events.eventAttendance.service.process.invitee.InviteeProcess;
import lk.icta.events.eventAttendance.service.process.user.MessageException;
import lk.icta.events.eventAttendance.service.process.utill.ConfigurationUtil;
import lk.icta.events.eventAttendance.view.InterfaceView;
import lk.icta.events.eventAttendance.view.UIVariables;

@Component
@Scope("view")
public class QrReadView implements InterfaceView {
	/**
	 * User Login Management
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(QrReadView.class);
	private InviteeDto inviteeDto = new InviteeDto();
	private long id;

	private String message = "";

	@Autowired
	private InviteeProcess inviteeProcess;

	@PostConstruct
	public void init() {
		try {

			Map<String, String> parameterMap = (Map<String, String>) FacesContext.getCurrentInstance()
					.getExternalContext().getRequestParameterMap();
			String param = parameterMap.get("reqID");
			id = Long.parseLong(param);
			try {
				inviteeDto = inviteeProcess.findByID(id);
				inviteeProcess.attendance(id,inviteeDto.getStatus());
				message = "Attendance Request Successfully Saved";
			} catch (MessageException e) {
				
				message = e.getMessage();
			}
		} catch (Exception e) {
			message = "Not a Valid Invitee";
		}

	}

	public InviteeDto getInviteeDto() {
		return inviteeDto;
	}

	public void setInviteeDto(InviteeDto inviteeDto) {
		this.inviteeDto = inviteeDto;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
