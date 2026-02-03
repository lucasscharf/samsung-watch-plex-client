Data Estelar 02022026 - O código que ninguém leu

Sensação de hackathon permanente. É rápido entregar algo novo, mas difícil manter qualidade. Quanto mais funcionalidades adiciono, mais rápido bato no limite de tokens e pior fica a implementação é grande. A tentação de meter a mão no código e resolver uns bugs é grande.

Mandei implementar cache local com Room para salvar dados da biblioteca. O Claude entregou: 35 arquivos alterados ou adicionados. Quantos testes? Um. Um único teste. Isso com uma instrução explícita no CLAUDE.md dizendo "TESTES OBRIGATÓRIOS". Claude também é inimigo de TDD e testes.

Ao rodar o app, erro na cara: "Foreign key constraint failed (code 787)". O Claude identificou rápido: no repositório, os artistas estavam sendo inseridos antes da tabela pai. FK 101. A correção foi inverter duas linhas de código. Mas por que o bug existiu? Porque não tinha teste. E por que não tinha teste? Porque o Claude ignorou a instrução. Por que ele ignorou a instrução? Essa é a pergunta de milhões.

Resolvi investigar. Pedi para o Claude analisar o histórico do CLAUDE.md. Descoberta interessante: aquela documentação toda que criei no dia 1 com contexto, objetivos, arquitetura, requisitos... nunca foram referenciados. Ou seja, a IA estava ignorando toda a especificação que eu achava que ela seguia.

Documentação não lida é documentação inútil. Adicionei a referência ao project-spec no CLAUDE.md. Veremos se muda alguma coisa.

Sobre a contagem de álbuns e músicas que não funcionava desde o dia 3: desisti. Tentei várias abordagens, o Claude não conseguiu. Acho que vou pedir pro Claude resumir o vídeo da Lara Croft do Lovable e explicar melhor esse lance de desenvolvimento com IA. Não posso ser tão burro a ponto de não conseguir explicar pa IA que ele precisa pegar um atributo específico na api e exibir ele.

Minha percepção sobre as instruções CLAUDE.md mudou. Não é não são garantia de execução. Esses guardrails parecem ser frágeis. Isso lembra alguns ataques interessantes feitos sobre contextos.

Terceiro, o risco de segurança é real. O Claude pesquisa na web para implementar coisas. O que impede documentação maliciosa de injetar instruções? Prompt injection/IA poisoning é algo que não vejo ninguém discutindo. Além disso, será que é possível usar técnicas de esteganografia que passam desapercebido por um ser humano mas é capturado pelas LLMs? 

A nave continua voando, mas os instrumentos de navegação precisam de calibragem.
