import api.DirectedWeightedGraphAlgoImpl;

import javax.swing.*;

public class GUI extends JFrame {
    private final DirectedWeightedGraphAlgoImpl Algo;

    public GUI(DirectedWeightedGraphAlgoImpl algo) {
        this.Algo = algo;
    }

    public void draw() {
        new MyFrame(Algo);
    }

}
