Data Estelar 03022026 - Fim da missão: a nave pousa antes do destino

TL;DR: Após um procedimento médico, tentei implementar um contador de reproduções. Não consegui. Decidi encerrar o experimento. Vibe coding não é o que prometem.

Dia de recuperação. Passei a maior parte do tempo grogue, me recuperando de um procedimento médico. Mesmo assim, a teimosia falou mais alto e, durante a noite, resolvi tentar uma última feature: um contador de vezes que cada música foi reproduzida. Parecia simples. Spoiler: não foi.

O Claude não conseguiu. Eu não consegui entender direito. O ciclo se repetiu: prompt, erro, correção, novo erro, mais prompt. AAAAAHHHHH. Aquela dança que já conhecia bem dos dias anteriores. Só que dessa vez, enquanto esperava mais uma resposta (afinal muito do tempo de vibe coding eu estava parado), veio a clareza que faltava.

O experimento perdeu o sentido.

A promessa do vibe coding é sedutora: tenha uma ideia, descreva em linguagem natural, e a IA constrói para você. Democratização do desenvolvimento. Qualquer um pode criar software. O futuro chegou.

Só que a realidade é outra. Para a IA funcionar minimamente, precisei:

- Documentar exaustivamente (project-spec com 7+ arquivos)

- Planejar a arquitetura antes

- Criar guardrails no CLAUDE.md (alguns deles ignorados)

- Debugar alucinações constantemente (e como era o próprio claude quem gerou os testes, os testes eram não confiáveis)

- Reimplementar features que a IA "esquecia"

- Ignorar inconsistências de estilo

Se eu preciso fazer todo esse trabalho de preparação e ainda assim não tenho garantia de que o código vai funcionar, qual a vantagem? A velocidade de prototipagem é real, isso eu admito. Três dias para um app tocando música no relógio, partindo do zero e sem conhecimento, é impressionante. Mas velocidade sem confiabilidade é apenas dívida técnica acelerada.

O método tradicional é mais lento para fazer, mas eu sei o que está sendo feito. Tenho confiança na entrega. Consigo debugar porque entendo o código. Com IA, é fé.

Quem é mais velho, já ouviu falar sobre a crise no desenvolvimento de software. Esssa "crise" nos acompanha desde os anos 80. A pandemia injetou gente menos preparada no mercado, e agora a IA vai acelerar isso. Amadores que deixariam Dunning-Krueger embasbacado colocando software em produção em uma semana só porque parece bonito e funcional. O que pode dar errado?

Agradeço a todos que acompanharam a saga até aqui. O repositório continua público para quem quiser estudar o código (ou rir dele). O app funciona, pelo menos para o básico. Talvez um dia eu volte para polir. Talvez não.

Acho que tentarei experimentar IA como outras formas de melhorar a produtividade. Por enquanto, deixá-la como autora principal de um programa não é o caminho.

Como diria o Capitão Kirk: "O espaço é a fronteira final." Mas às vezes, a fronteira mais sábia é saber quando voltar para casa.

Vida longa e próspera.