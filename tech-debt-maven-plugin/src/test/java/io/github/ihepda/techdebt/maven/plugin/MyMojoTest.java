package io.github.ihepda.techdebt.maven.plugin;


import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.WithoutMojo;
import org.junit.Test;

public class MyMojoTest extends AbstractMojoTestCase
{

    /**
     * @throws Exception if any
     */
    @Test
    public void testSomething()
            throws Exception
    {
        File pom = new File( "target/test-classes/project-to-test/pom.xml" );
        assertNotNull( pom );
        assertTrue( pom.exists() );

        MyMojo mojo = (MyMojo) lookupMojo( "touch", pom );
        assertNotNull( mojo );
        
        mojo.execute();
    }

    /** Do not need the MojoRule. */
    @WithoutMojo
    @Test
    public void testSomethingWhichDoesNotNeedTheMojoAndProbablyShouldBeExtractedIntoANewClassOfItsOwn()
    {
        assertTrue( true );
    }

}

