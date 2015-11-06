package br.edu.ws;

import br.edu.evento.Evento;
import br.edu.evento.EventoDAO;
import br.edu.eventocheckin.EventoCheckin;
import br.edu.eventocheckin.EventoCheckinDAO;
import br.edu.eventoinscricao.EventoInscricao;
import br.edu.eventoinscricao.EventoInscricaoDAO;
import br.edu.eventoinscricao.EventoInscricaoItens;
import br.edu.eventoinscricao.EventoInscricaoItensDAO;
import br.edu.eventoitens.EventoItens;
import br.edu.eventoitens.EventoItensDAO;
import br.edu.eventousuario.EventoUsuario;
import br.edu.eventousuario.EventoUsuarioDAO;
import br.edu.usuario.Usuario;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public class EventWS {

    @WebMethod
    public List<Evento> listaEventos(int id) {
        EventoDAO eDAO = new EventoDAO();
        return eDAO.checkExists("id", id);
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
    public List<EventoItens> listaEventosItens(int idEvento) {
        EventoDAO eDAO = new EventoDAO();
        Evento e = eDAO.checkExists("id", idEvento).get(0);

        if (e != null) {
            EventoItensDAO eiDAO = new EventoItensDAO();
            return eiDAO.checkExists("evento", e);
        }

        return null;
    }

    @WebMethod
    public List<EventoInscricaoItens> listaEventoInscricaoItens(int idEvento) {
        EventoDAO eDAO = new EventoDAO();
        Evento e = eDAO.checkExists("id", idEvento).get(0);

        EventoInscricaoDAO eiDAO = new EventoInscricaoDAO();
        List<EventoInscricao> listaEI = eiDAO.checkExists("evento", e);

        if (e != null) {
            EventoInscricaoItensDAO eiiDAO = new EventoInscricaoItensDAO();
            return eiiDAO.verificaItensInscricao(listaEI);
        }

        return null;
    }

    @WebMethod
    public boolean insereCheckin(@WebParam(name = "iditeminscricao") String id,
            @WebParam(name = "data") String data,
            @WebParam(name = "hora") String hora) {
        EventoInscricaoItensDAO eiDAO = new EventoInscricaoItensDAO();
        List<EventoInscricaoItens> eventoInscricaoItens = eiDAO.checkExists("id", Integer.parseInt(id));
        if (!eventoInscricaoItens.isEmpty()) {
            EventoCheckinDAO ecDAO = new EventoCheckinDAO();
            List<EventoCheckin> eventoCheckins = ecDAO.checkExists("eventoItem", eventoInscricaoItens.get(0));
            if (eventoCheckins.isEmpty()) {

                EventoCheckin ec = new EventoCheckin();
                ec.setEventoItem(eventoInscricaoItens.get(0));
                try {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    java.sql.Date date = new java.sql.Date(format.parse(data).getTime());
                    ec.setData(date);
                } catch (Exception e) {

                }

                try {
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    java.sql.Date date = new java.sql.Date(format.parse(hora).getTime());
                    ec.setHora(date);
                } catch (Exception e) {

                }
                ecDAO.add(ec);

            }

        }
        return true;

    }

}
