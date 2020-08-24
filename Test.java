import java.io.Console;
import java.util.Arrays;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Test {
	  public static void main(String[] args){
				String str = "";
				String prefix;
				Node root = null;
				while(true) {
					int choice;
					Scanner in = new Scanner(System.in);
					choice = in.nextInt();


					Autocomplete autocomplete = new Autocomplete();
					switch(choice) {
							case 1: System.out.println("Load From Binary File"); break;
							case 2: System.out.println("Save To Binary File"); break;
							case 3: 	System.out.println("Populate Dictionary");
												root = autocomplete.read();
												break;
							case 4: 	System.out.println("Suggest Word");
												if(root != null) {
													prefix = in.next();
													autocomplete.suggestWord(root, prefix);
												}
												else {
													System.out.println("Dictionary Not Full");
												}
												break;
							case 5: 	System.out.println("Print");
												if(root != null) {
													autocomplete.printDict(root, str);
												}
												else {
													System.out.println("Dictionary Not Full");
												}
												break;
							case 6: 	System.out.println("Quit");
												return;
					}
				}
			}
}
