package org.otto;

import winstone.Launcher;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author damien bourdette <a href="https://github.com/dbourdette">dbourdette on github</a>
 * @version \$Revision$
 */
public class StartServer {
    public static void main(String[] args) throws Exception {
        Map params = new HashMap();
        params.put("useJasper", "true");
        params.put("warfile", new File("C:/Users/devarf/Documents/workspace-sts/otto/target/otto-0.1.0-SNAPSHOT.war").getAbsolutePath());
        Launcher.initLogger(params);
        Launcher winstone = new Launcher(params);

        /*BasicConfigurator.configure();

    Server server = new Server(8080);

    WebAppContext webapp = new WebAppContext();
    webapp.setWar(new File("C:/Users/devarf/Documents/workspace-sts/otto/target/otto-0.1.0-SNAPSHOT.war").getAbsolutePath());

    webapp.setParentLoaderPriority(true);

    server.setHandler(webapp);

    server.start();
    server.join();    */
    }
}
