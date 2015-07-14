/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genelet.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Peter
 */
public class ViewJsp {
    public String proj;
    public String scri;
    public List<String> tables;
    
    public ViewJsp(String p, String s, List<String> t) {
        this.proj = p;
        this.scri = s;
        this.tables = t;
    }
        
    String top0() {
        return "<!doctype html>" +
"\n<html>" +
"\n<head>" +
"\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" +
"\n<title>Administrative Pages</title>" +
"\n</head>" +
"\n<body>" +
"\n<h2>" + GeneHelp.nice(proj).toUpperCase() + "</h2>" +
"\n";
    }

    public String top() {
        return top0() + 
"\n<h4>Welcome <em><%= ARGS.get(\"login\") %></em>! " +
"\nYou are role <em><%= ARGS.get(\"_grole\") %></em>." +
"\n<a href='<%= ARGS.get(\"guri_json\") %>'>JSON View</a>" +
"\n<a href=\"logout\">LOGOUT</a></h4>";
}

    public String bottom() {
        return "</body></html>";
    }
    
    public String login() {
        String str = "<h3>{{ .Other.Errorstr }}</h3>" +
"\n<FORM METHOD=\"POST\" ACTION=\"{{ OTHER.get(\"Login_name\") }}\">" +
"\n<INPUT TYPE=\"HIDDEN\" NAME=\"{{ OTHER.get(\"Go_uri_name\") }}\" VALUE=\"{{ OTHER.get(\"Uri\") }}\" />" +
"\n<pre>" +
"\n   Login: <INPUT TYPE=\"TEXT\" NAME=\"{{ OTHER.get(\"Login\") }}\" />" +
"\nPassword: <INPUT TYPE=\"PASSWORD\" NAME=\"{{ OTHER.get(\"Password\") }}\" />" +
"\n<INPUT TYPE=\"SUBMIT\" VALUE=\" Log In \" />" +
"\n</pre>" +
"\n</FORM>";
    return top0()+str+bottom();
}

    public String startnew(String table, String pk, List<String> nons, List<String> fields) {
        if (nons.isEmpty()) {
            nons.add(pk);
            for (String v : fields) {
                nons.add(v);
            }
        }

        String str = "<h3>Create New</h3>" +
"\n<form method=post action=\"" + table + "\">" +
"\n<input type=hidden name=action value=\"insert\" />" +
"\n<pre>";
        Map<String, String> ts = GeneHelp.titles(nons);
        for (String val : nons) {
            str += ts.get(val) + ": <input type=text name=\"" + val + "\" />";
        }
        str += "</pre>" +
"\n<input type=submit value=\" Submit \" />" +
"\n</form>" +
"\n";
    return top()+str+bottom();
}
    
    public String edit(String table, String pk, List<String> fields) {
        String str = "<h3>Update Record</h3>" +
"\n<form method=post action=\"" + table + "\">" +
"\n<input type=hidden name=action value=\"update\" />" +
"\n<input type=hidden name=" + pk + " value=\"{{ ARGS.get(\"" + pk + "\") }} />" +
"\n<pre>";
        Map<String, String> ts = GeneHelp.titles(fields);
        for (String val : fields) {
            str += ts.get(val) + ": <input type=text name=\"" + val + "\" value=\"{{ " + val + " }}\" />";
        }
        str += "</pre>" +
"\n<input type=submit value=\" Submit \" />" +
"\n</form>" +
"\n";
    return top()+str+bottom();
}

    public String topics(String table, String pk, List<String> fields) {
        List<String> news = new ArrayList<>(fields);
        news.add(pk);
        String str = "<h3>List of Records</h3>" +
"\n<table>" +
"\n<thead>" + 
"\n<tr>";
        for (String val : news) {
            str += "<th>" + GeneHelp.nice(val) + "</th>";
        }
        str += "</tr>" +
"\n</thead>" +
"\n<tbody>" +
"\n<tr>";
        for (String val : news) {
            if (pk.equals(val)) {
                str += "<td><a href=\"" + table + "?action=edit&" + pk + "={{ " + pk + " }}\">{{ " + pk + " }}</a></td>";
            } else {
                str += "<td>{{ " + val + " }}</td>";
            }
        }
        str += "<td><a href=\"" + table + "?action=delete&" + pk + "={{ " + pk + " }}\">DEL</a></td>" +
"\n</tr>" +
"\n</tbody>" +
"\n</table>" + 
"\n<h3><a href=\"" + table + "?action=startnew\">Create New</a></h3>";

        return top()+str+bottom();
    }

}
