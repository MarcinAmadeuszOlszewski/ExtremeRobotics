package com.mo.extremerobotics.download;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;

import com.mo.extremerobotics.conf.DbConf;
import com.mo.extremerobotics.db.Queries;

public class DownloaderInitial extends Downloader {
	private String yearLinkBegin="http://www.nbp.pl/kursy/Archiwum/archiwum_tab_a_";
	private String yearLinkEnd=".csv";
	
	public boolean isTableFilled(){
		boolean out=false;
		Connection connection = DbConf.getConnection();

		try (Statement s1 = connection.createStatement();ResultSet rs = s1.executeQuery(Queries.isTableFilled());) {
			if (rs != null) {
				while (rs.next()) {
					out = rs.getInt(1)>365?true:false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DbConf.closeConnection(connection);
		}
		return out;
	}

	public void downloadHistory(){
		int currYear=Year.now().getValue();
		for( int i=currYear;i>2013;i--){
			saveFile(yearLinkBegin+i+yearLinkEnd, DEST_DIR);
		}
	}
	
}
