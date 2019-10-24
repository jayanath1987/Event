package lk.icta.events.eventAttendance.view.beneficary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lk.icta.events.eventAttendance.service.dto.benefiary.AverageFamilyExpensesDto;
import lk.icta.events.eventAttendance.service.dto.common.SelectionCodeDto;
import lk.icta.events.eventAttendance.service.process.benefiary.AverageFamilyExpenseProcess;
import lk.icta.events.eventAttendance.service.process.master.MasterDataProcess;
import lk.icta.events.eventAttendance.service.process.utill.ConfigurationUtil;
import lk.icta.events.eventAttendance.view.AbstractView;
import lk.icta.events.eventAttendance.view.UIVariables;

@Component
@Scope("view")
public class AverageFamilyExpView extends AbstractView {
	private final static Logger logger = Logger.getLogger(AverageFamilyExpView.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -2803601327236900353L;

	@Autowired
	private MasterDataProcess masterDataProcess;
	@Autowired
	private AverageFamilyExpenseProcess averageFamilyExpenseProcess;

	private Collection<AverageFamilyExpensesDto> averageFamilyExpenses = new ArrayList<AverageFamilyExpensesDto>();
	private String uiMode = null;

	private long benifiaryID;

	@PostConstruct
	public void init() {
		try {
			// TODO Add Logger
			benifiaryID = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.get(UIVariables.PARAM_BENEFICARY_ID));

			averageFamilyExpenses = averageFamilyExpenseProcess.listByBenefiary(benifiaryID, selectedLanguage);
			if (averageFamilyExpenses.size() == 0) {

				Collection<SelectionCodeDto> objs = masterDataProcess
						.getSelectionCodeList(ConfigurationUtil.SELECTION_CODE_FAMILY_EXPENSES, selectedLanguage);
				Iterator<SelectionCodeDto> itr = objs.iterator();
				while (itr.hasNext()) {
					averageFamilyExpenses.add(new AverageFamilyExpensesDto(itr.next()));
				}
				if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
						.containsKey(UIVariables.PARAM_MODE))
					uiMode = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
							.get(UIVariables.PARAM_MODE);
				else
					uiMode = UIVariables.UI_MODES[0];
			} else {
				if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
						.containsKey(UIVariables.PARAM_MODE)) {
					uiMode = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
							.get(UIVariables.PARAM_MODE);
				} else {
					uiMode = UIVariables.UI_MODES[3];
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Load Error", e.getMessage()));
			logger.error("Error Loading BENEFICARY Details ", e);
		}

	}

	public Collection<AverageFamilyExpensesDto> getAverageFamilyExpenses() {
		return averageFamilyExpenses;
	}

	public void setAverageFamilyExpenses(Collection<AverageFamilyExpensesDto> averageFamilyExpenses) {
		this.averageFamilyExpenses = averageFamilyExpenses;
	}

	public String getUiMode() {
		return uiMode;
	}

	public void setUiMode(String uiMode) {
		this.uiMode = uiMode;
	}

	public double getTotal() {

		double total = 0;
		Iterator<AverageFamilyExpensesDto> itr = averageFamilyExpenses.iterator();
		while (itr.hasNext()) {
			AverageFamilyExpensesDto obj = itr.next();
			total = total + (obj.getExpenditure().equals("")?0.0:Double.parseDouble(obj.getExpenditure()));
		}
		return total;
	}

	public String save() {
		try {
			String returnURL = "#";
			if (uiMode.equalsIgnoreCase(UIVariables.UI_MODES[0])) {
				averageFamilyExpenseProcess.saveList(averageFamilyExpenses, benifiaryID, getUserDetails());
				returnURL = "BIMonthlyIncome.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "="
						+ benifiaryID;
			} else if (uiMode.equalsIgnoreCase(UIVariables.UI_MODES[1])) {
				averageFamilyExpenseProcess.update(averageFamilyExpenses, benifiaryID, getUserDetails());
				returnURL = "BIMonthlyIncome.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "="
						+ benifiaryID + "&" + UIVariables.PARAM_MODE + "=" + uiMode;
			} else {
				returnURL = "BIMonthlyIncome.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "="
						+ benifiaryID + "&" + UIVariables.PARAM_MODE + "=" + uiMode;
			}
			return returnURL;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save Error", e.getMessage()));
			return "#";
		}
	}

	public String getNavParam() {
		return "action=".concat(uiMode).concat("&beneficary=").concat(Long.toString(benifiaryID));
	}

}
