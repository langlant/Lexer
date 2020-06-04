
public class LexerRev {
	   private String errorMessage = "";
	   Lexer lexer = new Lexer("C:\\Users\\Langley\\Desktop\\CIS_675\\digraph.txt");
	    
	   public String dot_parse() {
		   graph_parse();
		   if(errorMessage =="") {
			   return "Text has no errors!";
		   } else {
			   System.out.println(errorMessage);
			   return errorMessage;
		   }
	   }
	   
	   public void graph_parse() {
		   if(lexer.currentToken() == TokenType.STRICT) {
			   lexer.next();
			   if(lexer.currentToken() == TokenType.DIGRAPH) {
				   lexer.next();
				   if(lexer.currentToken() == TokenType.ID) {
					   lexer.next();
					   if(lexer.currentToken() == TokenType.LBRACE) {
						   lexer.next();
						   if(stmnt_list()) {
							   lexer.next();
							   if(!(lexer.currentToken() == TokenType.RBRACE)) {
								   lexer.next();
							   } else if(lexer.currentToken() == TokenType.RBRACE) {
								   return;
							   }
						   }
					   }
				   }
			   } else if(lexer.currentToken() == TokenType.GRAPH) {
				   lexer.next();
				   if(lexer.currentToken() == TokenType.ID) {
					   lexer.next();
					   if(lexer.currentToken() == TokenType.LBRACE) {
						   lexer.next();
						   if(stmnt_list()) {
							   lexer.next();
							   if(!(lexer.currentToken() == TokenType.RBRACE)) {
								   lexer.next();
							   } else if(lexer.currentToken() == TokenType.RBRACE) {
								   return;
							   }
						   } else {
							   errorMessage = "Graph: Must include statement list";
						   }
					   } else {
						   errorMessage ="Graph: ID must be followed by a left brace";
					   }
				   } else {
					   errorMessage = "Graph: must include ID";
				   }
			   } else {
				    errorMessage = "Graph: No Digraph or Graph";
		   }
	   }
	}
	   
	   public Boolean stmnt_list() {
		  if(stmnt()) {
			lexer.next();
			if(lexer.currentToken() == TokenType.SEMI) {
				while(lexer.currentToken() == TokenType.SEMI) {
					lexer.next();
					if(stmnt()) {
						lexer.next();
					}
				}
			}
		} else {
			errorMessage = "Statment List: No statement list";
			return false;
		}
		return true;
	}
	
	public Boolean stmnt() {
		if(node_stmnt()) {
			lexer.next();
		} else if(edge_stmnt()) {
			lexer.next();
		} else if(attr_stmnt()) {
			lexer.next();
		} else if(subgraph()) {
			lexer.next();
		} else if(lexer.currentToken() == TokenType.ID) {
			lexer.next();
			if(lexer.currentToken() == TokenType.EQUAL) {
				lexer.next();
				if(lexer.currentToken() == TokenType.ID) {
					lexer.next();
				}
			}
		}else {
			errorMessage = "Statement: No statement";
			return false;
		}
		return true;
	}
	
	public Boolean attr_stmnt() {
		
	}
}
