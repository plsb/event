/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection getConnection() {
        System.out.println("Conectando ao Banco de Dados...");
        try {
            return DriverManager.getConnection("jdbc:mysql://srv07.brasilwork.com.br:3306/loyqfhoo_banco_event",  
                    "loyqfhoo_event", "12345event");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
