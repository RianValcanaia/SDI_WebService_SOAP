package sca;

import Aluno.Aluno;
import Turma.Turma;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPFaultException;

public class WSClient {
    public static void main(String[] args) { 
        String urlServico = (args.length < 1) ? "http://127.0.0.1:9876/WSSCA" : args[0];

        try {
            URL url = URI.create(urlServico + "?wsdl").toURL();
            QName qname = new QName("http://sca/", "WSSCAService");

            Service service = Service.create(url, qname);

            WSSCA server = service.getPort(WSSCA.class);

            // Cadastrando aluno de teste
            System.out.println("== Cadastrando aluno de teste ==");
            server.cadastrarAluno(new Aluno("Rian", "rian.teste@email.com", "111111"));
            System.out.println("Aluno cadastrado: " + server.consultarAluno("111111"));

            // Cadastrando turma de teste
            System.out.println("\n== Cadastrando turma de teste ==");
            String codigoTurma = "SDI-2026";
            server.cadastrarTurma(new Turma(codigoTurma));
            System.out.println("Turma cadastrada: " + server.consultarTurma(codigoTurma));

            // Listando alunos cadastrados
            System.out.println("\n== Listando alunos cadastrados ==");
            for (Aluno a : server.listarAlunos()) {
                System.out.println(a);
            }

            // Listando turmas cadastradas
            System.out.println("\n== Listando turmas cadastradas ==");
            for (Turma t : server.listarTurmas()) {
                System.out.println(t);
            }

            // Matriculando aluno na turma pela Secretaria
            System.out.println("\n== Matriculando aluno na turma via Secretaria ==");
            server.matricularAluno("111111", codigoTurma);
            System.out.println("Alunos na turma " + codigoTurma + ":");
            for (Aluno a : server.listarAlunos(codigoTurma)) {
                System.out.println(a);
            }

            // Cancelando matricula via secretaria
            System.out.println("\n== Cancelando matrícula via Secretaria ==");
            server.cancelarMatricula("111111", codigoTurma);
            System.out.println("Alunos na turma " + codigoTurma + " após cancelamento:");
            for (Aluno a : server.listarAlunos(codigoTurma)) {
                System.out.println(a);
            }

            // Removendo dados de teste
            System.out.println("\n== Removendo dados de teste ==");
            server.removerAluno("111111");
            server.removerTurma(codigoTurma);
            System.out.println("Concluído.");

        } catch (Exception e) {
            System.out.println("Erro WSClient main: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
