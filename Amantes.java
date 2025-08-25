import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class AplicativoNamoro {
    public static void main(String[] args) {
        Random random = new Random();
        while (true) {
            try {
                int opcao = Integer.parseInt(JOptionPane.showInputDialog(null, "Bem-vindo ao Seu Par Perfeito!\n" +
                        "1 - Cadastrar Usuário\n" +
                        "2 - Listar Parceiros\n" +
                        "3 - Dar Like\n" +
                        "4 - Ver Matches\n" +
                        "5 - Deletar Conta\n" +
                        "0 - Sair", "Seu par perfeito", JOptionPane.INFORMATION_MESSAGE));
                switch (opcao) {
                    case 0:
                        System.exit(0);
                        break;

                    case 1:
                        String nome = JOptionPane.showInputDialog(null, "Digite seu nome: ", "Cadastro", JOptionPane.INFORMATION_MESSAGE);
                        int idade = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite sua idade : ", "Cadastro", JOptionPane.INFORMATION_MESSAGE));
                        String genero = JOptionPane.showInputDialog(null, "Digite seu Gênero : ", "Cadastro", JOptionPane.INFORMATION_MESSAGE);
                        String preferencia = JOptionPane.showInputDialog(null, "Digite sua preferência romântica(gênero) : ", "Cadastro", JOptionPane.INFORMATION_MESSAGE);
                        String sobre = JOptionPane.showInputDialog(null, "Digite sobre você e seus gostos : ", "Cadastro", JOptionPane.INFORMATION_MESSAGE);
                        int casado = JOptionPane.showConfirmDialog(null, "Você é CASADO(A)?", "Cadastro", JOptionPane.YES_NO_OPTION);

                        if (casado == JOptionPane.NO_OPTION) {
                            JOptionPane.showMessageDialog(null, "Este aplicativo é apenas para amantes....", "ERRO", JOptionPane.ERROR_MESSAGE);
                            System.exit(0);
                        } else {
                            if (nome.isBlank() || genero.isBlank() || preferencia.isBlank() || sobre.isBlank()) {
                                JOptionPane.showMessageDialog(null, "Preencha os campos necessários para o cadastro.", "ERRO", JOptionPane.ERROR_MESSAGE);
                                break;
                            } else {
                                int numConta = Math.abs(random.nextInt());

                                try (Formatter output = new Formatter(new FileWriter("idConta.txt", true))) {
                                    output.format("%d\n", numConta);
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }

                                try (Formatter output = new Formatter("conta" + numConta + ".txt")) {
                                    output.format("ID:\t\t%d\nNome da Conta:\t\t%s\nIdade:\t\t%d\nGênero:\t\t%s\nPreferência:\t\t%s\nSobre:\t\t%s",
                                            numConta, nome.toUpperCase(), idade, genero.toUpperCase(), preferencia.toUpperCase(), sobre);
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }

                                try (Formatter formatter = new Formatter(new FileWriter("usuarios.txt", true))) {
                                    formatter.format("%s, %d, %s, %s, %s\n",
                                            nome.toUpperCase(), idade, genero.toUpperCase(), preferencia.toUpperCase(), sobre);
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }

                                JOptionPane.showMessageDialog(null, "Seja Bem-vindo(a) " + nome.toUpperCase() +
                                        " ao paraíso do amantes!\n\nSEU ID (NÃO ESQUEÇA-O): " + numConta);
                                break;
                            }
                        }

                    case 2:
                        try (Scanner input = new Scanner(new File("usuarios.txt"))) {
                            StringBuilder user = new StringBuilder();
                            while (input.hasNextLine()) {
                                user.append(input.nextLine()).append("\n");
                            }
                            JOptionPane.showMessageDialog(null, "Parceiros:\n\n" + user, "Parceiros", JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case 3:
                        int idPaquera1 = Integer.parseInt(JOptionPane.showInputDialog(null,
                                "Digite seu número de ID para encontrar seu/sua twotwok!",
                                "Paquera", JOptionPane.INFORMATION_MESSAGE));

                        List<Integer> idsCurtidos = new ArrayList<>();
                        try (Scanner input = new Scanner(new File("idConta.txt"))) {
                            while (input.hasNextLine()) {
                                int id = Integer.parseInt(input.nextLine());
                                if (id != idPaquera1) {
                                    int resposta = JOptionPane.showConfirmDialog(null,
                                            "Você sente um calor flamejante com a conta de ID: " + id + "?",
                                            "Paquera", JOptionPane.YES_NO_OPTION);

                                    if (resposta == JOptionPane.YES_OPTION) {
                                        idsCurtidos.add(id);
                                    }
                                }
                            }

                            try (Formatter output = new Formatter(new FileWriter("likes.txt", true))) {
                                for (int idCurtido : idsCurtidos) {
                                    output.format("%d,%d\n", idPaquera1, idCurtido);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            JOptionPane.showMessageDialog(null, "Likes enviados com sucesso!", "Like", JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case 4:
                        int meuId = Integer.parseInt(JOptionPane.showInputDialog(null,
                                "Digite seu ID para ver seus matches:",
                                "Matches", JOptionPane.INFORMATION_MESSAGE));

                        List<String> likes = new ArrayList<>();
                        try (Scanner input = new Scanner(new File("likes.txt"))) {
                            while (input.hasNextLine()) {
                                likes.add(input.nextLine());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Set<Integer> matches = new HashSet<>();
                        for (String like1 : likes) {
                            String[] parts1 = like1.split(",");
                            if (parts1.length != 2) continue;

                            int from = Integer.parseInt(parts1[0]);
                            int to = Integer.parseInt(parts1[1]);

                            if (from == meuId) {
                                for (String like2 : likes) {
                                    String[] parts2 = like2.split(",");
                                    if (parts2.length != 2) continue;

                                    int otherFrom = Integer.parseInt(parts2[0]);
                                    int otherTo = Integer.parseInt(parts2[1]);

                                    if (to == otherFrom && meuId == otherTo) {
                                        matches.add(to);
                                    }
                                }
                            }
                        }

                        if (matches.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Ainda não há matches... 😢", "Matches", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            StringBuilder msg = new StringBuilder("🎉 Matches encontrados com os IDs:\n");
                            for (int matchId : matches) {
                                msg.append("ID: ").append(matchId).append("\n");
                            }
                            JOptionPane.showMessageDialog(null, msg.toString(), "Matches", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;

                    case 5:
                        try {
                            int chave = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite o seu id: ", "Deletando...", JOptionPane.QUESTION_MESSAGE));
                            if (JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar sua conta? Não será possível recuperá-la.",
                                    "AVISO", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == 0) {
                                File arquivo = new File("conta" + chave + ".txt");
                                if (arquivo.exists()) {
                                    arquivo.delete();
                                    JOptionPane.showMessageDialog(null, "Conta deletada com sucesso!", "Deletando...", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null, "ID não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "O processo falhou devido a: " + e, "ERRO", JOptionPane.ERROR_MESSAGE);
                        }
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Digite apenas valores do menu.", "ERRO", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}