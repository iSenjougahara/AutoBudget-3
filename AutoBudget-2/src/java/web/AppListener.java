package web;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.Date;
import java.sql.*;
import java.security.*;
import java.math.*;
import model.User;
import model.Estoque;
import model.ModeloCarro;
import model.Pecas;

@WebListener
public class AppListener implements ServletContextListener {
    public static final String CLASS_NAME = "org.sqlite.JDBC";
    public static final String URL = "jdbc:sqlite:parkapp.db";
    public static String initializeLog = "";
    public static Exception exception = null;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        try {
             
             Connection c = AppListener.getConnection();
            Statement s = c.createStatement();
            initializeLog += new Date() + ": Initializing database creation; ";
        
          //  s.execute("DROP TABLE Pecas;");
          //  initializeLog += new Date() + ":pecas gone ";
            initializeLog += new Date() + ": Initializing database creation; ";
            
            //USERS
            initializeLog += "Creating Users table if not exists...";
            s.execute(User.getCreateStatement());
            initializeLog += "done; ";
            
            if(User.getUsers().isEmpty())
            {
                initializeLog += "Adding default users...";
                User.insertUser("admin", "Administrador", "ADMIN", "1234");
                initializeLog += "Admin added; ";
                User.insertUser("Vendedor", "Vendedor1", "Vendedor", "1234");
                 User.insertUser("Vendedor", "Vendedor2", "Vendedor", "1234");
                initializeLog += "Vendedor added; ";
            }
            
            // Estoque
            initializeLog += "Creating Estoque table...";
            s.execute(Estoque.getCreateStatement());
        
            // Pecas
            initializeLog += "Creating Pecas table...";
            s.execute(Pecas.getCreateStatement());
            initializeLog += "done; ";
        
            // ModeloCarro
            initializeLog += "Creating ModeloCarro table...";
            s.execute(ModeloCarro.getCreateStatement());
            initializeLog += "done; ";
            

            if (ModeloCarro.getModeloCarros().isEmpty()) 
            {
                initializeLog += "Adding default ModeloCarros...";
                ModeloCarro.insertModeloCarro("ModeloCarro 1", new Date(), "Marca 1");
                ModeloCarro.insertModeloCarro("ModeloCarro 2", new Date(), "Marca 2");
                ModeloCarro.insertModeloCarro("ModeloCarro 3", new Date(), "Marca 3");
                initializeLog += "ModeloCarros added; ";
            }
            if (Pecas.getPecas().isEmpty()) 
            {
                initializeLog += "Adding default Pecas...";
                Pecas.insertPecas("Peca 1", 10.0, 1);
                Pecas.insertPecas("Peca 2", 20.0, 2);
                Pecas.insertPecas("Peca 3", 30.0, 3);
                Pecas.insertPecas("Peca 4", 30.0, 3);
                initializeLog += "kek; ";
                System.out.println("inseriu AEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                System.out.println("Initialization completed.");
            }

            initializeLog += "Fetching recently inserted data from Pecas table...\n";

                try 
                {
                    Connection con = AppListener.getConnection();
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM Pecas");

                    initializeLog += "Recently inserted data:\n";

                    while (rs.next()) {
                    String nomePeca = rs.getString("nomePeca");
                    double preco = rs.getDouble("preco");
                    long modeloCarroRowId = rs.getLong("modeloCarro_rowId");

                    initializeLog += "Nome da Peça: " + nomePeca + ", Preço: " + preco + ", Modelo Carro RowId: " + modeloCarroRowId + "\n";
                }

                rs.close();
                stmt.close();
                con.close();
        
                } catch (Exception ex) {
                    initializeLog += "Error fetching data: " + ex.getMessage();
                }

            initializeLog += "Data fetch completed.\n";
        
            s.close();
            c.close();
        } 
        catch (Exception ex) 
        {
            initializeLog += "Error: " + ex.getMessage();
        }
         
    }

    public static String getMd5Hash(String text) throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(text.getBytes(), 0, text.length());
        return new BigInteger(1, m.digest()).toString();
    }

    public static Connection getConnection() throws Exception {
        Class.forName(CLASS_NAME);
        return DriverManager.getConnection(URL);
    }
    
}
     
    
    

