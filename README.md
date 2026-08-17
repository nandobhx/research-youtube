# Análise de canais do YouTube Brasil no nicho de programação por meio da centralidade e modularidade temática nas redes de coocorrência de palavras.

---

## 📄 Resumo

O YouTube tornou-se uma importante fonte de aprendizado em programação, porém ainda são poucos os estudos que investigam como os canais se organizam tematicamente a partir dos metadados dos vídeos. Este trabalho propõe uma abordagem baseada em Processamento de Linguagem Natural (PNL) e redes complexas para analisar títulos e descrições de vídeos de canais brasileiros do nicho de programação. A partir de 362 vídeos de 233 canais, foram construídos um grafo bipartido de palavras e canais e um grafo de coocorrência de palavras, analisados por meio da Betweenness Centrality e do algoritmo de Louvain. Os resultados identificaram palavras que desempenham papel estrutural na conexão entre canais, revelaram três comunidades temáticas interconectadas e mostraram que o porte dos canais exerce baixa influência na formação dessas comunidades. Esses resultados demonstram que padrões de organização temática podem ser inferidos apenas por meio dos metadados dos vídeos, contribuindo para a compreensão da estrutura de conteúdo do YouTube no domínio de programação.

---

## 👤 Autor

**Fernando L. R. L. Belém**

**Programa de Pós-Graduação em Informática**  
Pontifícia Universidade Católica de Minas Gerais (PUC-MG)

📍 Belo Horizonte - MG - Brasil

📧 fernando.belem@sga.pucminas.br

---

## ⚙️ Requisitos

Para a execução do experimento, são necessários os seguintes requisitos:

- **Java 17**
- **Conexão com a internet**

---

## 🔧 Configuração para execução

Crie, no diretório raiz do projeto, um arquivo denominado `security.properties` contendo a seguinte propriedade:


```properties
API_KEY=<SUA_CHAVE_DE_API_DO_YOUTUBE>
```

---

## 📊 Dados

Os arquivos de dados e o projeto do Gephi utilizados na análise dos resultados estão disponíveis na pasta `data`.