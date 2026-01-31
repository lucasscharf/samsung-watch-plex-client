Data Estelar 30012026.1 — Primeiro dia de jornada no projeto PlexWatch. O capitão ainda não sabe pilotar a nave.

TL;DR: Bati o limite de tokens 4 vezes. O projeto compila, mas não funciona. Descobri que a vantagem de IA é forçar os devs a escreverem documentação.

Primeiro dia de "vibe coding" de verdade. O Claude montou todo o scaffolding do projeto: estrutura de pastas, dependências, arquitetura limpa com MVVM. Tudo bonitinho. Eu? Fiquei olhando e dando enter. 

O problema começou quando tentei rodar tudo via Docker. O Dockerfile que o Claude gerou não tinha cache de dependências. Cada build baixava o mundo inteiro. Erro de amador. Tentei fazer alguns ajustes. Porém, a estratégia de mandar ele ficar escrevendo os dockers/scripts por docker não deram muito certo. Acabei desistindo e indo para construção local. 

Aí veio a próxima surpresa: um teste com timeout infinito travando tudo. Uns 15 minutos esperando para descobrir que era um teste mal configurado. Deletei e segui em frente. Até aí, tudo bem. Partindo do princípio que código é algo barato, deletar código e mandar gerar novos testes é algo suave.
 
No meio do caminho, alguém me deu uma dica de ouro: "Doc as Code". A ideia é que quanto mais documentação estruturada você dá para a IA, melhor ela trabalha. Faz sentido. Mandei o claude criar uma pasta com especificações detalhadas, arquitetura, regras de negócio, casos de erro, tudo mastigado para o Claude.
 
Atualmente, o app compila, roda os testes, instala no emulador, abre uma tela, mostra um botão, e... volta para o launcher. Não faço a mais puta ideia do que está acontecendo, mas pelo menos não fritou nada ainda. Se o claude não mentiu para mim, o projeto está pronto, só falta fazer funcionar.
 
Estou quase estourando o limite semanal de tokens (mentira, está em 21%, mas achei absurdo isso em uma madrugada) no primeiro dia. Se o ritmo continuar assim, vou ter que racionar conversas. O próximo passo é mandar o claude investigar por que o app fecha sozinho.