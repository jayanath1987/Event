package lk.icta.events.eventAttendance.view.beneficary;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lk.icta.events.eventAttendance.service.dto.benefiary.ReviewCommentsDto;
import lk.icta.events.eventAttendance.service.process.benefiary.CommentProcess;
import lk.icta.events.eventAttendance.service.process.master.MasterDataProcess;
import lk.icta.events.eventAttendance.view.AbstractView;
import lk.icta.events.eventAttendance.view.UIVariables;

@Component
@Scope("view")
public class Commentview extends AbstractView {
	private final static Logger logger = Logger.getLogger(Commentview.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -2803601327236900353L;

	@Autowired
	private MasterDataProcess masterDataProcess;
	@Autowired
	private CommentProcess commentprocess;

	private ReviewCommentsDto reviewcommentsDto = new ReviewCommentsDto();

	private String uiMode = null;

	private long benifiaryID;

	@PostConstruct
	public void init() {
		try {
			// TODO Add Logger
			benifiaryID = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.get(UIVariables.PARAM_BENEFICARY_ID));

			reviewcommentsDto = commentprocess.findBenefiaryByID(benifiaryID);
			if (reviewcommentsDto == null) {
				reviewcommentsDto = new ReviewCommentsDto();
				if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
						.containsKey(UIVariables.PARAM_MODE))
					uiMode = UIVariables.UI_MODES[1];
				else
					uiMode = UIVariables.UI_MODES[0];
			} else if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.containsKey(UIVariables.PARAM_MODE)) {
				uiMode = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
						.get(UIVariables.PARAM_MODE);
			} else {
				uiMode = UIVariables.UI_MODES[3];
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Load Error", e.getMessage()));
			logger.error("Error Loading BENEFICARY Details ", e);
		}

	}

	public String getUiMode() {
		return uiMode;
	}

	public void setUiMode(String uiMode) {
		this.uiMode = uiMode;
	}

	public String save() {
		try {
			if (uiMode.equals(UIVariables.UI_MODES[0])) {
				commentprocess.create(reviewcommentsDto, benifiaryID, getUserDetails());
			} else if (uiMode.equals(UIVariables.UI_MODES[1])) {
				commentprocess.update(reviewcommentsDto, benifiaryID, getUserDetails());
			}
			return "BIView.xhtml?faces-redirect=true&" + UIVariables.PARAM_BENEFICARY_ID + "=" + benifiaryID + "&"
					+ UIVariables.PARAM_MODE + "=" + uiMode;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save Error", e.getMessage()));
			return "#";
		}

	}

	public Commentview() {

	}

	public ReviewCommentsDto getReviewcommentsDto() {
		return reviewcommentsDto;
	}

	public void setReviewcommentsDto(ReviewCommentsDto reviewcommentsDto) {
		this.reviewcommentsDto = reviewcommentsDto;
	}
	public String getNavParam() {
		return "action=".concat(uiMode).concat("&beneficary=").concat(Long.toString(benifiaryID));
	}

}
