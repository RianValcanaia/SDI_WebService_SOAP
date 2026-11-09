package sca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Turma implements Serializable {
    // igual ao valor padrão calculado pelo Java para a classe Turma do RMI do outro projeto
    private static final long serialVersionUID = -2678285911935011748L;

    private String codigo;
    private List<Aluno> alunos; 

    // exigido pelo jaxb para converter a Turma xml no ws soap
    public Turma() {
        this.alunos = new ArrayList<>();
    }

    public Turma(String codigo) {
        this.codigo = codigo;
        this.alunos = new ArrayList<>();
    }
    
    public void adicionarAluno(Aluno aluno) {
        this.alunos.add(aluno);
    }

    public boolean removerAlunoPorMatricula(String matricula) {
        return alunos.removeIf(aluno -> aluno.getMatricula().equals(matricula));
    }

    // ========== SETTERS ==========
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    // ========== GETTERS ==========
    public String getCodigo() {
        return codigo;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Turma: ").append(codigo).append("\nLista de Alunos:\n");
        if (alunos.isEmpty()) {
            sb.append("- Nenhum aluno matriculado.");
        } else {
            for (Aluno aluno : alunos) {
                sb.append("- ").append(aluno).append("\n");
            }
        }
        return sb.toString();
    }
}

