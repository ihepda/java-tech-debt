package io.github.ihepda.techdebt.maven.plugin;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TechDebtReportMojoTest  extends AbstractMojoTestCase {

	@Test
	public void testReport() throws Exception{
        File pom = new File( "target/test-classes/project-to-test/pom.xml" );
        assertNotNull( pom );
        assertTrue( pom.exists() );

        TechDebtReportMojo mojo = (TechDebtReportMojo) lookupMojo( "report", pom );
        assertNotNull( mojo );
        
        mojo.execute();
	}

}
