package br.edu.bean;

import br.edu.bean.util.RelatorioUtil;
import br.edu.evento.Evento;
import br.edu.eventocheckin.EventoCheckin;
import br.edu.eventocheckin.EventoCheckinDAO;
import br.edu.eventoinscricao.EventoInscricao;
import br.edu.eventoinscricao.EventoInscricaoDAO;
import br.edu.eventoinscricao.EventoInscricaoItens;
import br.edu.eventoinscricao.EventoInscricaoItensDAO;
import br.edu.eventoitens.EventoItens;
import br.edu.eventoitens.EventoItensDAO;
import br.edu.usuario.Usuario;
import br.edu.util.Util;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class EventoItensBean {

    private EventoInscricao eii = new EventoInscricao();
    private List<SelectItem> itensSelect;
    private EventoItens eventoItens;
    private List<EventoInscricaoItens> lEventoInscricaoItens = new ArrayList<>();
    private EventoInscricaoItens eventoInscricaoItens;

    public String remover() {
        if (eventoInscricaoItens != null) {
            EventoCheckinDAO ecDAO = new EventoCheckinDAO();
            List<EventoCheckin> eventoCheckins = ecDAO.checkExists("eventoItem", eventoInscricaoItens);
            if (eventoCheckins.isEmpty()) {
                EventoInscricaoItensDAO eiidao = new EventoInscricaoItensDAO();
                eiidao.remove(eventoInscricaoItens);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Item Removido!");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Item não pode ser removido!");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
        return "";
    }

    public EventoInscricaoItens getEventoInscricaoItens() {
        return eventoInscricaoItens;
    }

    public void setEventoInscricaoItens(EventoInscricaoItens eventoInscricaoItens) {
        this.eventoInscricaoItens = eventoInscricaoItens;
    }

    public List<EventoInscricaoItens> getlEventoInscricaoItens() {
        lEventoInscricaoItens = new ArrayList<>();
        EventoInscricaoItensDAO eiidao = new EventoInscricaoItensDAO();
        return eiidao.checkExists("eventoInscricao", eii);
    }

    public void setlEventoInscricaoItens(List<EventoInscricaoItens> lEventoInscricaoItens) {

        this.lEventoInscricaoItens = lEventoInscricaoItens;
    }

    public EventoItens getEventoItens() {
        return eventoItens;
    }

    public void setEventoItens(EventoItens eventoItens) {
        this.eventoItens = eventoItens;
    }

    public String inscreverItens() {
        if (eventoItens == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Não possui ítens para adicionar!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            EventoItens item = eventoItens;
            EventoInscricaoItensDAO eiiDAO = new EventoInscricaoItensDAO();
            List<EventoInscricaoItens> verificacao = eiiDAO.verificaItensInscricao(item, eii);
            if (verificacao == null) {
                verificacao = new ArrayList<>();
            }
            if (!verificacao.isEmpty()) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                        item.getDescricao() + ", já foi adicionado!");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
//                if (!eiiDAO.verificaDataEHora(eventoItens.getData(), eventoItens.getHora()).isEmpty()) {
//                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
//                            "Já existem um item com data e hora igual ao selecionado!");
//                    FacesContext.getCurrentInstance().addMessage(null, msg);
//
//                } else {
                List<EventoInscricaoItens> lItensInscricao = eiiDAO.checkExists("eventoItens", eventoItens);
                EventoItensDAO eiDAO = new EventoItensDAO();
                List<EventoItens> lItensEvento = eiDAO.checkExists("id", eventoItens.getId());
                if (lItensInscricao.size() == lItensEvento.get(0).getQuantidadePermitida()) {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                            "Não há vagas disponíveis!");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                } else {
                    EventoInscricaoItens eventoInII = new EventoInscricaoItens();
                    eventoInII.setEventoInscricao(eii);
                    eventoInII.setValidada(false);
                    eventoInII.setEventoItens(item);
                    eiiDAO.add(eventoInII);
                }
//              }
            }

        }
        eventoItens = new EventoItens();
        return "";
    }

    public List<SelectItem> getItensSelect() {
        this.itensSelect = new ArrayList<>();
        if (this.itensSelect.isEmpty()) {
            this.itensSelect = new ArrayList<SelectItem>();
            //ContextoBean contextoBean = scs.util.ContextoUtil.getContextoBean();

            EventoItensDAO dao = new EventoItensDAO();
            List<EventoItens> categorias = dao.checkExists("evento", eii.getEvento());
            this.showDataSelectItens(this.itensSelect, categorias, "");
        }

        return itensSelect;
    }

    private void showDataSelectItens(List<SelectItem> select, List<EventoItens> itens, String prefixo) {

        SelectItem item = null;
        if (itens != null) {
            EventoInscricaoItensDAO eiiDAO = new EventoInscricaoItensDAO();
            for (EventoItens eitem : itens) {
                List<EventoInscricaoItens> listaQtdVagas = eiiDAO.checkExists("eventoItens", eitem);
                int qtd = 0;
                if (!listaQtdVagas.isEmpty()) {
                    qtd = listaQtdVagas.size();
                }
                item = new SelectItem(eitem,
                        eitem.getDescricao() + ", " + eitem.getData() + " " + eitem.getHora()
                        + " | Valor R$: " + eitem.getValor() + " | Vagas: " + (eitem.getQuantidadePermitida() - qtd));
                item.setEscape(false);

                select.add(item);

                //this.montaDadosSelect(select, usuario.getNome(), prefixo + "&nbsp;&nbsp;");
            }
        }
    }

    public EventoInscricao getEii() {
        return eii;
    }

    public void setEii(EventoInscricao eii) {
        this.eii = eii;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.eii);
        hash = 19 * hash + Objects.hashCode(this.itensSelect);
        hash = 19 * hash + Objects.hashCode(this.eventoItens);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EventoItensBean other = (EventoItensBean) obj;
        if (!Objects.equals(this.eii, other.eii)) {
            return false;
        }
        if (!Objects.equals(this.itensSelect, other.itensSelect)) {
            return false;
        }
        if (!Objects.equals(this.eventoItens, other.eventoItens)) {
            return false;
        }
        return true;
    }
    
    private StreamedContent arqRelComp;

    public StreamedContent getArqRelComp() {
        FacesContext context = FacesContext.getCurrentInstance();
        RelatorioUtil relatorioUtil = new RelatorioUtil();
        HashMap parametrosRelatorio = new HashMap<>();
        parametrosRelatorio.put("sql", eii.getId());
        try {
            this.arqRelComp = relatorioUtil.geraRelatorio(
                    parametrosRelatorio, "compr_inscricao_info",
                    "compr_inscricao_info", 1);
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(e.getMessage()));
        }

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Comprovante Info Gerado!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return arqRelComp;
    }
    
    private StreamedContent arqRelCompInscr;

    public StreamedContent getArqRelCompInscr() {
        FacesContext context = FacesContext.getCurrentInstance();
        RelatorioUtil relatorioUtil = new RelatorioUtil();
        HashMap parametrosRelatorio = new HashMap<>();
        parametrosRelatorio.put("sql", eii.getId());
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
    }
    
    public String getValorTotal(){
        double valor=0;
        for(EventoInscricaoItens eii : getlEventoInscricaoItens()){
            valor+=eii.getEventoItens().getValor();
        }
        return String.valueOf(valor);
    }
    
    private Evento evento;

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
    
    public String realizarInscricao() {
        EventoInscricaoDAO eiDAO = new EventoInscricaoDAO();
        Usuario usuario = new ContextBean().getUser();
        List<EventoInscricao> listaEi = eiDAO.verificaInscricao(usuario, evento);
        if (listaEi == null) {
            listaEi = new ArrayList<>();
        }
        if (listaEi.size() != 0) {
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Usuário Já cadastrado para este Evento");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
            eii = listaEi.get(0);

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
                        + "\nParticipante: " + ei.getUsuario().getName()
                        + "\nNúmero de insrição: " + ei.getId()
                        + "\nSituação: " + ei.getSituacao()
                        + "\nData da Inscrição: " + ei.getData()
                        + "\n\n att, \nEquipe Event.");
            } catch (Exception e) {

            }

            eiDAO.add(ei);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Inscriçao Realizada!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            eii = ei;
        }

        return "/private/itensevento.jsf";
    }
    


    
    
}
