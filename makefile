.PHONY: class clean clean-data aluno-server turma-server secretaria-server ws-server server server-down ws-client test

# lib/ contém o JAX-WS (removido do JDK a partir do Java 11)
CP = ./class:./lib/*
JAVAC ?= javac
JAVA_RELEASE = 11

# Endereço de cada serviço. Para trocar: make server ALUNO_HOST=10.0.0.1
ALUNO_HOST ?= 127.0.0.2
TURMA_HOST ?= 127.0.0.3
SECRETARIA_HOST ?= 127.0.0.4
WS_HOST ?= 127.0.0.5

RUN_ALUNO = java -Djava.rmi.server.hostname=$(ALUNO_HOST) -cp "$(CP)" Aluno.AlunoServer
RUN_TURMA = java -Djava.rmi.server.hostname=$(TURMA_HOST) -cp "$(CP)" Turma.TurmaServer $(ALUNO_HOST)
RUN_SECRETARIA = java -Djava.rmi.server.hostname=$(SECRETARIA_HOST) -cp "$(CP)" Secretaria.SecretariaServer $(ALUNO_HOST) $(TURMA_HOST)
RUN_WS = java -cp "$(CP)" sca.WSServerPublisher $(ALUNO_HOST) $(TURMA_HOST) $(SECRETARIA_HOST) $(WS_HOST)
RUN_CLIENT = java -cp "$(CP)" sca.WSClient http://$(WS_HOST):9876/WSSCA

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
	$(RUN_ALUNO)

turma-server:
	$(RUN_TURMA)

secretaria-server:
	$(RUN_SECRETARIA)

ws-server:
	$(RUN_WS)

server:
	$(RUN_ALUNO) & \
	sleep 1; \
	$(RUN_TURMA) & \
	sleep 1; \
	$(RUN_SECRETARIA) & \
	sleep 1; \
	$(RUN_WS) & \
	wait

server-down:
	-pkill -f "Aluno.AlunoServer"
	-pkill -f "Turma.TurmaServer"
	-pkill -f "Secretaria.SecretariaServer"
	-pkill -f "sca.WSServerPublisher"

# cliente externo: acessa somente o WSSCA
ws-client:
	$(RUN_CLIENT)

test: clean-data class
	$(RUN_ALUNO) & \
	ALUNO_PID=$$!; \
	sleep 1; \
	$(RUN_TURMA) & \
	TURMA_PID=$$!; \
	sleep 1; \
	$(RUN_SECRETARIA) & \
	SECRETARIA_PID=$$!; \
	sleep 1; \
	$(RUN_WS) & \
	WS_PID=$$!; \
	sleep 3; \
	$(RUN_CLIENT); \
	kill $$ALUNO_PID $$TURMA_PID $$SECRETARIA_PID $$WS_PID 2>/dev/null
