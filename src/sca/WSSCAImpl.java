package sca;

import Aluno.Aluno;
import Aluno.AlunoService;
import Secretaria.SecretariaService;
import Turma.Turma;
import Turma.TurmaService;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import javax.jws.WebService;

@WebService(endpointInterface = "sca.WSSCA", serviceName = "WSSCAService", portName = "WSSCAPort")
public class WSSCAImpl implements WSSCA {

    // ========== AlunoService ==========
    @Override
    public void cadastrarAluno(Aluno aluno) throws RemoteException {
        alunoService().cadastrarAluno(aluno);
    }

    @Override
    public Aluno consultarAluno(String matricula) throws RemoteException {
        return alunoService().consultarAluno(matricula);
    }

    @Override
    public List<Aluno> listarAlunos() throws RemoteException {
        return alunoService().listarAlunos();
    }

    @Override
    public void removerAluno(String matricula) throws RemoteException {
        alunoService().removerAluno(matricula);
    }

    // ========== TurmaService ==========
    @Override
    public void cadastrarTurma(Turma turma) throws RemoteException {
        turmaService().cadastrarTurma(turma);
    }

    @Override
    public Turma consultarTurma(String codigo) throws RemoteException {
        return turmaService().consultarTurma(codigo);
    }

    @Override
    public List<Turma> listarTurmas() throws RemoteException {
        return turmaService().listarTurmas();
    }

    @Override
    public void removerTurma(String codigo) throws RemoteException {
        turmaService().removerTurma(codigo);
    }

    @Override
    public void adicionarAluno(String codigoTurma, String matricula) throws RemoteException {
        turmaService().adicionarAluno(codigoTurma, matricula);
    }

    @Override
    public void removerAluno(String codigoTurma, String matricula) throws RemoteException {
        turmaService().removerAluno(codigoTurma, matricula);
    }

    @Override
    public List<Aluno> listarAlunos(String codigoTurma) throws RemoteException {
        return turmaService().listarAlunos(codigoTurma);
    }

    // ========== SecretariaService ==========
    @Override
    public void matricularAluno(String matricula, String codigoTurma) throws RemoteException {
        secretariaService().matricularAluno(matricula, codigoTurma);
    }

    @Override
    public void cancelarMatricula(String matricula, String codigoTurma) throws RemoteException {
        secretariaService().cancelarMatricula(matricula, codigoTurma);
    }

    // ========== Acesso aos serviços RMI ==========
    private AlunoService alunoService() throws RemoteException {
        String propriedadeHost = "sca.aluno.host";
        int porta = Integer.getInteger("sca.aluno.port", 1099);
        String service = "AlunoService";

        String host = System.getProperty(propriedadeHost, "localhost");
        try {
            return (AlunoService) Naming.lookup("//" + host + ":" + porta + "/" + service);
        } catch (NotBoundException | MalformedURLException e) {
            throw new RemoteException("Serviço RMI indisponível: " + service, e);
        }
    }

    private TurmaService turmaService() throws RemoteException {
        String propriedadeHost = "sca.turma.host";
        int porta = Integer.getInteger("sca.turma.port", 1100);
        String service = "TurmaService";

        String host = System.getProperty(propriedadeHost, "localhost");
        try {
            return (TurmaService) Naming.lookup("//" + host + ":" + porta + "/" + service);
        } catch (NotBoundException | MalformedURLException e) {
            throw new RemoteException("Serviço RMI indisponível: " + service, e);
        }
    }

    private SecretariaService secretariaService() throws RemoteException {
        String propriedadeHost = "sca.secretaria.host";
        int porta = Integer.getInteger("sca.secretaria.port", 1101);
        String service = "SecretariaService";

        String host = System.getProperty(propriedadeHost, "localhost");
        try {
            return (SecretariaService) Naming.lookup("//" + host + ":" + porta + "/" + service);
        } catch (NotBoundException | MalformedURLException e) {
            throw new RemoteException("Serviço RMI indisponível: " + service, e);
        }
    }
}
