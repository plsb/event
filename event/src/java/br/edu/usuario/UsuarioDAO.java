
package br.edu.usuario;

import br.edu.util.GenericDAO;
import br.edu.util.HibernateUtil;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

public class UsuarioDAO extends GenericDAO<Usuario>{

    public UsuarioDAO() {
        super(Usuario.class);
    }
    
    public boolean autentica(String login, String senha){
        List<Usuario> list;
        try {
            this.setSessao(HibernateUtil.getSessionFactory().openSession());
            this.setTransacao(getSessao().beginTransaction());
            list = this.getSessao().createCriteria(Usuario.class).
                    add(Restrictions.eq("login", login)).
                    add(Restrictions.eq("password", senha)).
                    add(Restrictions.eq("active", true)).
                    list();
           
        } catch (HibernateException e) {
            System.out.println(e.getMessage()+" | "+e.getCause());
            
            return false;
        } finally {
            
            getSessao().close();
        }
        return list.size()>0;
    }
    
}
