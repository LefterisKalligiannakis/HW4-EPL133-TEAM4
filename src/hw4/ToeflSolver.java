package hw4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.io.FileWriter;

public class ToeflSolver {

    private final HashMap<String, HashMap<String, Integer>> semantic_descriptors;
    public ToeflSolver(String[] files){
        semantic_descriptors = Trainer.build_semantic_descriptors(Trainer.get_sentence_lists_from_files(files));
    }

    public HashMap<String, HashMap<String, Integer>> getSemantic_descriptors() {
        return semantic_descriptors;
    }

    private double norm(HashMap<String, Integer> vec){
        double sum_of_squares = 0.0;

        for(String x : vec.keySet())
            sum_of_squares += vec.get(x)*vec.get(x);

        return Math.sqrt(sum_of_squares);
    }
    private double cosine_similarity(HashMap<String, Integer> vec1,HashMap<String, Integer> vec2){
        double dot_product = 0.0;

        for(String x : vec1.keySet())
            if(vec2.containsKey(x))
                dot_product += vec1.get(x)*vec2.get(x);

        return dot_product/(norm(vec1)*norm(vec2));
    }
    private String[] splitQuestion(String[] question){
        String[] n = new String[question.length-1];

        for(int i=0; i<question.length-1; i++)
            n[i] = question[i+1];

        return n;
    }
    public String most_similar_word(String word, String[] choices, HashMap<String, HashMap<String, Integer>> semantic_descriptors) {
        String mostSimilar = choices[0];
        double maxcosine =  -1;
        HashMap<String, Integer> wordVec;
        HashMap<String, Integer> choiceVec;

        if(semantic_descriptors.containsKey(word))
            wordVec = semantic_descriptors.get(word);
        else
            return mostSimilar;

        for(String choice : choices) {
            if(semantic_descriptors.containsKey(choice)) {
                choiceVec = semantic_descriptors.get(choice);
                if (cosine_similarity(wordVec, choiceVec) > maxcosine) {
                    maxcosine = cosine_similarity(wordVec, choiceVec);
                    mostSimilar = choice;
                }
            }
        }


        return mostSimilar;
    }
    public void run_similarity_test(String filename, HashMap<String, HashMap<String, Integer>> hash_map) {

        String word, answer, output = "";
        int counter = 0, correct = 0;


        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {

                Question question = new Question(line.split(" "));
                counter++;
                question.setGivenAnswer(most_similar_word(question.getWord(), question.getChoices(), hash_map));

                System.out.println(question);
                output += question + "\n\n";


                if (question.getAnswer().equals(question.getCorrectAnswer()))
                    correct++;

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter("output.txt");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        output += "\n\n" + (double)((correct/(double)counter)*100) + "% correct";

        try{
        writer.write(output);
        writer.close();
        }catch (IOException e){
            System.out.println("error writing in file");
        }

        System.out.println((double)((correct/(double)counter)*100) + "% correct");


    }
    public static void main(String args[]){
        String[] trainingfiles = {"pg7178.txt", "pg2600.txt", "brown-train-sentences.txt"};
        ToeflSolver solver = new ToeflSolver(trainingfiles);
        solver.run_similarity_test("questionfile.txt", solver.semantic_descriptors);
    }
}
