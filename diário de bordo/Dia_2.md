Data Estelar 31012026 - A nave falha porque a IA mente com confiança

Acordei determinado a resolver o problema do PIN que não funcionava. Mandei o Claude investigar. A resposta? "Ah, é que eu usei uma API antiga com autenticação diferente." Em bom brasileiro: ele inventou uma integração que não existia. Não foi um bug sutil. Foi erro grosseiro. Junto com as falhas do docker, são erros que não são aceitáveis.

Depois de queimar uma quantidade absurda de tokens debugando, o login finalmente funcionou. Apareceu a lista de servidores. Cliquei no meu servidor e... nada. Tela vazia. Cadê as músicas?

Plot twist: o Claude nunca implementou a busca por músicas. Aquela mesma feature que ele disse estar pronta no dia anterior (veja a data estelar de 30012026). A IA mentiu descaradamente sobre o estado do projeto. Em linguagem corporativa, ela "alucinolou" que tinha feito o trabalho. 

Mais token queimado e cheguei em algo que funciona. Certo? Errado. Após implementar a funcionalidade de músicas, descobri que havia um problema de servidor. Meu servidor Plex não aceita conexões diretas. Precisa usar algo chamado relay. Segundo o claude, o relay é um intermediário da Plex que roteia as conexões permitindo o envio de dados mesmo em situações de bloqueio de internet.

Mandei implementar o relay. A resposta dele? Tentar implementar conexão direta de novo. Falei para ele implementar o relay de novo. Ele insistiu que era muito trabalho e devia ir pela conexão direta. Solução esta que eu falei que não funciona. Nesse sentido, ele imitou o comportamento de ~ certos ~ devs e PMs.

Foi aí que percebi um padrão: a IA parece ter dificuldade em mudar de direção. Ela quer continuar no caminho que escolheu, mesmo quando você mostra que é o caminho errado. Tive que ser bem explícito dizendo que NÃO QUERO CONEXÔES DIRETAS QUERO APENAS RELAY. 

Talvez eu podesse ter resolvido isso limpando contexto (que dizem que é bom ficar sempre abaixo dos 50%).

No final do dia, ainda não toquei música nenhuma. Mas pelo menos entendi melhor as limitações da ferramenta.O experimento continua. Amanhã: fazer o relay funcionar de verdade.

Ps.
Como a postagem de hoje é do dia de ontem e como trabalhei madrugada a dentro, amanhã haverá surpresas.

Pps.
Quem quiser acompanhar o github do projeto, é só pedir.