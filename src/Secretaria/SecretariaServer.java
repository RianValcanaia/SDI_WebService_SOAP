package Secretaria;

import Aluno.AlunoService;
import Turma.TurmaService;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class SecretariaServer {
    public static void main(String[] args) {
        String alunoHost = (args.length < 1) ? "localhost" : args[0];
        String turmaHost = (args.length < 2) ? "localhost" : args[1];

        try{
            AlunoService aluno_service = (AlunoService) Naming.lookup("//" + alunoHost + ":7000/AlunoService");
            TurmaService turma_service = (TurmaService) Naming.lookup("//" + turmaHost + ":7010/TurmaService");

            SecretariaServent secretaria_servent = new SecretariaServent(aluno_service, turma_service);

            SecretariaService stub = (SecretariaService) UnicastRemoteObject.exportObject(secretaria_servent, 0);

            LocateRegistry.createRegistry(7020);

            Naming.rebind("//localhost:7020/SecretariaService", stub);

            System.out.println("SecretariaService Server iniciado em localhost:7020/SecretariaService");

        }catch(Exception e){
            System.out.println("Erro SecretariaServer main: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
