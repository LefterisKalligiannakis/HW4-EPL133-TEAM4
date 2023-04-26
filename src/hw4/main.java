package hw4;

public class main {

    public static void main(String args[]){
        String[] trainingfiles = {"pg7178.txt", "pg2600.txt", "brown-train-sentences.txt"};
        ToeflSolver solver = new ToeflSolver(trainingfiles);
        solver.run_similarity_test("questionfile.txt", solver.getSemantic_descriptors());
    }
}
