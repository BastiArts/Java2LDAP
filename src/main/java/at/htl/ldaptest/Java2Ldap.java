/*
 * ************************************************
 *            -= Sebastian Schiefermayr =-       
 *                     4AHITM   
 *  > Java2Ldap
 *  > 11:11:18
 * 
 *  E-Mail: basti@bastiarts.com 
 *  Web: https://bastiarts.com
 *  Github: https://github.com/BastiArts
 * ************************************************
 */
package at.htl.ldaptest;

import com.sun.jndi.ldap.LdapCtxFactory;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.AuthenticationException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;

/**
 *
 * @author Basti
 */
public class Java2Ldap {

    public static void main(String[] args) {

        /**
         * @var domainName Authentifizierungsadresse
         * [benutzer@edu.htl-leonding.ac.at]
         *
         * @var serverName LDAP Serveradresse
         *
         * @var username selbsterklärend
         *
         * @var password selbsterklärend
         */
        final String domainName = "edu.htl-leonding.ac.at";
        final String serverName = "addc01.edu.htl-leonding.ac.at";

        final String username = "username";
        final String password = "password";

        System.out.println("Authenticating " + username + "@" + domainName + " through " + serverName);

        Hashtable props = new Hashtable();
        /**
         * @var principalName
         * Authentifizierungsformat [benutzer@edu.htl-leonding.ac.at]
         */
        String principalName = username + "@" + domainName;
        props.put(Context.SECURITY_PRINCIPAL, principalName);
        props.put(Context.SECURITY_CREDENTIALS, password);
        DirContext context;

        try {
            context = LdapCtxFactory.getLdapCtxInstance("ldap://" + serverName + '/', props);
            System.out.println("Authentication succeeded!");

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
                System.out.println("Cannot locate user information for " + username);
                System.exit(1);
            }
            SearchResult result = renum.next();
            System.out.println(result.getAttributes());
            

            context.close();
        } catch (AuthenticationException a) {
            System.out.println("Authentication failed: " + a);
            System.exit(1);
        } catch (NamingException e) {
            System.out.println("Failed to bind to LDAP / get account information: " + e);
            System.exit(1);
        }
    }
}
