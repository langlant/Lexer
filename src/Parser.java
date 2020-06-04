import java.io.IOException; 

public class Parser {
    private String errorMessage = "";
    Lexer lexer = new Lexer("C:\\Users\\Langley\\Desktop\\CIS_675\\digraph.txt");
    
    public String dot_parse() {
		graph_parse();
		if(errorMessage == "") {
			return "Text has no errors!";
		} else {
    	System.out.println(errorMessage);
    	return errorMessage;
		}
    }
    
    public void graph_parse() {
    	if(lexer.currentToken() == null) {
    		if(lexer.currentToken() == TokenType.DIGRAPH || lexer.currentToken() == TokenType.GRAPH){
    			lexer.next();
    			if(lexer.currentToken() == TokenType.ID || lexer.currentToken() == TokenType.MAIN) {
    				lexer.next();
    				if(lexer.currentToken() == TokenType.LBRACE) {
    					lexer.next();
    					if(stmnt_list()) {
    						lexer.next();
    						if(!(lexer.currentToken() == TokenType.RBRACE)) {
    							lexer.next();
    					} else if( lexer.currentToken() == TokenType.RBRACE) {
    						return;
    					}
    				} else {
    					errorMessage = "Graph: Must include statement list";
    				}
    			} else {
    				errorMessage = "Graph: ID must be followed by Left Brace";
    			}
    		} else {
    			errorMessage = "Graph: Digraph must include ID";
    		}
    	} else {
    		errorMessage = "Graph: No Digraph";
    	}
    }
 }
    
    public Boolean stmnt_list() {
    	if(stmnt()) {
    		lexer.next();
    		if (lexer.currentToken() == TokenType.SEMI) {
    			while(lexer.currentToken() == TokenType.SEMI) {
    				lexer.next();
    				if(stmnt()) {
    					lexer.next();
    				}
    			}
    		}
    	} else {
    		errorMessage = "Statement List: No statement list";
    		return false;
    	}
    	return true;
    }
    
    public Boolean stmnt() {
    	if(node_stmnt() || edge_stmnt() || attr_stmnt() || subgraph()) {
    		lexer.next();
    		
    	} else if((lexer.currentToken() == TokenType.ID || lexer.currentToken() == TokenType.MAIN)) {
    		lexer.next();
    		if(lexer.currentToken() == TokenType.EQUAL) {
    			lexer.next();
    			if(lexer.currentToken() == TokenType.ID || lexer.currentToken() == TokenType.MAIN) {
    				lexer.next();
    			}
    		}
    	} else {
    		errorMessage = "Statement: No Statement";
    		return false;

    	}
    	return true;
    }
    
    public Boolean attr_stmnt() {
    	if(lexer.currentToken() == TokenType.GRAPH || lexer.currentToken() == TokenType.NODE || lexer.currentToken() == TokenType.EDGE) {
    		lexer.next();
    		if(attr_list()) {
    			lexer.next();
    	} else {
    		errorMessage = "Attribute Statement: Not a proper attribute statement";
    		return false;
    			}
    		}
    	return true;
    }
    
    public Boolean attr_list() {
    	if(lexer.currentToken() == TokenType.LBRACKET) {
    		lexer.next();
    		if(a_list()) {
    			while(lexer.currentToken() == TokenType.RBRACKET) {
    				lexer.next();
    				if(lexer.currentToken() == TokenType.LBRACKET) {
    					lexer.next();
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
    	if(lexer.currentToken() == TokenType.ID) {
    		lexer.next();
    		if(lexer.currentToken() == TokenType.EQUAL) {
    			lexer.next();
    			if(lexer.currentToken() == TokenType.ID) {
    				lexer.next();
    				if(lexer.currentToken() == TokenType.SEMI || lexer.currentToken() == TokenType.COMMA) {
    					lexer.next();
    					while(lexer.currentToken() == TokenType.SEMI || lexer.currentToken() == TokenType.COMMA) {
        					if(lexer.currentToken() == TokenType.ID) {
        						lexer.next();
        						if(lexer.currentToken() == TokenType.EQUAL) {
        							lexer.next();
        							if(lexer.currentToken() == TokenType.ID || lexer.currentToken() == TokenType.MAIN) {
        								lexer.next();
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
    		lexer.next();
    		if(edge_RHS()) {
    			lexer.next();
    			if(attr_list()) {
    				lexer.next();
    			}
    		} else {
    			errorMessage = "Edge Statement: Must have edgeRHS";
    			return false;
    		}
    	}
    	return true;
    }
    
    public Boolean edge_RHS() {
    	if(lexer.currentToken() == TokenType.EDGEOP || lexer.currentToken() == TokenType.ARROW) {
    		lexer.next();
    		if(node_id()) {
    			lexer.next();
    			while(lexer.currentToken() == TokenType.EDGEOP || lexer.currentToken() == TokenType.ARROW) {
    				lexer.next();
    				if(node_id() || subgraph()) {
    					lexer.next();
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
    		lexer.next();
    		if(attr_list()) {
    			lexer.next();
    		}
    	} else {
    		errorMessage = "Node Statment: Must begin with node id";
    		return false;
    	}
    	return true;
    }
    
    public Boolean node_id() {
    	if(lexer.currentToken() ==  TokenType.ID) {
    		lexer.next();
    		if(port()) {
    			lexer.next();
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
    	if(lexer.currentToken() == TokenType.COLON) {
    		lexer.next();
    		if(lexer.currentToken() == TokenType.ID || compass_pt()) {
    			lexer.next();
    			if(compass_pt()) {
    				lexer.next();
    			}
    		} else {
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
    	if(lexer.currentToken() == TokenType.SUBGRAPH) {
    		lexer.next();
    		if(lexer.currentToken() == TokenType.ID || lexer.currentToken() == TokenType.MAIN) {
    			lexer.next();
    			if(lexer.currentToken() == TokenType.LBRACE) {
    				lexer.next();
    				if(stmnt_list()) {
    					lexer.next();
    					if(lexer.currentToken() == TokenType.RBRACE) {
    						lexer.next();
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
    	if(lexer.currentToken() == TokenType.N) {
    		lexer.next();
    	} else if(lexer.currentToken() == TokenType.NE) {
    		lexer.next();
    	}else if(lexer.currentToken() == TokenType.E) {
    		lexer.next();
    	} else if(lexer.currentToken() == TokenType.SE) {
    		lexer.next();
    	} else if(lexer.currentToken() == TokenType.S) {
    		lexer.next();
    	} else if(lexer.currentToken() == TokenType.SW) {
    		lexer.next();
    	} else if(lexer.currentToken() == TokenType.W) {
    		lexer.next();
    	} else if(lexer.currentToken() == TokenType.NW) {
    		lexer.next();
    	} else if(lexer.currentToken() == TokenType.C) {
    		lexer.next();
    	} else {
    		errorMessage = "Compass: Must declare a direction";
    		return false;
    	}
    	return true;
    }
    
}
