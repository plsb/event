/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.evento;

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
public class EventoDAO extends GenericDAO<Evento>{

    public EventoDAO() {
        super(Evento.class);
    }
    
    public List<Evento> listaEventosAtivos(String descricao){
        List<Evento> list=null;
        try {           
            this.setSessao(HibernateUtil.getSessionFactory().openSession());
            this.setTransacao(getSessao().beginTransaction());
            if(descricao==null){
                descricao = "";
            }
            if( descricao.equals("")){
                list = this.getSessao().createCriteria(Evento.class).
                    add(Restrictions.ge("dataFim", new Date())).
                    addOrder(Order.desc("dataInicio")).
                    list();
            } else {
                list = this.getSessao().createCriteria(Evento.class).
                    add(Restrictions.ge("dataFim", new Date())).
                    add(Restrictions.like("descricao", descricao, MatchMode.START)).
                    addOrder(Order.desc("dataInicio")).
                    list();
            }
            
           
        } catch (HibernateException e) {
            System.out.println(e.getMessage()+" | "+e.getCause());
            
        } finally {
            
            getSessao().close();
        }
        return list;
    }
    
    
}
