package lk.icta.events.eventAttendance.view.beneficary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lk.icta.events.eventAttendance.service.dto.benefiary.BenefiaryDto;
import lk.icta.events.eventAttendance.service.dto.common.SelectionCodeDto;
import lk.icta.events.eventAttendance.service.dto.districts.DistrictDto;
import lk.icta.events.eventAttendance.service.dto.divinagumaZone.DivinagumaZoneDto;
import lk.icta.events.eventAttendance.service.dto.divisionalSecDivision.DivisionalSecDivisionDto;
import lk.icta.events.eventAttendance.service.dto.gnd.GNDDto;
import lk.icta.events.eventAttendance.service.dto.province.ProvinceDto;
import lk.icta.events.eventAttendance.service.dto.user.UserSettingsDto;
import lk.icta.events.eventAttendance.service.process.benefiary.BenefiaryProcess;
import lk.icta.events.eventAttendance.service.process.master.MasterDataProcess;
import lk.icta.events.eventAttendance.service.process.user.MessageException;
import lk.icta.events.eventAttendance.service.process.user.UserDetailsProcess;
import lk.icta.events.eventAttendance.service.process.utill.ConfigurationUtil;
import lk.icta.events.eventAttendance.view.AbstractView;
import lk.icta.events.eventAttendance.view.UIVariables;

@Component
@Scope("view")
public class IdentificationInfoView extends AbstractView {

	private final static Logger logger = Logger.getLogger(IdentificationInfoView.class);
	private static final long serialVersionUID = 1L;

	@Autowired
	private BenefiaryProcess benefiaryProcess;

	@Autowired
	private MasterDataProcess masterDataProcess;

	@Autowired
	private UserDetailsProcess userDetailsProcess;

	private Collection<ProvinceDto> provinceList = new ArrayList<ProvinceDto>();
	private Collection<DistrictDto> districtList = new ArrayList<DistrictDto>();
	private Collection<DivisionalSecDivisionDto> divisionalSecList = new ArrayList<DivisionalSecDivisionDto>();
	private Collection<DivinagumaZoneDto> divinagumaZoneList = new ArrayList<DivinagumaZoneDto>();
	private Collection<GNDDto> gndList = new ArrayList<GNDDto>();
	private Collection<SelectionCodeDto> statusList = new ArrayList<SelectionCodeDto>();

	private ProvinceDto provinceDto = new ProvinceDto();
	private DistrictDto districtDto = new DistrictDto();
	private DivisionalSecDivisionDto divisionalSec = new DivisionalSecDivisionDto();
	private DivinagumaZoneDto divinagumaZone = new DivinagumaZoneDto();
	private BenefiaryDto benefiaryDto = new BenefiaryDto();

	private String uiMode = UIVariables.UI_MODES[0];
	private UserSettingsDto defaultSettings = new UserSettingsDto();

