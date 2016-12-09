package eu.ddmore.workflow.bwf.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import eu.ddmore.workflow.bwf.client.model.User;
import eu.ddmore.workflow.bwf.client.service.UserService;
import eu.ddmore.workflow.bwf.client.util.Primitives;
import eu.ddmore.workflow.bwf.web.spring.AppContext;

@FacesConverter("userConverter")
public class UserConverter implements Converter {
	
	private UserService userService;
 
	@Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (Primitives.isNotEmpty(value) && !value.equalsIgnoreCase("null")) {
        	return getUserService().getByUsername(value, false, false, false);
        }
        return null;
    }

	@Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null && object instanceof User) {
            return ((User) object).getUsername();
        }
        return null;
    }

	private UserService getUserService() {
		if (this.userService == null) {
			this.userService = AppContext.getBean(UserService.class);
		}
		return this.userService;
	}   
}
