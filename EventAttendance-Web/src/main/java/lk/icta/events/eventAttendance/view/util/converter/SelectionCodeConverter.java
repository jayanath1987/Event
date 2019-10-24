package lk.icta.events.eventAttendance.view.util.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import lk.icta.events.eventAttendance.service.dto.common.SelectionCodeDto;

@FacesConverter("selectionCodeConverter")
public class SelectionCodeConverter implements Converter {
 
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
            	SelectionCodeDto service = new SelectionCodeDto();
            	service.setId(Integer.valueOf(value));
            	return service;
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Selection."));
            }
        }
        else {
            return null;
        }
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return object.toString();
        }
        else {
            return null;
        }
    }   
}        