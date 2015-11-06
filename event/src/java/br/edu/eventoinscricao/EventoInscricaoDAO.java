/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.eventoinscricao;

import br.edu.evento.Evento;
import br.edu.usuario.Usuario;
import br.edu.usuario.UsuarioDAO;
import br.edu.util.GenericDAO;
import br.edu.util.HibernateUtil;
import java.util.ArrayList;
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
public class EventoInscricaoDAO extends GenericDAO<EventoInscricao> {

    public EventoInscricaoDAO() {
        super(EventoInscricao.class);
    }

    public List<EventoInscricao> listaInscricoesEvento(String descricao, Evento evento) {
        List<EventoInscricao> list = null;
        try {
            this.setSessao(HibernateUtil.getSessionFactory().openSession());
            this.setTransacao(getSessao().beginTransaction());
            List<Usuario> listUsuario = new ArrayList<>();
            if (descricao != null) {
                UsuarioDAO u = new UsuarioDAO();
                listUsuario = u.checkExistsLike("name", descricao);
            }
            if (listUsuario.isEmpty()) {
                list = this.getSessao().createCriteria(EventoInscricao.class).
                        add(Restrictions.eq("evento", evento)).
                        addOrder(Order.asc("data")).
                        list();
            } else {
                list = this.getSessao().createCriteria(EventoInscricao.class).
                        add(Restrictions.eq("evento", evento)).
                        add(Restrictions.in("usuario", listUsuario)).
                        addOrder(Order.asc("data")).
                        list();
            }

        } catch (HibernateException e) {
            System.out.println(e.getMessage() + " | " + e.getCause());

        } finally {

            getSessao().close();
        }
        return list;
    }

    public List<EventoInscricao> verificaInscricao(Usuario usuario, Evento evento) {
        List<EventoInscricao> list = null;
        try {
            this.setSessao(HibernateUtil.getSessionFactory().openSession());
            this.setTransacao(getSessao().beginTransaction());
            list = this.getSessao().createCriteria(EventoInscricao.class).
                    add(Restrictions.eq("usuario", usuario)).
                    add(Restrictions.eq("evento", evento)).
                    list();

        } catch (HibernateException e) {
            System.out.println(e.getMessage() + " | " + e.getCause());

        } finally {

            getSessao().close();
        }
        return list;

    }
    
    
}
