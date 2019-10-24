package lk.icta.events.eventAttendance.view.beneficary;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lk.icta.events.eventAttendance.service.dto.benefiary.BenefiaryDto;
import lk.icta.events.eventAttendance.service.dto.benefiary.SocialogicalInfoDto;
import lk.icta.events.eventAttendance.service.dto.common.SelectionCodeDto;
import lk.icta.events.eventAttendance.service.process.benefiary.BenefiaryProcess;
import lk.icta.events.eventAttendance.service.process.benefiary.SocialogicalInfoProcess;
import lk.icta.events.eventAttendance.service.process.master.MasterDataProcess;
import lk.icta.events.eventAttendance.service.process.user.MessageException;
import lk.icta.events.eventAttendance.service.process.utill.ConfigurationUtil;
import lk.icta.events.eventAttendance.view.AbstractView;
import lk.icta.events.eventAttendance.view.UIVariables;

@Component
@Scope("view")
public class SocialogicalInfoView extends AbstractView {
	private final static Logger logger = Logger.getLogger(SocialogicalInfoView.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -2803601327236900353L;

	@Autowired
	private MasterDataProcess masterDataProcess;

	@Autowired
	private BenefiaryProcess benefiaryProcess;

	@Autowired
	private SocialogicalInfoProcess socialogicalInfoProcess;

	private Collection<SelectionCodeDto> relationshipList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> genderList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> maritualStatusList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> presentEducationList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> educationLevelList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> achievedNVQList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> presentInvolvementList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> mainIncomeList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> diabilityList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> congentageousDiseasesList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> professionList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> samurdhRecivingList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> yesNoList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> migrateReasonList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> commonList = new ArrayList<SelectionCodeDto>();

	private long benifiaryID = 1;

	private Collection<SocialogicalInfoDto> socialogicalInfo = new ArrayList<SocialogicalInfoDto>();
	private String uiMode = UIVariables.UI_MODES[0];

	private int addItemCount = 0;

	@PostConstruct
	public void init() {

		try {
			benifiaryID = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.get(UIVariables.PARAM_BENEFICARY_ID));

			yesNoList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_YES_NO,
					selectedLanguage);
			relationshipList = masterDataProcess
					.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_APPLICANT_RELATION, selectedLanguage);
			genderList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_APPLICANT_GENDER,
					selectedLanguage);
			maritualStatusList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_MARITAL_STATUS,
					selectedLanguage);
			presentEducationList = masterDataProcess
					.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_PRESENT_EDUCATION, selectedLanguage);
			educationLevelList = masterDataProcess
					.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_EDUCATION_LEVEL, selectedLanguage);
			achievedNVQList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_ACHIVED_NVQ,
					selectedLanguage);
			presentInvolvementList = masterDataProcess
					.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_PRESENT_ENVOLVEMENT, selectedLanguage);
			mainIncomeList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_NATURE_OF_INCOME,
					selectedLanguage);
			diabilityList = masterDataProcess
					.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_NATURE_OF_DISABILITY, selectedLanguage);
			congentageousDiseasesList = masterDataProcess.getSelectionCodeList(
					ConfigurationUtil.SELECTION_CODE_NON_CONGENTAGEOUS_DISEASES, selectedLanguage);
			professionList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_MAIN_PROFESSION,
					selectedLanguage);
			samurdhRecivingList = masterDataProcess
					.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_SAMUDHI_WALFARE, selectedLanguage);
			migrateReasonList = masterDataProcess
					.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_MIGRATING_REASON, selectedLanguage);
			commonList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_COMMON,
					selectedLanguage);

			this.socialogicalInfo = socialogicalInfoProcess.listByBenefiary(benifiaryID, selectedLanguage);

			if (socialogicalInfo.size() == 0) {

				BenefiaryDto benefiaryDto = benefiaryProcess.findByID(benifiaryID, selectedLanguage);
				this.socialogicalInfo.add(new SocialogicalInfoDto(benefiaryDto.getFullName()));

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

		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void removeBenificiary(SocialogicalInfoDto socialogicalInfoDto) {
		if (socialogicalInfoDto.getId() == 0) {
			this.socialogicalInfo.remove(socialogicalInfoDto);
		}
	}

	public String save() {
		String returnURL = "#";
		if (uiMode.equals(UIVariables.UI_MODES[0])) {
			socialogicalInfoProcess.saveList(socialogicalInfo, benifiaryID);
			returnURL = "BIFamilyExpenses.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "="
					+ benifiaryID;
		} else if (uiMode.equals(UIVariables.UI_MODES[1])) {
			// socialogicalInfoProcess.de(benefiaryDto.getSocialogicalInfo(),
			// benifiaryID);
			socialogicalInfoProcess.updateList(socialogicalInfo, benifiaryID);
			returnURL = "BIFamilyExpenses.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "="
					+ benifiaryID + "&" + UIVariables.PARAM_MODE + "=" + uiMode;
		} else {
			returnURL = "BIFamilyExpenses.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "="
					+ benifiaryID + "&" + UIVariables.PARAM_MODE + "=" + uiMode;
		}
		return returnURL;
	}

	public long getBenifiaryID() {
		return benifiaryID;
	}

	public void setBenifiaryID(long benifiaryID) {
		this.benifiaryID = benifiaryID;
	}

	public void addMultipleItems() {
		int count = socialogicalInfo.size();
		for (int loopCount = 0; loopCount < addItemCount; loopCount++) {
			count++;
			socialogicalInfo.add(new SocialogicalInfoDto(count));
		}
		RequestContext.getCurrentInstance().update("frmSocialogicalInfo:socialogicalInfo");
	}

	public void removeItem(SocialogicalInfoDto socialogicalInfoDto) {

		try {
			if (uiMode.equals(UIVariables.UI_MODES[0])) {
				socialogicalInfo.remove(socialogicalInfoDto);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Remove", "Remove Item Successfuly"));
			}
		} catch (Exception ex) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Load Error", ex.getMessage()));
			logger.error("Error loading BENEFICARY", ex);
		}

	}

	public Collection<SelectionCodeDto> getRelationshipList() {
		return relationshipList;
	}

	public void setRelationshipList(Collection<SelectionCodeDto> relationshipList) {
		this.relationshipList = relationshipList;
	}

	public Collection<SelectionCodeDto> getGenderList() {
		return genderList;
	}

	public void setGenderList(Collection<SelectionCodeDto> genderList) {
		this.genderList = genderList;
	}

	public Collection<SelectionCodeDto> getMaritualStatusList() {
		return maritualStatusList;
	}

	public void setMaritualStatusList(Collection<SelectionCodeDto> maritualStatusList) {
		this.maritualStatusList = maritualStatusList;
	}

	public Collection<SelectionCodeDto> getPresentEducationList() {
		return presentEducationList;
	}

	public void setPresentEducationList(Collection<SelectionCodeDto> presentEducationList) {
		this.presentEducationList = presentEducationList;
	}

	public Collection<SelectionCodeDto> getEducationLevelList() {
		return educationLevelList;
	}

	public void setEducationLevelList(Collection<SelectionCodeDto> educationLevelList) {
		this.educationLevelList = educationLevelList;
	}

	public Collection<SelectionCodeDto> getAchievedNVQList() {
		return achievedNVQList;
	}

	public void setAchievedNVQList(Collection<SelectionCodeDto> achievedNVQList) {
		this.achievedNVQList = achievedNVQList;
	}

	public Collection<SelectionCodeDto> getPresentInvolvementList() {
		return presentInvolvementList;
	}

	public void setPresentInvolvementList(Collection<SelectionCodeDto> presentInvolvementList) {
		this.presentInvolvementList = presentInvolvementList;
	}

	public Collection<SelectionCodeDto> getMainIncomeList() {
		return mainIncomeList;
	}

	public void setMainIncomeList(Collection<SelectionCodeDto> mainIncomeList) {
		this.mainIncomeList = mainIncomeList;
	}

	public Collection<SelectionCodeDto> getDiabilityList() {
		return diabilityList;
	}

	public void setDiabilityList(Collection<SelectionCodeDto> diabilityList) {
		this.diabilityList = diabilityList;
	}

	public Collection<SelectionCodeDto> getCongentageousDiseasesList() {
		return congentageousDiseasesList;
	}

	public void setCongentageousDiseasesList(Collection<SelectionCodeDto> congentageousDiseasesList) {
		this.congentageousDiseasesList = congentageousDiseasesList;
	}

	public Collection<SelectionCodeDto> getProfessionList() {
		return professionList;
	}

	public void setProfessionList(Collection<SelectionCodeDto> professionList) {
		this.professionList = professionList;
	}

	public String getUiMode() {
		return uiMode;
	}

	public void setUiMode(String uiMode) {
		this.uiMode = uiMode;
	}

	public Collection<SelectionCodeDto> getYesNoList() {
		return yesNoList;
	}

	public void setYesNoList(Collection<SelectionCodeDto> yesNoList) {
		this.yesNoList = yesNoList;
	}

	public Collection<SelectionCodeDto> getSamurdhRecivingList() {
		return samurdhRecivingList;
	}

	public void setSamurdhRecivingList(Collection<SelectionCodeDto> samurdhRecivingList) {
		this.samurdhRecivingList = samurdhRecivingList;
	}

	public Collection<SelectionCodeDto> getMigrateReasonList() {
		return migrateReasonList;
	}

	public void setMigrateReasonList(Collection<SelectionCodeDto> migrateReasonList) {
		this.migrateReasonList = migrateReasonList;
	}

	public int getAddItemCount() {
		return addItemCount;
	}

	public void setAddItemCount(int addItemCount) {
		this.addItemCount = addItemCount;
	}

	public Collection<SocialogicalInfoDto> getSocialogicalInfo() {
		return socialogicalInfo;
	}

	public void setSocialogicalInfo(Collection<SocialogicalInfoDto> socialogicalInfo) {
		this.socialogicalInfo = socialogicalInfo;
	}

	public String getNavParam() {
		return "action=".concat(uiMode).concat("&beneficary=").concat(Long.toString(benifiaryID));
	}

	public Collection<SelectionCodeDto> getCommonList() {
		return commonList;
	}

	public void setCommonList(Collection<SelectionCodeDto> commonList) {
		this.commonList = commonList;
	}

}
