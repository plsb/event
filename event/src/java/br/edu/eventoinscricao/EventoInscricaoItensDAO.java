/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.eventoinscricao;

import br.edu.eventoitens.EventoItens;
import br.edu.util.GenericDAO;
import br.edu.util.HibernateUtil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Pedro Saraiva
 */
public class EventoInscricaoItensDAO extends GenericDAO<EventoInscricaoItens>{

    public EventoInscricaoItensDAO() {
        super(EventoInscricaoItens.class);
    }
    
    public List<EventoInscricaoItens> verificaItensInscricao(EventoItens eventoItem, EventoInscricao eventoInscricao) {
        List<EventoInscricaoItens> list=null;
        try {
            this.setSessao(HibernateUtil.getSessionFactory().openSession());
            this.setTransacao(getSessao().beginTransaction());
            list = this.getSessao().createCriteria(EventoInscricaoItens.class).
                    add(Restrictions.eq("eventoItens", eventoItem)).
                    add(Restrictions.eq("eventoInscricao", eventoInscricao)).
                    list();
           
        } catch (HibernateException e) {
            System.out.println(e.getMessage()+" | "+e.getCause());
            
        } finally {
            
            getSessao().close();
        }
        return list;
    
    }
    
}
