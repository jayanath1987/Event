package lk.icta.events.eventAttendance.view.beneficary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lk.icta.events.eventAttendance.service.dto.benefiary.AssetsHouseBuildingDto;
import lk.icta.events.eventAttendance.service.dto.benefiary.AssetsOfFamilyDto;
import lk.icta.events.eventAttendance.service.dto.benefiary.AssetsOtherLandsDto;
import lk.icta.events.eventAttendance.service.dto.benefiary.SocialogicalInfoDto;
import lk.icta.events.eventAttendance.service.dto.common.SelectionCodeDto;
import lk.icta.events.eventAttendance.service.process.benefiary.AssetsOfFamilyProcess;
import lk.icta.events.eventAttendance.service.process.benefiary.SocialogicalInfoProcess;
import lk.icta.events.eventAttendance.service.process.master.MasterDataProcess;
import lk.icta.events.eventAttendance.service.process.utill.ConfigurationUtil;
import lk.icta.events.eventAttendance.view.AbstractView;
import lk.icta.events.eventAttendance.view.UIVariables;

@Component
@Scope("view")
public class AssetsView extends AbstractView {
	private final static Logger logger = Logger.getLogger(SocialRelationshipView.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -2803601327236900353L;

	@Autowired
	private MasterDataProcess masterDataProcess;

	@Autowired
	private AssetsOfFamilyProcess assetsOfFamilyProcess;

	@Autowired
	private SocialogicalInfoProcess socialogicalInfoProcess;

	private long benifiaryID;

	private AssetsOfFamilyDto assetsOfFamily;

	private Collection<SelectionCodeDto> yesNoList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> natureOfOwnershipOtherList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> natureOfOwnershipHomeList = new ArrayList<SelectionCodeDto>();
	private String uiMode = UIVariables.UI_MODES[0];

	private ArrayList<SocialogicalInfoDto> rightSocialogicalInfoOtherHouse = new ArrayList<SocialogicalInfoDto>();
	private ArrayList<SocialogicalInfoDto> leftSocialogicalInfoOtherHouse = new ArrayList<SocialogicalInfoDto>();

	private DualListModel<SocialogicalInfoDto> memberModelOtherHouse = new DualListModel<SocialogicalInfoDto>(
			leftSocialogicalInfoOtherHouse, rightSocialogicalInfoOtherHouse);

	private ArrayList<SocialogicalInfoDto> rightSocialogicalInfoUpLands = new ArrayList<SocialogicalInfoDto>();
	private ArrayList<SocialogicalInfoDto> leftSocialogicalInfoUpLands = new ArrayList<SocialogicalInfoDto>();

	private DualListModel<SocialogicalInfoDto> memberModelUpLands = new DualListModel<SocialogicalInfoDto>(
			leftSocialogicalInfoUpLands, rightSocialogicalInfoUpLands);

	private ArrayList<SocialogicalInfoDto> rightSocialogicalInfoLowLands = new ArrayList<SocialogicalInfoDto>();
	private ArrayList<SocialogicalInfoDto> leftSocialogicalInfoLowLands = new ArrayList<SocialogicalInfoDto>();

	private DualListModel<SocialogicalInfoDto> memberModelLowLands = new DualListModel<SocialogicalInfoDto>(
			leftSocialogicalInfoLowLands, rightSocialogicalInfoLowLands);

	@PostConstruct
	public void init() {
		benifiaryID = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get(UIVariables.PARAM_BENEFICARY_ID));

		yesNoList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_YES_NO, selectedLanguage);
		natureOfOwnershipHomeList=masterDataProcess.getSelectionCodeList(
				ConfigurationUtil.SELECTION_CODE_NATURE_OF_ASSETS_OWNERSHIP_HOME, selectedLanguage);
		natureOfOwnershipOtherList = masterDataProcess.getSelectionCodeList(
				ConfigurationUtil.SELECTION_CODE_NATURE_OF_ASSETS_OWNERSHIP_OTHER, selectedLanguage);

