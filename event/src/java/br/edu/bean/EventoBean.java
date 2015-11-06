package br.edu.bean;

import br.edu.bean.util.RelatorioUtil;
import br.edu.evento.Evento;
import br.edu.evento.EventoDAO;
import br.edu.eventoinscricao.EventoInscricao;
import br.edu.eventoinscricao.EventoInscricaoDAO;
import br.edu.eventoinscricao.EventoInscricaoItens;
import br.edu.eventoinscricao.EventoInscricaoItensDAO;
import br.edu.eventoitens.EventoItensDAO;
import br.edu.usuario.Usuario;
import br.edu.util.UsuarioAtivo;
import br.edu.util.Util;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class EventoBean {

    private List<Evento> lista;
    private EventoDAO dao = new EventoDAO();
    private String pesquisaEvento;
    private Evento evento = new Evento();
    private EventoInscricao eventoInscricao = new EventoInscricao();
    private String usuarioAtivo;
    private List<EventoInscricao> listaEI;

    public List<EventoInscricao> getListaEI() {
        EventoInscricaoDAO eiDAO = new EventoInscricaoDAO();
        listaEI = eiDAO.verificaInscricao(new ContextBean().getUser(), evento);
        return listaEI;
    }

    public String realizarInscricao() {
        EventoInscricaoDAO eiDAO = new EventoInscricaoDAO();
        Usuario usuario = new ContextBean().getUser();
        List<EventoInscricao> listaEi = eiDAO.verificaInscricao(usuario, evento);
        if (listaEi == null) {
            listaEi = new ArrayList<>();
        }
        if (listaEi.size() != 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Usuário Já cadastrado para este Evento");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else {
            EventoInscricao ei = new EventoInscricao();
            ei.setAtivo(false);
            ei.setData(new Date());
            ei.setHora(new Date());
            ei.setUsuario(usuario);
            SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");
            String strHora = hora.format(new Date(System.currentTimeMillis()));
            String strData = data.format(new Date(System.currentTimeMillis()));
            String codigo = usuario.getId().toString() + strHora.replaceAll(":", "")
                    + strData.replace("/", "");
            ei.setId(codigo);
            ei.setEvento(evento);

            try {
                Util.mandarEmail("eventsoftwareinfo@gmail.com", "pedro040908",
                        evento.getDono().getEmail(), "Inscrição [" + evento.getDescricao() + "]",
                        "Olá " + evento.getDono().getName() + ","
                        + "\nInscrição Realizada para o evento."
                        + "\nLocal do Evento: " + evento.getLocal().getDescricao() + ", " + evento.getLocal().getEndereco() + ", "
                        + evento.getLocal().getCidade().getDescricao() + "-" + evento.getLocal().getCidade().getEstado()
                        + "\nPeríodo: " + evento.getDataInicio() + " á " + evento.getDataFim()
                        + "\nParticipante: " + eventoInscricao.getUsuario().getName()
                        + "\nNúmero de insrição: " + eventoInscricao.getId()
                        + "\nSituação: " + eventoInscricao.getSituacao()
                        + "\nData da Inscrição: " + eventoInscricao.getData()
                        + "\n\n att, \nEquipe Event.");
            } catch (Exception e) {

            }

            eiDAO.add(ei);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Inscriçao Realizada!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        return "";
    }

    public String getUsuarioAtivo() {
        usuarioAtivo = new ContextBean().getUser().getName();
        return usuarioAtivo;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public EventoDAO getDao() {
        return dao;
    }

    public void setDao(EventoDAO dao) {
        this.dao = dao;
    }

    public String getPesquisaEvento() {
        return pesquisaEvento;
    }

    public void setPesquisaEvento(String pesquisaEvento) {
        this.pesquisaEvento = pesquisaEvento;
    }

    public List<Evento> getLista() {
        lista = dao.listaEventosAtivos(pesquisaEvento);
        return lista;
    }

    public EventoInscricao getEventoInscricao() {
        return eventoInscricao;
    }

    public void setEventoInscricao(EventoInscricao eventoInscricao) {
        this.eventoInscricao = eventoInscricao;
    }

    private StreamedContent arqRelCompInscr;

    public StreamedContent getArqRelCompInscr() {
        if (eventoInscricao.isAtivo()) {
            FacesContext context = FacesContext.getCurrentInstance();
            RelatorioUtil relatorioUtil = new RelatorioUtil();
            HashMap parametrosRelatorio = new HashMap<>();
            parametrosRelatorio.put("sql", eventoInscricao.getId());
            try {
                this.arqRelCompInscr = relatorioUtil.geraRelatorio(
                        parametrosRelatorio, "comp_inscricao",
                        "compInscricao", 1);
            } catch (Exception e) {
                context.addMessage(null, new FacesMessage(e.getMessage()));
            }

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Comprovante de Inscrição Gerado!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return arqRelCompInscr;
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Inscrição Inativa!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return null;
    }

    private StreamedContent arqRelProgramacao;

    public StreamedContent getArqRelProgramacao() {
        FacesContext context = FacesContext.getCurrentInstance();
        RelatorioUtil relatorioUtil = new RelatorioUtil();
        HashMap parametrosRelatorio = new HashMap<>();
        parametrosRelatorio.put("sql", evento.getId());
        try {
            this.arqRelProgramacao = relatorioUtil.geraRelatorio(
                    parametrosRelatorio, "programacao",
                    "programacao", 1);
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(e.getMessage()));
        }

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Programação Gerada!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return arqRelProgramacao;
    }

    public String remover() {
        EventoInscricaoItensDAO eiidao = new EventoInscricaoItensDAO();
        List<EventoInscricaoItens> lista = eiidao.checkExists("eventoInscricao", eventoInscricao);
        if (!lista.isEmpty()) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "ERRO: Existem Itens para Essa Inscrição, exclua o itens primeiro!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            EventoInscricaoDAO eiDAo = new EventoInscricaoDAO();
            eiDAo.remove(eventoInscricao);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Inscrição excluída!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return "";
    }

}
