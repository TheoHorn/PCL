package syntaxer;
import java.util.ArrayList;

public class Node {
    private String name;
    private ArrayList<Node> children;

    public Node(String name) {
        this.name = name;
        this.children = new ArrayList<>();

    }

    public void addChild(Node type) {
        this.children.add(type);
    }

    public void addChildren(ArrayList<Node> children) {
        this.children.addAll(children);
    }

    public void removeChild(Node child) {
        this.children.remove(child);
    }

    public void removeChildren(ArrayList<Node> children) {
        this.children.removeAll(children);
    }

    public void removeChildren() {
        this.children.clear();
    }

    public ArrayList<Node> getChildren() {
        return this.children;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.name).append("\n");
        s.append("-- \n");
        for (Node child : this.children) {
            s.append(child.toString());
        }
        return s.toString();
    }
    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Node)) return false;
        Node n = (Node) obj;
        if (!this.name.equals(n.getName())) return false;
        if (this.children.size() != n.getChildren().size()) return false;
        for (int i = 0; i < this.children.size(); i++) {
            if (!this.children.get(i).equals(n.getChildren().get(i))) return false;
        }
        return true;
    }
}
