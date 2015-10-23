package br.edu.eventoinscricao;

import br.edu.evento.Evento;
import br.edu.eventoitens.EventoItens;
import br.edu.usuario.Usuario;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class EventoInscricao {
    
    @Id
    private String id;
    
    @ManyToOne
    private Usuario usuario;
    
//    @OneToMany(mappedBy = "eventoInscricao")
//    private EventoInscricaoItens eventoItens;
    
    @Temporal(TemporalType.DATE)
    private Date data;
    
    @Temporal(TemporalType.TIME)
    private Date hora;
    
    @Column(columnDefinition = "boolean default true")
    private boolean ativo;
    
    @ManyToOne
    private Evento evento;

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

//    public EventoInscricaoItens getEventoItens() {
//        return eventoItens;
//    }
//
//    public void setEventoItens(EventoInscricaoItens eventoItens) {
//        this.eventoItens = eventoItens;
//    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    public String getSituacao(){
        if(ativo==true){
            return "Ativo";
        } else {
            return "Inativo";
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.usuario);
        hash = 97 * hash + Objects.hashCode(this.data);
        hash = 97 * hash + Objects.hashCode(this.hora);
        hash = 97 * hash + (this.ativo ? 1 : 0);
        hash = 97 * hash + Objects.hashCode(this.evento);
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
        final EventoInscricao other = (EventoInscricao) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.usuario, other.usuario)) {
            return false;
        }
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        if (!Objects.equals(this.hora, other.hora)) {
            return false;
        }
        if (this.ativo != other.ativo) {
            return false;
        }
        if (!Objects.equals(this.evento, other.evento)) {
            return false;
        }
        return true;
    }
    
    public String getValorInscricao(){
        EventoInscricaoItensDAO eiDAO = new EventoInscricaoItensDAO();
        EventoInscricaoDAO eInscDAO = new EventoInscricaoDAO();
        EventoInscricao einscricao = eInscDAO.checkExists("id", id).get(0);
                
        List<EventoInscricaoItens> listaValores = eiDAO.checkExists("eventoInscricao", einscricao);
        double valor=0.0;
        if(!listaValores.isEmpty()){
            for (EventoInscricaoItens listaValore : listaValores) {
                valor+=listaValore.getEventoItens().getValor();
            }
        }
        
        return "R$ "+String.valueOf(valor);
    }
    
    
    
}
