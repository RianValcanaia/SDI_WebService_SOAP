package sca;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "WSSCA")

// livro fala que da para usar rpc ou document, não da para usar document, pois JAX não aceita list como retorno
@SOAPBinding(style = Style.DOCUMENT)
public interface WSSCA extends Remote {
    
    // ======== AlunoService ========
    @WebMethod
    void cadastrarAluno(@WebParam(name = "aluno") Aluno aluno) throws RemoteException;

    @WebMethod
    Aluno consultarAluno(@WebParam(name = "matricula") String matricula) throws RemoteException;

    @WebMethod
    List<Aluno> listarAlunos() throws RemoteException;

    @WebMethod
    void removerAluno(@WebParam(name = "matricula") String matricula) throws RemoteException;

    // ======== TurmaService ========
    @WebMethod
    void cadastrarTurma(@WebParam(name = "turma") Turma turma) throws RemoteException;

    @WebMethod
    Turma consultarTurma(@WebParam(name = "codigo") String codigo) throws RemoteException;

    @WebMethod
    List<Turma> listarTurmas() throws RemoteException;

    @WebMethod
    void removerTurma(@WebParam(name = "codigo") String codigo) throws RemoteException;

    @WebMethod
    void adicionarAluno(@WebParam(name = "codigoTurma") String codigoTurma,
                        @WebParam(name = "matricula") String matricula) throws RemoteException;

    /*
        WSDL não aceita dus operacoes com mesmo nome, removerAluno e listarAluno 
        foram alteradas o nome, mas somente no WSDL.
    */
    @WebMethod(operationName = "removerAlunoDaTurma")
    @RequestWrapper(className = "sca.jaxws.RemoverAlunoDaTurma")
    @ResponseWrapper(className = "sca.jaxws.RemoverAlunoDaTurmaResponse")
    void removerAluno(@WebParam(name = "codigoTurma") String codigoTurma,
                      @WebParam(name = "matricula") String matricula) throws RemoteException;

    @WebMethod(operationName = "listarAlunosDaTurma")
    @RequestWrapper(className = "sca.jaxws.ListarAlunosDaTurma")
    @ResponseWrapper(className = "sca.jaxws.ListarAlunosDaTurmaResponse")
    List<Aluno> listarAlunos(@WebParam(name = "codigoTurma") String codigoTurma) throws RemoteException;

    // ======== SecretariaService ========
    @WebMethod
    void matricularAluno(@WebParam(name = "matricula") String matricula,
                         @WebParam(name = "codigoTurma") String codigoTurma) throws RemoteException;

    @WebMethod
    void cancelarMatricula(@WebParam(name = "matricula") String matricula,
                           @WebParam(name = "codigoTurma") String codigoTurma) throws RemoteException;
}
