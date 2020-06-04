/*Anthony Langley
 * CIS 675
 * Lexer
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TokenType {
	
    EQUAL ("\\="),
    LPARAN ("\\("),
    RPARAN ("\\)"), 
    SEMI (";"),
    COLON(":"),
    COMMA (","), 
    LBRACE ("\\{"),
    PERIOD("\\."),
    RBRACE ("\\}"),
    LBRACKET("\\["),
    RBRACKET("\\]"),
    STRING ("\"[^\"]+\""),
    COMMENT("\\/\\*.+\\*\\/"),
    DIGRAPH("digraph"),
    GRAPH("graph"),
    EDGEOP("edgeop"),
    ARROW("->"),
    EDGE("edge"),
    NODE("node"),
    SUBGRAPH("subgraph"),
    MAIN("main"),
    WSPACE(" +"),
    N("/^n$"),
    NE("/ne$"),
    E("/e$"),
    SE("/se$"),
    S("/s$"),
    SW("/sw$"),
    W("/w$"),
    NW("/nw$"),
    C("/c$"),
    ID ("\\w+");
	
	

    private final Pattern pattern;

    TokenType(String regex) {
        pattern = Pattern.compile("^" + regex);
    }

    int endOfMatch(String s) {
        Matcher m = pattern.matcher(s);

        if (m.find()) {
            return m.end();
        }
        return -1;
    }
}
