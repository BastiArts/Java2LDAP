/*
 * ************************************************
 *            -= Sebastian Schiefermayr =-       
 *                     4AHITM   
 *  > EVSBridge
 *  > 08:29:01
 * 
 *  E-Mail: basti@bastiarts.com 
 *  Web: https://bastiarts.com
 *  Github: https://github.com/BastiArts
 * ************************************************
 */
package evs.ldapconnection;

import com.sun.jndi.ldap.LdapCtxFactory;
import java.util.Hashtable;
import java.util.Scanner;
import javax.naming.Context;
import javax.naming.AuthenticationException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;

/**
 *
 * @author Basti
 */
public class EVSBridge {

    final String prefix = EVSColorizer.yellow() + "EVSBridge: " + EVSColorizer.reset();
    final String domainName = "edu.htl-leonding.ac.at";
    final String serverName = "addc01.edu.htl-leonding.ac.at";
    final String baseDN = "ou=HTL,dc=edu,dc=htl-leonding,dc=ac,dc=at";
    final int port = 636;
          String username;

    Hashtable props = new Hashtable();
    SearchControls controls = new SearchControls();
    DirContext context;
    SearchResult result;

    private static EVSBridge instance = null;

    private EVSBridge() {
        System.setProperty("javax.net.ssl.trustStore", "D:\\Daten\\Schule\\Zertifikate\\Keystore\\keystore.bin");
    }

    /**
     * @param username Benutzer
     *
     * @param password Passwort
     *
     * Das Resultat wird global gespeichert und kann dank des Singletons nur der
     * User verwendet werden. -> Threadsafe
     */
    public boolean login(final String username, final String password) {

        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(Context.PROVIDER_URL, "ldaps://" + serverName + ":" + port);
        props.put(Context.SECURITY_PRINCIPAL, username + "@" + domainName);
        props.put(Context.SECURITY_CREDENTIALS, password);

        System.out.println(prefix + "Authenticating " + username + "@" + domainName + " through " + serverName);

        try {
             context = new InitialDirContext(props);

            /**
             * LDAP-Tree durchsuchen
             */
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SUBTREE_SCOPE);

            /**
             * @Method context.search
             * @param (BasisURL, Filter, Controls)
             */
            NamingEnumeration<SearchResult> renum = context.search("OU=HTL,DC=EDU,DC=HTL-LEONDING,DC=AC,DC=AT",
                    "cn=" + username, controls);

            // ...wenn User nicht gefunden, dann...
            if (!renum.hasMore()) {
                System.out.println(prefix + "Cannot locate user information for " + username);
                context.close();
                return false;
            } else {
                System.out.println(prefix + "Successfully authenticated!");
                this.username = username;
                result = renum.next();
                context.close();
                return true;
            }

        } catch (AuthenticationException a) {
            System.out.println(prefix + "Authentication failed: " + a);
            return false;
        } catch (NamingException e) {
            System.out.println(prefix + "Failed to bind to LDAP / get account information: " + e);
            return false;
        }

    }

    public String getStudentName() {
        String name = result.getAttributes().get("displayname").toString().split(": ")[1];
        return name != null ? name : "";
    }

    public String getRole() {
        String role = result.getAttributes().get("distinguishedname").toString().split(",")[3].split("=")[1];
        return role != null ? role : "";
    }

    public String getSchoolClass() {
        if (getRole().equalsIgnoreCase("Students")) {
            return result.getAttributes().get("distinguishedname").toString().split(",")[1].split("=")[1];
        } else {
            return "";
        }
    }
    public String getStudentId(){
        return this.username != null ? username : "";
    }
   
    

    public static EVSBridge getInstance() {
        if (instance == null) {
            instance = new EVSBridge();
        }
        return instance;
    }
}
