package maven.demo;

import java.util.Scanner;

public class Principal {
	
public static void main(String[] args) {
		
		DAO dao = new DAO();
		
		dao.conectar();

		Scanner scanner = new Scanner(System.in);
        int opcao;
        
        do {
            System.out.println("\n=== SISTEMA CRUD JAVA + POSTGRESQL ===");
            System.out.println("1. Criar usuário");
            System.out.println("2. Listar todos os usuários");
            System.out.println("3. Buscar usuário por ID");
            System.out.println("4. Atualizar usuário");
            System.out.println("5. Deletar usuário");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer
            
            switch (opcao) {
                case 1:
                    dao.criarUsuario(scanner);
                    break;
                case 2:
                    dao.listarUsuarios();
                    break;
                case 3:
                    dao.buscarUsuarioPorMatricula(scanner);
                    break;
                case 4:
                    dao.atualizarUsuario(scanner);
                    break;
                case 5:
                    dao.deletarUsuario(scanner);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
            
        } while (opcao != 0);
        
        scanner.close();
        
		dao.close();
	}
}
