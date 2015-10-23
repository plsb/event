/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.eventoitens;

import br.edu.evento.Evento;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class EventoItens {
    
    @Id
    @GeneratedValue
    private Integer id;
    
    private String descricao;
    
    @Temporal(TemporalType.DATE)
    private Date data;
    
    @Temporal(TemporalType.TIME)
    private Date hora;
    
    @ManyToOne
    private Evento evento;
    
    private int quantidadePermitida;
    
    private double valor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public int getQuantidadePermitida() {
        return quantidadePermitida;
    }

    public void setQuantidadePermitida(int quantidadePermitida) {
        this.quantidadePermitida = quantidadePermitida;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
        hash = 71 * hash + Objects.hashCode(this.descricao);
        hash = 71 * hash + Objects.hashCode(this.data);
        hash = 71 * hash + Objects.hashCode(this.hora);
        hash = 71 * hash + Objects.hashCode(this.evento);
        hash = 71 * hash + this.quantidadePermitida;
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.valor) ^ (Double.doubleToLongBits(this.valor) >>> 32));
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
        final EventoItens other = (EventoItens) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
    
}
