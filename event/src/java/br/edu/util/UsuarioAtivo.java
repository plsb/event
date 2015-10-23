/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.util;

import br.edu.usuario.Usuario;

/**
 *
 * @author Pedro Saraiva
 */
public class UsuarioAtivo {
    
    private static Usuario usuario;

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        UsuarioAtivo.usuario = usuario;
    }
    
}
