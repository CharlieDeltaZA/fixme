package router;

import java.io.BufferedReader;
import java.io.PrintWriter;

// Essentially just a class to store either Broker or Market objects to be
// referenced from the routers public methods.
public class Instance {
    private int ID;
    private PrintWriter out;
    private BufferedReader in;

    public Instance(int ID, PrintWriter output, BufferedReader input) {
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
