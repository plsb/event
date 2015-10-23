/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.eventoitens;

import br.edu.evento.Evento;
import br.edu.eventocheckin.EventoCheckin;
import br.edu.util.GenericDAO;
import br.edu.util.HibernateUtil;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Pedro Saraiva
 */
public class EventoItensDAO extends GenericDAO<EventoItens> {

    public EventoItensDAO() {
        super(EventoItens.class);
    }

    
}
