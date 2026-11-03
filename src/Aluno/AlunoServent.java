package Aluno;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AlunoServent implements AlunoService{

    private static final String ARQUIVO_CSV = "src/data/aluno.csv";
    private List<Aluno> alunos;

    public AlunoServent(){
        this.alunos = lerCSV();
    }

    @Override
    public void cadastrarAluno(Aluno aluno) throws RemoteException {
        alunos.add(aluno);
        escreverCSV();
    }

    @Override
    public Aluno consultarAluno(String matricula) throws RemoteException {
        for (Aluno aluno : alunos){
            if(aluno.getMatricula().equals(matricula)){
                return aluno;
            }
        }
        return null;
    }

    @Override
    public List<Aluno> listarAlunos() throws RemoteException {
        return alunos;
    }

    @Override
    public void removerAluno(String matricula) throws RemoteException {
        alunos.removeIf(aluno -> aluno.getMatricula().equals(matricula));
        escreverCSV();
    }

    // formato aluno.csv: nome,email,matricula;
    private List<Aluno> lerCSV(){
        List<Aluno> lista = new ArrayList<>();

        if (!new File(ARQUIVO_CSV).exists()){
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_CSV))){

            String linha;
            while((linha = br.readLine()) != null){
                if (linha.isBlank()){
                    continue;
                }

                String[]campos = linha.split(",");
                
                Aluno novo = new Aluno(campos[0].trim(), campos[1].trim(), campos[2].trim());
                
                lista.add(novo);
            }
        } catch (IOException e) {
            System.out.println("Erro lerCSV em AlunoServent: " + e.getMessage());
        }

        return lista;
    }

    private void escreverCSV(){
        try (FileWriter fw = new FileWriter(ARQUIVO_CSV)){
            for (Aluno a : alunos){
                fw.write(a.getNome() + "," + a.getEmail() + "," + a.getMatricula() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Erro escreverCSV em AlunoServet: " + e.getMessage());
        }

    }

    
}
