package br.com.inatel.FranciscoJunior_GotProject.exception;

import org.hibernate.exception.JDBCConnectionException;

public class ConnectionJDBCFailedException extends RuntimeException {
    public ConnectionJDBCFailedException(JDBCConnectionException jdbcConnectionException) {
        super("Connection with DataBase Failed!" + jdbcConnectionException.getMessage());
    }
}
