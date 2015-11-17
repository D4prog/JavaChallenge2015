package net.aicomp.javachallenge2015;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class SampleTest {
	@Test
	public void test() {
		assertThat(((Integer) 1).toString(), equalTo("1"));
	}
}
