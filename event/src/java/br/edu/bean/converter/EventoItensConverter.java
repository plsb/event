/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.bean.converter;

import br.edu.eventoitens.EventoItens;
import br.edu.eventoitens.EventoItensDAO;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Pedro Saraiva
 */
@FacesConverter(forClass = EventoItens.class)
public class EventoItensConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext contex, UIComponent componente, String value) {
        EventoItens ev = new EventoItens();
        if (!value.isEmpty()) {
            Integer codigo = Integer.valueOf(value);
            try {
                EventoItensDAO eDAO = new EventoItensDAO();
                return eDAO.checkExists("id", codigo).get(0);
            } catch (Exception e) {
                throw new ConverterException("Não foi possível encontrar código " + value + "." + e.getMessage());
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
        if (value instanceof EventoItens) {
            EventoItens e = (EventoItens) value;
            return e.getId().toString();
        }
        return "";
    }

}
