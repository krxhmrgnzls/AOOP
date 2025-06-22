package payrollsystem;
import java.sql.*;
import java.util.ArrayList;

public class Login extends Credentials {
    
    Login(String id, String password) {
        super(id, password);
    }
    
    @Override
    ArrayList<ArrayList<String>> checkCredentials() {
        ArrayList<ArrayList<String>> userDetails = new ArrayList<>();
        String sql = "SELECT * FROM login_credentials WHERE employee_id = ? AND password = ?";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, getUserID());
            pstmt.setString(2, getUserPassword());
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                ArrayList<String> details = new ArrayList<>();
                details.add(String.valueOf(rs.getInt("employee_id")));
                details.add(rs.getString("name"));
                details.add(rs.getString("password"));
                details.add(rs.getString("role"));
                userDetails.add(details);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return userDetails;
    }
}