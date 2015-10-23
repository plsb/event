/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.bean;

import br.edu.bean.util.RelatorioUtil;
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
import br.edu.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class EventoUsuarioBean {

    private List<Evento> listaEvento;
    private EventoDAO eDAO = new EventoDAO();
    private Evento evento;
    private String pesquisaEvento;
    private List<EventoInscricao> listaInscricoes;
    private List<EventoInscricaoItens> listaInscricoesItens;
    private EventoInscricao eventoInscricao;
    private EventoInscricaoItens eventoInscricaoItens;

    public String mudaSituacao() {
        if (eventoInscricao.isAtivo() == false) {
            try {
                Util.mandarEmail("eventsoftwareinfo@gmail.com", "pedro040908",
                        eventoInscricao.getUsuario().getEmail(), "Situação de Inscrição " + eventoInscricao.getId()
                        + "[" + eventoInscricao.getEvento().getDescricao() + "]",
                        "Olá " + eventoInscricao.getUsuario().getName() + ","
                        + "\nSua inscrição no evento " + eventoInscricao.getEvento().getDescricao() + " foi Ativada,"
                        + " seu comprovante de inscrição está disponível."
                        + "\n\n att, \nEquipe Event.");
            } catch (Exception e) {

            }
        }
        EventoInscricaoDAO evDAO = new EventoInscricaoDAO();
        eventoInscricao.setAtivo(true);
        evDAO.update(eventoInscricao);
        ativaItensInscricao(eventoInscricao);
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Inscrição " + eventoInscricao.getId()
                + ", ativada!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return "";
    }

    private void ativaItensInscricao(EventoInscricao ei) {
        EventoInscricaoItensDAO daoEII = new EventoInscricaoItensDAO();
        List<EventoInscricaoItens> listaEii = daoEII.checkExists("eventoInscricao", ei);
        for (EventoInscricaoItens item : listaEii) {
            item.setValidada(true);
            daoEII.update(item);
        }
    }

    public EventoInscricao getEventoInscricao() {
        return eventoInscricao;
    }

    public void setEventoInscricao(EventoInscricao eventoInscricao) {
        this.eventoInscricao = eventoInscricao;
    }

    public List<EventoInscricao> getListaInscricoes() {
        EventoInscricaoDAO dao = new EventoInscricaoDAO();
        return dao.listaInscricoesEvento(pesquisaEvento, evento);
    }

    public void setListaInscricoes(List<EventoInscricao> listaInscricoes) {
        this.listaInscricoes = listaInscricoes;
    }

    public String getPesquisaEvento() {
        return pesquisaEvento;
    }

    public void setPesquisaEvento(String pesquisaEvento) {
        this.pesquisaEvento = pesquisaEvento;
    }

    public EventoDAO geteDAO() {
        return eDAO;
    }

    public void seteDAO(EventoDAO eDAO) {
        this.eDAO = eDAO;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public List<Evento> getListaEvento() {
        return eDAO.checkExistsDesc("dono", new ContextBean().getUser(), "dataInicio");
    }

    public void setListaEvento(List<Evento> listaEvento) {
        this.listaEvento = listaEvento;
    }

    public List<EventoInscricaoItens> getListaInscricoesItens() {
        EventoInscricaoItensDAO daoEII = new EventoInscricaoItensDAO();
        List<EventoInscricaoItens> listaEii = daoEII.checkExists("eventoInscricao", eventoInscricao);
        return listaEii;
    }

    public void setListaInscricoesItens(List<EventoInscricaoItens> listaInscricoesItens) {
        this.listaInscricoesItens = listaInscricoesItens;
    }

    public EventoInscricaoItens getEventoInscricaoItens() {
        return eventoInscricaoItens;
    }

    public void setEventoInscricaoItens(EventoInscricaoItens eventoInscricaoItens) {
        this.eventoInscricaoItens = eventoInscricaoItens;
    }

    public String situacaoItem() {
        EventoInscricaoItensDAO evDAO = new EventoInscricaoItensDAO();
        eventoInscricaoItens.setValidada(!eventoInscricaoItens.isValidada());
        evDAO.update(eventoInscricaoItens);

        try {
            Util.mandarEmail("eventsoftwareinfo@gmail.com", "pedro040908",
                    eventoInscricao.getUsuario().getEmail(), "Situação de Inscrição " + eventoInscricao.getId()
                    + "[" + eventoInscricao.getEvento().getDescricao() + "]",
                    "Olá " + eventoInscricao.getUsuario().getName() + ","
                    + "\nO item " + eventoInscricaoItens.getEventoItens().getDescricao() + ", "
                    + "do evento " + eventoInscricaoItens.getEventoInscricao().getEvento().getDescricao()
                    + ", possui situação " + eventoInscricaoItens.getSituacao() + "."
                    + "\n\n att, \nEquipe Event.");
        } catch (Exception e) {

        }

        return "";
    }

    private String pesquisaChekin;

    public String getPesquisaChekin() {
        if(pesquisaChekin==null){
            pesquisaChekin="";
        }
        return pesquisaChekin;
    }

    public void setPesquisaChekin(String pesquisaChekin) {
        this.pesquisaChekin = pesquisaChekin;
    }

    public String pesquisarInscricaoChekinUsuario() {
        EventoInscricaoDAO eDAO = new EventoInscricaoDAO();
        List<EventoInscricao> listaInscricoes
                = eDAO.checkExists("id", pesquisaChekin);
        if (listaInscricoes.isEmpty()) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Inscrição não encontrada!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            if (listaInscricoes.get(0).isAtivo()) {

                EventoInscricaoItensDAO eiiDAO = new EventoInscricaoItensDAO();
                List<EventoInscricaoItens> eii = eiiDAO.verificaItensInscricao(evntoItem, listaInscricoes.get(0));
                if (eii.isEmpty()) {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Participante não possui inscrição no ítem selecionado!");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                } else {
                    if (eii.get(0).isValidada()) {

                        boolean fazCheckin = false;
                        EventoInscricaoItens itemDoEventoFazCheckin = null;
                        for (EventoInscricaoItens itens : eii) {
                            if (evntoItem.getId() == itens.getEventoItens().getId()) {
                                itemDoEventoFazCheckin = itens;
                                fazCheckin = true;
                            }
                        }

                        if (fazCheckin) {
                            EventoCheckinDAO ecDAO = new EventoCheckinDAO();
                            List<EventoCheckin> listaVerificacao = ecDAO.checkExists("eventoItem", itemDoEventoFazCheckin);
                            if (listaVerificacao.isEmpty()) {
                                EventoCheckin ec = new EventoCheckin();
                                ec.setData(new Date());
                                ec.setHora(new Date());
                                ec.setEventoItem(itemDoEventoFazCheckin);
                                ecDAO.add(ec);
                                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Checkin feito com sucesso!");
                                FacesContext.getCurrentInstance().addMessage(null, msg);
                            } else {
                                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Ja foi realizado Checkin!");
                                FacesContext.getCurrentInstance().addMessage(null, msg);
                            }
                        } else {
                            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Participante não possui inscrição no ítem selecionado!");
                            FacesContext.getCurrentInstance().addMessage(null, msg);
                        }
                    } else {
                        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Item de Inscrição Não esta Ativo!");
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                    }
                }
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Inscrição Não esta Ativa!");
                FacesContext.getCurrentInstance().addMessage(null, msg);

            }
        }
        pesquisaChekin="";
        return "";

    }

    private List<SelectItem> itensSelect;

    public List<SelectItem> getItensSelect() {
        this.itensSelect = new ArrayList<>();
        if (this.itensSelect.isEmpty()) {
            this.itensSelect = new ArrayList<SelectItem>();
            //ContextoBean contextoBean = scs.util.ContextoUtil.getContextoBean();

            EventoItensDAO dao = new EventoItensDAO();
            List<EventoItens> categorias = dao.checkExists("evento", evento);
            this.showDataSelectItens(this.itensSelect, categorias, "");
        }

        return itensSelect;
    }

    private void showDataSelectItens(List<SelectItem> select, List<EventoItens> itens, String prefixo) {

        SelectItem item = null;
        if (itens != null) {
            for (EventoItens eitem : itens) {
                item = new SelectItem(eitem, eitem.getDescricao() + ", " + eitem.getData() + " " + eitem.getHora());
                item.setEscape(false);

                select.add(item);

                //this.montaDadosSelect(select, usuario.getNome(), prefixo + "&nbsp;&nbsp;");
            }
        }
    }

    private EventoItens evntoItem;

    public EventoItens getEvntoItem() {
        return evntoItem;
    }

    public void setEvntoItem(EventoItens evntoItem) {
        this.evntoItem = evntoItem;
    }
    
    private List<EventoCheckin> listaCheckin;

    public List<EventoCheckin> getListaCheckin() {
        EventoCheckinDAO ecDAo = new EventoCheckinDAO();
        if(evntoItem!=null){
            listaCheckin = ecDAo.listaCheckins(evntoItem);
        } else {
            listaCheckin = new ArrayList<>();
        }
        
        return listaCheckin;
    }

    public void setListaCheckin(List<EventoCheckin> listaCheckin) {
        this.listaCheckin = listaCheckin;
    }
    
    private List<Evento> listaEventosParticipante;

    public List<Evento> getListaEventosParticipante() {
        Usuario u = new ContextBean().getUser();
        if(u!=null){
            listaEventosParticipante = new ArrayList<>();
            EventoInscricaoDAO eiDAO = new EventoInscricaoDAO();
            List<EventoInscricao> listaInscricoes = eiDAO.checkExists("usuario", u);
            for (EventoInscricao listaInscricoe : listaInscricoes) {
                listaEventosParticipante.add(listaInscricoe.getEvento());
            }
            
        } else {
            listaEventosParticipante = new ArrayList<>();
        }
        return listaEventosParticipante;
    }

    public void setListaEventosParticipante(List<Evento> listaEventosParticipante) {
        this.listaEventosParticipante = listaEventosParticipante;
    }
    
    private StreamedContent arqRelFrequencia;

    public StreamedContent getArqRelFrequencia() {
        if (evntoItem!=null) {
            FacesContext context = FacesContext.getCurrentInstance();
            RelatorioUtil relatorioUtil = new RelatorioUtil();
            HashMap parametrosRelatorio = new HashMap<>();
            parametrosRelatorio.put("sql", evntoItem.getId());
            try {
                this.arqRelFrequencia = relatorioUtil.geraRelatorio(
                        parametrosRelatorio, "frequencia_itens",
                        "frequencia_itens", 1);
            } catch (Exception e) {
                context.addMessage(null, new FacesMessage(e.getMessage()));
            }

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Relatório Gerado!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return arqRelFrequencia;
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Selecione o Item!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return null;
    }

    public void setArqRelFrequencia(StreamedContent arqRelFrequencia) {
        this.arqRelFrequencia = arqRelFrequencia;
    }
    
    private StreamedContent arqRelInscritos;

    public StreamedContent getArqRelInscritos() {
        if (evntoItem!=null) {
            FacesContext context = FacesContext.getCurrentInstance();
            RelatorioUtil relatorioUtil = new RelatorioUtil();
            HashMap parametrosRelatorio = new HashMap<>();
            parametrosRelatorio.put("sql", evntoItem.getId());
            try {
                this.arqRelFrequencia = relatorioUtil.geraRelatorio(
                        parametrosRelatorio, "inscritos_item_evento",
                        "inscritos_item_evento", 1);
            } catch (Exception e) {
                context.addMessage(null, new FacesMessage(e.getMessage()));
            }

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Relatório Gerado!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return arqRelFrequencia;
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Selecione o Item!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return null;
    }
    

    private List<EventoUsuario> listausuarioMobile;

    public List<EventoUsuario> getListausuarioMobile() {
         EventoUsuarioDAO euDAO = new EventoUsuarioDAO();
        return euDAO.checkExists("evento", evento);
    }

    public void setListausuarioMobile(List<EventoUsuario> listausuarioMobile) {
        this.listausuarioMobile = listausuarioMobile;
    }
    
    private EventoUsuario eventoUsuarioMobile;

    public EventoUsuario getEventoUsuarioMobile() {
        return eventoUsuarioMobile;
    }

    public void setEventoUsuarioMobile(EventoUsuario eventoUsuarioMobile) {
        this.eventoUsuarioMobile = eventoUsuarioMobile;
    }

    public String removerUsuarioMobile(){
        EventoUsuarioDAO euDAO = new EventoUsuarioDAO();
        euDAO.remove(eventoUsuarioMobile);
        return "";
    }
    
    public String novoUsuarioMobile(){
        eventoUsuarioMobile = new EventoUsuario();
        return "/private/usuariomobileedicao.jsf";
    }
    
    private String confirmarSenhaUsuarioMobile;

    public String getConfirmarSenhaUsuarioMobile() {
        return confirmarSenhaUsuarioMobile;
    }

    public void setConfirmarSenhaUsuarioMobile(String confirmarSenhaUsuarioMobile) {
        this.confirmarSenhaUsuarioMobile = confirmarSenhaUsuarioMobile;
    }
    
    public String salvarUsuarioMobile(){
        if(eventoUsuarioMobile.getLogin().equals("")){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Informe o Login!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "";
        } else if(eventoUsuarioMobile.getNome().equals("")){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Informe o Nome!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "";
        } else if(eventoUsuarioMobile.getSenha().equals("") || confirmarSenhaUsuarioMobile.equals("")){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Informe a Senha!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "";
        } else if(!eventoUsuarioMobile.getSenha().equals(confirmarSenhaUsuarioMobile)){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Senha digitada incorretamente!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "";
        } else {
            EventoUsuarioDAO euDAO = new EventoUsuarioDAO();
            eventoUsuarioMobile.setEvento(evento);
            eventoUsuarioMobile.setNome(eventoUsuarioMobile.getNome().toUpperCase());
            if(evento.getId()==0){
                euDAO.add(eventoUsuarioMobile);
            } else {
                euDAO.update(eventoUsuarioMobile);
            }
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Cadastro de Usuário Mobile Realizado com Sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        
        return "/private/usuariosmobile.jsf";
    }
    
    public String proximaTelaCheckin(){
        if(evntoItem==null){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Informe o Item!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "";
        }
        return "/private/checkin.jsf";
    }
       
    
        
}
