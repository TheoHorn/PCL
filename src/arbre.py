import networkx as nx
import matplotlib.pyplot as plt

import json

file = "src/arbre.json"
G = nx.Graph()
nodes = []

with open(file, 'r') as json_file:
    data = json.load(json_file)
    for node in data:
        nodes.append(node)
        G.add_node(node)
        for child in data[node]:
            G.add_edge(node, child)

nx.draw(G, with_labels=True, font_weight='bold')
plt.show()