package com.github.cooker.maven;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

public class BuildMojoTest extends AbstractMojoTestCase {

    public void testExecute() throws Exception {
        File pom = getTestFile( "src/test/resources/project-to-build/pom.xml" );
        assertNotNull( pom );
        assertTrue( pom.exists() );
        BuildMojo myMojo = (BuildMojo) lookupMojo( "build", pom );
        assertNotNull( myMojo );
        myMojo.execute();
    }
}