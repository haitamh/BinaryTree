package exercise;

import junit.framework.Assert;
import org.junit.Test;

public class TestBinaryTree
{
    @Test
    public void testBinaryTreeTrivial()
    {
        BinaryTree binaryTree = new BinaryTree("Simple", null, null);
        String str = binaryTree.serializeTree();
        BinaryTree binaryTree2 = BinaryTree.deserializeTree(str);
        Assert.assertTrue(BinaryTree.EQ(binaryTree, binaryTree2));
    }

    @Test
    public void testBinaryTreeNotThatTrivial()
    {
        BinaryTree binaryTree =
                new BinaryTree("Node1",
                    new BinaryTree("Node2",
                        new BinaryTree("Node3", null, null), null),
                    new BinaryTree("Node4",
                        new BinaryTree("Node5",
                            new BinaryTree("Node6", null, null),
                            new BinaryTree("Node7", null, null)
                    ), null));

        String str = binaryTree.serializeTree();
        BinaryTree binaryTree2 = BinaryTree.deserializeTree(str);
        Assert.assertTrue(BinaryTree.EQ(binaryTree, binaryTree2));
    }

    @Test(expected=BinaryTreeSerializationException.class)
    public void testBinaryTree_WrongStructureRaisesException()
    {
        BinaryTree binaryTree = new BinaryTree("Node1", null, null);
        BinaryTree binaryTree2 = new BinaryTree("Node2", new BinaryTree("Node3", null, null), binaryTree);
        binaryTree.setLeft(binaryTree2);
        binaryTree.serializeTree();                
    }


}
