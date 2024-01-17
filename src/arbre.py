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
    root = data["name"]
    G.add_node(root)

    def process_node(node_data, parent_name=None):
        node_name = node_data["name"]
        G.add_node(node_name)
        if parent_name is not None:
            G.add_edge(parent_name, node_name)
        for child in node_data.get("children", []):
            process_node(child, node_name)

    process_node(data)


#pos_hierarchy = hierarchy_pos(G, root=root, width=1., vert_gap=0.2, vert_loc=0, xcenter=0.5)

# Dessiner le graphe
#nx.draw(G, pos=pos_hierarchy, with_labels=True, font_weight='bold', arrowsize=20, node_size=700, node_color='skyblue', font_size=8)
    
nx.draw(G, with_labels=True, font_weight='bold', arrowsize=20, node_size=700, node_color='skyblue', font_size=8)
plt.show()

