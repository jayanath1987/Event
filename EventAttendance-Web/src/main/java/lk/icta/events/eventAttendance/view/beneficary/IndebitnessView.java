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

import lk.icta.events.eventAttendance.service.dto.benefiary.IndebitnessDto;
import lk.icta.events.eventAttendance.service.dto.benefiary.SocialogicalInfoDto;
import lk.icta.events.eventAttendance.service.dto.common.SelectionCodeDto;
import lk.icta.events.eventAttendance.service.process.benefiary.IndebitnessProcess;
import lk.icta.events.eventAttendance.service.process.benefiary.SocialogicalInfoProcess;
import lk.icta.events.eventAttendance.service.process.master.MasterDataProcess;
import lk.icta.events.eventAttendance.service.process.utill.ConfigurationUtil;
import lk.icta.events.eventAttendance.view.AbstractView;
import lk.icta.events.eventAttendance.view.UIVariables;

@Component
@Scope("view")
public class IndebitnessView extends AbstractView {

	private final static Logger logger = Logger.getLogger(IndebitnessView.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -2803601327236900353L;

	@Autowired
	private MasterDataProcess masterDataProcess;

	@Autowired
	private IndebitnessProcess indebitnessProcess;

	@Autowired
	private SocialogicalInfoProcess socialogicalInfoProcess;

	private long benifiaryID;
	private Collection<IndebitnessDto> indebitNess = new ArrayList<IndebitnessDto>();
	private DualListModel<SocialogicalInfoDto> memberModel = new DualListModel<SocialogicalInfoDto>(
			new ArrayList<SocialogicalInfoDto>(), new ArrayList<SocialogicalInfoDto>());

	private Collection<SelectionCodeDto> yesNoList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> reasonForBurrowingList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> lenderList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> replacementList = new ArrayList<SelectionCodeDto>();
	private Collection<SelectionCodeDto> commonList = new ArrayList<SelectionCodeDto>();
	private String uiMode = null;

	private ArrayList<SocialogicalInfoDto> rightSocialogicalInfo = new ArrayList<SocialogicalInfoDto>();
	private ArrayList<SocialogicalInfoDto> leftSocialogicalInfo = new ArrayList<SocialogicalInfoDto>();

	private SelectionCodeDto indebited = new SelectionCodeDto();

	@PostConstruct
	public void init() {
		benifiaryID = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get(UIVariables.PARAM_BENEFICARY_ID));

		this.yesNoList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_YES_NO,
				selectedLanguage);
		this.reasonForBurrowingList = masterDataProcess
				.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_REASON_FOR_BURROWING, selectedLanguage);
		this.lenderList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_LENDER,
				selectedLanguage);
		this.replacementList = masterDataProcess
				.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_METHOD_FOR_REPLACEMENT, selectedLanguage);

		rightSocialogicalInfo = new ArrayList<SocialogicalInfoDto>(
				indebitnessProcess.listAllMembersForIndebitness(benifiaryID));
		indebitNess = indebitnessProcess.listByBenefiary(benifiaryID);

		commonList = masterDataProcess.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_COMMON, selectedLanguage);

		if (indebitNess.size() == 0) {
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

	public Collection<SelectionCodeDto> getYesNoList() {
		return yesNoList;
	}

	public void setYesNoList(Collection<SelectionCodeDto> yesNoList) {
		this.yesNoList = yesNoList;
	}

	public Collection<SelectionCodeDto> getReasonForBurrowingList() {
		return reasonForBurrowingList;
	}

	public void setReasonForBurrowingList(Collection<SelectionCodeDto> reasonForBurrowingList) {
		this.reasonForBurrowingList = reasonForBurrowingList;
	}

	public Collection<SelectionCodeDto> getLenderList() {
		return lenderList;
	}

	public void setLenderList(Collection<SelectionCodeDto> lenderList) {
		this.lenderList = lenderList;
	}

	public Collection<SelectionCodeDto> getReplacementList() {
		return replacementList;
	}

	public void setReplacementList(Collection<SelectionCodeDto> replacementList) {
		this.replacementList = replacementList;
	}

	public String save() {
		String returnURL = "#";
		if (uiMode.equals(UIVariables.UI_MODES[0])) {
			indebitnessProcess.saveList(indebitNess, benifiaryID);
			returnURL = "BIHouse.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + benifiaryID;
		} else if (uiMode.equals(UIVariables.UI_MODES[1])) {
			indebitnessProcess.updateList(indebitNess, benifiaryID);
			returnURL = "BIHouse.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + benifiaryID + "&"
					+ UIVariables.PARAM_MODE + "=" + uiMode;
		} else {
			returnURL = "BIHouse.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + benifiaryID + "&"
					+ UIVariables.PARAM_MODE + "=" + uiMode;
		}
		return returnURL;
	}

	public void addToItemList() {
		Iterator<SocialogicalInfoDto> itr = memberModel.getTarget().iterator();
		while (itr.hasNext()) {
			SocialogicalInfoDto obj = itr.next();
			IndebitnessDto indebitnessDto = new IndebitnessDto(obj.getClone());
			indebitnessDto.getMember()
					.setName(socialogicalInfoProcess.findNameByID(indebitnessDto.getMember().getId()));
			indebitNess.add(indebitnessDto);

		}
		populateMemberModel();

	}

	public void removeItem(IndebitnessDto indebitnessDto) {
		try {
			indebitNess.remove(indebitnessDto);
		} catch (Exception ex) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Remove Error", ex.getMessage()));
			logger.error("Error Removing Item", ex);
		}
	}

	public DualListModel<SocialogicalInfoDto> getMemberModel() {
		return memberModel;
	}

	public void setMemberModel(DualListModel<SocialogicalInfoDto> memberModel) {
		this.memberModel = memberModel;
	}

	public Collection<IndebitnessDto> getIndebitNess() {
		return indebitNess;
	}

	public void setIndebitNess(Collection<IndebitnessDto> indebitNess) {
		this.indebitNess = indebitNess;
	}

	public SelectionCodeDto getIndebited() {
		return indebited;
	}

	public void setIndebited(SelectionCodeDto indebited) {
		this.indebited = indebited;
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
