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
    private String ast = "";
    
    
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
     		   if(currentToken() == TokenType.STRICT) {
     			   ast = ast + "(GRAPH ";
     			   next();
     			   if(currentToken() == TokenType.DIGRAPH) {
     				   ast = ast + " (DIGRAPH";
     				   next();
     				   if(currentToken() == TokenType.ID) {
     					   ast = ast + " ID";
     					   next();
     					   if(currentToken() == TokenType.LBRACE) {
     						   ast = ast + " LBRACE";
     						   next();
     						   if(stmnt_list()) {
     							   ast = ast + "(Stmnt_list (";
     							   next();
     							   if(!(currentToken() == TokenType.RBRACE)) {
     								   next();
     							   } else if(currentToken() == TokenType.RBRACE) {
     								   ast = ast + "RBRACE)";
     								   return;
     							   }
     						   }
     					   }
     				   }
     			   } else if(currentToken() == TokenType.GRAPH) {
     				   ast = ast + "(GRAPH";
     				   next();
     				   if(currentToken() == TokenType.ID) {
     					   ast = ast + " ID"; 
     					   next();
     					   if(currentToken() == TokenType.LBRACE) {
     						  ast = ast + " LBRACE";
     						   next();
     						   if(stmnt_list()) {
     							  ast = ast + "(Stmnt_list (";
     							   next();
     							   if(!(currentToken() == TokenType.RBRACE)) {
     								   next();
     							   } else if(currentToken() == TokenType.RBRACE) {
     								  ast = ast + "RBRACE)";
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
     			ast = ast + "(stmnt (";
     			  next();
     			if(currentToken() == TokenType.SEMI) {
     				while(currentToken() == TokenType.SEMI) {
     					ast = ast + ") SEMI";
     					next();
     					if(stmnt()) {
     						ast = ast + "(stmnt (";
     						next();
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
     			ast = ast + "(NODE_STMNT(";
     			next();
     		} else if(edge_stmnt()) {
     			ast = ast + "(EDGE_STMNT(";
     			next();
     		} else if(attr_stmnt()) {
     			ast = ast + "(ATTR_STMNT(";
     			next();
     		} else if(subgraph()) {
     			ast = ast + "(SUBGRAPH(";
     			next();
     		} else if(currentToken() == TokenType.ID) {
     			ast = ast + "ID ";
     			next();
     			if(currentToken() == TokenType.EQUAL) {
     				ast = ast + "EQUAL";
     				next();
     				if(currentToken() == TokenType.ID) {
     					ast = ast + " ID";
     					next();
     				}
     			}
     		}else {
     			errorMessage = "Statement: No statement";
     			return false;
     		}
     		return true;
     	}
     	
     	public Boolean attr_stmnt() {
     		if(currentToken() == TokenType.GRAPH) {
     			ast = ast + "(GRAPH ";
     			next();
     			if(attr_list()) {
     				ast = ast + "(ATTR_LIST (";
     				next();
     			}
     		}else if(currentToken() == TokenType.NODE) {
     			ast = ast + "NODE";
     			next();
     			if(attr_list()) {
     				ast = ast + "(ATTR_LIST (";
     				next();
     			}
     		}else if(currentToken() == TokenType.EDGE) {
     			ast = ast + "EDGE";
     			next();
     			if(attr_list()) {
     				ast = ast + "(ATTR_LIST (";
     				next();
     			}
     		}else {
     			errorMessage = "Attribute Statement: Not a proper statement";
     			return false;
     		}
     		return true;
     	}
     	
         public Boolean attr_list() {
         	if(currentToken() == TokenType.LBRACKET) {
         		ast = ast + " [";
         		next();
         		if(a_list()) {
         			ast = ast + "(A_LIST (";
         			while(currentToken() == TokenType.RBRACKET) {
         				ast = ast + "]";
         				next();
         				if(currentToken() == TokenType.LBRACKET) {
         					ast = ast + " [";
         					next();
         				}
         			} 
         		} else {
         			errorMessage = "Attribute List: Must have an a list";
         			return false;
         		}
         	} else {
         		errorMessage = "Attribute List: Must begin with left bracket";
         		return false;
         	}
         	return true;
         }
      
         public Boolean a_list() {
         	if(currentToken() == TokenType.ID) {
         		ast = ast + "ID ";
         		next();
         		if(currentToken() == TokenType.EQUAL) {
         			ast = ast + "EQUAL"
         			next();
         			if(currentToken() == TokenType.ID) {
         				ast = ast + " ID";
         				next();
         				if(currentToken() == TokenType.SEMI || currentToken() == TokenType.COMMA) {
         					ast = ast + "SEMI";
         					next();
         					while(currentToken() == TokenType.SEMI || currentToken() == TokenType.COMMA) {
             					if(currentToken() == TokenType.ID) {
             						ast = ast + "ID "
             						next();
             						if(currentToken() == TokenType.EQUAL) {
             		         			ast = ast + "EQUAL"
             							next();
             							if(currentToken() == TokenType.ID) {
             								ast = ast + " ID";
             								next();
             								}
             							}

         							}
         						}
         				
         					} else {
         						errorMessage = "A List: Must end with a comma or semi-colon";
         						return false;
         					}
         				} else {
         					errorMessage = "A List: Must be followed by ID";
         					return false;
         				}
         			} else {
         				errorMessage = "A List: ID must be follwed by =";
         				return false;
         			}
         		} else {
         			errorMessage = "A List: Must begin with ID";
         			return false;
         		}	
         		return true;

         	}
         
         public Boolean edge_stmnt() {
         	if(node_id() || subgraph()) {
         		next();
         		if(edge_RHS()) {
         			next();
         			if(attr_list()) {
         				next();
         			}
         		} else {
         			errorMessage = "Edge Statement: Must have edgeRHS";
         			return false;
         		}
         	}
         	return true;
         }
         
         public Boolean edge_RHS() {
         	if(currentToken() == TokenType.EDGEOP) {
         		ast = ast +"EDGEOP"
         		next();
         		if(node_id()) {
         			ast = ast +"(NODE_ID";
         			next();
         			while(currentToken() == TokenType.EDGEOP || currentToken() == TokenType.ARROW) {
         				ast = ast + "EDGEOP";
         				next();
         				if(node_id() || subgraph()) {
         					ast = ast + "NODE_ID";
         					next();
         				}
         			}
         		
         		} else {
         			errorMessage = "Edge RHS: Edgepop must be followed by node id or subgraph";
         			return false;
         		}
         	} else {
         		errorMessage = "Edge RHS: Must begin with edgepop";
         		return false;
         	}
         	return true;
         }
         
         public Boolean node_stmnt() {
         	if(node_id()) {
         		ast = ast + "(NODE_ID";
         		next();
         		if(attr_list()) {
         			ast = ast + "(ATTR_LIST";
         			next();
         		}
         	} else {
         		errorMessage = "Node Statment: Must begin with node id";
         		return false;
         	}
         	return true;
         }
         
         public Boolean node_id() {
         	if(currentToken() == TokenType.ID) {
         		ast = ast + "ID";
         		next();
         		if(port()) {
         			ast = ast + "PORT";
         			next();
         		} else {
         			errorMessage = "Node ID: Must be followed by port";
         			return false;
         		}
         	}else {
         		errorMessage = "Node ID: node id must begin with ID";
         		return false;
         	}
         	return true;
         }
         
         public Boolean port() {
         	if(currentToken() == TokenType.COLON) {
         		ast = ast +"COLON"
         		next();
         		if(currentToken() == TokenType.ID) {
         			ast = ast
         			next();
         			if(compass_pt()) {
         				next();
         			}
         		}else if(compass_pt()) {
         			next();
         			if(compass_pt()) {
         				next();
         			}
         		}
         		else {
      
         			errorMessage = "Port: Must follow a colon with an ID or compass_pt";
         			return false;
         		}
         	} else {
         		errorMessage = "Port: Must begin with colon";
         		return false;
         	}
         	return true;
         }
         
         public Boolean subgraph() {
         	if(currentToken() == TokenType.SUBGRAPH) {
         		next();
         		if(currentToken() == TokenType.ID) {
         			next();
         			if(currentToken() == TokenType.LBRACE) {
         				next();
         				if(stmnt_list()) {
         					next();
         					if(currentToken() == TokenType.RBRACE) {
         						next();
         					} else {
         						errorMessage = "Subgraph: Must end with }";
         						return false;
         					}
         				} else {
         					errorMessage = "Subgraph: Must have a statement list";
         					return false;
         				}
         			} else {
         				errorMessage = "Subgraph: ID ust be followed by {";
         				return false;
         			}
         		} else {
         			errorMessage = "Subgraph: should have an ID";
         			return false;
         		}
         	} else {
         		errorMessage = "Subgraph: Must begin with subgraph";
         		return false;
         	}
         	return true;
         }
         
         public Boolean compass_pt() {
         	if(currentToken() == TokenType.N) {
         		next();
         	} else if(currentToken() == TokenType.NE) {
         		next();
         	}else if(currentToken() == TokenType.E) {
         		next();
         	} else if(currentToken() == TokenType.SE) {
         		next();
         	} else if(currentToken() == TokenType.S) {
         		next();
         	} else if(currentToken() == TokenType.SW) {
         		next();
         	} else if(currentToken() == TokenType.W) {
         		next();
         	} else if(currentToken() == TokenType.NW) {
         		next();
         	} else if(currentToken() == TokenType.C) {
         		next();
         	} else {
         		errorMessage = "Compass: Must declare a direction";
         		return false;
         	}
         	return true;
         }

        
        public static void main(String[] args) {

        	
        	Lexer lexer = new Lexer("C:\\Users\\Anthony & Stephanie\\Desktop\\Lexer\\digraph.txt");
            

            
            while (!lexer.isEmpty()) {
            	if((lexer.currentToken() != TokenType.COMMENT) && (lexer.currentToken() != TokenType.WSPACE)) {
            		System.out.printf("%s" + " ", lexer.currentToken());
                lexer.next();
            	} else {
            		lexer.next();
            	}
            }
            
           System.out.printf(lexer.dot_parse());
           System.out.println(ast);
            
        }
}