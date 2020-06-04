import java.util.ArrayList;
import java.util.List;

public class ASTree {
	String node ; 
	List <ASTree> childnodes; // operands
	public ASTree(String node) { this.node = node; }
	public void addChild(ASTree t) {
		if ( childnodes==null ) childnodes = new ArrayList<ASTree>();
		childnodes.add(t);
	}

	public String toString() {

		return node.toString() ; 
	}

	public Boolean isNil() {
		if ( childnodes==null || childnodes.size()==0 )  {
			return true;
		}
		else 
		{
			return false;
		}
	}
	public String toStringTree() {
		StringBuilder buf = new StringBuilder(); 
		if ( childnodes==null || childnodes.size()==0 ) return this.toString(); 
		if ( !isNil() ) { 
			buf.append("("); 
			buf.append(this.toString()); 
			buf.append(' '); }
			buf.append("(");
		for (int i=0; childnodes!=null && i < childnodes.size(); i++) 	{
			
			ASTree t = (ASTree)childnodes.get(i);
			if ( i>0 ) buf.append(' '); 
			buf.append(t.toStringTree());
		}
		if ( !isNil() ) {
			buf.append(")");
			}
			buf.append(")");
	 
		return buf.toString(); 
	}

}