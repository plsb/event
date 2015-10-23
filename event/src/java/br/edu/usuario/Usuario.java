/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.usuario;

import br.edu.util.Util;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
public class Usuario implements Serializable {
    
    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 150, nullable = false)
    private String name;
    
    private String cpf;
    
    @Column(length = 50, nullable = false)
    private String login;

    @Column(length = 50, nullable = false)
    private String password;
  
    @Column(columnDefinition = "boolean default true")
    private boolean active;
    
    private String email;
    
    private String codigoVerificacao;
    
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_permission",
            uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user", "permission"})},
            joinColumns = @JoinColumn(name = "user"))
    @Column(name = "permission", length = 50)
    private List<String> permission;// = new HashSet<String>();
    
    @Temporal(TemporalType.DATE)
    private Date dataCadastro;
    
    private String telefone;

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getPermission() {
        return permission;
    }

    public void setPermission(List<String> permission) {
        this.permission = permission;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCodigoVerificacao() {
        return codigoVerificacao;
    }

    public void setCodigoVerificacao(String codigoVerificacao) {
        this.codigoVerificacao = codigoVerificacao;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.name);
        hash = 37 * hash + Objects.hashCode(this.cpf);
        hash = 37 * hash + Objects.hashCode(this.login);
        hash = 37 * hash + Objects.hashCode(this.password);
        hash = 37 * hash + (this.active ? 1 : 0);
        hash = 37 * hash + Objects.hashCode(this.email);
        hash = 37 * hash + Objects.hashCode(this.permission);
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
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.cpf, other.cpf)) {
            return false;
        }
        if (!Objects.equals(this.login, other.login)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (this.active != other.active) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.permission, other.permission)) {
            return false;
        }
        return true;
    }

    
}
