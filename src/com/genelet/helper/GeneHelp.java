package com.genelet.helper;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 *
 * @author Peter
 */

public class GeneHelp {
    @Option(name="-force", usage="default false")
    private boolean force;
        
    @Option(name="-root", usage="program root, default .")
    public String root = ".";
    
    @Option(name="-project", usage="project name, default 'myproject'")
    public String proj = "myproject";
    
    @Option(name="-script", usage="script name, default 'myjava'")
    public String scri = "myjava";
    
    @Option(name="-dbuser", usage="db username, mandatory")
    public String dbuser = "";
   
    @Option(name="-dbpass", usage="db password, mandatory")
    public String dbpass = "";
        
    @Option(name="-dbname", usage="db name, mandatory")
    public String dbname = "";
    
    @Argument
    public List<String> tables = new ArrayList<>();
    
    Error select_label(Connection conn, List<Map<String,Object>> lists, String sql, List<String> labels) throws SQLException {
        
        PreparedStatement sth = conn.prepareStatement(sql);
        
        ResultSet rs = sth.executeQuery();
        int columnCount = labels.size();
        while (rs.next()) {
            Map<String,Object> obj = new HashMap<>();
            for (int i=1; i<=columnCount; i++) {
                obj.put(labels.get(i-1), rs.getObject(i));
            }
            lists.add(obj);
        }
        
        return null;
    }
    
    public List<Object> obj(Connection conn, String table) throws SQLException {

        List<Map<String,Object>> lists = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        labels.add("Field");
        labels.add("Type");
        labels.add("Null");
        labels.add("Key");
        labels.add("Default");
        labels.add("Extra");
        Error err = select_label(conn, lists, "DESC "+table, labels);
        if (err != null) { return null; }

        String pk = "";
        String ak = "";
        List<String>   nons = new ArrayList<>();
        List<String>      k = new ArrayList<>();
        List<String>     uk = new ArrayList<>();
        List<String> fields = new ArrayList<>();

        for (Map item : lists) {
            String field = (String) item.get("Field");

            if (item.containsKey("Default") && "CURRENT_TIMESTAMP".equals((String)item.get("Default"))) {
                continue;
            }
            if (item.containsKey("Extra") && "auto_increment".equals((String)item.get("Extra"))) {
                ak = field;
                if ("PRI".equals((String)item.get("Key"))) {
                    pk = field;
                }
                continue;
            }
            Boolean non = false;
            if (item.containsKey("Key")) {
                String key = (String) item.get("Key");
                switch (key) {
                    case "PRI":
                        pk = field;
                        continue;
                    case "UNI":
                        uk.add(field);
                        non = true;
                        break;
                    case "MUL":
                        k.add(field);
                        non = true;
                        break;
                }
            }
            if (non || "No".equals((String)item.get("Null"))) {
                nons.add(field);
            }
            fields.add(field);
        }

        if ("".equals(pk) && uk.size()>0) {
            pk = uk.get(0);
        }

        List<Object> ret = new ArrayList<>();
        ret.add(pk);
        ret.add(ak);
        ret.add(nons);
        ret.add(fields);
        return ret;
    }

