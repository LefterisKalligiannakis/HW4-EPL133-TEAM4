package hw4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Trainer {

    private static void addWord(ArrayList<ArrayList<String>> list, String word, int sentence){
        list.get(sentence).add(word.toLowerCase());
    }

    private static void newSentence(ArrayList<ArrayList<String>> list){
        list.add(new ArrayList<String>());
    }

    private static void newHash(HashMap<String, HashMap<String, Integer>> hash_map, String word){
        hash_map.put(word, new HashMap<String, Integer>());
    }

    private static void addToSubhash(HashMap<String, HashMap<String, Integer>> hash_map ,String word, String word2){

        if(hash_map.get(word).containsKey(word2)){
            int val = hash_map.get(word).get(word2) + 1;
            hash_map.get(word).put(word2, val);
        }else{
            hash_map.get(word).put(word2, 1);
        }

    }

    public static ArrayList<ArrayList<String>> getSentenceLists(String text){


        ArrayList<ArrayList<String>> list = new  ArrayList<ArrayList<String>>();

        //Sentence flags
        ArrayList<Character> sFlags = new ArrayList<Character>(
                Arrays.asList('.', '?', '!')
        );

        ArrayList<Character> cFlags = new ArrayList<Character>(
                Arrays.asList(':', '-', ';',',')
        );


        int sentence = 0;
        String word = "";

        //initial sentence
        newSentence(list);

        for(int i=0; i<text.length(); i++){
            if(sFlags.contains(text.charAt(i))) {
                if (!word.equals(""))
                    addWord(list, word, sentence);
                if(!list.get(sentence).isEmpty()){
                    newSentence(list);
                    sentence++;
                }
                word = "";
            } else if (cFlags.contains(text.charAt(i)) || text.charAt(i) == ' ') {
                if (!word.equals("")) {
                    addWord(list, word, sentence);
                    word = "";
                }
            }else
                word += text.charAt(i);
        }

        if(list.get(sentence).isEmpty())
            list.remove(sentence);

        return list;
    }

    public static ArrayList<ArrayList<String>> get_sentence_lists_from_files(String[] filenames){

        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();

        for(String f: filenames){

            String filecontent = "";
            temp.clear();
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) {
                    filecontent += line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            temp = getSentenceLists(filecontent);
            list.addAll(temp);

        }



        return list;
    }

    public static HashMap<String, HashMap<String, Integer>> build_semantic_descriptors(ArrayList<ArrayList<String>> list){

        HashMap<String, HashMap<String, Integer>> hash_map = new HashMap<String, HashMap<String, Integer>>();

        for(ArrayList<String> sentence : list)
            for(String word : sentence) {
                if(!hash_map.containsKey(word)){
                    newHash(hash_map, word);
                    for (String word2 : sentence)
                        if (!word.equals(word2))
                            hash_map.get(word).put(word2, 1);
                }else {
                    for (String word2 : sentence)
                        if (!word.equals(word2)) {
                            addToSubhash(hash_map, word, word2);
                        }
                }
            }



        return hash_map;
    }



}
