package lk.icta.events.eventAttendance.view.beneficary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lk.icta.events.eventAttendance.service.dto.benefiary.AssetsOtherMachineDto;
import lk.icta.events.eventAttendance.service.dto.common.SelectionCodeDto;
import lk.icta.events.eventAttendance.service.process.benefiary.AssetsOtherMachinesProcess;
import lk.icta.events.eventAttendance.service.process.master.MasterDataProcess;
import lk.icta.events.eventAttendance.service.process.utill.ConfigurationUtil;
import lk.icta.events.eventAttendance.view.AbstractView;
import lk.icta.events.eventAttendance.view.UIVariables;

@Component
@Scope("view")
public class OtherAssetsView extends AbstractView {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2803601327236900353L;

	@Autowired
	private MasterDataProcess masterDataProcess;

	@Autowired
	private AssetsOtherMachinesProcess assetsOtherMachinesProcess;

	private long benifiaryID;

	private Collection<AssetsOtherMachineDto> assetsOtherVehicles = new ArrayList<AssetsOtherMachineDto>();
	private Collection<AssetsOtherMachineDto> assetsOtherHouseHoldEq = new ArrayList<AssetsOtherMachineDto>();
	private Collection<AssetsOtherMachineDto> assetsOtherMachine = new ArrayList<AssetsOtherMachineDto>();
	private Collection<AssetsOtherMachineDto> assetsOtherAnimals = new ArrayList<AssetsOtherMachineDto>();
	private Collection<SelectionCodeDto> yesNoList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> natureOfOwnershipList = new ArrayList<SelectionCodeDto>();
	private String uiMode = null;

	@PostConstruct
	public void init() {
		benifiaryID = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get(UIVariables.PARAM_BENEFICARY_ID));

		this.assetsOtherVehicles = assetsOtherMachinesProcess.listByVehicleBenefiary(benifiaryID, selectedLanguage);
		this.assetsOtherAnimals = assetsOtherMachinesProcess.listByHusandaryBenefiary(benifiaryID, selectedLanguage);
		this.assetsOtherMachine = assetsOtherMachinesProcess.listByMachineBenefiary(benifiaryID, selectedLanguage);
		this.assetsOtherHouseHoldEq = assetsOtherMachinesProcess.listByHouseHoldEqBenefiary(benifiaryID,
				selectedLanguage);

		if (assetsOtherVehicles.size() == 0) {
			Iterator<SelectionCodeDto> itrVehicle = masterDataProcess
					.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_VEHICLES, selectedLanguage).iterator();
			while (itrVehicle.hasNext()) {
				this.assetsOtherVehicles.add(new AssetsOtherMachineDto(itrVehicle.next()));
			}

			Iterator<SelectionCodeDto> itrHouseHoldItems = masterDataProcess
					.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_HOUSE_HOLD_EQUIPMENTS, selectedLanguage)
					.iterator();
			while (itrHouseHoldItems.hasNext()) {
				this.assetsOtherHouseHoldEq.add(new AssetsOtherMachineDto(itrHouseHoldItems.next()));
			}

			Iterator<SelectionCodeDto> itrMachines = masterDataProcess
					.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_MACHINARY_EQUIPMENTS, selectedLanguage)
					.iterator();
			while (itrMachines.hasNext()) {
				this.assetsOtherMachine.add(new AssetsOtherMachineDto(itrMachines.next()));
			}

			Iterator<SelectionCodeDto> itrAnimals = masterDataProcess
					.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_ANIMAL_AUACULTURE, selectedLanguage)
					.iterator();
			while (itrAnimals.hasNext()) {
				this.assetsOtherAnimals.add(new AssetsOtherMachineDto(itrAnimals.next()));
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
		String returnURL;
		if (uiMode.equals(UIVariables.UI_MODES[0])) {
			assetsOtherMachinesProcess.saveList(assetsOtherVehicles, benifiaryID, getUserDetails());
			assetsOtherMachinesProcess.saveList(assetsOtherHouseHoldEq, benifiaryID, getUserDetails());
			assetsOtherMachinesProcess.saveList(assetsOtherMachine, benifiaryID, getUserDetails());
			assetsOtherMachinesProcess.saveList(assetsOtherAnimals, benifiaryID, getUserDetails());
			
			returnURL = "BIIndebitness.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + benifiaryID;
		} else if (uiMode.equals(UIVariables.UI_MODES[1])) {
			assetsOtherMachinesProcess.update(assetsOtherVehicles, benifiaryID, getUserDetails());
			assetsOtherMachinesProcess.update(assetsOtherHouseHoldEq, benifiaryID, getUserDetails());
			assetsOtherMachinesProcess.update(assetsOtherMachine, benifiaryID, getUserDetails());
			assetsOtherMachinesProcess.update(assetsOtherAnimals, benifiaryID, getUserDetails());
			returnURL = "BIIndebitness.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + benifiaryID
					+ "&" + UIVariables.PARAM_MODE + "=" + uiMode;
		}else{
			returnURL = "BIIndebitness.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + benifiaryID
					+ "&" + UIVariables.PARAM_MODE + "=" + uiMode;
		}
		return returnURL;
	}

	public Collection<AssetsOtherMachineDto> getAssetsOtherVehicles() {
		return assetsOtherVehicles;
	}

	public void setAssetsOtherVehicles(Collection<AssetsOtherMachineDto> assetsOtherVehicles) {
		this.assetsOtherVehicles = assetsOtherVehicles;
	}

	public Collection<AssetsOtherMachineDto> getAssetsOtherHouseHoldEq() {
		return assetsOtherHouseHoldEq;
	}

	public void setAssetsOtherHouseHoldEq(Collection<AssetsOtherMachineDto> assetsOtherHouseHoldEq) {
		this.assetsOtherHouseHoldEq = assetsOtherHouseHoldEq;
	}

	public Collection<AssetsOtherMachineDto> getAssetsOtherMachine() {
		return assetsOtherMachine;
	}

	public void setAssetsOtherMachine(Collection<AssetsOtherMachineDto> assetsOtherMachine) {
		this.assetsOtherMachine = assetsOtherMachine;
	}

	public Collection<AssetsOtherMachineDto> getAssetsOtherAnimals() {
		return assetsOtherAnimals;
	}

	public void setAssetsOtherAnimals(Collection<AssetsOtherMachineDto> assetsOtherAnimals) {
		this.assetsOtherAnimals = assetsOtherAnimals;
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

	public Collection<SelectionCodeDto> getNatureOfOwnershipList() {
		return natureOfOwnershipList;
	}

	public void setNatureOfOwnershipList(Collection<SelectionCodeDto> natureOfOwnershipList) {
		this.natureOfOwnershipList = natureOfOwnershipList;
	}
	public String getNavParam() {
		return "action=".concat(uiMode).concat("&beneficary=").concat(Long.toString(benifiaryID));
	}
}