	@PostConstruct
	public void init() {

		provinceList = masterDataProcess.getProvinceList(selectedLanguage);
		statusList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_APPLICANT_STATUS,
				selectedLanguage);
		try {
			UserSettingsDto userSettings = userDetailsProcess.findDefaultSeByID(getUserDetails().getUsername(),
					selectedLanguage);
			if (userSettings != null) {
				logger.info("User Settings Exsists. Populate GND");
				defaultSettings = userSettings;
			} else {
				defaultSettings.setUserDetails(getUserDetails());
				logger.info("User Settings Not Exsists");
			}
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.containsKey(UIVariables.PARAM_BENEFICARY_ID)) {
				if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
						.containsKey(UIVariables.PARAM_MODE)) {
					uiMode = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
							.get(UIVariables.PARAM_MODE);
				} else {
					uiMode = UIVariables.UI_MODES[3];
				}
				benefiaryDto = benefiaryProcess
						.findByID(
								Long.parseLong(FacesContext.getCurrentInstance().getExternalContext()
										.getRequestParameterMap().get(UIVariables.PARAM_BENEFICARY_ID)),
								selectedLanguage);
				populateGND(benefiaryDto.getGndDto());
			} else if (uiMode.equals(UIVariables.UI_MODES[0])) {
				if (defaultSettings.getGnd() != null) {
					populateGND(defaultSettings.getGnd());
					benefiaryDto.setGndDto(defaultSettings.getGnd());
				}

			}
		} catch (NumberFormatException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Load Error", e.getMessage()));
			logger.error("Error Convert BENEFICARY ID to number ", e);
		} catch (MessageException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Load Error", e.getMessage()));
			logger.error("Error loading BENEFICARY", e);
		}
	}

	private void populateGND(GNDDto gnd) {
		this.divinagumaZone = gnd.getDivinagumaZone();
		this.divisionalSec = gnd.getDivinagumaZone().getDivisionalSecDivision();
		this.districtDto = gnd.getDivinagumaZone().getDivisionalSecDivision().getDistrict();
		this.provinceDto = gnd.getDivinagumaZone().getDivisionalSecDivision().getDistrict().getProvinceDto();

		onProvinceChange();
		onDistrictChange();
		onDivisionalSecListChange();
		onDivinagumaZoneListChange();
	}

	public void onProvinceChange() {
		if (provinceDto != null) {
			districtList = masterDataProcess.getDistrictsList(provinceDto.getId(), selectedLanguage);
			divisionalSecList = new ArrayList<DivisionalSecDivisionDto>();
			divinagumaZoneList = new ArrayList<DivinagumaZoneDto>();
			gndList = new ArrayList<GNDDto>();
		} else {
			districtList = new ArrayList<DistrictDto>();
			divisionalSecList = new ArrayList<DivisionalSecDivisionDto>();
			divinagumaZoneList = new ArrayList<DivinagumaZoneDto>();
			gndList = new ArrayList<GNDDto>();
		}

	}

	public void onDistrictChange() {
		if (districtDto != null) {
			divisionalSecList = masterDataProcess.getDivisionalSecList(districtDto.getId(), selectedLanguage);
			divinagumaZoneList = new ArrayList<DivinagumaZoneDto>();
			gndList = new ArrayList<GNDDto>();
		} else {
			divisionalSecList = new ArrayList<DivisionalSecDivisionDto>();
			divinagumaZoneList = new ArrayList<DivinagumaZoneDto>();
			gndList = new ArrayList<GNDDto>();
		}

	}

	public void onDivisionalSecListChange() {
		if (divinagumaZoneList != null) {
			divinagumaZoneList = masterDataProcess.getDivinagumaZoneList(divisionalSec.getId(), selectedLanguage);
			gndList = new ArrayList<GNDDto>();
		} else {
			divinagumaZoneList = new ArrayList<DivinagumaZoneDto>();
			gndList = new ArrayList<GNDDto>();
		}
	}

	public void onDivinagumaZoneListChange() {
		if (gndList != null) {
			gndList = masterDataProcess.getGNDList(divinagumaZone.getId(), selectedLanguage);
		} else {
			gndList = new ArrayList<GNDDto>();
		}

	}

	public String save() {
		try {
			String returnURL = "#";
			if (uiMode.equals(UIVariables.UI_MODES[0])) {

				benefiaryProcess.create(benefiaryDto, getUserDetails());
				returnURL = "BISocialogicalInfo.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "="
						+ benefiaryDto.getId();
				if (defaultSettings.getGnd() == null
						|| !defaultSettings.getGnd().getId().equals(benefiaryDto.getGndDto().getId())) {
					defaultSettings.setGnd(benefiaryDto.getGndDto());
					defaultSettings.setUserDetails(getUserDetails());
					userDetailsProcess.saveDefaultDetails(defaultSettings);
				}
			} else if (uiMode.equals(UIVariables.UI_MODES[1])) {
				benefiaryProcess.update(benefiaryDto, getUserDetails());
				returnURL = "BISocialogicalInfo.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "="
						+ benefiaryDto.getId() + "&" + UIVariables.PARAM_MODE + "=" + uiMode;
				if (defaultSettings.getGnd() == null
						|| !defaultSettings.getGnd().getId().equals(benefiaryDto.getGndDto().getId())) {
					defaultSettings.setGnd(benefiaryDto.getGndDto());
					defaultSettings.setUserDetails(getUserDetails());
					userDetailsProcess.saveDefaultDetails(defaultSettings);
				}
			} else {
				returnURL = "BISocialogicalInfo.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "="
						+ benefiaryDto.getId() + "&" + UIVariables.PARAM_MODE + "=" + uiMode;
			}

			return returnURL;

		} catch (MessageException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save Error", e.getMessage()));
			return "#";
		}
	}

	public IdentificationInfoView() {

	}

	public Collection<ProvinceDto> getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(Collection<ProvinceDto> provinceList) {
		this.provinceList = provinceList;
	}

	public Collection<DistrictDto> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(Collection<DistrictDto> districtList) {
		this.districtList = districtList;
	}

	public Collection<DivisionalSecDivisionDto> getDivisionalSecList() {
		return divisionalSecList;
	}

	public void setDivisionalSecList(Collection<DivisionalSecDivisionDto> divisionalSecList) {
		this.divisionalSecList = divisionalSecList;
	}

	public Collection<DivinagumaZoneDto> getDivinagumaZoneList() {
		return divinagumaZoneList;
	}

	public void setDivinagumaZoneList(Collection<DivinagumaZoneDto> divinagumaZoneList) {
		this.divinagumaZoneList = divinagumaZoneList;
	}

	public Collection<GNDDto> getGndList() {
		return gndList;
	}

	public void setGndList(Collection<GNDDto> gndList) {
		this.gndList = gndList;
	}

	public Collection<SelectionCodeDto> getStatusList() {
		return statusList;
	}

	public void setStatusList(Collection<SelectionCodeDto> statusList) {
		this.statusList = statusList;
	}

	public ProvinceDto getProvinceDto() {
		return provinceDto;
	}

	public void setProvinceDto(ProvinceDto provinceDto) {
		this.provinceDto = provinceDto;
	}

	public DistrictDto getDistrictDto() {
		return districtDto;
	}

	public void setDistrictDto(DistrictDto districtDto) {
		this.districtDto = districtDto;
	}

	public DivisionalSecDivisionDto getDivisionalSec() {
		return divisionalSec;
	}

	public void setDivisionalSec(DivisionalSecDivisionDto divisionalSec) {
		this.divisionalSec = divisionalSec;
	}

	public DivinagumaZoneDto getDivinagumaZone() {
		return divinagumaZone;
	}

	public void setDivinagumaZone(DivinagumaZoneDto divinagumaZone) {
		this.divinagumaZone = divinagumaZone;
	}

	public BenefiaryDto getBenefiaryDto() {
		return benefiaryDto;
	}

	public void setBenefiaryDto(BenefiaryDto benefiaryDto) {
		this.benefiaryDto = benefiaryDto;
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

	public void changeLanguage() {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put(UIVariables.PARAM_SELECTED_LANGUAGE, "si");

		this.selectedLanguage = ConfigurationUtil.LANGUAGES_SINHALA;

		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("si"));
		logger.info("Change Language to Sinhala");
	}

	public String getNavParam() {
		return "action=".concat(uiMode).concat("&beneficary=").concat(Long.toString(benefiaryDto.getId()));
	}

}
