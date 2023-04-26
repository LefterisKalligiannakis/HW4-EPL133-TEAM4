package hw4;

public class Question {
    private String word, correctAnswer, givenAnswer;
    private String[] choices;

    public Question(String[] question){
        this.word = question[0];

        this.correctAnswer = findCorrect(question);
        this.choices = findChoices(question, correctAnswer);
    }

    private String[] findChoices(String[] question, String correctAnswer){
        int j = 0;
        boolean flag = false;
        String [] choices = new String[3];
        for(int i=1; i<question.length; i++)
            if(!question[i].equals(correctAnswer)) {
                choices[j] = question[i];
                j++;
            }else if (question[i].equals(correctAnswer) && !flag){
                    choices[j] = question[i];
                    flag = true;
                    j++;
            }

        return choices;
    }
    private String findCorrect(String[] question){
        for(String q : question)
            for(String q2 : question)
                if(q != q2 && q.equals(q2))
                    return q;

        return question[1];
    }
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getAnswer() {
        return givenAnswer;
    }


    public void setGivenAnswer(String givenAnswer){
        this.givenAnswer = givenAnswer;
    }

    public String toString(){
        return  word + "\n  (a) " + choices[0] + "\n  (b) " + choices[1] +"\n  (c) " + choices[2] + "\n Given answer: " + givenAnswer + " Correct answer: " + correctAnswer;
    }


    public String getWord() {
        return word;
    }

    public String[] getChoices() {
        return choices;
    }


}
