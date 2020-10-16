package router;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class Instance {

    private final int ID;
    private final PrintWriter out;
    private final BufferedReader in;

    public Instance(int id, PrintWriter out, BufferedReader in) {
        this.ID = id;
        this.out = out;
        this.in = in;
    }

    public int getID() {
        return ID;
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }
}
