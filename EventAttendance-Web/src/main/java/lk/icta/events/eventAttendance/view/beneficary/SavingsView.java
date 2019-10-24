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

import lk.icta.events.eventAttendance.service.dto.benefiary.AverageMonthlyIncomeDto;
import lk.icta.events.eventAttendance.service.dto.benefiary.SavingsDto;
import lk.icta.events.eventAttendance.service.dto.benefiary.SocialogicalInfoDto;
import lk.icta.events.eventAttendance.service.process.benefiary.SavingsProcess;
import lk.icta.events.eventAttendance.service.process.benefiary.SocialogicalInfoProcess;
import lk.icta.events.eventAttendance.view.AbstractView;
import lk.icta.events.eventAttendance.view.UIVariables;

@Component
@Scope("view")
public class SavingsView extends AbstractView {
	private final static Logger logger = Logger.getLogger(SavingsView.class);
	private static final long serialVersionUID = -2803601327236900353L;

	@Autowired
	private SavingsProcess savingsProcess;
	@Autowired
	private SocialogicalInfoProcess socialogicalInfoProcess;

	private long benifiaryID;
	private DualListModel<SocialogicalInfoDto> memberModel = new DualListModel<SocialogicalInfoDto>(
			new ArrayList<SocialogicalInfoDto>(), new ArrayList<SocialogicalInfoDto>());

	private ArrayList<SocialogicalInfoDto> rightSocialogicalInfo = new ArrayList<SocialogicalInfoDto>();
	private ArrayList<SocialogicalInfoDto> leftSocialogicalInfo = new ArrayList<SocialogicalInfoDto>();

	private Collection<SavingsDto> savings = new ArrayList<SavingsDto>();
	private String uiMode = UIVariables.UI_MODES[0];

	@PostConstruct
	public void init() {
		benifiaryID = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get(UIVariables.PARAM_BENEFICARY_ID));

		rightSocialogicalInfo = new ArrayList<SocialogicalInfoDto>(
				savingsProcess.listMembersNotContributeingToSavings(benifiaryID));
		savings = savingsProcess.listByBenefiary(benifiaryID);

		if (savings.size() == 0) {
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

	public String getUiMode() {
		return uiMode;
	}

	public void setUiMode(String uiMode) {
		this.uiMode = uiMode;
	}

	public DualListModel<SocialogicalInfoDto> getMemberModel() {
		return memberModel;
	}

	public void setMemberModel(DualListModel<SocialogicalInfoDto> memberModel) {
		this.memberModel = memberModel;
	}

	public void addToItemList() {
		Iterator<SocialogicalInfoDto> itr = memberModel.getTarget().iterator();
		while (itr.hasNext()) {
			SavingsDto savingsDto = new SavingsDto(itr.next());
			savingsDto.getMember().setName(socialogicalInfoProcess.findNameByID(savingsDto.getMember().getId()));
			savings.add(savingsDto);
		}
		memberModel.getTarget().clear();
	}

	public String save() {
		String returnURL = "#";
		if (uiMode.equals(UIVariables.UI_MODES[0])) {
			savingsProcess.saveList(savings, benifiaryID);
			returnURL = "BIAssets.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + benifiaryID;
		} else if (uiMode.equals(UIVariables.UI_MODES[1])) {
			savingsProcess.updateList(savings, benifiaryID);
			returnURL = "BIAssets.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + benifiaryID
					+ "&" + UIVariables.PARAM_MODE + "=" + uiMode;
		} else {
			returnURL = "BIAssets.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + benifiaryID
					+ "&" + UIVariables.PARAM_MODE + "=" + uiMode;
		}
		return returnURL;
	}
	public void removeItem(SavingsDto savingsDto) {

		try {
			memberModel.getSource().add(savingsDto.getMember());
			savings.remove(savingsDto);
		} catch (Exception ex) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Remove Error", ex.getMessage()));
			logger.error("Error Removing Item", ex);
		}
	}

	public Collection<SavingsDto> getSavings() {
		return savings;
	}

	public void setSavings(Collection<SavingsDto> savings) {
		this.savings = savings;
	}
	public String getNavParam() {
		return "action=".concat(uiMode).concat("&beneficary=").concat(Long.toString(benifiaryID));
	}
}
