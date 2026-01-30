# Dia 1 e 2
Comecei a toda. Bati o limite 2 vezes.
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