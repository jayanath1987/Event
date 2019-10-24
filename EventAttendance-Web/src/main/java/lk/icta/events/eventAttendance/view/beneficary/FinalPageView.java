package lk.icta.events.eventAttendance.view.beneficary;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lk.icta.events.eventAttendance.view.AbstractView;
import lk.icta.events.eventAttendance.view.UIVariables;

@Component
@Scope("view")
public class FinalPageView extends AbstractView {
	private final static Logger logger = Logger.getLogger(FinalPageView.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -2803601327236900353L;

	private long benifiaryID;


	@PostConstruct
	public void init() {
		benifiaryID = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get(UIVariables.PARAM_BENEFICARY_ID));
	}
	
	public long getBenifiaryID() {
		return benifiaryID;
	}


	public void setBenifiaryID(long benifiaryID) {
		this.benifiaryID = benifiaryID;
	}
	
	
	
}