    public void write_it(String filename, String content) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		
	String fn = System.getProperty("user.dir")+"/"+filename;
        File f = new File(fn);
        if (force || !f.exists()) {
            FileWriter fw = new FileWriter(f.getAbsoluteFile());
	    BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
	    bw.close();
        }
    }
    
    public void dir_all() {
        List<String> dirs = new ArrayList<>();
        dirs.add(root);
        dirs.add(root+"/src");
        dirs.add(root+"/src/java");
        dirs.add(root+"/src/java/"+proj);
        dirs.add(root+"/web");
        dirs.add(root+"/web/admin");
        dirs.add(root+"/web/WEB-INF");
        dirs.add(root+"/www");
        dirs.add(root+"/www/admin");
        dirs.add(root+"/www/public");
        dirs.add(root+"/www/public/"+tables.get(0));
        
        for (String v : tables) {
            dirs.add(root+"/src/java/"+proj+"/"+v);
            dirs.add(root+"/web/admin/"+v);
            dirs.add(root+"/www/admin/"+v);
        }
        
        for (String dir : dirs) {
            new File(dir).mkdir();
            System.err.printf("Checking %s\n", dir);
        }
    }

    public void run(String[] args) throws FileNotFoundException, IOException, SQLException, CmdLineException {
        CmdLineParser parser = new CmdLineParser(this);

        parser.parseArgument(args);
        if( tables.isEmpty() ) {
            System.err.println("-dbname=\"\" -dbuser=\"\" -dbpass=\"\" [-root -project -script -force] tables ...");
            System.exit(1);
        }
        
        dir_all();
        
        GeneConfig cnf = new GeneConfig(root, proj, scri);
        GeneClass  cls = new GeneClass(proj, scri, tables);
        ViewAG      ag = new ViewAG(proj, scri, tables);
        ViewJsp    jsp = new ViewJsp(proj, scri, tables);
        
        if (chdir(root+"/web/WEB-INF")) {
            write_it(scri+".json", cnf.config(dbuser, dbuser, dbpass));
            write_it("web.xml", cnf.xml());
        }

        if (chdir(root+"/src/java/"+proj)) { 
            write_it(firstUpper(proj)+"Filter.go", cls.filter());
            write_it(firstUpper(proj)+"Model.go", cls.model());
            write_it(firstUpper(proj)+"Servlet.go", cls.servlet());
            write_it(firstUpper(proj)+"Listener.go", cls.listener());
        }
        
        if (chdir(root+"/web/admin")) {
            write_it("login.html", jsp.login());
        }

        if (chdir(root+"/www")) {
            write_it("index.html", ag.index());
            write_it("init.js", ag.init());
            //gh.write_it("genelet.js", ag.ag_geneletjs());
        }
        
        if (chdir(root+"/www/admin")) {
            write_it("header.html", ag.header(ag.bar()));
            write_it("footer.html", "");
            write_it("login.html", ag.login());
        }
        
        if (chdir(root+"/www/public")) {
            write_it("header.html", ag.header(""));
            write_it("footer.html", "");
        }
        
        if (chdir(root+"/www/public/"+ag.tables.get(0))) {
            write_it("startnew.html", ag.rolepublic());
        }
        
            
        //Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbname, dbuser, dbpass);
        
        int i=0;
        for (String t : tables) {
            List<Object> tmp = obj(conn, t);
            String pk = (String) tmp.get(0);
            String ak = (String) tmp.get(1);
            List<String> nons = (List<String>) tmp.get(2);
            List<String> fields = (List<String>) tmp.get(3);

            if (chdir(root+"/src/java/"+proj+"/"+t)) {
                if (i==0) {
                    write_it("Filter.go", cls.obj_filter(t, pk, nons, "\"groups\":{\"public\"}"));
                } else {
                    write_it("Filter.go", cls.obj_filter(t, pk, nons, ""));
                }
                write_it("Model.go", cls.obj_model(t, pk, ak, fields));
            }

            if (chdir(root+"/web/admin/"+t)) {
                write_it("insert.html", jsp.top()+"<h3>Added</h3>"  +jsp.bottom());
                write_it("delete.html", jsp.top()+"<h3>Deleted</h3>"+jsp.bottom());
                write_it("update.html", jsp.top()+"<h3>Updated</h3>"+jsp.bottom());
                write_it("startnew.html",jsp.startnew(t, pk, nons, fields));
                write_it("topics.html",    jsp.topics(t, pk, fields));
                write_it("dashboard.html", jsp.topics(t, pk, fields));
                write_it("edit.html",        jsp.edit(t, pk, fields));
            }
            
            if (chdir(root+"/www/admin/"+t)) {
                write_it("startnew.html", ag.startnew(t, pk, nons, fields));
                write_it("topics.html",     ag.topics(t, pk, fields));
                write_it("dashboard.html",  ag.topics(t, pk, fields));
                write_it("edit.html",         ag.edit(t, pk, fields));
            }
        }
        conn.close();
    } 
    
    static String nice(String name) {
        String[] names = name.split(" ");
        String str = "";
        for (String v : names) {
            str += firstUpper(v) + " ";
        }
        return str.substring(0, str.length()-1);
    }
    
    public static String firstUpper(String v) {
        return v.substring(0, 1).toUpperCase() + v.substring(1);
    }
       
    static Map<String,String> titles(List<String> fields) {
        int n = 0;
        Map<String, String> ts = new HashMap<>();
        for (String name : fields) {
            if (name.length()>n) {
                n = name.length();
            }
        }
        for (String name : fields) {
            int i = n-name.length();
            char[] chars = new char[i];
            Arrays.fill(chars, ' ');        
            ts.put(name, new String(chars) + GeneHelp.nice(name));
        }
        return ts;
    }

    public static boolean chdir(String directory_name) {
        boolean result = false;
        File    directory;

        directory = new File(directory_name).getAbsoluteFile();
        if (directory.exists()) {
            result = (System.setProperty("user.dir", directory.getAbsolutePath()) != null);
        }
        return result;
    }
}
