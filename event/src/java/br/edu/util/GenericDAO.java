/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.util;

import com.lowagie.text.pdf.PdfName;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author @author JOABB
 */
@SuppressWarnings("unchecked")
public abstract class GenericDAO<T> {

    /*
     * Essa classe possui operações comuns a todas as classes persistentes
     * para utilizar-la deverá criar uma classe DAO específica para a classe
     * persistente e extends GenericDAO<T> onde T é a classe Persistente 
     */
    private Session sessao;
    private Transaction transacao;
    private Class classe;

    public GenericDAO(Class classe) {
        this.classe = classe;
    }

    public boolean add(T entity) {
        try {
            this.setSessao(HibernateUtil.getSessionFactory().openSession());
            this.setTransacao(getSessao().beginTransaction());
            this.getSessao().save(entity);
            this.getTransacao().commit();

           
        } catch (HibernateException e) {
            System.out.println(e.getMessage()+" | "+e.getCause());
            System.out.println("Não foi possível inserir " + entity.getClass()
                    + ". Erro: " + e.getMessage());
            return false;
        } finally {
            
            getSessao().close();
        }
        return true;
    }

    public boolean update(T entity) {
        try {
            this.setSessao(HibernateUtil.getSessionFactory().openSession());
            this.setTransacao(getSessao().beginTransaction());
            this.getSessao().merge(entity);
            this.getTransacao().commit();
            
        } catch (HibernateException e) {
            System.out.println( "Não foi possível atualizar " + entity.getClass()
                    + ". Erro: " + e.getMessage());
            return false;
        } finally {
            getSessao().close();

        }
        return true;
    }

    public boolean remove(T entity) {
        try {
            this.setSessao(HibernateUtil.getSessionFactory().openSession());
            this.setTransacao(getSessao().beginTransaction());
            this.getSessao().delete(entity);
            this.getTransacao().commit();
            
        } catch (HibernateException e) {
            System.out.println( "Não foi possível remover " + entity.getClass()
                    + ". Erro: " + e.getMessage());
            return false;
        } finally {
            getSessao().close();

        }
        return true;
    }

    public List<T> list() {
        List<T> lista = null;
        try {
            this.setSessao(HibernateUtil.getSessionFactory().openSession());
            setTransacao(getSessao().beginTransaction());
            lista = this.getSessao().createCriteria(classe).list();
            
        } catch (Throwable e) {
            if (getTransacao().isActive()) {
                getTransacao().rollback();
            }
            System.out.println( "Não foi possível listar: " + e.getMessage());
        } finally {
            sessao.close();
        }
        return lista;
    }
    
    
    
    /*
     * ao passar uma chave primária
     * ele retorna um objeto referente a chave primária
     */
    public T carregaChavePrimaria(int chavePrimaria) {
        try {
            sessao = HibernateUtil.getSessionFactory().openSession();
            Object o = sessao.load(classe, chavePrimaria);
            return (T) o;
        } catch (HibernateException e) {
            System.out.println("Erro ao carregar por chave primária" + e.getMessage());
        } finally {
            sessao.close();
        }
        return null;
    }

    public List<T> checkExists(String campo, Object valor) {
        List<T> lista = null;
        try {
            this.setSessao(HibernateUtil.getSessionFactory().openSession());
            setTransacao(getSessao().beginTransaction());
            lista = this.getSessao().createCriteria(classe).add(Restrictions.eq(campo, valor)).list();
            
        } catch (Throwable e) {
            if (getTransacao().isActive()) {
                getTransacao().rollback();
            }
            System.out.println( "Não foi possível listar: " + e.getMessage());
        } finally {
            sessao.close();
        }
        return lista;

    }
    
    public List<T> checkExists(String campo, List<Object> valores) {
        List<T> lista = null;
        try {
            this.setSessao(HibernateUtil.getSessionFactory().openSession());
            setTransacao(getSessao().beginTransaction());
            lista = this.getSessao().createCriteria(classe).add(Restrictions.in(campo, valores)).list();
            
        } catch (Throwable e) {
            if (getTransacao().isActive()) {
                getTransacao().rollback();
            }
            System.out.println( "Não foi possível listar: " + e.getMessage());
        } finally {
            sessao.close();
        }
        return lista;

    }
    
    public List<T> checkExistsAsc(String campo, Object valor, String orderAsc) {
        List<T> lista = null;
        try {
            this.setSessao(HibernateUtil.getSessionFactory().openSession());
            setTransacao(getSessao().beginTransaction());
            lista = this.getSessao().createCriteria(classe).add(Restrictions.eq(campo, valor)).
                    addOrder(Order.asc(orderAsc)).list();
            
        } catch (Throwable e) {
            if (getTransacao().isActive()) {
                getTransacao().rollback();
            }
            System.out.println( "Não foi possível listar: " + e.getMessage());
        } finally {
            sessao.close();
        }
        return lista;

    }
    
    public List<T> checkExistsDesc(String campo, Object valor, String orderAsc) {
        List<T> lista = null;
        try {
            this.setSessao(HibernateUtil.getSessionFactory().openSession());
            setTransacao(getSessao().beginTransaction());
            lista = this.getSessao().createCriteria(classe).add(Restrictions.eq(campo, valor)).
                    addOrder(Order.desc(orderAsc)).list();
            
        } catch (Throwable e) {
            if (getTransacao().isActive()) {
                getTransacao().rollback();
            }
            System.out.println( "Não foi possível listar: " + e.getMessage());
        } finally {
            sessao.close();
        }
        return lista;

    }
    
    public List<T> checkExistsLike(String campo, String valor) {
        List<T> lista = null;
        try {
            this.setSessao(HibernateUtil.getSessionFactory().openSession());
            setTransacao(getSessao().beginTransaction());
            lista = this.getSessao().createCriteria(classe).add(Restrictions.like(campo, valor, MatchMode.START)).
                   list();
            
        } catch (Throwable e) {
            if (getTransacao().isActive()) {
                getTransacao().rollback();
            }
            System.out.println( "Não foi possível listar: " + e.getMessage());
        } finally {
            sessao.close();
        }
        return lista;

    }

    /**
     * @return the sessao
     */
    public Session getSessao() {
        return sessao;
    }

    /**
     * @param sessao the sessao to set
     */
    public void setSessao(Session sessao) {
        this.sessao = sessao;
    }

    /**
     * @return the transacao
     */
    public Transaction getTransacao() {
        return transacao;
    }

    /**
     * @param transacao the transacao to set
     */
    public void setTransacao(Transaction transacao) {
        this.transacao = transacao;
    }
}
