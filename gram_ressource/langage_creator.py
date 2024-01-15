from nltk.parse.generate import generate
from nltk import CFG
from random import choice

def get_production() -> str:
    with open('pcl/gram_ressource/gram.txt', 'r') as f:
        productions = f.readlines()
        dico = {}
        for production in productions:
            if '\n' in production:
                production = production.replace('\n', '')
                production = production.split('->')
            if len(production) != 2:
                continue

            l = production[0].strip()
            r = production[1].split(' ')
            for i in range(len(r)):
                if not r[i].isupper() and r[i] !='':
                    if "character'val" in r[i]:
                        r[i] = "'characterval'"
                    else:
                        if r[i] == "''":
                            continue
                        else:
                            r[i] = "'" + r[i] + "'"  
            r = ' '.join(r)

            if l not in dico:
                dico.update({l: r})
            else:
                dico.update({l: dico[l] + ' | ' + r})

    ans = ''
    for key in dico:
        ans += key + ' -> ' + dico[key] + '\n'
    return ans

def produce(grammar, symbol):
    words = []
    productions = grammar.productions(lhs = symbol)
    production = choice(productions)
    for sym in production.rhs():
        if isinstance(sym, str):
            words.append(sym)
        else:
            words.extend(produce(grammar, sym))
    return words


#print(get_production())
grammar = CFG.fromstring(get_production())
#print(grammar)


for sentence in generate(grammar, depth=7, n=200):
    print(' '.join(sentence))