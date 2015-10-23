package br.edu.bean;

import br.edu.eventoinscricao.EventoInscricao;
import br.edu.eventoinscricao.EventoInscricaoItens;
import br.edu.eventoinscricao.EventoInscricaoItensDAO;
import br.edu.eventoitens.EventoItens;
import br.edu.eventoitens.EventoItensDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

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
            EventoInscricaoItensDAO eiidao = new EventoInscricaoItensDAO();
            eiidao.remove(eventoInscricaoItens);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Item Removido!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
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
            for (EventoItens eitem : itens) {
                item = new SelectItem(eitem, eitem.getDescricao() + ", " + eitem.getData() + " " + eitem.getHora()+
                    " | Valor R$: "+eitem.getValor());
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
    
    
}
