Data Estelar 01022026 - A nave chegou onde queria (ou quase)

Comecei a madrugada de domingo ainda lutando com o relay. Depois de toda a resistência do dia anterior, finalmente consegui fazer o Claude implementar direito. Mas aí veio uma surpresa desagradável: o login parou de funcionar de novo. O mesmo problema que já tinha sido resolvido voltou a aparecer.
Já tava malemolente nesse problema. Mandei o Claude pesquisar a API de login novamente. Funcionou. É estranho ter que relembrar algo que já estava funcionando. Me pergunto qual viabilidade de fazer isso em um projeto com muitas regras.

Com login e relay funcionando juntos, testei no emulador. Carregou a biblioteca. Mostrou os artistas. Listou as músicas. Apertei play e deu tudo certo. Realmente tocou música. Foi impressionante.

Peguei o relógio. Instalei no relógio. Abri o app no relógio. Fiz login no relógio. Selecionei o servidor no relógio. Dei play no relógio e... funcionu. Back in Black no auto falante do relógio do Galaxy Watch.

Confesso que fiquei impressionado. Três dias atrás eu não sabia nada de Wear OS, não conhecia a API do Plex, nunca tinha usado Jetpack Compose. E agora tenho um app minimamente funcional no pulso. Tudo isso enquanto lia Orgulho e Preconceito.

Não tenho ilusões. O código tem bugs, a arquitetura precisa de ajustes, e está longe de ser algo que eu colocaria em produção. Mas como prova de conceito? Impressionante. Contudo, a velocidade de prototipagem é real.

Avaliando rapidamente, as principais dores que eu vi:

* Mesmo com alguns guardrails, o código gerado não segue um padrão consistente. Cada parte parece ter sido escrita por uma pessoa diferente.

* Parece que quanto mais contexto acumula, mais a IA perde informações antigas. Coisas que ela "sabia" ela "passa a esquecer". Soluções precisaram ser reimplementadas porque ela cagou o rolê. Consequência da cadeia de Markov. O negócio é aceitar que as coisas são assim.

* O Claude pesquisa informações na web para implementar coisas. O que impede alguém de colocar instruções maliciosas em uma documentação de API? A IA pode estar executando código que veio de uma fonte comprometida.

* Mesmo tentando MUITAS vezes, não consegui fazer fazer com que claude retornasse a quantidade de faixas no álbum. Pesquisando no google, achei a origem da informação errada dele. Um dev humano conseguiria ter a malemolência de fazer a pesquisa correta.

* Por algum motivo, o sistema resolveu fazer duas telas diferentes para o tocador. Dependendo do caminho que tu segue, você vê algo diferente.

E o que eu vou fazer? Bem, o app funciona, mas precisa de polimento. A interface está feia, a navegação é confusa, a parte de contagem de álbuns e músicas está ruim, não consigo pausar a música de uma forma rápida, não tem nada de memória/cache no relógio e provavelmente tem memory leaks em algum lugar. Enquanto os tokens permitirem, vou mandando o Claude corrigir.

Como diria Fitzwilliam Darcy, a prova de conceito foi tolerável.