package sca;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SecretariaService extends Remote {

    void matricularAluno(String matricula, String codigoTurma)
            throws RemoteException;

    void cancelarMatricula(String matricula, String codigoTurma)
            throws RemoteException;

}
