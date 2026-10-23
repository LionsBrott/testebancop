
import java.sql.*;
import java.util.Scanner;

public class teste {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/teste_java";
        String usuario = "postgres";
        String senha = "senha";

        try (Scanner entrada = new Scanner(System.in);
             Connection conexao =
                     DriverManager.getConnection(url, usuario, senha)) {

            System.out.print("Digite uma palavra: ");
            String palavra = entrada.nextLine();

            if (palavra.length() > 25) {
                System.out.println("A palavra deve ter até 25 caracteres.");
                return;
            }

            String sql = """
                    INSERT INTO palavras (palavra)
                    VALUES (?)
                    RETURNING id
                    """;

            int id;

            try (PreparedStatement comando =
                         conexao.prepareStatement(sql)) {

                comando.setString(1, palavra);

                try (ResultSet resultado = comando.executeQuery()) {
                    resultado.next();
                    id = resultado.getInt("id");
                }
            }

            System.out.println("Palavra inserida com sucesso!");

            System.out.print("Deseja editar a palavra? (s/n): ");

            if (entrada.nextLine().equalsIgnoreCase("s")) {

                System.out.print("Digite a nova palavra: ");
                String novaPalavra = entrada.nextLine();

                if (novaPalavra.length() > 25) {
                    System.out.println(
                            "A palavra deve ter até 25 caracteres.");
                    return;
                }

                String editar =
                        "UPDATE palavras SET palavra = ? WHERE id = ?";

                try (PreparedStatement comando =
                             conexao.prepareStatement(editar)) {

                    comando.setString(1, novaPalavra);
                    comando.setInt(2, id);

                    comando.executeUpdate();

                    System.out.println("Palavra editada com sucesso!");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao acessar o PostgreSQL:");
            e.printStackTrace();
        }
    }
}
