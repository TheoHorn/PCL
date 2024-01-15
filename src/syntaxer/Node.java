package syntaxer;
import java.io.*;
import java.util.ArrayList;

public class Node {
    private String name;
    private ArrayList<Node> children;

    public Node(String name) {
        this.name = name;
        this.children = new ArrayList<>();

    }

    public void addChild(Node type) {
        if (type != null){
            this.children.add(type);
        }
    }

    public void addChild(ArrayList<Node> children) {
        if (children == null) return;
        for (Node child : children) {
            this.addChild(child);
        }
    }

    public void addChildToFront(Node child) {
        if (child != null) {
            this.children.add(0, child);
        }
    }

    public void addChildToFront(ArrayList<Node> children) {
        if (children == null) return;
        for (int i = children.size() - 1; i >= 0; i--) {
            this.addChildToFront(children.get(i));
        }
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

    public void generateGraphviz(String outputPath) {
        StringBuilder dotCode = new StringBuilder("digraph G {\n");
        generateDotCode(this, dotCode);
        dotCode.append("}");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write(dotCode.toString());
            System.out.println("Graphviz code has been written to: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateDotCode(Node node, StringBuilder dotCode) {
        if (node != null) {
            dotCode.append("  \"").append(node.name).append("\";\n");

            for (Node child : node.children) {
                dotCode.append("  \"").append(node.name).append("\" -> \"").append(child.name).append("\";\n");
                generateDotCode(child, dotCode);
            }
        }
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
