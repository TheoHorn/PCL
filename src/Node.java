import java.util.ArrayList;

public class Node {
    private String name;
    private ArrayList<Node> children;

    public Node(String name) {
        this.name = name;
        this.children = new ArrayList<>();

    }

    public void addChild(Node child) {
        this.children.add(child);
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
        s.append("-- \n")
        for (Node child : this.children) {
            s.append(child.toString());
        }
        return s.toString();
    }
    
}
