package sca;

import javax.xml.ws.Endpoint;

public class WSServerPublisher {

    public static void main(String[] args) {
        // hosts dos serviços RMI, lidos pelo WSSCAImpl a cada chamada
        System.setProperty("sca.aluno.host", (args.length < 1) ? "localhost" : args[0]);
        System.setProperty("sca.turma.host", (args.length < 2) ? "localhost" : args[1]);
        System.setProperty("sca.secretaria.host", (args.length < 3) ? "localhost" : args[2]);

        try {
            Endpoint.publish("http://127.0.0.1:9876/WSSCA", new WSSCAImpl());
            System.out.println("WSSCA publicado em http://127.0.0.1:9876/WSSCA");
        } catch (Exception e) {
            System.out.println("Erro WSServer main: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
