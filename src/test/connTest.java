import org.junit.Test;

import java.io.IOException;
import java.sql.*;

public class connTest {
    @Test(expected = ClassNotFoundException.class)
    public void shouldThrowClassNotFound() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc");
    }


    @Test(expected = SQLException.class)
    public void shouldThrowExceptionIfConNotEstablished() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con= DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/chat","user","123");
        con.close();
    }

    @Test
    public void shouldEstablishConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con= DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/chat","user","password");
        con.isValid(1);
        con.close();
    }
}
