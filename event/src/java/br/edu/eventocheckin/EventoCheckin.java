package br.edu.eventocheckin;

import br.edu.eventoinscricao.EventoInscricaoItens;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class EventoCheckin {
    
    @Id
    @GeneratedValue
    private int id;
    
    @ManyToOne
    private EventoInscricaoItens eventoItem;
    
    @Temporal(TemporalType.DATE)
    private Date data;
    
    @Temporal(TemporalType.TIME)
    private Date hora;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EventoInscricaoItens getEventoItem() {
        return eventoItem;
    }

    public void setEventoItem(EventoInscricaoItens eventoItem) {
        this.eventoItem = eventoItem;
    }

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
    
    
    
}
