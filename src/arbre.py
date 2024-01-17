import networkx as nx
import matplotlib.pyplot as plt
import random

import json

file = "src/arbre.json"
G = nx.DiGraph()

def hierarchy_pos(G, root=None, width=1., vert_gap = 0.2, vert_loc = 0, xcenter = 0.5):

    
    if not nx.is_tree(G):
        raise TypeError('cannot use hierarchy_pos on a graph that is not a tree')

    if root is None:
        if isinstance(G, nx.DiGraph):
            root = next(iter(nx.topological_sort(G)))  #allows back compatibility with nx version 1.11
        else:
            root = random.choice(list(G.nodes))

    # def _hierarchy_pos(G, root, width=1., vert_gap = 0.2, vert_loc = 0, xcenter = 0.5, pos = None, parent = None):
    
    #     if pos is None:
    #         pos = {root:(xcenter,vert_loc)}
    #     else:
    #         pos[root] = (xcenter, vert_loc)
    #     children = list(G.neighbors(root))
    #     if not isinstance(G, nx.DiGraph) and parent is not None:
    #         children.remove(parent)  
    #     if len(children)!=0:
    #         dx = width/len(children) 
    #         nextx = xcenter - width/2 - dx/2
    #         for child in children:
    #             nextx += dx
    #             pos = _hierarchy_pos(G,child, width = dx, vert_gap = vert_gap, 
    #                                 vert_loc = vert_loc-vert_gap, xcenter=nextx,
    #                                 pos=pos, parent = root)
    #     return pos

            
    # return _hierarchy_pos(G, root, width, vert_gap, vert_loc, xcenter)



with open(file, 'r') as json_file:
    data = json.load(json_file)

G = nx.DiGraph()

def process_node(node_data, parent_id=None):
    node_id = node_data["id"]
    node_name = node_data["name"]
    G.add_node(node_id, id=node_id, name=node_name)  # Ajout des attributs ID et name au nœud
    if parent_id is not None:
        G.add_edge(parent_id, node_id)
    for child in node_data.get("children", []):
        process_node(child, node_id)

process_node(data)

# Affichage du graphe
pos = nx.spring_layout(G)
node_labels = {node_id: f"{G.nodes[node_id]['name']}" for node_id in G.nodes}
nx.draw(G, pos, with_labels=True, labels=node_labels)
plt.show()

#pos_hierarchy = hierarchy_pos(G, root=root, width=1., vert_gap=0.2, vert_loc=0, xcenter=0.5)

# Dessiner le graphe
#nx.draw(G, pos=pos_hierarchy, with_labels=True, font_weight='bold', arrowsize=20, node_size=700, node_color='skyblue', font_size=8)
    
nx.draw(G, with_labels=True, font_weight='bold', arrowsize=20, node_size=700, node_color='skyblue', font_size=8)
plt.show()

