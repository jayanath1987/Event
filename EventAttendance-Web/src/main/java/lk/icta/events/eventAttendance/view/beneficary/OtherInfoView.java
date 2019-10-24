package lk.icta.events.eventAttendance.view.beneficary;

import java.util.Collection;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lk.icta.events.eventAttendance.service.dto.benefiary.BeneficiaryOtherInfoDto;
import lk.icta.events.eventAttendance.service.dto.benefiary.ProposalForEnhancementDto;
import lk.icta.events.eventAttendance.service.dto.common.SelectionCodeDto;
import lk.icta.events.eventAttendance.service.process.benefiary.OtherInfoProcess;
import lk.icta.events.eventAttendance.service.process.master.MasterDataProcess;
import lk.icta.events.eventAttendance.service.process.utill.ConfigurationUtil;
import lk.icta.events.eventAttendance.view.AbstractView;
import lk.icta.events.eventAttendance.view.UIVariables;

@Component
@Scope("view")
public class OtherInfoView extends AbstractView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2803601327236900353L;

	@Autowired
	private OtherInfoProcess otherInfoProcess;
	@Autowired
	private MasterDataProcess masterDataProcess;

	private long benifiaryID;

	private BeneficiaryOtherInfoDto beneficiaryOtherInfoDto;

	private String uiMode = null;

	@PostConstruct
	public void init() {
		benifiaryID = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get(UIVariables.PARAM_BENEFICARY_ID));

		beneficiaryOtherInfoDto = otherInfoProcess.findBeneficiaryOtherInfoByBeneficiaryIfExsists(benifiaryID,
				selectedLanguage);
		if (beneficiaryOtherInfoDto == null) {
			beneficiaryOtherInfoDto = new BeneficiaryOtherInfoDto();

			Collection<SelectionCodeDto> objs = masterDataProcess
					.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_ENHANCE_FAMILY_INCOME, selectedLanguage);
			Iterator<SelectionCodeDto> itr = objs.iterator();
			while (itr.hasNext()) {
				beneficiaryOtherInfoDto.getProposalForEnhancement().add(new ProposalForEnhancementDto(itr.next()));
			}
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

	}

	public String save() {
		String returnURL = "#";
		if (uiMode.equalsIgnoreCase(UIVariables.UI_MODES[0])) {
			otherInfoProcess.create(beneficiaryOtherInfoDto, benifiaryID);
			returnURL = "Comments.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "="
					+ benifiaryID;
		} else if (uiMode.equalsIgnoreCase(UIVariables.UI_MODES[1])) {
			otherInfoProcess.update(beneficiaryOtherInfoDto, benifiaryID);
			returnURL = "Comments.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "="
					+ benifiaryID + "&" + UIVariables.PARAM_MODE + "=" + uiMode;
		} else {
			returnURL = "Comments.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "="
					+ benifiaryID + "&" + UIVariables.PARAM_MODE + "=" + uiMode;
		}
		return returnURL;
	}

	public String getUiMode() {
		return uiMode;
	}

	public void setUiMode(String uiMode) {
		this.uiMode = uiMode;
	}

	public BeneficiaryOtherInfoDto getBeneficiaryOtherInfoDto() {
		return beneficiaryOtherInfoDto;
	}

	public void setBeneficiaryOtherInfoDto(BeneficiaryOtherInfoDto beneficiaryOtherInfoDto) {
		this.beneficiaryOtherInfoDto = beneficiaryOtherInfoDto;
	}
	public String getNavParam() {
		return "action=".concat(uiMode).concat("&beneficary=").concat(Long.toString(benifiaryID));
	}
}
