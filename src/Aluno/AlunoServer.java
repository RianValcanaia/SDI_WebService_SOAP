package Aluno;

import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;

public class AlunoServer {
    public static void main(String[] args) {
        try{
            AlunoServent s_aluno = new AlunoServent();

            AlunoService stub = (AlunoService)UnicastRemoteObject.exportObject(s_aluno, 0);

            LocateRegistry.createRegistry(7000);

            Naming.rebind("//localhost:7000/AlunoService", stub);

            System.out.println("AlunoService Server iniciado em localhost:7000/AlunoService");

        }catch(Exception e){
            System.out.println("Erro AlunoServer main: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
