package com.example.demo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.web.bind.annotation.PathVariable;


import com.example.demo.model.data;
import com.example.demo.model.historydata;


public class SensorDAO {
	private String jdbcURL="jdbc:mysql://localhost:3306/jdbc_demo";
	private String jdbcUsername="root";
	private String jdbcPass="dainam2002";
    String brokerUrl = "tcp://172.20.10.7:1883";
    private static final String selectAllDataChange="SELECT * FROM test.control_history ORDER BY idChange DESC;";
    private static final String addCtrlOrder="INSERT INTO `test`.`control_history` (`device`, `data_change`, `time_change`) VALUES (?, ?, ?);";
	private static final String selectAllRecentData="SELECT * FROM test.sensor_data ORDER BY idData DESC LIMIT 7;";
	private static final String selectAllSensorData="SELECT * FROM test.sensor_data ORDER BY idData DESC;";
	public SensorDAO(){
	}
	
	protected Connection getConnection() {
		Connection connection = null;
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection = DriverManager.getConnection(jdbcURL,jdbcUsername,jdbcPass);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return connection;
	}
	//view all sensor data
	public List<data> selectAllSensorData(){
		List<data> datas = new ArrayList<>();
		try (Connection connection = getConnection();
				PreparedStatement ps = connection.prepareStatement(selectAllSensorData);)
		{
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				datas.add(new data(rs.getDouble("temperature"),rs.getDouble("humidity"),rs.getDouble("dust"),rs.getDouble("light"),rs.getString("time")));
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return datas;
	}
	
	//view all recent data
	public List<data> selectAllRecentData(){
		List<data> datas = new ArrayList<>();
		try (Connection connection = getConnection();
				PreparedStatement ps = connection.prepareStatement(selectAllRecentData);)
		{
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				datas.add(new data(rs.getDouble("temperature"),rs.getDouble("humidity"),rs.getDouble("dust"),rs.getDouble("light"),rs.getString("time")));
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return datas;
	}
	
	public List<historydata> selectAllHistoryData(){
		List<historydata> historyDatas = new ArrayList<>();
		try (Connection connection = getConnection();
				PreparedStatement ps = connection.prepareStatement(selectAllDataChange);)
		{
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String device = rs.getString("device");
				int data_change = rs.getInt("data_change");
				String time_change = rs.getString("time_change");
				historyDatas.add(new historydata(device,data_change,time_change));
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return historyDatas;
	}
	
	//publish dữ liệu
	public boolean publishMessage(int device) {
		//device sẽ gồm 10, 11,20,21 với số trước là thiết bị và số sau thể hiện bật tắt
		String topic = (device/10 == 1) ? "sensor/Light_buld" : ((device/10 == 2) ? "sensor/Air_conditioner" : "sensor/Fan");
		String payload =""+ device%10;
		// Lấy thời gian thực hiện tại
        LocalDateTime currentTime = LocalDateTime.now();
        // Định dạng thời gian thành chuỗi
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
		try (Connection connection = getConnection();
		         PreparedStatement ps = connection.prepareStatement(addCtrlOrder)) {
		        ps.setString(1, (device/10 == 1) ? "Light_buld" : ((device/10 == 2) ? "Air_conditioner" : "Fan"));
		        ps.setInt(2, device%10);
		        ps.setString(3, formattedTime);
		        ps.executeUpdate();
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
        try {
        	
            MqttClient mqttClient = new MqttClient(brokerUrl, MqttClient.generateClientId(), new MemoryPersistence());
            mqttClient.connect();
            MqttMessage message = new MqttMessage(payload.getBytes());
            mqttClient.publish(topic, message);
            mqttClient.disconnect();
            return true;
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return false;
    }
}
