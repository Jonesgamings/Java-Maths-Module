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
    public boolean equals(Object obj) {
        if (obj == null) {return false;}

        if (obj.getClass() != this.getClass()) {return false;}
        LogNode node = (LogNode) obj;
        if (!base.equals(node.base)) {return false;}
        if (!insideLog.equals(node.insideLog)) {return false;}
        return true;
    }

    @Override
    public String toString()
    {
        return "LOG : (" + this.base + "," + this.insideLog + ")";
    }
}
