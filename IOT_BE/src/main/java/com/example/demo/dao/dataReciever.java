package com.example.demo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.example.demo.model.data;
import com.google.gson.Gson;

public class dataReciever {
	//Lớp SensorDataReceiver

 public dataReciever() {
     String brokerUrl = "tcp://172.20.10.7:1883";
     String topic = "sensor/data";
 	 String jdbcURL="jdbc:mysql://localhost:3306/jdbc_demo";
 	 String jdbcUsername="root";
 	 String jdbcPass="dainam2002";
     MemoryPersistence persistence = new MemoryPersistence();

     try {
         MqttClient mqttClient = new MqttClient(brokerUrl, "SensorDataReceiver", persistence);

         MqttConnectOptions connOpts = new MqttConnectOptions();
         connOpts.setCleanSession(true);
         mqttClient.connect(connOpts);

         mqttClient.subscribe(topic, (topic1, message) -> {
        	    String payload = new String(message.getPayload());

        	    try {
        	        // Phân tích dữ liệu JSON
        	        Gson gson = new Gson();
        	        data data = gson.fromJson(payload, data.class);
        	        LocalDateTime currentTime = LocalDateTime.now();
        	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        	        System.out.println(data);
        	        String formattedTime = currentTime.format(formatter);
        	        // Tiến hành lưu data vào cơ sở dữ liệu
        	        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPass)) {
        	            String insertQuery = "INSERT INTO `test`.`sensor_data` (`temperature`, `humidity`,`dust`,`light`,`time`) VALUES (?,?,?,?,?);";
        	            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
        	            preparedStatement.setDouble(1, data.getTemperature());
        	            preparedStatement.setDouble(2, data.getHumidity());
        	            preparedStatement.setDouble(3, data.getDust());
        	            preparedStatement.setDouble(4, data.getLight());
        	            preparedStatement.setString(5, formattedTime);
        	            preparedStatement.executeUpdate();
        	            preparedStatement.close();
        	        } catch (SQLException e) {
        	            e.printStackTrace();
        	        }
        	    } catch (Exception e) {
        	        e.printStackTrace();
        	    }
        	});
     } catch (MqttException e) {
         e.printStackTrace();
     }
 }




}
