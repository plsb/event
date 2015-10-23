package br.edu.ws;

import br.edu.evento.Evento;
import br.edu.evento.EventoDAO;
import br.edu.eventoinscricao.EventoInscricao;
import br.edu.eventoinscricao.EventoInscricaoDAO;
import br.edu.eventoinscricao.EventoInscricaoItens;
import br.edu.eventoinscricao.EventoInscricaoItensDAO;
import br.edu.eventoitens.EventoItens;
import br.edu.eventoitens.EventoItensDAO;
import br.edu.eventousuario.EventoUsuario;
import br.edu.eventousuario.EventoUsuarioDAO;
import br.edu.usuario.Usuario;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class EventWS {

    @WebMethod
    public List<Evento> listaEventos() {
        EventoDAO eDAO = new EventoDAO();
        return eDAO.listaEventosAtivos("");
    }

    @WebMethod
    public List<EventoUsuario> listaUsuariosEvento(int idEvento) {
        EventoDAO eDAO = new EventoDAO();
        Evento e = eDAO.checkExists("id", idEvento).get(0);

        if (e != null) {
            EventoUsuarioDAO euDAO = new EventoUsuarioDAO();
            return euDAO.checkExists("evento", e);
        }
        
        return null;
    }
    
    @WebMethod
    public List<EventoItens> listaEventosItens(int idEvento){
         EventoDAO eDAO = new EventoDAO();
        Evento e = eDAO.checkExists("id", idEvento).get(0);

        if (e != null) {
            EventoItensDAO eiDAO = new EventoItensDAO();
            return eiDAO.checkExists("evento", e);
        }
        
        return null;
    }
    
    @WebMethod
    public List<EventoInscricaoItens> listaEventoInscricaoItens(int idEvento){
        EventoDAO eDAO = new EventoDAO();
        Evento e = eDAO.checkExists("id", idEvento).get(0);
        
        EventoInscricaoDAO eiDAO = new EventoInscricaoDAO();
        List<EventoInscricao> listaEI = eiDAO.checkExists("evento", e);

        if (e != null) {
            EventoInscricaoItensDAO eiiDAO = new EventoInscricaoItensDAO();
            return eiiDAO.checkExists("eventoInscricao", listaEI);
        }
        
        return null;
    }

}
