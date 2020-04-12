import java.sql.*;


public class ResultSetModel {

	ResultSet aRS;
	boolean empty = true;
	
	public ResultSetModel(){
		aRS = null;
	}
	
	public void setRS(ResultSet pRS) {
		empty = true;
		aRS = pRS;
	}
	
	//search for a given number w/ a given column name
	public String search(String primaryKey, String pKeyValue, String columnName) {
		
		//iterate through ResultSet and find column and value
		try {
			aRS.first();
			int index = aRS.findColumn(columnName);
			while(aRS.next()) {
				//check value at index
				if(aRS.getString(primaryKey)==pKeyValue) {
					return aRS.getString(index);
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public void setValue(String primaryKey, String columnName,String newValue) {
		//set a value
		try {
			int index = aRS.findColumn(columnName);
			while(aRS.next()) {
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isEmpty() {
		//return true if the ResultTable is empty
		//useful for determining whether queries actually returned any values
		return this.empty;
		
	}
	
	//toString()
	public String toString() {
		String result = "";

        try
        {
            ResultSetMetaData metaData = aRS.getMetaData();

            int cols = metaData.getColumnCount();

            for(int i = 1; i <= cols; i++){
                result += metaData.getColumnLabel(i) + ",";
            }
            
            int j = 0;
            result += "\n";

            while (aRS.next()) {
            	
            	empty = false;            	
                for (int i = 1; i <= cols; i++) {
                    result += aRS.getString(i);

                    if(i != cols)
                    {
                        result += ",";
                    }else {
                    	result += "\n";
                    }
                }
            }
        }
        catch(SQLException e)
        {
            ConnectionManager.closeConnection();
            e.printStackTrace();
        }

        return result;
	}
}
