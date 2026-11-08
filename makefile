.PHONY: class clean clean-data aluno-server turma-server secretaria-server ws-server server server-down ws-client test

# lib/ contém o JAX-WS (removido do JDK a partir do Java 11)
CP = ./class:./lib/*

# O cluster roda Java 11 e não tem javac: as classes são geradas para Java 11
# em uma máquina com JDK e versionadas em class/.
# Para usar outro compilador: make class JAVAC=/caminho/do/jdk/bin/javac
JAVAC ?= javac
JAVA_RELEASE = 11

class:
	@if command -v "$(JAVAC)" >/dev/null 2>&1; then \
		mkdir -p ./class; \
		echo "$(JAVAC) --release $(JAVA_RELEASE) -cp \"$(CP)\" -d class ..."; \
		"$(JAVAC)" --release $(JAVA_RELEASE) -cp "$(CP)" -d class $$(find src -name "*.java"); \
	elif [ -f class/sca/WSClient.class ]; then \
		echo "$(JAVAC) não encontrado: usando as classes já compiladas em class/"; \
	else \
		echo "$(JAVAC) não encontrado e class/ não tem as classes compiladas" >&2; \
		exit 1; \
	fi

clean-data:
	rm -f src/data/aluno.csv src/data/turma.csv

clean: clean-data
	@if command -v "$(JAVAC)" >/dev/null 2>&1; then \
		rm -rf ./class; \
	else \
		echo "$(JAVAC) não encontrado: class/ preservada"; \
	fi

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

test: clean-data class
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
