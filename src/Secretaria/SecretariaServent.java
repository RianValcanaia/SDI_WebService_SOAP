package Secretaria;

import Aluno.AlunoService;
import Turma.TurmaService;
import java.rmi.RemoteException;

public class SecretariaServent implements SecretariaService{
    private AlunoService aluno_service;
    private TurmaService turma_service;

    public SecretariaServent (AlunoService aluno_service, TurmaService turma_service){
        this.aluno_service = aluno_service;
        this.turma_service = turma_service;
    }

    @Override
    public void matricularAluno(String matricula, String codigoTurma) throws RemoteException {
        if (aluno_service.consultarAluno(matricula) == null){
            throw new  RemoteException("Aluno não encontrado: " + matricula);
        }

        if (turma_service.consultarTurma(codigoTurma) == null){
            throw new RemoteException("Turma não encontrada: " + codigoTurma);
        }

        turma_service.adicionarAluno(codigoTurma, matricula);
    }

    @Override
    public void cancelarMatricula(String matricula, String codigoTurma) throws RemoteException {   
        if (turma_service.consultarTurma(codigoTurma) == null){
            throw new RemoteException("Turma não encontrada: " + codigoTurma);
        }

        turma_service.removerAluno(codigoTurma, matricula);

    }
    
    
}
