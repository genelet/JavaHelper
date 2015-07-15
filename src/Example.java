import com.genelet.helper.GeneHelp;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.kohsuke.args4j.CmdLineException;

/*
 * Copyright (C) 2015 Peter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 *
 * @author Peter
 */
public class Example {
    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     * @throws java.io.FileNotFoundException
     * @throws org.kohsuke.args4j.CmdLineException
     */
    public static void main(String[] args) throws SQLException, FileNotFoundException, IOException, CmdLineException {
        GeneHelp gh = new GeneHelp();
        gh.parse(args);
        
        String dbname = gh.dbname;
        String dbuser = gh.dbuser;
        String dbpass = gh.dbpass;
        
        //Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbname, dbuser, dbpass);
        gh.run(conn);
        conn.close();
    }
}
