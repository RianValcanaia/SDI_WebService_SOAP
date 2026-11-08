package Aluno;

import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.Naming;
import java.net.InetAddress;
import java.net.ServerSocket;

public class AlunoServer {
    public static void main(String[] args) {
        // endereço deste serviço: as portas RMI só escutam nele
        String host = System.getProperty("java.rmi.server.hostname", "localhost");
        try{
            RMIServerSocketFactory sockets = porta -> new ServerSocket(porta, 50, InetAddress.getByName(host));

            AlunoServent s_aluno = new AlunoServent();

            AlunoService stub = (AlunoService)UnicastRemoteObject.exportObject(s_aluno, 0, null, sockets);

            LocateRegistry.createRegistry(7000, null, sockets);

            Naming.rebind("//" + host + ":7000/AlunoService", stub);

            System.out.println("AlunoService Server iniciado em " + host + ":7000/AlunoService");

        }catch(Exception e){
            System.out.println("Erro AlunoServer main: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
