.PHONY: class clean aluno-server turma-server secretaria-server ws-server server server-down ws-client test

# lib/ contém o JAX-WS (removido do JDK a partir do Java 11)
CP = ./class:./lib/*

class:
	mkdir -p ./class
	javac -cp "$(CP)" -d class $$(find src -name "*.java")

clean:
	rm -rf ./class src/data/aluno.csv src/data/turma.csv

aluno-server:
	java -cp "$(CP)" Aluno.AlunoServer

turma-server:
	java -cp "$(CP)" Turma.TurmaServer localhost

secretaria-server:
	java -cp "$(CP)" Secretaria.SecretariaServer localhost localhost

ws-server:
	java -cp "$(CP)" sca.WSServerPublisher localhost localhost localhost

server:
	java -cp "$(CP)" Aluno.AlunoServer & \
	sleep 1; \
	java -cp "$(CP)" Turma.TurmaServer localhost & \
	sleep 1; \
	java -cp "$(CP)" Secretaria.SecretariaServer localhost localhost & \
	sleep 1; \
	java -cp "$(CP)" sca.WSServerPublisher localhost localhost localhost & \
	wait

server-down:
	-pkill -f "Aluno.AlunoServer"
	-pkill -f "Turma.TurmaServer"
	-pkill -f "Secretaria.SecretariaServer"
	-pkill -f "sca.WSServerPublisher"

# cliente externo: acessa somente o WSSCA
ws-client:
	java -cp "$(CP)" sca.WSClient http://127.0.0.1:9876/WSSCA

test: clean class
	java -cp "$(CP)" Aluno.AlunoServer & \
	ALUNO_PID=$$!; \
	sleep 1; \
	java -cp "$(CP)" Turma.TurmaServer localhost & \
	TURMA_PID=$$!; \
	sleep 1; \
	java -cp "$(CP)" Secretaria.SecretariaServer localhost localhost & \
	SECRETARIA_PID=$$!; \
	sleep 1; \
	java -cp "$(CP)" sca.WSServerPublisher localhost localhost localhost & \
	WS_PID=$$!; \
	sleep 3; \
	java -cp "$(CP)" sca.WSClient http://127.0.0.1:9876/WSSCA; \
	kill $$ALUNO_PID $$TURMA_PID $$SECRETARIA_PID $$WS_PID 2>/dev/null
