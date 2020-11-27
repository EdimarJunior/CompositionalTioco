package br.edu.ufcg.symbolrt.compiler.synthesis;

import br.edu.ufcg.symbolrt.base.TIOSTS;
import br.edu.ufcg.symbolrt.compiler.facade.Compiler;

public class TriangleTest extends GenericTest {

	@Override
	protected TIOSTS setupProcessedModel() {
		Compiler.compile("./examples/Triangle.srt", "Triangle");
		return Compiler.getSpecification("Triangle");
	}

	@Override
	protected TIOSTS setupModel() {
		return UtilTest.createTriangleSPEC();
	}

}
