
enum WordType{NO_CAPS, ALL_CAPS, FIRST_CAP};

public class Node {

    private Node[] nodeArray;
    private Node parent;
    private boolean isTerminal;
    private WordType type;
    private int pos;

    //constructor
    public Node(Node[] nodes, Node parentVal, boolean isTerminalVal, WordType typeVal, int posVal) {
        pos = posVal;
        nodeArray = nodes;
        parent = parentVal;
        type = typeVal;
        pos = posVal;
        isTerminal = isTerminalVal;
    }

    //methods
    public void setNodeArray(int posVal, Node node) {
      nodeArray[posVal] = node;
    }
    public Node getNode (int posVal) {
        return nodeArray[posVal];
    }
    public Node getParent () {
        return parent;
    }
    public void setIsTerminal(boolean isTerminalVal) {
        isTerminal = isTerminalVal;
    }
    public boolean getIsTerminal() {
        return isTerminal;
    }
    public void setType(WordType typeVal) {
        type = typeVal;
    }
    public WordType getType() {
        return type;
    }
    public int getPos() {
         return pos;
    }

}
