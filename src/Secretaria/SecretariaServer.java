package Secretaria;

import Aluno.AlunoService;
import Turma.TurmaService;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.net.InetAddress;
import java.net.ServerSocket;

public class SecretariaServer {
    public static void main(String[] args) {
        String alunoHost = (args.length < 1) ? "localhost" : args[0];
        String turmaHost = (args.length < 2) ? "localhost" : args[1];
        // endereço deste serviço: as portas RMI só escutam nele
        String host = System.getProperty("java.rmi.server.hostname", "localhost");

        try{
            RMIServerSocketFactory sockets = porta -> new ServerSocket(porta, 50, InetAddress.getByName(host));

            AlunoService aluno_service = (AlunoService) Naming.lookup("//" + alunoHost + ":7000/AlunoService");
            TurmaService turma_service = (TurmaService) Naming.lookup("//" + turmaHost + ":7000/TurmaService");

            SecretariaServent secretaria_servent = new SecretariaServent(aluno_service, turma_service);

            SecretariaService stub = (SecretariaService) UnicastRemoteObject.exportObject(secretaria_servent, 0, null, sockets);

            LocateRegistry.createRegistry(7000, null, sockets);

            Naming.rebind("//" + host + ":7000/SecretariaService", stub);

            System.out.println("SecretariaService Server iniciado em " + host + ":7000/SecretariaService");

        }catch(Exception e){
            System.out.println("Erro SecretariaServer main: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
