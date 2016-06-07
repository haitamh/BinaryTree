package exercise;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

public class BinaryTree
{
	private static final String TREE_OPENER = "{";
	private static final String TREE_CLOSER = "}";
	private static final String CHILD_SEPERATOR = ",";
	private static final String TREE_TEMPLATE = TREE_OPENER + "%s" + CHILD_SEPERATOR + "%s" + CHILD_SEPERATOR + "%s" + TREE_CLOSER;
	private static final String TREE_DELIM = TREE_OPENER + TREE_CLOSER + CHILD_SEPERATOR;
	private static final String NULL_CHILD = "null";
	
	private String name;
    private BinaryTree left;
    private BinaryTree right;

    public BinaryTree(String name, BinaryTree left, BinaryTree right) {
        if(name == null) throw new NullPointerException("Node name mast not be null");
        this.name = name;
        this.left = left;
        this.right = right;
    }

    // For unit test only
    void setLeft(BinaryTree left) {
        this.left = left;
    }

    // For unit test only
    void setRight(BinaryTree right) {
        this.right = right;
    }

    public static boolean EQ(BinaryTree tree1, BinaryTree tree2) {
        if(tree1 == null && tree2 == null ) return true;
        if(tree1 == null || tree2 == null ) return false;
        if(!tree1.getName().equals(tree2.getName())) return false;
        return EQ(tree1.getLeft(), tree2.getLeft()) && EQ(tree1.getRight(), tree2.getRight());
    }



    public String getName() {
        return name;
    }

    public BinaryTree getLeft() {
        return left;
    }

    public BinaryTree getRight() {
        return right;
    }

	public String serializeTree() {
		Set<BinaryTree> treeNodes = new HashSet<BinaryTree>();
		return serializeTreeHelper(treeNodes);
	}

	private String serializeTreeHelper(Set<BinaryTree> treeNodes) {
		if(treeNodes.contains(this)) {
			throw new BinaryTreeSerializationException();
		}
		treeNodes.add(this);
		return String.format(TREE_TEMPLATE,
				             this.name,
				             this.left != null ? this.left.serializeTreeHelper(treeNodes) : NULL_CHILD,
				             this.right != null ? this.right.serializeTreeHelper(treeNodes) : NULL_CHILD);
	}

    public static BinaryTree deserializeTree(String str) {
    	if(!isValidBinaryTreeString(str)) {
    		return null;
    	}
    	Stack<Object> s = new Stack<Object>();
    	StringTokenizer tokenizer = new StringTokenizer(str, TREE_DELIM, true);
		while (tokenizer.hasMoreElements()) {
			Object curr = tokenizer.nextElement();
			if(TREE_OPENER.equals(curr) || CHILD_SEPERATOR.equals(curr)) {
				continue;
			}
			else if(TREE_CLOSER.equals(curr)) {
				if(s.size() < 3) {
					return null;
				}
				Object right = s.pop();
				Object left = s.pop();
				Object root = s.pop();
				BinaryTree currTree = new BinaryTree((String)root, (BinaryTree)left, (BinaryTree)right);
				if(s.isEmpty()) {
					return currTree;
				}
				else {
					s.push(currTree);
				}
			}
			else if(NULL_CHILD.equals(curr)) {
				s.push(null);
			}
			else {
				s.push(curr);
			}
		}
		return null;
    }
	
	private static boolean isValidBinaryTreeString(String str) {
		Stack<Integer> treeChildsCounters = new Stack<Integer>();
    	StringTokenizer tokenizer = new StringTokenizer(str, TREE_DELIM, true);
    	boolean lastIsComma = false;
		boolean lastIsOpener = false;
    	while (tokenizer.hasMoreElements()) {
			String curr = (String) tokenizer.nextElement();
			if(TREE_OPENER.equals(curr)) {
				if(lastIsOpener) {
					return false;
				}
				treeChildsCounters.push(0);
				lastIsOpener = true;
			}
			else if(TREE_CLOSER.equals(curr)) {
				if(lastIsComma || lastIsOpener || treeChildsCounters.empty()) {
					return false;
				}
				
				Integer currTreeChilds = treeChildsCounters.pop();
				if(currTreeChilds != 2) {
					return false;
				}
			}
			else if (CHILD_SEPERATOR.equals(curr)) {
				if(lastIsComma || lastIsOpener || treeChildsCounters.isEmpty()) {
					return false;
				}
				
				Integer currTreeChilds = treeChildsCounters.pop();
				if(currTreeChilds > 1) {
					return false;
				}
				
				treeChildsCounters.push(currTreeChilds + 1);
				lastIsComma = true;
			}
			else {
				if(lastIsOpener && NULL_CHILD.equals(curr)) {
					return false;
				}
				lastIsComma = false;
				lastIsOpener = false;
			}
		}
		
		return treeChildsCounters.isEmpty();
	}
}
