# 07_LICENSE.md - Licença do Projeto

## Licença

Este projeto é licenciado sob a **GNU General Public License v3.0 (GPL-3.0)**.

O texto completo da licença está disponível no arquivo `LICENSE.md` na raiz do repositório.

---

## O que a GPL-3.0 Permite

| Permissão | Descrição |
|-----------|-----------|
| Uso comercial | O software pode ser usado para fins comerciais |
| Modificação | O código pode ser modificado |
| Distribuição | O software pode ser distribuído |
| Uso de patentes | Contribuidores concedem direitos de patente |
| Uso privado | O software pode ser usado e modificado de forma privada |

---

## Condições da GPL-3.0

| Condição | Descrição |
|----------|-----------|
| Código fonte disponível | O código fonte deve estar disponível ao distribuir |
| Mesma licença | Trabalhos derivados devem usar GPL-3.0 |
| Aviso de licença | A licença e copyright devem ser preservados |
| Documentar mudanças | Alterações no código devem ser documentadas |

---

## Instruções para Desenvolvimento

### ⚠️ NÃO Adicionar Cabeçalhos de Licença

**IMPORTANTE:** Este projeto **NÃO utiliza cabeçalhos de licença** nos arquivos de código fonte.

```kotlin
// ❌ NÃO FAZER ISSO:
/*
 * Copyright (C) 2024 PlexWatch
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License...
 */
package com.plexwatch.domain.model

// ✅ CORRETO - Apenas o código, sem cabeçalho:
package com.plexwatch.domain.model
```

### Motivos para Não Usar Cabeçalhos

1. **Simplicidade** - Mantém os arquivos limpos e focados no código
2. **Manutenção** - Evita necessidade de atualizar ano/autor em múltiplos arquivos
3. **Licença centralizada** - O arquivo `LICENSE.md` na raiz cobre todo o projeto
4. **Padrão moderno** - Muitos projetos open source modernos adotam essa abordagem

### Cobertura Implícita

Todos os arquivos no repositório estão **implicitamente cobertos** pela licença GPL-3.0 definida no arquivo `LICENSE.md`, exceto quando explicitamente indicado de outra forma.

---

## Ao Contribuir

Ao contribuir com código para este projeto, você concorda que:

1. Sua contribuição será licenciada sob GPL-3.0
2. Você tem o direito de licenciar o código sob GPL-3.0
3. Sua contribuição não viola direitos de terceiros

---

## Referências

- [Texto completo da GPL-3.0](https://www.gnu.org/licenses/gpl-3.0.html)
- [FAQ da GPL](https://www.gnu.org/licenses/gpl-faq.html)
- [Choose a License - GPL-3.0](https://choosealicense.com/licenses/gpl-3.0/)