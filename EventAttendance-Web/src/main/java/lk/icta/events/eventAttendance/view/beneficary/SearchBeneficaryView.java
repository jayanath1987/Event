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

import lk.icta.events.eventAttendance.service.dto.benefiary.BenefiarySearchDto;
import lk.icta.events.eventAttendance.service.dto.districts.DistrictDto;
import lk.icta.events.eventAttendance.service.dto.divinagumaZone.DivinagumaZoneDto;
import lk.icta.events.eventAttendance.service.dto.divisionalSecDivision.DivisionalSecDivisionDto;
import lk.icta.events.eventAttendance.service.dto.gnd.GNDDto;
import lk.icta.events.eventAttendance.service.dto.province.ProvinceDto;
import lk.icta.events.eventAttendance.service.process.benefiary.BenefiaryProcess;
import lk.icta.events.eventAttendance.service.process.benefiary.SocialogicalInfoProcess;
import lk.icta.events.eventAttendance.service.process.master.MasterDataProcess;
import lk.icta.events.eventAttendance.service.process.user.MessageException;
import lk.icta.events.eventAttendance.service.process.utill.ConfigurationUtil;
import lk.icta.events.eventAttendance.view.AbstractView;
import lk.icta.events.eventAttendance.view.UIVariables;

@Component
@Scope("view")
public class SearchBeneficaryView extends AbstractView {

	private final static Logger logger = Logger.getLogger(SearchBeneficaryView.class);
	private static final long serialVersionUID = 1L;

	@Autowired
	private BenefiaryProcess benefiaryProcess;

	@Autowired
	private MasterDataProcess masterDataProcess;

	@Autowired
	private SocialogicalInfoProcess socialogicalInfoProcess;

	private Collection<ProvinceDto> provinceList = new ArrayList<ProvinceDto>();
	private Collection<DistrictDto> districtList = new ArrayList<DistrictDto>();
	private Collection<DivisionalSecDivisionDto> divisionalSecList = new ArrayList<DivisionalSecDivisionDto>();
	private Collection<DivinagumaZoneDto> divinagumaZoneList = new ArrayList<DivinagumaZoneDto>();
	private Collection<GNDDto> gndList = new ArrayList<GNDDto>();

	private ProvinceDto provinceDto = new ProvinceDto();
	private DistrictDto districtDto = new DistrictDto();
	private DivisionalSecDivisionDto divisionalSec = new DivisionalSecDivisionDto();
	private DivinagumaZoneDto divinagumaZone = new DivinagumaZoneDto();
	private GNDDto gnd = new GNDDto();

	private long beneficaryId = 0;
	private String beneficaryName = "";
	private String beneficaryNIC = "";

	private String searchMode = "All";

	private Collection<BenefiarySearchDto> selectedBeneficaryList = new ArrayList<BenefiarySearchDto>();

	private Collection<BenefiarySearchDto> filteredBeneficaryList = new ArrayList<BenefiarySearchDto>();

	public Collection<BenefiarySearchDto> getSelectedBeneficaryList() {
		return selectedBeneficaryList;
	}

	public void setSelectedBeneficaryList(Collection<BenefiarySearchDto> selectedBeneficaryList) {
		this.selectedBeneficaryList = selectedBeneficaryList;
	}

	@PostConstruct
	public void init() {
		try {

			if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.containsKey(UIVariables.PARAM_BENEFICARY_NAME)) {
				beneficaryName = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
						.get(UIVariables.PARAM_BENEFICARY_NAME);
				searchMode = UIVariables.PARAM_BENEFICARY_NAME;
				populateByName();
			} else if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.containsKey(UIVariables.PARAM_BENEFICARY_ID)) {
				beneficaryId = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext()
						.getRequestParameterMap().get(UIVariables.PARAM_BENEFICARY_ID));
				searchMode = UIVariables.PARAM_BENEFICARY_ID;
				populateByID();
			} else if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.containsKey(UIVariables.PARAM_BENEFICARY_NIC)) {
				beneficaryNIC = FacesContext.getCurrentInstance().getExternalContext()
						.getRequestParameterMap().get(UIVariables.PARAM_BENEFICARY_NIC);
				searchMode = UIVariables.PARAM_BENEFICARY_NIC;
				populateByNIC();
			} else {
				searchMode = "All";
				provinceList = masterDataProcess.getProvinceList(selectedLanguage);
			}
		} catch (NumberFormatException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Load Error", e.getMessage()));
			logger.error("Error Convert BENEFICARY ID to number ", e);
		}
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

	public SearchBeneficaryView() {

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

	public GNDDto getGnd() {
		return gnd;
	}

	public void setGnd(GNDDto gnd) {
		this.gnd = gnd;
	}

	public long getBeneficaryId() {
		return beneficaryId;
	}

	public void setBeneficaryId(long beneficaryId) {
		this.beneficaryId = beneficaryId;
	}

	public String getBeneficaryName() {
		return beneficaryName;
	}

	public void setBeneficaryName(String beneficaryName) {
		this.beneficaryName = beneficaryName;
	}

	public String getBeneficaryNIC() {
		return beneficaryNIC;
	}

	public void setBeneficaryNIC(String beneficaryNIC) {
		this.beneficaryNIC = beneficaryNIC;
	}

	public void populateByAria() {
		try {
			if (gnd != null) {
				selectedBeneficaryList = benefiaryProcess.searchBenefiaryByGND(gnd.getId());
			} else if (divinagumaZone != null) {
				selectedBeneficaryList = benefiaryProcess.searchBenefiaryByDivinagumaZone(divinagumaZone.getId());
			} else if (divisionalSec != null) {
				selectedBeneficaryList = benefiaryProcess.searchBenefiaryByDivisionalSec(divisionalSec.getId());
			} else if (districtDto != null) {
				selectedBeneficaryList = benefiaryProcess.searchBenefiaryByDistrict(districtDto.getId());
			} else if (provinceDto != null) {
				selectedBeneficaryList = benefiaryProcess.searchBenefiaryByProvince(provinceDto.getId());
			}
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void populateByID() {
		try {
			if (beneficaryId != 0) {

				selectedBeneficaryList = benefiaryProcess.searchBenefiaryByID(String.valueOf(beneficaryId));

			}
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void populateByName() {
		try {
			if (!beneficaryName.equals("")) {

				selectedBeneficaryList = benefiaryProcess.searchBenefiaryByName(beneficaryName);

			}
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void populateByNIC() {
		try {
			if (!beneficaryNIC.equals("")) {

				selectedBeneficaryList = socialogicalInfoProcess.listBenefiaryByNIC(beneficaryNIC);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Collection<BenefiarySearchDto> getFilteredBeneficaryList() {
		return filteredBeneficaryList;
	}

	public void setFilteredBeneficaryList(Collection<BenefiarySearchDto> filteredBeneficaryList) {
		this.filteredBeneficaryList = filteredBeneficaryList;
	}

	public String getSearchMode() {
		return searchMode;
	}

	public void setSearchMode(String searchMode) {
		this.searchMode = searchMode;
	}

}
