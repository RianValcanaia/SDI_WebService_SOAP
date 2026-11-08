# Sistema de matrícula com RMI + Web Service SOAP

Implementação de um sistema de matrícula escolar usando Java RMI, dividido em três serviços remotos independentes: `Aluno`, `Turma` e `Secretaria`. Cada um roda em seu próprio processo, e a `Secretaria` orquestra os outros dois para matricular e cancelar matrícula de alunos em turmas.

### Arquitetura

Pacotes: `Aluno`, `Turma`, `Secretaria` (RMI) e `sca` (Web Service), todos em [src/](src/).

Cada serviço RMI segue o padrão `Service` (interface remota) / `Servent` (implementação) / `Server` (processo que sobe o RMI registry e publica o serviço):

| Serviço | Interface | Implementação | Bootstrap | Porta |
|---|---|---|---|---|
| Aluno | `AlunoService` | `AlunoServent` | `AlunoServer` | 7000 |
| Turma | `TurmaService` | `TurmaServent` | `TurmaServer` | 7010 |
| Secretaria | `SecretariaService` | `SecretariaServent` | `SecretariaServer` | 7020 |

- `AlunoServent` e `TurmaServent` persistem os dados em CSV (`src/data/aluno.csv` e `src/data/turma.csv`, caminhos relativos à raiz do projeto, de onde o makefile roda), lidos ao iniciar e regravados a cada alteração.
- `TurmaServent` não guarda os dados completos do aluno — só a matrícula — e resolve o restante consultando `AlunoService`  via RMI.
- `SecretariaServent` não guarda estado próprio: recebe referências remotas de `AlunoService` e `TurmaService` e delega as operações.

Por isso, a ordem de inicialização importa: `AlunoServer` precisa estar no ar antes do `TurmaServer`, e ambos antes do `SecretariaServer`.

## Web Service SOAP (`sca`)

O `WSServerPublisher` publica um único serviço, `WSSCA`, cuja implementação é **cliente RMI** dos três serviços acima. O cliente externo (`WSClient`) só conversa com o WS e não tem acesso ao RMI.

```
WSClient --SOAP/HTTP--> WSServerPublisher (despachante + esqueleto) --> WSSCAImpl --RMI--> AlunoServer, TurmaServer, SecretariaServer
```

### Dependências

O JAX-WS saiu do JDK a partir do Java 11. Os JARs ficam em [lib/](lib/).

## Compilar

```bash
make class
```

## Rodar

Cada servidor recebe como argumento os hosts dos serviços de que depende (padrão `localhost`). O endereço do próprio servidor vem de `-Djava.rmi.server.hostname` nos serviços RMI e do último argumento no WS; as portas só escutam nesse endereço. Ordem: Aluno → Turma → Secretaria → WS.

```bash
# máquina do Aluno
java -Djava.rmi.server.hostname=<host-aluno> -cp "./class:./lib/*" Aluno.AlunoServer

# máquina da Turma (precisa do host do Aluno)
java -Djava.rmi.server.hostname=<host-turma> -cp "./class:./lib/*" Turma.TurmaServer <host-aluno>

# máquina da Secretaria (precisa dos hosts do Aluno e da Turma)
java -Djava.rmi.server.hostname=<host-secretaria> -cp "./class:./lib/*" Secretaria.SecretariaServer <host-aluno> <host-turma>

# máquina do WS (precisa dos 3 hosts RMI e do próprio endereço)
java -cp "./class:./lib/*" sca.WSServerPublisher <host-aluno> <host-turma> <host-secretaria> <host-ws>

# cliente externo (só precisa do URL do serviço)
java -cp "./class:./lib/*" sca.WSClient http://<host-ws>:9876/WSSCA
```

Tudo localmente, com cada serviço em um endereço de loopback (Aluno `127.0.0.2`, Turma `127.0.0.3`, Secretaria `127.0.0.4`, WS `127.0.0.5`):

```bash
make server        # sobe os 3 servidores RMI + WS
make ws-client     # cliente SOAP
make server-down   # derruba os processos
```

Para usar outros endereços: `make server ALUNO_HOST=10.0.0.1`.

## Testar

Sobe tudo, roda o `WSClient` e derruba os servidores:

```bash
make test
```

## Limpar artefatos gerados

```bash
make clean
```
