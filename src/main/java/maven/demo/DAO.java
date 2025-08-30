package maven.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;

public class DAO {
	private Connection conexao;
	
	public DAO() {
		conexao = null;
	}
	
	public boolean conectar() {
		String driverName = "org.postgresql.Driver";                    
		String serverName = "localhost";
		String mydatabase = "Alunos";
		int porta = 5432;
		String url = "jdbc:postgresql://" + serverName + ":" + porta +"/" + mydatabase;
		String username = "postgres";
		String password = "ti@cc";
		boolean status = false;

		try {
			Class.forName(driverName);
			conexao = DriverManager.getConnection(url, username, password);
			status = (conexao != null);
			System.out.println("Conexão efetuada com o postgres!");
		} catch (ClassNotFoundException e) { 
			System.err.println("Conexão NÃO efetuada com o postgres -- Driver não encontrado -- " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
		}

		return status;
	}
	
	// Criar usuário
		public boolean criarUsuario(Scanner scanner) {
		    System.out.println("\n--- CRIAR NOVO ALUNO ---");
		    
		    System.out.print("Matrícula: ");
		    int matricula = scanner.nextInt();
		    scanner.nextLine(); // Limpar buffer
		    
		    System.out.print("Nome: ");
		    String nome = scanner.nextLine();
		    
		    System.out.print("Curso: ");
		    String curso = scanner.nextLine();
		    
		    System.out.print("Período: ");
		    int periodo = scanner.nextInt();
		    scanner.nextLine(); // Limpar buffer
		    
		    System.out.print("Data de nascimento (AAAA-MM-DD): ");
		    String dataStr = scanner.nextLine();
		    LocalDate nascimento = LocalDate.parse(dataStr);
		    
		    try {
		        String sql = "INSERT INTO aluno (matricula, nome, curso, periodo, nascimento) VALUES (?, ?, ?, ?, ?)";
		        PreparedStatement ps = conexao.prepareStatement(sql);
		        ps.setInt(1, matricula);
		        ps.setString(2, nome);
		        ps.setString(3, curso);
		        ps.setInt(4, periodo);
		        ps.setDate(5, java.sql.Date.valueOf(nascimento));
		        
		        int rowsAffected = ps.executeUpdate();
		        ps.close();
		        
		        if (rowsAffected > 0) {
		            System.out.println("Aluno criado com sucesso!");
		            return true;
		        }
		    } catch (SQLException e) {
		        System.err.println("Erro ao criar aluno: " + e.getMessage());
		    }
		    return false;
		}
		
		// Listar todos os usuários
		public void listarUsuarios() {
		    System.out.println("\n--- LISTA DE ALUNOS ---");
		    Aluno[] alunos = getUsuarios();
		    
		    if (alunos != null && alunos.length > 0) {
		        for (Aluno aluno : alunos) {
		            System.out.println(aluno);
		        }
		        System.out.println("Total de alunos: " + alunos.length);
		    } else {
		        System.out.println("Nenhum aluno encontrado.");
		    }
		}
		
		// Buscar usuário por matrícula
		public void buscarUsuarioPorMatricula(Scanner scanner) {
		    System.out.println("\n--- BUSCAR ALUNO POR MATRÍCULA ---");
		    System.out.print("Digite a matrícula: ");
		    int matricula = scanner.nextInt();
		    scanner.nextLine(); // Limpar buffer
		    
		    try {
		        String sql = "SELECT * FROM aluno WHERE matricula = ?";
		        PreparedStatement ps = conexao.prepareStatement(sql);
		        ps.setInt(1, matricula);
		        ResultSet rs = ps.executeQuery();
		        
		        if (rs.next()) {
		            java.sql.Date sqlDate = rs.getDate("nascimento");
		            LocalDate dataNascimento = (sqlDate != null) ? sqlDate.toLocalDate() : null;
		            
		            Aluno aluno = new Aluno(
		                rs.getInt("matricula"),
		                rs.getString("nome"),
		                rs.getString("curso"),
		                rs.getInt("periodo"),
		                dataNascimento
		            );
		            System.out.println("Aluno encontrado:");
		            System.out.println(aluno);
		        } else {
		            System.out.println("Aluno com matrícula " + matricula + " não encontrado.");
		        }
		        
		        ps.close();
		    } catch (SQLException e) {
		        System.err.println("Erro ao buscar aluno: " + e.getMessage());
		    }
		}
		
		// Deletar usuário
		public boolean deletarUsuario(Scanner scanner) {
		    System.out.println("\n--- DELETAR ALUNO ---");
		    System.out.print("Digite a matrícula do Aluno a ser deletado: ");
		    int matricula = scanner.nextInt();
		    scanner.nextLine(); // Limpar buffer
		    
		    try {
		        String sql = "DELETE FROM aluno WHERE matricula = ?";
		        PreparedStatement ps = conexao.prepareStatement(sql);
		        ps.setInt(1, matricula);
		        
		        int rowsAffected = ps.executeUpdate();
		        ps.close();
		        
		        if (rowsAffected > 0) {
		            System.out.println("Aluno deletado com sucesso!");
		            return true;
		        } else {
		            System.out.println("Aluno com matrícula " + matricula + " não encontrado.");
		        }
		    } catch (SQLException e) {
		        System.err.println("Erro ao deletar aluno: " + e.getMessage());
		    }
		    return false;
		}
		
		// Atualizar usuário (versão simplificada)
		public boolean atualizarUsuario(Scanner scanner) {
		    System.out.println("\n--- ATUALIZAR ALUNO ---");
		    System.out.print("Digite a matrícula do Aluno a ser atualizado: ");
		    int matricula = scanner.nextInt();
		    scanner.nextLine(); // Limpar buffer
		    
		    // Primeiro verifica se o aluno existe e mostra os dados atuais
		    try {
		        String checkSql = "SELECT * FROM aluno WHERE matricula = ?";
		        PreparedStatement checkPs = conexao.prepareStatement(checkSql);
		        checkPs.setInt(1, matricula);
		        ResultSet rs = checkPs.executeQuery();
		        
		        if (!rs.next()) {
		            System.out.println("Aluno com matrícula " + matricula + " não encontrado.");
		            checkPs.close();
		            return false;
		        }
		        
		        // Mostrar dados atuais
		        java.sql.Date sqlDate = rs.getDate("nascimento");
		        LocalDate dataNascimento = (sqlDate != null) ? sqlDate.toLocalDate() : null;
		        
		        Aluno alunoAtual = new Aluno(
		            rs.getInt("matricula"),
		            rs.getString("nome"),
		            rs.getString("curso"),
		            rs.getInt("periodo"),
		            dataNascimento
		        );
		        
		        System.out.println("Dados atuais do aluno:");
		        System.out.println(alunoAtual);
		        System.out.println("\nDigite os novos dados:");
		        
		        checkPs.close();
		    } catch (SQLException e) {
		        System.err.println("Erro ao verificar aluno: " + e.getMessage());
		        return false;
		    }
		    
		    // Solicitar novos dados
		    System.out.print("Novo nome: ");
		    String nome = scanner.nextLine();
		    
		    System.out.print("Novo curso: ");
		    String curso = scanner.nextLine();
		    
		    System.out.print("Novo período: ");
		    int periodo = scanner.nextInt();
		    scanner.nextLine(); // Limpar buffer
		    
		    System.out.print("Nova data de nascimento (AAAA-MM-DD): ");
		    String dataStr = scanner.nextLine();
		    LocalDate nascimento = LocalDate.parse(dataStr);
		    
		    try {
		        String sql = "UPDATE aluno SET nome = ?, curso = ?, periodo = ?, nascimento = ? WHERE matricula = ?";
		        PreparedStatement ps = conexao.prepareStatement(sql);
		        
		        ps.setString(1, nome);
		        ps.setString(2, curso);
		        ps.setInt(3, periodo);
		        ps.setDate(4, java.sql.Date.valueOf(nascimento));
		        ps.setInt(5, matricula);
		        
		        int rowsAffected = ps.executeUpdate();
		        ps.close();
		        
		        if (rowsAffected > 0) {
		            System.out.println("Aluno atualizado com sucesso!");
		            return true;
		        }
		    } catch (SQLException e) {
		        System.err.println("Erro ao atualizar aluno: " + e.getMessage());
		    }
		    return false;
		}
	
	public Aluno[] getUsuarios() {
	    Aluno[] alunos = null;
	    
	    try {
	        Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	        ResultSet rs = st.executeQuery("SELECT * FROM aluno");        
	        if(rs.next()){
	            rs.last();
	            alunos = new Aluno[rs.getRow()];
	            rs.beforeFirst();

	            for(int i = 0; rs.next(); i++) {
	            	// Converter java.sql.Date para LocalDate
	                java.sql.Date sqlDate = rs.getDate("nascimento");
	                LocalDate dataNascimento = (sqlDate != null) ? sqlDate.toLocalDate() : null;
	                
	                alunos[i] = new Aluno(
	                        rs.getInt("matricula"), 
	                        rs.getString("nome"), 
	                        rs.getString("curso"), 
	                        rs.getInt("periodo"),
	                        dataNascimento
	                    );
	            }
	        }
	        st.close();
	    } catch (Exception e) {
	        System.err.println(e.getMessage());
	    }
	    return alunos;
	}

	
	public boolean close() {
		boolean status = false;
		
		try {
			conexao.close();
			status = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return status;
	}
}
