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
import lk.icta.events.eventAttendance.service.dto.benefiary.SocialogicalInfoDto;
import lk.icta.events.eventAttendance.service.process.benefiary.AverageMonthlyIncomeProcess;
import lk.icta.events.eventAttendance.service.process.benefiary.SocialogicalInfoProcess;
import lk.icta.events.eventAttendance.view.AbstractView;
import lk.icta.events.eventAttendance.view.UIVariables;

@Component
@Scope("view")
public class AverageMonthlyIncomeView extends AbstractView {
	private final static Logger logger = Logger.getLogger(AverageMonthlyIncomeView.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -2803601327236900353L;

	@Autowired
	private AverageMonthlyIncomeProcess averageMonthlyIncomeProcess;

	@Autowired
	private SocialogicalInfoProcess socialogicalInfoProcess;

	private long benifiaryID;
	private DualListModel<SocialogicalInfoDto> memberModel = new DualListModel<SocialogicalInfoDto>(
			new ArrayList<SocialogicalInfoDto>(), new ArrayList<SocialogicalInfoDto>());

	private ArrayList<SocialogicalInfoDto> rightSocialogicalInfo = new ArrayList<SocialogicalInfoDto>();
	private ArrayList<SocialogicalInfoDto> leftSocialogicalInfo = new ArrayList<SocialogicalInfoDto>();
	private Collection<AverageMonthlyIncomeDto> averageMonthlyIncome = new ArrayList<AverageMonthlyIncomeDto>();
	private String uiMode = null;

	@PostConstruct
	public void init() {

		benifiaryID = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get(UIVariables.PARAM_BENEFICARY_ID));
		rightSocialogicalInfo = new ArrayList<SocialogicalInfoDto>(
				averageMonthlyIncomeProcess.listMembersNotContributeingToIncome(benifiaryID));
		averageMonthlyIncome = averageMonthlyIncomeProcess.listByBenefiary(benifiaryID);
		if (averageMonthlyIncome.size() == 0) {
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
			AverageMonthlyIncomeDto averageMonthlyIncomeDto = new AverageMonthlyIncomeDto(itr.next());
			averageMonthlyIncomeDto.getMember()
					.setName(socialogicalInfoProcess.findNameByID(averageMonthlyIncomeDto.getMember().getId()));
			averageMonthlyIncome.add(averageMonthlyIncomeDto);
		}
		memberModel.getTarget().clear();
	}

	public void populateMemberModel() {
		logger.debug("*******Start populateMemberModel benifiaryID - " + benifiaryID);
		memberModel = new DualListModel<SocialogicalInfoDto>(rightSocialogicalInfo, leftSocialogicalInfo);
		logger.debug("*******Start populateMemberModel benifiaryID - " + benifiaryID);
	}

	public String save() {
		String returnURL = "#";
		if (uiMode.equals(UIVariables.UI_MODES[0])) {
			averageMonthlyIncomeProcess.saveList(averageMonthlyIncome, benifiaryID);
			returnURL = "BISavings.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + benifiaryID;
		} else if (uiMode.equals(UIVariables.UI_MODES[1])) {
			// socialogicalInfoProcess.de(benefiaryDto.getSocialogicalInfo(),
			// benifiaryID);
			averageMonthlyIncomeProcess.updateList(averageMonthlyIncome, benifiaryID);
			returnURL = "BISavings.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + benifiaryID
					+ "&" + UIVariables.PARAM_MODE + "=" + uiMode;
		} else {
			returnURL = "BISavings.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + benifiaryID
					+ "&" + UIVariables.PARAM_MODE + "=" + uiMode;
		}
		return returnURL;
	}

	public void removeItem(AverageMonthlyIncomeDto averageMonthlyIncomeDto) {

		try {
			memberModel.getSource().add(averageMonthlyIncomeDto.getMember());
			averageMonthlyIncome.remove(averageMonthlyIncomeDto);
		} catch (Exception ex) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Remove Error", ex.getMessage()));
			logger.error("Error Removing Item", ex);
		}

	}

	public Collection<AverageMonthlyIncomeDto> getAverageMonthlyIncome() {
		return averageMonthlyIncome;
	}

	public void setAverageMonthlyIncome(Collection<AverageMonthlyIncomeDto> averageMonthlyIncome) {
		this.averageMonthlyIncome = averageMonthlyIncome;
	}

	public String getNavParam() {
		return "action=".concat(uiMode).concat("&beneficary=").concat(Long.toString(benifiaryID));
	}
}