		this.leftSocialogicalInfoOtherHouse = new ArrayList<SocialogicalInfoDto>(
				assetsOfFamilyProcess.listMembersNotAssociatedWithAssetsHouseBuilding(benifiaryID));
		memberModelOtherHouse = new DualListModel<SocialogicalInfoDto>(leftSocialogicalInfoOtherHouse,
				rightSocialogicalInfoOtherHouse);

		this.leftSocialogicalInfoUpLands = new ArrayList<SocialogicalInfoDto>(
				assetsOfFamilyProcess.listMembersNotAssociatedWithAssetsUpLands(benifiaryID));
		memberModelUpLands = new DualListModel<SocialogicalInfoDto>(leftSocialogicalInfoUpLands,
				rightSocialogicalInfoUpLands);

		this.leftSocialogicalInfoLowLands = new ArrayList<SocialogicalInfoDto>(
				assetsOfFamilyProcess.listMembersNotAssociatedWithAssetsLowLands(benifiaryID));
		memberModelLowLands = new DualListModel<SocialogicalInfoDto>(leftSocialogicalInfoLowLands,
				rightSocialogicalInfoLowLands);

		assetsOfFamily = assetsOfFamilyProcess.findByBenefiaryIfExists(benifiaryID, selectedLanguage);
		if (assetsOfFamily == null) {
			assetsOfFamily = new AssetsOfFamilyDto();
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.containsKey(UIVariables.PARAM_MODE)) {
				uiMode = UIVariables.UI_MODES[1];
			} else {
				uiMode = UIVariables.UI_MODES[0];
			}
		} else {
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.containsKey(UIVariables.PARAM_MODE)) {
				uiMode = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
						.get(UIVariables.PARAM_MODE);
			} else {
				uiMode = UIVariables.UI_MODES[3];
			}
		}
	}

	public String save() {
		String returnURL = "#";
		if (uiMode.equals(UIVariables.UI_MODES[0])) {
			assetsOfFamilyProcess.create(assetsOfFamily, benifiaryID);
			returnURL = "BIOtherAssets.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "="
					+ benifiaryID;
		} else if (uiMode.equals(UIVariables.UI_MODES[1])) {
			assetsOfFamilyProcess.update(assetsOfFamily, benifiaryID);
			returnURL = "BIOtherAssets.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + benifiaryID
					+ "&" + UIVariables.PARAM_MODE + "=" + uiMode;
		} else {
			returnURL = "BIOtherAssets.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + benifiaryID
					+ "&" + UIVariables.PARAM_MODE + "=" + uiMode;
		}
		return returnURL;
	}

	public void changeLanguage() {
		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(selectedLanguage));
	}

	public void addToItemListOtherHouseAndBuilnding() {
		Iterator<SocialogicalInfoDto> itr = memberModelOtherHouse.getTarget().iterator();
		while (itr.hasNext()) {
			AssetsHouseBuildingDto assetsHouseBuildingDto = new AssetsHouseBuildingDto(itr.next());
			assetsHouseBuildingDto.getMember()
					.setName(socialogicalInfoProcess.findNameByID(assetsHouseBuildingDto.getMember().getId()));
			assetsOfFamily.getHomeAndBuildings().add(assetsHouseBuildingDto);
		}
		memberModelOtherHouse.getTarget().clear();
	}

	public void removeItemOtherHouseAndBuilnding(AssetsHouseBuildingDto assetsHouseBuildingDto) {

		try {
			memberModelOtherHouse.getSource().add(assetsHouseBuildingDto.getMember());
			assetsOfFamily.getHomeAndBuildings().remove(assetsHouseBuildingDto);
		} catch (Exception ex) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Remove Error", ex.getMessage()));
			logger.error("Error Removing Item", ex);
		}
	}

	public void addToItemListUpLands() {
		Iterator<SocialogicalInfoDto> itr = memberModelUpLands.getTarget().iterator();
		while (itr.hasNext()) {
			AssetsOtherLandsDto assetsOtherLandsDto = new AssetsOtherLandsDto(itr.next());
			assetsOtherLandsDto.getMember()
					.setName(socialogicalInfoProcess.findNameByID(assetsOtherLandsDto.getMember().getId()));
			assetsOfFamily.getUpLands().add(assetsOtherLandsDto);
		}
	memberModelUpLands.getTarget().clear();
	}

	public void removeItemUpLoands(AssetsOtherLandsDto assetsOtherLandsDto) {

		try {
			memberModelUpLands.getSource().add(assetsOtherLandsDto.getMember());
			assetsOfFamily.getUpLands().remove(assetsOtherLandsDto);
		} catch (Exception ex) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Remove Error", ex.getMessage()));
			logger.error("Error Removing Item", ex);
		}
	}

	public void addToItemListLowLands() {
		Iterator<SocialogicalInfoDto> itr = memberModelLowLands.getTarget().iterator();
		while (itr.hasNext()) {
			AssetsOtherLandsDto assetsOtherLandsDto = new AssetsOtherLandsDto(itr.next());
			assetsOtherLandsDto.getMember()
					.setName(socialogicalInfoProcess.findNameByID(assetsOtherLandsDto.getMember().getId()));
			assetsOfFamily.getLowLands().add(assetsOtherLandsDto);
		}
		memberModelLowLands.getTarget().clear();
	}

	public void removeItemLowLoands(AssetsOtherLandsDto assetsOtherLandsDto) {

		try {
			memberModelLowLands.getSource().add(assetsOtherLandsDto.getMember());
			assetsOfFamily.getLowLands().remove(assetsOtherLandsDto);
		} catch (Exception ex) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Remove Error", ex.getMessage()));
			logger.error("Error Removing Item", ex);
		}
	}

	public AssetsOfFamilyDto getAssetsOfFamily() {
		return assetsOfFamily;
	}

	public void setAssetsOfFamily(AssetsOfFamilyDto assetsOfFamily) {
		this.assetsOfFamily = assetsOfFamily;
	}

	public Collection<SelectionCodeDto> getYesNoList() {
		return yesNoList;
	}

	public void setYesNoList(Collection<SelectionCodeDto> yesNoList) {
		this.yesNoList = yesNoList;
	}

	public String getUiMode() {
		return uiMode;
	}

	public void setUiMode(String uiMode) {
		this.uiMode = uiMode;
	}

	public DualListModel<SocialogicalInfoDto> getMemberModelOtherHouse() {
		return memberModelOtherHouse;
	}

	public void setMemberModelOtherHouse(DualListModel<SocialogicalInfoDto> memberModelOtherHouse) {
		this.memberModelOtherHouse = memberModelOtherHouse;
	}

	public DualListModel<SocialogicalInfoDto> getMemberModelUpLands() {
		return memberModelUpLands;
	}

	public void setMemberModelUpLands(DualListModel<SocialogicalInfoDto> memberModelUpLands) {
		this.memberModelUpLands = memberModelUpLands;
	}

	public DualListModel<SocialogicalInfoDto> getMemberModelLowLands() {
		return memberModelLowLands;
	}

	public void setMemberModelLowLands(DualListModel<SocialogicalInfoDto> memberModelLowLands) {
		this.memberModelLowLands = memberModelLowLands;
	}

	public String getNavParam() {
		return "action=".concat(uiMode).concat("&beneficary=").concat(Long.toString(benifiaryID));
	}

	public Collection<SelectionCodeDto> getNatureOfOwnershipOtherList() {
		return natureOfOwnershipOtherList;
	}

	public void setNatureOfOwnershipOtherList(Collection<SelectionCodeDto> natureOfOwnershipOtherList) {
		this.natureOfOwnershipOtherList = natureOfOwnershipOtherList;
	}

	public Collection<SelectionCodeDto> getNatureOfOwnershipHomeList() {
		return natureOfOwnershipHomeList;
	}

	public void setNatureOfOwnershipHomeList(Collection<SelectionCodeDto> natureOfOwnershipHomeList) {
		this.natureOfOwnershipHomeList = natureOfOwnershipHomeList;
	}

}
