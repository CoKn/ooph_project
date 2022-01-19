import java.util.ArrayList;

public class GenericTree {

    static class Node{
        private final Schedule data;
        ArrayList<Node> children;

        Node(Schedule data){
            this.data = data;
            children = new ArrayList<>();
        }

        public Schedule getData() {
            return data;
        }
    }
    private final Node root;

    GenericTree(Schedule schedule){
        this.root = new Node(schedule);
    }

    /**
     * Adds a new node to a given parent node
     * @param schedule
     * @param parent
     */
    public void addNode(Schedule schedule, Node parent){
        Node node = new Node(schedule);
        parent.children.add(node);
    }

    /**
     * Iterrates over a tree
     * @param root
     * @return
     */
    public Node iterTree(Node root){
        Node node = root;
        for(int i=0; i<root.children.size(); i++){
            node = nextNode(root, i);
            iterTree(node);
            // node.getData().displayJobs();
        }
        return node;
    }

    public Node nextNode(Node parent, int i){
        return parent.children.get(i);
    }


    /**
     * displays all schedule data of a tree
     */
    public void display(){
        display(this.getRoot());
    }

    private void display(GenericTree.Node node){
        StringBuilder str = new StringBuilder(node.data.displayJobs() + "-> ");

        for(GenericTree.Node child: node.children){
            str.append(child.data.displayJobs()).append(", ");
        }
        System.out.println(str);
        for(GenericTree.Node child: node.children){
            display(child);
        }
    }

    /**
     * Get the root of a tree
     * @return
     */
    public Node getRoot() {
        return root;
    }
}