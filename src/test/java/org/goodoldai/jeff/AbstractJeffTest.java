/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.goodoldai.jeff;

import org.junit.BeforeClass;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

public class AbstractJeffTest {

    private static File rootFolder;

    @BeforeClass
    public static void init() {
        try {
            String root = AbstractJeffTest.class.getResource( "." ).toURI().getPath();
            String path = AbstractJeffTest.class.getPackage().getName().replaceAll( "\\.", "/" );
            root = root.substring( 0, root.indexOf( path ) );
            rootFolder = new File( root );
        } catch ( URISyntaxException e ) {
            e.printStackTrace();
        }
    }

    protected static File newFile( String fileName ) {
        return new File( AbstractJeffTest.rootFolder.getAbsolutePath() + File.separator + fileName);
    }


    public void setUp() throws Exception {
        ResourceBundle.clearCache();
    }
}
