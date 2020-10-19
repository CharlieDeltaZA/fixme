package router;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class SomeName {
    private int ID;
    private PrintWriter out;
    private BufferedReader in;

    public SomeName(int ID, PrintWriter output, BufferedReader input) {
        this.ID = ID;
        this.out = output;
        this.in = input;
    }

    public int getID() {
        return (this.ID);
    }

    public PrintWriter getOut() {
        return (this.out);
    }

    public BufferedReader getIn() {
        return (this.in);
    }
}
