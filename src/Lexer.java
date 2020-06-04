/*Anthony Langley
 * CIS 675
 * Lexer
 */

import java.io.IOException; 
import java.nio.file.Files; 
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Lexer {
    private StringBuilder input = new StringBuilder();
    private TokenType token;
    private String lex;
    private boolean empty = false;
    private String errorMessage = "";
    
    
    
    public Lexer(String filePath) {
        try (Stream<String> st = Files.lines(Paths.get(filePath))) {
            st.forEach(input::append);
        } catch (IOException ex) {
            empty = true;
            errorMessage = "file not read: " + filePath;
            return;
        }
        }
        
        public void next() {
            if (empty) {
                return;
            }
            if (input.length() == 0) {
                empty = true;
                return;
            }
            if (nextToken()) {
                return;
            }
            empty = true;  
        }
        private boolean nextToken() {
            for (TokenType t : TokenType.values()) {
                int end = t.endOfMatch(input.toString());

                if (end != -1) {
                    token = t;
                    lex = input.substring(0, end);
                    input.delete(0, end);
                    return true;
                }
            }

            return false;
        }

        public TokenType currentToken() {
        	return token;
        }

        public String currentLexema() {
            return lex;
        }


        public boolean isEmpty() {
            return empty;
        }
        

        
        public static void main(String[] args) {

        	Lexer lexer = new Lexer("C:\\Users\\Langley\\Desktop\\CIS_675\\digraph.txt");
            //String output = "";
            
            
            while (!lexer.isEmpty()) {
            	if((lexer.currentToken() != TokenType.COMMENT) && (lexer.currentToken() != TokenType.WSPACE)) {
            		System.out.printf("%s" + " ", lexer.currentToken());
                lexer.next();
            	} else {
            		lexer.next();
            	}
            }
            
            System.out.printf(lexer.dot_parse());
            
            
        }
}