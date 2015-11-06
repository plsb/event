package br.edu.bean;

import br.edu.usuario.Usuario;
import br.edu.usuario.UsuarioDAO;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class UsuarioConfiguracoesBean {

    private Usuario usuario = new ContextBean().getUser();

    private String senhaAnterior, novaSenha, confirmarSenha;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getSenhaAnterior() {
        return senhaAnterior;
    }

    public void setSenhaAnterior(String senhaAnterior) {
        this.senhaAnterior = senhaAnterior;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

    public String salvar() {
        if (senhaAnterior.equals("") || novaSenha.equals("") || confirmarSenha.equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Informe a senha!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if(!senhaAnterior.equals(usuario.getPassword())){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Senha anterior incorreta!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if(!novaSenha.equals(confirmarSenha)){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Confirme a senha corretamente!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            usuario.setPassword(novaSenha);
            UsuarioDAO uDAO = new UsuarioDAO();
            uDAO.update(usuario);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Alteração de Senha Realizada!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return "/private/principal.jsf";
    }

}
