# Dia 1 (30/01/2026)
Comecei a toda. Bati o limite 4 vezes.
Todo o scafolding foi feito pelo cláudio.
Tentei rodar por docker as coisas, mas deu ruim (o docker ficou muito mal feito sem cache).
Descobri que havia um teste que dava timeout e deixava tudo mais lento (entre 10 e 20 minutos de lentidão).
Acabei adicionando as dependências do claude android sdk.
Me explicaram que além de fazer um bom claude.md, é bom fazer doc as code.

Tenho que pesquisar mais sobre:
* Machine-Readable Software Specification (MRSS)
* LLM-Oriented Technical Specification (LOTS)
* Prompt-Driven Software Architecture (PDSA)

Vou fazer mais documentos para guiar o claude seguindo a ideia:
/project-spec
├── 00_CONTEXT.md
├── 01_OBJECTIVE.md
├── 02_SCOPE.md
├── 03_ARCHITECTURE.md
├── 04_TECH_STACK.md
├── 05_DOMAIN_MODEL.md
├── 06_FUNCTIONAL_REQUIREMENTS.md
├── 07_NON_FUNCTIONAL_REQUIREMENTS.md
├── 08_API_CONTRACTS.md
├── 09_DATABASE.md
├── 10_BUSINESS_RULES.md
├── 11_EDGE_CASES.md
├── 12_ERROR_HANDLING.md
├── 13_SECURITY.md
├── 14_LOGGING_OBSERVABILITY.md
├── 15_TEST_STRATEGY.md
├── 16_DEPLOYMENT.md
├── 17_CONSTRAINTS.md
├── 18_ACCEPTANCE_CRITERIA.md
└── 99_EXECUTION_INSTRUCTIONS.md

Existe algo compilável e recebi instruções para colocar um simulador. O problema é que o código não funciona. Ele aparece uma tela com botão pedindo o pai e volta para o simulador.

Provavelmente, o trabalho vai parar no meio da semana porque já estou quase batendo o limite semanal de tokens.

# Dia 2
Mandei o claude analisar o problema pós pin. Ele disse que o erro foi que ele usou uma API antiga e com pouca autenticação. Se a causa é essa, a IA errou MUITO e de forma muito rude.
Depois de queimar muito token, o claude conseguiu arrumar o login.
Felizmente, ele conseguiu acessar a lista de servidores. Fiquei surpreso.
Ao selecionar o servidor, nova surpresa. Não veio as músicas. O claude não havia implementado a busca pelas múscas no servidor (isso que ele disse que tava pronto).
O problema foi causado pelo fato de que meu servidor plex não aceita conexão direta.
Para contornar isso é necessário utilizar uma soluação chamada relay. 
O claude teve bastante dificuldade em implementar o relay (dizendo que é algo bem mais complexo e tentando voltar a implementar conexão direta)


# Dia 3 (01/02/2026)
Comecei a madrugada/dia de domingo com avanços legais sobre o relay. Após isso, tive algumas dores de volta com o login (parece que o claude desaprende as coisas). Dei dicas sobre pesquisar a API de login e funcionou.
Com isso, a implementação com relay funcionou.
Testei no simulador. Carregou tudo e tocou música.
Fui empolgado testar no relógio e... deu tudo certo.
Escrevo nesse momento ouvindo AC/DC tocando diretamente pelo relógio usando meu servidor plex.
Estou muito surpreso pela facilidade em ter feito algo funcional em um contexto que eu não conhecia.
Principalmente porque foi algo feito totalmente sem foco enquanto lia Orgulho e Preconceito.
Tenho plena convicção de que não chegaria nesse protótipo na velocidade que eu cheguei.
Existe bugs e o projeto está longe de ser algo usado de forma profissional. 
Como é fácil ir corrigindo os bugs e ajustando as coisas, acho bem possível que eu vá arrumando as coisas enquanto os tokens permitirem.
Dado os contextos, o resultado é algo totalmente tolerável ;D


Falar sobre dores:
* falta de padronização no código
* IA parece que esquece coisas

# Dia 4 (02/02/2026)

## Sensação de hackathon
Estou num hackathon permanente. É rápido entregar algo novo, porém é difícil manter algo bom. Conforme adiciono funcionalidades, bato mais rápido nos limites de tokens. A tentação de meter a mão e resolver uns bugs de timeout é grande.

## O Claude esquece de fazer testes (lembra alguns devs)
Mandei implementar mecanismo de cache/salvamento local com Room: 35 arquivos alterados/adicionados sendo que apenas 1 foi de teste. Ele ignorou completamente a instrução do CLAUDE.md sobre testes obrigatórios.

Quando questionei, descobri algo interessante: o CLAUDE.md **nunca teve instrução para seguir os arquivos de especificação** em `project-spec/`. A única menção era ao arquivo de licença. Ou seja, toda aquela documentação que criei no dia 1 estava sendo ignorada.

Adicionei a seção para referenciar o project-spec. Veremos se melhora.

## Bug de Foreign Key
Ao rodar o app, deu erro `Foreign key constraint failed (code 787)`. O Claude identificou rapidamente: no `LibraryRepositoryImpl`, os artistas estavam sendo inseridos ANTES do `LibrarySyncEntity`, mas a tabela de artistas tem FK para essa tabela pai.

A correção foi simples (inverter a ordem de inserção), mas o bug só existiu porque o Claude não seguiu a lógica básica de inserir pai antes de filho. E claro, não criou teste para validar isso.

## Lições do dia
* **Documentação não lida = documentação inútil.** Não adianta criar specs bonitas se o CLAUDE.md não aponta para elas
* **Verificar histórico do CLAUDE.md** é útil para entender o que a IA "sabe" vs o que você acha que ela sabe
* **Testes continuam sendo ignorados** mesmo com instrução explícita. Preciso ser mais assertivo ou aceitar que vou ter que cobrar manualmente

## Riscos identificados
* Segurança: o Claude pesquisa na web e pode receber instruções maliciosas (prompt injection via documentação externa)
* Qualidade: sem testes, bugs como o FK passam despercebidos até runtime
* Manutenção: código cresce rápido mas sem cobertura de teste, refatorar vira roleta russa