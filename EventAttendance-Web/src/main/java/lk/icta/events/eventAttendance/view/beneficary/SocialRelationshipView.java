package lk.icta.events.eventAttendance.view.beneficary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lk.icta.events.eventAttendance.service.dto.benefiary.SocialRelationshipDto;
import lk.icta.events.eventAttendance.service.dto.benefiary.SocialogicalInfoDto;
import lk.icta.events.eventAttendance.service.process.benefiary.SocialRelationshipProcess;
import lk.icta.events.eventAttendance.service.process.benefiary.SocialogicalInfoProcess;
import lk.icta.events.eventAttendance.view.AbstractView;
import lk.icta.events.eventAttendance.view.UIVariables;

@Component
@Scope("view")
public class SocialRelationshipView extends AbstractView {
	private final static Logger logger = Logger.getLogger(SocialRelationshipView.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -2803601327236900353L;

	@Autowired
	private SocialRelationshipProcess socialRelationshipProcess;

	@Autowired
	private SocialogicalInfoProcess socialogicalInfoProcess;

	private long benifiaryID;
	private DualListModel<SocialogicalInfoDto> memberModel = new DualListModel<SocialogicalInfoDto>(
			new ArrayList<SocialogicalInfoDto>(), new ArrayList<SocialogicalInfoDto>());

	private ArrayList<SocialogicalInfoDto> rightSocialogicalInfo = new ArrayList<SocialogicalInfoDto>();
	private ArrayList<SocialogicalInfoDto> leftSocialogicalInfo = new ArrayList<SocialogicalInfoDto>();
	private Collection<SocialRelationshipDto> socialRelationship = new ArrayList<SocialRelationshipDto>();
	private String uiMode = null;

	@PostConstruct
	public void init() {
		benifiaryID = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get(UIVariables.PARAM_BENEFICARY_ID));

		rightSocialogicalInfo = new ArrayList<SocialogicalInfoDto>(
				socialRelationshipProcess.listMembersNotContributeingToSocialRelationship(benifiaryID));
		socialRelationship = socialRelationshipProcess.listByBenefiary(benifiaryID);

		if (socialRelationship.size() == 0) {
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.containsKey(UIVariables.PARAM_MODE))
				uiMode = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
						.get(UIVariables.PARAM_MODE);
			else
				uiMode = UIVariables.UI_MODES[0];
		} else if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.containsKey(UIVariables.PARAM_MODE)) {
			uiMode = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.get(UIVariables.PARAM_MODE);
		} else {
			uiMode = UIVariables.UI_MODES[3];

		}

		populateMemberModel();
	}

	public void populateMemberModel() {
		logger.debug("*******Start populateMemberModel benifiaryID - " + benifiaryID);
		memberModel = new DualListModel<SocialogicalInfoDto>(rightSocialogicalInfo, leftSocialogicalInfo);
		logger.debug("*******Start populateMemberModel benifiaryID - " + benifiaryID);
	}

	public Collection<SocialRelationshipDto> getSocialRelationship() {
		return socialRelationship;
	}

	public void setSocialRelationship(Collection<SocialRelationshipDto> socialRelationship) {
		this.socialRelationship = socialRelationship;
	}

	public String getUiMode() {
		return uiMode;
	}

	public void setUiMode(String uiMode) {
		this.uiMode = uiMode;
	}

	public void addToItemList() {
		Iterator<SocialogicalInfoDto> itr = memberModel.getTarget().iterator();
		while (itr.hasNext()) {
			SocialRelationshipDto socialRelationshipDto = new SocialRelationshipDto(itr.next());
			socialRelationshipDto.getMember()
					.setName(socialogicalInfoProcess.findNameByID(socialRelationshipDto.getMember().getId()));
			socialRelationship.add(socialRelationshipDto);
		}
		memberModel.getTarget().clear();
	}

	public void removeItem(SocialRelationshipDto socialRelationshipDto) {

		try {
			memberModel.getSource().add(socialRelationshipDto.getMember());
			socialRelationship.remove(socialRelationshipDto);
		} catch (Exception ex) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Remove Error", ex.getMessage()));
			logger.error("Error Removing Item", ex);
		}
	}

	public String save() {
		String returnURL;
		if (uiMode.equals(UIVariables.UI_MODES[0])) {
			socialRelationshipProcess.saveList(socialRelationship, benifiaryID);
			returnURL = "BIOtherInfo.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + benifiaryID
					+ "&" + UIVariables.PARAM_MODE + "=" + uiMode;
		} else if (uiMode.equals(UIVariables.UI_MODES[1])) {
			socialRelationshipProcess.updateList(socialRelationship, benifiaryID);
			returnURL = "BIOtherInfo.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + benifiaryID
					+ "&" + UIVariables.PARAM_MODE + "=" + uiMode;
		} else {
			returnURL = "BIOtherInfo.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + benifiaryID
					+ "&" + UIVariables.PARAM_MODE + "=" + uiMode;
		}
		return returnURL;
	}

	public DualListModel<SocialogicalInfoDto> getMemberModel() {
		return memberModel;
	}

	public void setMemberModel(DualListModel<SocialogicalInfoDto> memberModel) {
		this.memberModel = memberModel;
	}

	public String getNavParam() {
		return "action=".concat(uiMode).concat("&beneficary=").concat(Long.toString(benifiaryID));
	}
}
