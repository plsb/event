
package br.edu.evento;

import br.edu.local.Local;
import br.edu.usuario.Usuario;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Evento {
    
    @Id
    @GeneratedValue
    private int id;
    
    private String descricao;
    
    @Temporal(TemporalType.DATE)
    private Date dataInicio;
    
    @Temporal(TemporalType.DATE)
    private Date dataFim;
    
    @ManyToOne
    private Usuario dono;
    
    @ManyToOne
    private Local local;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Usuario getDono() {
        return dono;
    }

    public void setDono(Usuario dono) {
        this.dono = dono;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this.id;
        hash = 37 * hash + Objects.hashCode(this.descricao);
        hash = 37 * hash + Objects.hashCode(this.dataInicio);
        hash = 37 * hash + Objects.hashCode(this.dataFim);
        hash = 37 * hash + Objects.hashCode(this.dono);
        hash = 37 * hash + Objects.hashCode(this.local);
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
        final Evento other = (Evento) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.dataInicio, other.dataInicio)) {
            return false;
        }
        if (!Objects.equals(this.dataFim, other.dataFim)) {
            return false;
        }
        if (!Objects.equals(this.dono, other.dono)) {
            return false;
        }
        if (!Objects.equals(this.local, other.local)) {
            return false;
        }
        return true;
    }
    
}
