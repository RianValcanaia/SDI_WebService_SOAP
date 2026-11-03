package Aluno;

import java.io.Serializable;

public class Aluno implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private String email;
    private String matricula;

    public Aluno() {
    }

    public Aluno(String nome, String email, String matricula) {
        this.nome = nome;
        this.email = email;
        this.matricula = matricula;
    }
    
    // ========== SETTERS ==========
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    // ========== GETTERS ==========
    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getMatricula() {
        return matricula;
    }

    @Override
    public String toString() {
        return "Aluno: " + nome + " | Email: " + email + " | Matrícula: " + matricula;
    }
}
