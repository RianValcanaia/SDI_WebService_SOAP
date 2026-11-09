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
        int alunoPorta = Integer.getInteger("sca.aluno.port", 1099);
        int turmaPorta = Integer.getInteger("sca.turma.port", 1100);
        int secretariaPorta = Integer.getInteger("sca.secretaria.port", 1101);

        try{
            RMIServerSocketFactory sockets = porta -> new ServerSocket(porta, 50, InetAddress.getByName(host));

            AlunoService aluno_service = (AlunoService) Naming.lookup("//" + alunoHost + ":" + alunoPorta + "/AlunoService");
            TurmaService turma_service = (TurmaService) Naming.lookup("//" + turmaHost + ":" + turmaPorta + "/TurmaService");

            SecretariaServent secretaria_servent = new SecretariaServent(aluno_service, turma_service);

            SecretariaService stub = (SecretariaService) UnicastRemoteObject.exportObject(secretaria_servent, 0, null, sockets);

            LocateRegistry.createRegistry(secretariaPorta, null, sockets);

            Naming.rebind("//" + host + ":" + secretariaPorta + "/SecretariaService", stub);

            System.out.println("SecretariaService Server iniciado em " + host + ":" + secretariaPorta + "/SecretariaService");

        }catch(Exception e){
            System.out.println("Erro SecretariaServer main: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
