package sca;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AlunoService extends Remote {

    void cadastrarAluno(Aluno aluno) throws RemoteException;

    Aluno consultarAluno(String matricula) throws RemoteException;

    List<Aluno> listarAlunos() throws RemoteException;

    void removerAluno(String matricula) throws RemoteException;
}
