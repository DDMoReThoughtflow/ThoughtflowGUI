package eu.ddmore.workflow.bwf.web.backing;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import eu.ddmore.workflow.bwf.client.util.Primitives;
import eu.ddmore.workflow.bwf.web.util.MessageProvider;

@ManagedBean
@ViewScoped
public class LoginBacking extends BaseBacking {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	
	@PostConstruct
	public void init() {
		if (Primitives.isNotEmpty(getParameter("login_error"))) {
			addMessageToComp("mainForm", FacesMessage.SEVERITY_ERROR, MessageProvider.getValue("v_login_error"), "");
		}
	}
	
	public void preRender() {
		if (getSessionBean().isLoggedIn()){
			String redirectPage = null;
			try {
				redirectPage = getWebProperties().getProperty("defaultPage");	
				getExternalContext().redirect(redirectPage);
			} catch (Exception ex) {
				getLog().error("Error redirect tp page: " + redirectPage, ex);
			}
		}
	}
	
	public String login() {
    	dispatchForward("/j_spring_security_check");
        return null;
    }

    public String logout() {
    	dispatchForward("/j_spring_security_logout");
    	return null;
    }
    
    private void dispatchForward(String url) {
		try {
        	ExternalContext context = getExternalContext();
        	RequestDispatcher dispatcher = ((ServletRequest)context.getRequest()).getRequestDispatcher(url);
			dispatcher.forward((ServletRequest)context.getRequest(), (ServletResponse) context.getResponse());
			getFacesContext().responseComplete();
		} catch (ServletException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
    
    @Override
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
