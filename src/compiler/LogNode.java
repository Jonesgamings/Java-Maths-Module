package compiler;

public class LogNode implements Node{

    Node base;
    Node insideLog;

    public LogNode(Node base, Node insideLog)
    {
        this.base = base;
        this.insideLog = insideLog;
    }

    @Override
    public String toString()
    {
        return "LOG : (" + this.base + "," + this.insideLog + ")";
    }
}
