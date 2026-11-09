package sca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class TurmaServent implements TurmaService{
    private static final String ARQUIVO_CSV = "src/data/turma.csv";
    private List<Turma> turmas;
    private AlunoService aluno_service;

    public TurmaServent(AlunoService aluno_service){
        this.aluno_service = aluno_service;
        this.turmas = lerCSV();
    }
    
    @Override
    public void cadastrarTurma(Turma turma) throws RemoteException {
        turmas.add(turma);
        escreverCSV();
    }
    
    @Override
    public Turma consultarTurma(String codigo) throws RemoteException {
        for(Turma t : turmas){
            if(t.getCodigo().equals(codigo)){
                return t;
            }
        }
        return null;
    }
    
    @Override
    public List<Turma> listarTurmas() throws RemoteException {
        return this.turmas;
    }
    
    @Override
    public void removerTurma(String codigo) throws RemoteException {
        this.turmas.removeIf(turma -> turma.getCodigo().equals(codigo));
        escreverCSV();
    }
    
    @Override
    public void adicionarAluno(String codigoTurma, String matricula) throws RemoteException {
        Turma t = consultarTurma(codigoTurma);
        if (t == null){
            throw new RemoteException("Turma não encontrada: " + codigoTurma);
        }

        Aluno a = aluno_service.consultarAluno(matricula);

        if (a == null){
            throw new RemoteException("Aluno não encontrado: " + matricula);
        }
        
        t.adicionarAluno(a);
        escreverCSV();
    }
    
    @Override
    public void removerAluno(String codigoTurma, String matricula) throws RemoteException {
        Turma t = consultarTurma(codigoTurma);
        if (t == null) {
            throw new RemoteException("Turma não encontrada: " + codigoTurma);
        }

        Aluno a = aluno_service.consultarAluno(matricula);

        if (a == null){
            throw new RemoteException("Aluno não encontrado: " + matricula);
        }

        t.removerAlunoPorMatricula(matricula);
        escreverCSV();
    }

    @Override
    public List<Aluno> listarAlunos(String codigoTurma) throws RemoteException {
        Turma t = consultarTurma(codigoTurma);

        if (t == null) {
            throw new RemoteException("Turma não encontrada: " + codigoTurma);
        }

        return t.getAlunos();
    }
    
    // formato turma.csv: codigo,matricula1;matricula2;...
    private List<Turma> lerCSV() {
        List<Turma> lista = new ArrayList<>();

        if (!new File(ARQUIVO_CSV).exists()){
            return lista;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_CSV))){
            String linha;
            while((linha = br.readLine()) != null){
                if (linha.isBlank()) continue;

                String[] campos = linha.split(",");

                Turma t = new Turma(campos[0].trim());

                if (campos.length > 1 && !campos[1].isBlank()){
                    for (String mat : campos[1].split(";")) {
                        Aluno a = aluno_service.consultarAluno(mat);
                        if (a != null){
                            t.adicionarAluno(a);
                        }
                    }
                }

                lista.add(t);
            }
        }catch (RemoteException re){
            System.out.println("Erro remoto lerCSV TurmaSErvent: " + re.getMessage());
        }catch(IOException e){
            System.out.println("Erro arquivo lerCSV em TurmaServent: " + e.getMessage());
        }
        return lista;
    }

    private void escreverCSV() {
        try (FileWriter fw =  new FileWriter(ARQUIVO_CSV)){
            for (Turma t : turmas){
                StringBuilder matriculas = new StringBuilder();
                for(Aluno a: t.getAlunos()){
                    if (matriculas.length() > 0){
                        matriculas.append(";");
                    }

                    matriculas.append(a.getMatricula());
                }
                fw.write(t.getCodigo() + "," + matriculas + "\n");
            }
        } catch (IOException e) {
            System.out.println("Erro escreverCSV em TurmaServent: " + e.getMessage());
        }
    }
}
