package lk.icta.events.eventAttendance.view.beneficary;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lk.icta.events.eventAttendance.service.dto.benefiary.BenefiaryDto;
import lk.icta.events.eventAttendance.service.dto.benefiary.HouseDto;
import lk.icta.events.eventAttendance.service.dto.common.SelectionCodeDto;
import lk.icta.events.eventAttendance.service.process.benefiary.HouseProcess;
import lk.icta.events.eventAttendance.service.process.master.MasterDataProcess;
import lk.icta.events.eventAttendance.service.process.utill.ConfigurationUtil;
import lk.icta.events.eventAttendance.view.AbstractView;
import lk.icta.events.eventAttendance.view.UIVariables;

@Component
@Scope("view")
public class HouseView extends AbstractView {

	private static final long serialVersionUID = -2803601327236900353L;

	@Autowired
	private MasterDataProcess masterDataProcess;

	@Autowired
	private HouseProcess houseProcess;

	private long benifiaryID;
	private HouseDto houseDto = new HouseDto();
	private Collection<SelectionCodeDto> natureOfUnitList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> flowAriaList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> wallMetirialList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> flowMetirialList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> roofMetirialList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> natureOfOwnershipList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> mainDrinkingSourceList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> toiletList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> natureOfTheToiletList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> mainLightingList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> cookingSourceList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> mealCountsList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> yesNoList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> commonList = new ArrayList<SelectionCodeDto>();

	private String uiMode = null;

	@PostConstruct
	public void init() {
		benifiaryID = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get(UIVariables.PARAM_BENEFICARY_ID));

		natureOfUnitList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_NATURE_OF_THE_UNIT,
				selectedLanguage);
		flowAriaList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_HOME_FLOOR_ARIA,
				selectedLanguage);
		wallMetirialList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_CONSTUCTION_METRIAL,
				selectedLanguage);
		flowMetirialList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_FLOOR,
				selectedLanguage);
		roofMetirialList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_ROOF,
				selectedLanguage);
		natureOfOwnershipList = masterDataProcess
				.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_NATURE_OF_OWNERSHIP, selectedLanguage);
		mainDrinkingSourceList = masterDataProcess
				.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_HOME_DRINKING_SOURCE, selectedLanguage);
		toiletList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_TOILET, selectedLanguage);
		natureOfTheToiletList = masterDataProcess
				.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_NATURE_OF_TOILET, selectedLanguage);
		mainLightingList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_MAIN_LIGHTING,
				selectedLanguage);
		cookingSourceList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_COOKING_SOURCE,
				selectedLanguage);
		mealCountsList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_MEAL_COUNT,
				selectedLanguage);
		yesNoList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_YES_NO, selectedLanguage);
		commonList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_COMMON, selectedLanguage);

		houseDto = houseProcess.findBenefiaryByID(benifiaryID);
		if (houseDto == null) {
			houseDto = new HouseDto();
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
		String returnURL;
		if (uiMode.equals(UIVariables.UI_MODES[0])) {
			houseProcess.create(houseDto, new BenefiaryDto(benifiaryID), getUserDetails());
			returnURL = "BISocialRelationship.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "="
					+ benifiaryID;

		} else if (uiMode.equals(UIVariables.UI_MODES[1])) {
			houseDto.setBenefiaryDto(new BenefiaryDto(benifiaryID));
			houseProcess.update(houseDto, getUserDetails());
			returnURL = "BISocialRelationship.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "="
					+ benifiaryID + "&" + UIVariables.PARAM_MODE + "=" + uiMode;
		} else {
			returnURL = "BISocialRelationship.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "="
					+ benifiaryID + "&" + UIVariables.PARAM_MODE + "=" + uiMode;
		}
		return returnURL;
	}

	public HouseDto getHouseDto() {
		return houseDto;
	}

	public void setHouseDto(HouseDto houseDto) {
		this.houseDto = houseDto;
	}

	public String getUiMode() {
		return uiMode;
	}

	public void setUiMode(String uiMode) {
		this.uiMode = uiMode;
	}

	public MasterDataProcess getMasterDataProcess() {
		return masterDataProcess;
	}

	public void setMasterDataProcess(MasterDataProcess masterDataProcess) {
		this.masterDataProcess = masterDataProcess;
	}

	public Collection<SelectionCodeDto> getNatureOfUnitList() {
		return natureOfUnitList;
	}

	public void setNatureOfUnitList(Collection<SelectionCodeDto> natureOfUnitList) {
		this.natureOfUnitList = natureOfUnitList;
	}

	public Collection<SelectionCodeDto> getFlowAriaList() {
		return flowAriaList;
	}

	public void setFlowAriaList(Collection<SelectionCodeDto> flowAriaList) {
		this.flowAriaList = flowAriaList;
	}

	public Collection<SelectionCodeDto> getWallMetirialList() {
		return wallMetirialList;
	}

	public void setWallMetirialList(Collection<SelectionCodeDto> wallMetirialList) {
		this.wallMetirialList = wallMetirialList;
	}

	public Collection<SelectionCodeDto> getFlowMetirialList() {
		return flowMetirialList;
	}

	public void setFlowMetirialList(Collection<SelectionCodeDto> flowMetirialList) {
		this.flowMetirialList = flowMetirialList;
	}

	public Collection<SelectionCodeDto> getRoofMetirialList() {
		return roofMetirialList;
	}

	public void setRoofMetirialList(Collection<SelectionCodeDto> roofMetirialList) {
		this.roofMetirialList = roofMetirialList;
	}

	public Collection<SelectionCodeDto> getNatureOfOwnershipList() {
		return natureOfOwnershipList;
	}

	public void setNatureOfOwnershipList(Collection<SelectionCodeDto> natureOfOwnershipList) {
		this.natureOfOwnershipList = natureOfOwnershipList;
	}

	public Collection<SelectionCodeDto> getMainDrinkingSourceList() {
		return mainDrinkingSourceList;
	}

	public void setMainDrinkingSourceList(Collection<SelectionCodeDto> mainDrinkingSourceList) {
		this.mainDrinkingSourceList = mainDrinkingSourceList;
	}

	public Collection<SelectionCodeDto> getToiletList() {
		return toiletList;
	}

	public void setToiletList(Collection<SelectionCodeDto> toiletList) {
		this.toiletList = toiletList;
	}

	public Collection<SelectionCodeDto> getNatureOfTheToiletList() {
		return natureOfTheToiletList;
	}

	public void setNatureOfTheToiletList(Collection<SelectionCodeDto> natureOfTheToiletList) {
		this.natureOfTheToiletList = natureOfTheToiletList;
	}

	public Collection<SelectionCodeDto> getMainLightingList() {
		return mainLightingList;
	}

	public void setMainLightingList(Collection<SelectionCodeDto> mainLightingList) {
		this.mainLightingList = mainLightingList;
	}

	public Collection<SelectionCodeDto> getCookingSourceList() {
		return cookingSourceList;
	}

	public void setCookingSourceList(Collection<SelectionCodeDto> cookingSourceList) {
		this.cookingSourceList = cookingSourceList;
	}

	public Collection<SelectionCodeDto> getMealCountsList() {
		return mealCountsList;
	}

	public void setMealCountsList(Collection<SelectionCodeDto> mealCountsList) {
		this.mealCountsList = mealCountsList;
	}

	public Collection<SelectionCodeDto> getYesNoList() {
		return yesNoList;
	}

	public void setYesNoList(Collection<SelectionCodeDto> yesNoList) {
		this.yesNoList = yesNoList;
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
