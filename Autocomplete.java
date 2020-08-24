import java.lang.String;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Autocomplete{

      public Autocomplete() {}

      //add words to dictionary
      public void addWordToDict(String text, Node parent) {
          int first_flag = 0;
          int second_flag = 0;
          for(int i=0; i<text.length(); i++) {
              int pos;

              if(text.charAt(i) < 'a') {
                pos = text.charAt(i) + 32 - 'a';
              }
              else {
                pos = text.charAt(i) - 'a';
              }
              //if the letter exists, don't create again the node
              if(parent.getNode(pos) != null) {
                  parent = parent.getNode(pos);
                  continue;
              }

              else {
                boolean isTerminal;
                WordType type = WordType.NO_CAPS;
                Node[] nodes = new Node[26];

                if (i == text.length() - 1) {
                  isTerminal = true;

                  if(first_flag == 1 && second_flag == 0) {
                    type = WordType.FIRST_CAP;
                  }
                  else if(second_flag == 1) {
                    type = WordType.ALL_CAPS;
                  }
                  else {
                    type = WordType.NO_CAPS;
                  }
                }
                else {
                   isTerminal = false;
                }

                if( Character.isUpperCase(text.charAt(0))) {
                  first_flag=1;
                }
                if(text.length() > 1) {

                  if( Character.isUpperCase(text.charAt(1) ) ) {
                    second_flag=1;
                  }
                }
                Node DNode = new Node(nodes, parent, isTerminal, type, pos);
                parent.setNodeArray(pos, DNode);

                parent = DNode;

              }

          }
      }

      public Node read() {
        //read file
        try (BufferedReader in = new BufferedReader( new FileReader("book1.txt")))  {
          int middle_flag = 0;
          int a;
          int isnumber;
          int countm=0;
          int caps_flag=0;
          boolean word_avoided;
          Node root;
          String l;

          Node[] nodes = new Node[26];

          root = new Node(nodes, null, false, null, 0);

          //read file line by line
          while ((l = in.readLine()) != null) {
            l = l.replaceAll("( )+", " ");

            //xorizei tin grammi se lexeis
            Scanner s = new Scanner(l).useDelimiter(" ");
            while(s.hasNext()) {
              isnumber = 0;
              word_avoided = false;
              middle_flag = 0;
              countm=0;
              String text = s.next();

             if(caps_flag == 1) {
                //convert capital to small letter
                text=text.toLowerCase();
                caps_flag=0;

              }

              Pattern pattern = Pattern.compile("\\p{Punct}");
              Matcher matcher = pattern.matcher(text);

              //check for non-letter characters and avoid them
              while(matcher.find()) {

                // if found in the beginning of the word, erase it
                if(matcher.start() == 0 ) {
                  text = text.substring(1);
                  matcher = pattern.matcher(text);

                }
                //if found in the end and there are before it, erase them as well
                else if( matcher.start() == text.length()-1) {
                  if(( middle_flag==1) ){
                    for(int i=0; i<countm; i++) {
                      text = text.substring(0,text.length() - 1);
                      matcher = pattern.matcher(text);
                    }


                    middle_flag = 0;

                  }

                  text = text.substring(0,text.length() - 1);

                  //caps flag = 1, if the next word contains a capital, convert it to small
                  caps_flag=1;

                  text = text.trim();
                  matcher = pattern.matcher(text);
                }
                else {

                  //if found in the middle, throw the word away
                  middle_flag = 1;
                  countm++;
                  a=matcher.start()+1;

                  String str=text.charAt(a)+ "\0";
                  Matcher matcher2=pattern.matcher(str);
                  if(!matcher2.find()){
                    word_avoided = true;
                    break;
                  }
                }
              }

              ////////////************
              // if the whole world is non-letter
              if(text.length() == 0 ) {
                continue;
              }
              for(int j=0; j<text.length(); j++) {
                if(Character.isDigit(text.charAt(j))) {
                  isnumber = 1;
                }
              }
              if(word_avoided == true || middle_flag == 1 || isnumber == 1) {
                continue;
              }



              ////// Add words to dictionary call
              addWordToDict(text, root);
            }

          }

          return root;

        } catch (IOException e) {
          e.printStackTrace();
          return null;
        }

      }

      public void printDict(Node node, String word) {
        String localWord = word;
        String tmp;

        if(node.getIsTerminal()){
          if(node.getType() == WordType.FIRST_CAP) {
            tmp = word.substring(0,1).toUpperCase();
            word = tmp + word.substring(1);
          }
          if(node.getType() == WordType.ALL_CAPS) {
            word = word.toUpperCase();
          }

          try(  PrintWriter out = new PrintWriter( new FileWriter("__" + "out.txt") )  ){
              out.println(word);
          } catch(Exception e) {
            e.printStackTrace();
          }

        }


        for(int i=0; i<26; i++){
          if(node.getNode(i)!=null) {
            int c = 'a' + (char)i;
            word = word + (char)c;
            printDict(node.getNode(i), word);
            word = localWord;
          }

        }
        return;
      }

      public Node findPrefix(Node node, String word) {
        int pos;
        int found = 0;

        for(int i=0; i<word.length(); i++) {
            //small letters
            if(word.charAt(i) < 'a') {
              pos = word.charAt(i) + 32 - 'a';
            }
            //capital letters
            else {
              pos = word.charAt(i) - 'a';
            }

            //if the prefix exists as a node, continue
            if(node.getNode(pos) != null) {
              node = node.getNode(pos);
              found = 1;
            }
            //otherise, stop
            else {
              System.out.println("Prefix Not Found");
              found = 0;
              break;
            }
        }
        //if the prefix exists in the dictionary, go back to the last node
        if(found == 1) {
          return node;
        }
        // if prefix not found, return null
        return null;

      }

      public void suggestWord(Node node, String word) {
          Node subtree_root = null;

          subtree_root = findPrefix(node, word);
          if(subtree_root == null) {
            return;
          }

          printDict(subtree_root, word);




      }
}
