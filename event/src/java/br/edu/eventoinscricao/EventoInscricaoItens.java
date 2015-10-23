package br.edu.eventoinscricao;

import br.edu.eventoitens.EventoItens;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class EventoInscricaoItens {
    
    @Id
    @GeneratedValue
    private int id;
    
    @ManyToOne
    private EventoInscricao eventoInscricao;
    
    @ManyToOne
    private EventoItens eventoItens;
    
    @Column(columnDefinition = "boolean default true")
    private boolean validada;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EventoInscricao getEventoInscricao() {
        return eventoInscricao;
    }

    public void setEventoInscricao(EventoInscricao eventoInscricao) {
        this.eventoInscricao = eventoInscricao;
    }

    public EventoItens getEventoItens() {
        return eventoItens;
    }

    public void setEventoItens(EventoItens eventoItens) {
        this.eventoItens = eventoItens;
    }

    public boolean isValidada() {
        return validada;
    }

    public void setValidada(boolean validada) {
        this.validada = validada;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.id;
        hash = 67 * hash + Objects.hashCode(this.eventoInscricao);
        hash = 67 * hash + Objects.hashCode(this.eventoItens);
        hash = 67 * hash + (this.validada ? 1 : 0);
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
        final EventoInscricaoItens other = (EventoInscricaoItens) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.eventoInscricao, other.eventoInscricao)) {
            return false;
        }
        if (!Objects.equals(this.eventoItens, other.eventoItens)) {
            return false;
        }
        if (this.validada != other.validada) {
            return false;
        }
        return true;
    }
    
    public String getSituacao(){
        if(validada==true){
            return "Ativo";
        } else {
            return "Inativo";
        }
    }
    
    
    
}
