/*
 * ************************************************
 *            -= Sebastian Schiefermayr =-       
 *                     4AHITM   
 *  > TheMain
 *  > 15:04:25
 * 
 *  E-Mail: basti@bastiarts.com 
 *  Web: https://bastiarts.com
 *  Github: https://github.com/BastiArts
 * ************************************************
 */
package at.htl.ldaptest;

/**
 *
 * @author Basti
 */
/*
 * ************************************************
 *            -= Sebastian Schiefermayr =-       
 *                     4AHITM   
 *  > TheMain
 *  > 11:11:18
 * 
 *  E-Mail: basti@bastiarts.com 
 *  Web: https://bastiarts.com
 *  Github: https://github.com/BastiArts
 * ************************************************
 */

import com.sun.jndi.ldap.LdapCtxFactory;
import java.util.Hashtable;
import java.util.Scanner;
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
public class TheMain {

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

    Scanner sc = new Scanner(System.in);

    System.out.print("User: ");
    String username = sc.next();
    
    System.out.print("Password: ");
    String password = sc.next();

    System.out.println("Authenticating " + username + "@" + domainName + " through " + serverName);

    Hashtable props = new Hashtable();
    /**
     * @var principalName Authentifizierungsformat
     * [benutzer@edu.htl-leonding.ac.at]
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
      System.out.println(result.getAttributes().get("distinguishedname"));
      //Schüler: distinguishedName: CN=it150178,OU=4AHITM,OU=IT,OU=Students,OU=HTL,DC=EDU,DC=HTL-LEONDING,DC=AC,DC=AT
      //Lehrer:  distinguishedName: CN=m.widmann,OU=Teachers,OU=HTL,DC=EDU,DC=HTL-LEONDING,DC=AC,DC=AT
        String role = result.getAttributes().get("distinguishedname").toString().split(",")[3].split("=")[1];
        if(role.equalsIgnoreCase("Students")){
            System.out.println("Deschboad Schüla");
        }
      
      context.close();
    }
    catch (AuthenticationException a) {
      System.out.println("Authentication failed: " + a);
      System.exit(1);
    }
    catch (NamingException e) {
      System.out.println("Failed to bind to LDAP / get account information: " + e);
      System.exit(1);
    }
  }
}

