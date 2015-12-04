package barba.joel.eng_test;

/**
 * Created by barba on 4/12/15.
 */
public class C_Word_Ask {

    public int id_word;
    public String word;
    public String kind;
    public String translation;

    public int correct_pos_answer;
    public String[] answers = new String[9];;

    public C_Word_Ask() {
        id_word = 0;
        word = "";
        kind = "";
        translation = "";
        correct_pos_answer = 0;
        for (int t = 0; t < 9; t++) answers[t] = "";

    }

}
