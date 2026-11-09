package Turma;

import Aluno.AlunoService;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.net.InetAddress;
import java.net.ServerSocket;

public class TurmaServer {
    public static void main(String[] args) {
        String alunoHost = (args.length < 1) ? "localhost" : args[0];
        // endereço deste serviço: as portas RMI só escutam nele
        String host = System.getProperty("java.rmi.server.hostname", "localhost");
        try{
            RMIServerSocketFactory sockets = porta -> new ServerSocket(porta, 50, InetAddress.getByName(host));

            AlunoService aluno_service = (AlunoService) Naming.lookup("//" + alunoHost + ":1099" + "/AlunoService");

            TurmaServent turma_servent = new TurmaServent(aluno_service);

            TurmaService stub = (TurmaService)UnicastRemoteObject.exportObject(turma_servent, 0, null, sockets);

            LocateRegistry.createRegistry(1100, null, sockets);

            Naming.rebind("//" + host + ":1100/TurmaService", stub);

            System.out.println("TurmaService Server iniciado em " + host + ":1100/TurmaService");
        }catch(Exception e){
            System.out.println("Erro TurmaServer main: " + e.getMessage());
            e.printStackTrace();
        }
    }    
}
