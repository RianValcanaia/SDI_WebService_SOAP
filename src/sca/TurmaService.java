package sca;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface TurmaService extends Remote {

    void cadastrarTurma(Turma turma) throws RemoteException;

    Turma consultarTurma(String codigo) throws RemoteException;

    List<Turma> listarTurmas() throws RemoteException;

    void removerTurma(String codigo) throws RemoteException;

    void adicionarAluno(String codigoTurma, String matricula)
            throws RemoteException;

    void removerAluno(String codigoTurma, String matricula)
            throws RemoteException;

    List<Aluno> listarAlunos(String codigoTurma)
            throws RemoteException;
}
