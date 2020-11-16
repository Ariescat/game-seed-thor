package dnl.utils.text.tree;

import org.junit.Test;

import javax.swing.tree.TreeModel;
import java.io.File;

/**
 *
 * @author Daniel Orr
 *
 */
public class TextTreeTest {

	@Test
	public void printTree() {
		TreeModel tm = new FsModel(new File("./target"));
		TextTree tt = new TextTree(tm);
		System.out.println(tt);
	}

}
