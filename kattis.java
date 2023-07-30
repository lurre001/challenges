import java.util.Scanner;
// Author Lukas Eriksson
// Solution to kattis problem hangman
public class kattis{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
    
        String word = sc.nextLine();
        String guess = sc.nextLine();
        int wordLength = word.length();
        int countCorrect = 0;
        int countWrong = 0;
        for(int i = 0; i < guess.length(); i++){
            //check if word contains the letter, then remove the letter and check contains again until false, otherwise increment countwrong
            if(word.contains(guess.substring(i, i+1))){
                // repeat this step until word does not contain the letter
                while(word.contains(guess.substring(i, i+1))){
                    countCorrect++;
                    word = word.replaceFirst(guess.substring(i, i+1), "");
                }
            }else{
                countWrong++;
            }
            if(countWrong == 10){
                System.out.print("LOSE");
                break;
            }
            if(countCorrect == wordLength){
                System.out.print("WIN");
                break;
            }
        }

        sc.close();
    }
}
