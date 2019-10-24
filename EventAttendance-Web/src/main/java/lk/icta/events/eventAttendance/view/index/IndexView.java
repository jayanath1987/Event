package lk.icta.events.eventAttendance.view.index;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lk.icta.events.eventAttendance.view.AbstractView;
import lk.icta.events.eventAttendance.view.MasterBean;
import lk.icta.events.eventAttendance.view.UIVariables;
@Component
@Scope("view")
public class IndexView extends AbstractView {
	
	private long beneficaryId = 0;
	private String beneficaryName = "";
	private String beneficaryNIC = "";
	
	
	private DashboardModel model;
	private final static Logger logger = Logger.getLogger(MasterBean.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 5411132867192790989L;
	
    
    @PostConstruct
    public void init() {
        model = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();
        
         
        column1.addWidget("beneficiary");
        column2.addWidget("searchBeneficiaryID");
        column2.addWidget("searchBeneficiaryName");
        column2.addWidget("searchBeneficiaryNIC");
        
 
        model.addColumn(column1);
        model.addColumn(column2);
    }
    
     
    public DashboardModel getModel() {
        return model;
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


	public void setModel(DashboardModel model) {
		this.model = model;
	}
    
	public String findBeneficaryByID(){
		return "../BeneficiaryInfo/BIFindBeneficiary.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + beneficaryId;
	}
	public String findBeneficaryByName(){
		return "../BeneficiaryInfo/BIFindBeneficiary.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_NAME + "=" + beneficaryName;
	}
	public String findBeneficaryByNIC(){
		return "../BeneficiaryInfo/BIFindBeneficiary.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_NIC + "=" + beneficaryNIC;
	}
    
}
