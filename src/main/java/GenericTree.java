import java.util.ArrayList;
import java.util.LinkedList;

public class GenericTree {

    static class Node implements Cloneable{

        private final Schedule data;
        LinkedList<Node> children;

        /**
         * Constructor for Node class
         * @param data is a schedule
         */
        Node(Schedule data){
            this.data = data;
            children = new LinkedList<>();
        }

        public Schedule getData() {
            return data;
        }

        /**
         * Creates a deep Copy of a Node
         * @return a deep Copy of a Node
         */
        @Override
        public Node clone() {
            try {
                // TODO: copy mutable state here, so the clone can't change the internals of the original
                return (Node) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }

    private final Node root;

    /**
     * Constructor for a new tree
     * @param schedule is a given sequence of jobs which are already planed
     */
    GenericTree(Schedule schedule){
        this.root = new Node(schedule);
    }

    /**
     * Adds a new node to a given parent node
     * @param schedule is a given sequence of jobs which are already planed
     * @param parent is the Node above the child node
     */
    public void addNode(Schedule schedule, Node parent){
        Node node = new Node(schedule);
        parent.children.add(node);
    }

    /**
     * Gets the ith child of a parent node
     * @param parent is the parent node
     * @param i denotes the ith child of a parent node
     * @return the ith child
     */
    public Node nextNode(Node parent, int i){
        return parent.children.get(i);
    }


    /**
     * displays all schedule data of a tree
     */
    public void display(){
        display(this.getRoot());
    }

    /**
     * Prints and calls the nodes of a tree recursively
     * @param node ist the parent node
     */
    private void display(GenericTree.Node node){

        int unscheduled = node.data.allJobs.length - node.data.scheduledSequence.size();

        StringBuilder str = new StringBuilder(node.data.displayJobList() + " " +
                "*".repeat(Math.max(0, unscheduled)) + " " + node.data.displayJobsArray() + "-> ");

        for(GenericTree.Node child: node.children){
            str.append(child.data.displayJobsArray()).append(", ");
        }
        System.out.println(str);
        for(GenericTree.Node child: node.children){
            display(child);
        }
    }

    /**
     * Get the root of a tree
     * @return object is the root node
     */
    public Node getRoot() {
        return root;
    }
}