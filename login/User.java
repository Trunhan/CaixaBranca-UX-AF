package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


//Classe que gerencia usuários e conecta com o banco de dados.
public class User {

    // Atributos da autenticação
    public String nome = "";
    public boolean result = false;

    //Estabelece a conexão com o banco de dados MySQL.
    public Connection conectarBD() {
        Connection conn = null;
        try {
            //Carrega o Driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            //String de conexão (URL, usuário e senha)
            String url = "jdbc:mysql://127.0.0.1/test?user=lopes&password=123";
            conn = DriverManager.getConnection(url);
            
        } catch (Exception e) {
            //Imprime o erro, evitando erro invisível
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
        return conn;
    }

    //Verifica se o login e a senha correspondem a um usuário no banco de dados.
    public boolean verificarUsuario(String login, String senha) {
        //Reinicia as variáveis de estado
        String sql = "";
        this.result = false; 

        //Abre a conexão
        Connection conn = conectarBD();

        //Verifica se a conexão falhou antes de prosseguir
        if (conn == null) {
            System.err.println("Sem conexão com o banco.");
            return false;
        }

        //Instrução SQL utilizando placeholders '?'
        sql = "select nome from usuarios where login = ? and senha = ?";

        //Garante o fechamento de recursos
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            
            //Passagem segura de parâmetros, evita SQL Injection
            st.setString(1, login);
            st.setString(2, senha);

            //Executa a consulta
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    this.result = true;
                    this.nome = rs.getString("nome");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao consultar usuário: " + e.getMessage());
            this.result = false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return this.result;
    }
} // fim da class