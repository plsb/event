package br.edu.bean;

import br.edu.usuario.Usuario;
import br.edu.usuario.UsuarioDAO;
import br.edu.util.HibernateUtil;
import br.edu.util.Util;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.swing.JOptionPane;
import org.hibernate.HibernateException;

@ManagedBean
public class UsuarioBean {

    private Usuario usuario = new Usuario();
    private String confirmarSenha;
    private String codigoValidacao;
    private FacesContext context = FacesContext.getCurrentInstance();

    public String salvar() {
        UsuarioDAO uDAO = new UsuarioDAO();
        if (usuario.getName().equals("")) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Informe o Nome!", ""));
            return "";
        } else if (usuario.getLogin().equals("")) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Informe o Login!", ""));
            return "";
        } else if (usuario.getEmail().equals("")) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Informe o EMAIL!", ""));
            return "";
        } else if (usuario.getCpf().equals("")) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Informe o CPF!", ""));
            return "";
        } else if (usuario.getTelefone().equals("")) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Informe o Telefone!", ""));
            return "";
        } else if (usuario.getPassword().equals("") || confirmarSenha.equals("")) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Informe as Senhas!", ""));
            return "";
        } else if (!usuario.getPassword().equals(confirmarSenha)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Senha confirmada incorretamente!", ""));
            return "";
        } else if (!Util.CPF(usuario.getCpf())) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "CPF Inválido!", ""));
            return "";
        } else if (uDAO.checkExists("login", usuario.getLogin()).size() > 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Login Já cadastrado!", ""));
            return "";
        } else if (uDAO.checkExists("email", usuario.getEmail()).size() > 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Email Já cadastrado!", ""));
            return "";
        } else if (uDAO.checkExists("cpf", usuario.getCpf()).size() > 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "CPF Já cadastrado!", ""));
            return "";
        } else {

            usuario.setActive(false);

            SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");
            String strHora = hora.format(new Date(System.currentTimeMillis()));
            String strData = data.format(new Date(System.currentTimeMillis()));
            String codigoVerificacao = strHora.replaceAll(":", "")
                    + strData.replace("/", "")+usuario.getCpf().substring(0, 3);
            usuario.setCodigoVerificacao(codigoVerificacao);
            usuario.setDataCadastro(new Date());
            usuario.setEmail(usuario.getEmail().toLowerCase());
            usuario.setName(usuario.getName().toUpperCase());

            java.util.List<String> permissoes = new ArrayList<String>();
            permissoes.add("ROLE_USER");

            usuario.setPermission(permissoes);

            try {
                Util.mandarEmail("eventsoftwareinfo@gmail.com", "pedro040908",
                        usuario.getEmail(), "Cadastro sistema Event",
                        "Olá " + usuario.getName() + ","
                        + "\nAcesse o site, e informe seu código de validação: " + usuario.getCodigoVerificacao() + "."
                        + " Seu Login é: " + usuario.getLogin()
                        + "\n\n att, \nEquipe Event.");
            } catch (Exception e) {
                context.addMessage(null, new FacesMessage("Falha ao enviar email para: "
                        + usuario.getEmail(), ""));
            }

            uDAO.add(usuario);
            context.addMessage(null, new FacesMessage("Usuário " + usuario.getName() + " adicionado! Vá até sua caixa"
                    + " de email e resgate seu código de validação!", ""));
            return "/public/validacao.jsf";
        }
    }

    public String getCodigoValidacao() {
        return codigoValidacao;
    }

    public void setCodigoValidacao(String codigoValidacao) {
        this.codigoValidacao = codigoValidacao;
    }

    public FacesContext getContext() {
        return context;
    }

    public void setContext(FacesContext context) {
        this.context = context;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

    public String autenticar() {
        if (usuario.getLogin().equals("")) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Informe o login!", ""));
            return "";
        } else if (usuario.getPassword().equals("")) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Informe a senha!", ""));
            return "";
        }
        UsuarioDAO uDAO = new UsuarioDAO();
        if (uDAO.autentica(usuario.getLogin(), usuario.getPassword())) {
            return "/private/principal.jsf";
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dados Incorretos!", ""));
            return "";
        }

    }

    public String validar() {
        if (codigoValidacao.equals("")) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Informe o código!", ""));
            return "";
        }
        UsuarioDAO uDAO = new UsuarioDAO();
        Usuario u = null;
        if (uDAO.checkExists("codigoVerificacao", codigoValidacao).size() > 0) {
            u = uDAO.checkExists("codigoVerificacao", codigoValidacao).get(0);
        }
        if (u != null) {
            u.setActive(true);
            uDAO.update(u);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Validação Concluída para o usuário: "
                    + u.getName() + ", Login: " + u.getLogin(), ""));
            return "/public/login.jsf";
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código Incorreto!", ""));
            return "";
        }
    }

}
