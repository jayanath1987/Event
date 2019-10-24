package lk.icta.events.eventAttendance.view.authentication;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lk.icta.events.eventAttendance.service.logger.ProcessLogger;
import lk.icta.events.eventAttendance.view.InterfaceView;

@Component
@Scope("view")
public class LoginView implements InterfaceView {
	/**
	 * User Login Management
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(ProcessLogger.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	private String userName;
	private String password;

	public LoginView() {
	}

	public void login() {
		logger.info("***** start login Request: " + userName + " *****");
		try {
			Authentication request = new UsernamePasswordAuthenticationToken(userName, password);
			Authentication result = authenticationManager.authenticate(request);
			SecurityContextHolder.getContext().setAuthentication(result);
			if (!(result instanceof AnonymousAuthenticationToken) && result.isAuthenticated()) {
				redirectToRoleIndex(result);
			}
			logger.info("***** end login Request: " + userName + " *****");
		} catch (AuthenticationException ex) {
			logger.info("***** Error login Request: " + userName + " *****", ex);
			ex.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Error", "Invalid Username or Password"));

		} catch (IOException e) {
			logger.info("***** Error login Request: " + userName + " *****");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Error", "Invalid Username or Password"));
		} catch (RuntimeException e) {
			logger.info("***** Error login Request: " + userName + " *****");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Error", "Invalid Username or Password"));
		}
	}

	private void redirectToRoleIndex(Authentication authentication) throws IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.redirect(externalContext.getRequestContextPath() + "/index/index.xhtml");
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
