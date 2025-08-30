package maven.demo;
import java.time.LocalDate;

public class Aluno {
	//properties
		private int matricula;
		private String nome;
		private String curso;
		private int periodo;
		private LocalDate nascimento;
		
		//constructors
		public Aluno() {
			this.matricula = -1;
			this.nome = "";
			this.nascimento = null;
			this.curso = "";
			this.periodo = -1;
		}
		public Aluno(int matricula, String nome, String curso, int periodo, LocalDate nascimento) {
			this.matricula = matricula;
			this.nome = nome;
			this.nascimento = nascimento;
			this.curso = curso;
			this.periodo = periodo;
		}
		
		//getters
		public int getMatricula() {
			return this.matricula;
		}
		public String getNome() {
			return this.nome;
		}
		public String getCurso() {
			return this.curso;
		}
		public int getPeriodo() {
			return this.periodo;
		}
		public LocalDate getNascimento() {
			return this.nascimento;
		}
		
		//setters
		public void setMatricula(int matricula) {
			this.matricula =  matricula;
		}
		public void setNome(String nome) {
			this.nome = nome;
		}
		public void setCurso(String curso) {
			this.curso = curso;
		}
		public void setPeriodo(int periodo) {
			this.periodo = periodo;
		}
		public void setNascimento(LocalDate nascimento) {
			this.nascimento = nascimento;
		}
		
		//methods
		@Override
		public String toString() {
			return "Aluno [" + 
					"matricula=" + this.matricula + 
					", nome=" + this.nome + 
					", curso=" + this.curso + 
					", periodo=" + this.periodo +
					", nascimento=" + this.nascimento.toString() + 
					"]";
		}
}
