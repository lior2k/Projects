import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgoImpl;
import api.DirectedWeightedGraphAlgorithms;

/**
 * This class is the main class for Ex2 - your implementation will be tested using this class.
 */
public class Ex2 {
    /**
     * This static function will be used to test your implementation
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    static String G3 = "src/data/G3.json";
    static String G2 = "src/data/G2.json";
    static String G1 = "out/artifacts/Ex2_jar/G1.json";
    static String nodes1000 = "src/data/1000Nodes.json";

    public static DirectedWeightedGraph getGrapg(String json_file) {
        DirectedWeightedGraphAlgorithms algo = new DirectedWeightedGraphAlgoImpl();
        algo.load(json_file);
        return algo.getGraph();
    }
    /**
     * This static function will be used to test your implementation
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        DirectedWeightedGraphAlgorithms ans = new DirectedWeightedGraphAlgoImpl();
        ans.load(json_file);
        return ans;
    }
    /**
     * This static function will run your GUI using the json fime.
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     *
     */
    public static void runGUI(String json_file) {
        DirectedWeightedGraphAlgorithms alg = getGrapgAlgo(json_file);
        GUI Gui = new GUI((DirectedWeightedGraphAlgoImpl) alg);
        Gui.draw();
    }

    public static void main(String[] args) {
        runGUI(args[0]);
    }
}