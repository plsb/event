/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.eventocheckin;

import br.edu.eventoinscricao.EventoInscricaoItens;
import br.edu.eventoinscricao.EventoInscricaoItensDAO;
import br.edu.eventoitens.EventoItens;
import br.edu.util.GenericDAO;
import br.edu.util.HibernateUtil;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Pedro Saraiva
 */
public class EventoCheckinDAO extends GenericDAO<EventoCheckin> {

    public EventoCheckinDAO() {
        super(EventoCheckin.class);
    }

    public List<EventoCheckin> listaCheckins(EventoItens ev) {
        List<EventoCheckin> list = null;
        try {
            this.setSessao(HibernateUtil.getSessionFactory().openSession());
            this.setTransacao(getSessao().beginTransaction());

            EventoInscricaoItensDAO evDAO = new EventoInscricaoItensDAO();
            List<EventoInscricaoItens> lista = evDAO.checkExists("eventoItens", ev);
            if (lista != null) {
                list = this.getSessao().createCriteria(EventoCheckin.class).
                        add(Restrictions.in("eventoItem", lista)).
                        addOrder(Order.desc("data")).
                        addOrder(Order.desc("hora")).
                        list();

            }

        } catch (HibernateException e) {
            System.out.println(e.getMessage() + " | " + e.getCause());

        } finally {

            getSessao().close();
        }
        return list;
    }

}
