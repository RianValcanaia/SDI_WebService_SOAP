package Turma;

import Aluno.AlunoService;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class TurmaServer {
    public static void main(String[] args) {
        String alunoHost = (args.length < 1) ? "localhost" : args[0];
        try{
            AlunoService aluno_service = (AlunoService) Naming.lookup("//" + alunoHost + ":7000" + "/AlunoService");

            TurmaServent turma_servent = new TurmaServent(aluno_service);

            TurmaService stub = (TurmaService)UnicastRemoteObject.exportObject(turma_servent, 0);

            LocateRegistry.createRegistry(7010);

            Naming.rebind("//localhost:7010/TurmaService", stub);

            System.out.println("TurmaService Server iniciado em localhost:7010/TurmaService");
        }catch(Exception e){
            System.out.println("Erro TurmaServer main: " + e.getMessage());
            e.printStackTrace();
        }
    }    
}
