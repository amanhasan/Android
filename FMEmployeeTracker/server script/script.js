/*
employee list    = http://192.168.0.113:1337/emplist
add employee     = http://192.168.0.113:1337/addemp?user_id=XXXX&name=XXXX
admin login      = http://192.168.0.113:1337/login?name=XXXX&pass=XXXX
employee login   = http://192.168.0.113:1337/emplogin?user=XXXX&pass=XXXX
change password  = http://192.168.0.113:1337/updatepassword?pass=XXXX&user=XXXX&name=XXXX
update location  = http://192.168.0.113:1337/updatelocation?user_id=XXXX&lat=XXXX&lng=XXXX
recent location = http://192.168.0.113:1337/recentlocation?user_id=XXXX
*/

var express = require('express');
var mysql = require('mysql');
var app = express();
//-----------------------------------------------------database----------------------------------------------------------------------------------------
var connection = mysql.createConnection({

	host: 'localhost',
	user: 'root',
	password: '',
	database: 'SampleDB'
});

//------------------------------------------------------CONNECTION------------------------------------------------------------------------------------
connection.connect(function(error) {
	if (!!error) {
		console.log('Error');
	}else{
		console.log('Connected');
	}
});
//-----------------------------------------------------EMPLOYEE LIST----------------------------------------------------------------------------------
app.get('/emplist',function(req, resp){
	//sql
	connection.query('SELECT NAME,USER_ID,ID FROM employeedb', function (error, rows, fields){
		//callback
		if (!!error) {
			console.log('Error in query');
		}else{
			resp.json(rows);
		}
	});
})
//------------------------------------------------------ADD EMPLOYEE-----------------------------------------------------------------------------------
app.get('/addemp',function(req, resp){
	var user_id = req.query.user_id;
	var name = req.query.name;
	var result = { 
		success:false,
		username: name
	};

	//INSERT INTO `employeedb` (`ID`, `USER_ID`, `NAME`, `PASSWORD`, `LATITUDE`, `LONGITUDE`) VALUES (NULL, 'a12', 'aman', 'aman', '12.5678', '98.765');
	connection.query("INSERT INTO `employeedb` (`USER_ID`, `NAME`) VALUES ('"+user_id+"', '"+name+"')", function (error, rows, fields){
		//callback
		if (!!error) {
			console.log('Error in query');
		}else{
			
			result.success = true;
		}
		resp.send(JSON.stringify(result));
	});
	//CREATE TABLE `sampledb`.`SUNNY` ( `ID` INT(8) NOT NULL AUTO_INCREMENT , `DATE_TIME` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) , `LATITUDE` FLOAT(16) NOT NULL , `LONGITUDE` FLOAT(16) NOT NULL , PRIMARY KEY (`ID`)) ENGINE = InnoDB;
	connection.query('CREATE TABLE `sampledb`.`'+user_id+'` ( `ID` INT(8) NOT NULL AUTO_INCREMENT , `DATE_TIME` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) , `LATITUDE` FLOAT(16) NOT NULL , `LONGITUDE` FLOAT(16) NOT NULL , PRIMARY KEY (`ID`)) ENGINE = InnoDB;', function (error, rows, fields){
		//callback
		if (!!error) {
			console.log('Error in query');
		}else{
			//resp.json(rows);
		}
	});
})
//-------------------------------------------------------ADMIN LOGIN------------------------------------------------------------------------------------

app.get('/login', function(req, resp) {

	var login = req.query.name;
	var pass = req.query.pass;

	var result = { 
		success:false,
		name: login
	};

	if (login == "admin" && pass == "123")
		result.success = true;

	// add more logins here

	resp.send(JSON.stringify(result));

});

//---------------------------------------------------------EMPLOYEE LOGIN------------------------------------------------------------------------------------------------
app.get('/emplogin',function(req, resp){
	var user = req.query.user;
	var pass = req.query.pass;

	connection.query('SELECT * FROM employeedb WHERE  USER_ID = "'+user+'" and PASSWORD = "'+pass+'"', function (error, rows, fields){
		//callback
		if (!!error) {
			console.log('Error in query');
		}else{
			resp.json(rows);
		}
	});
})

//-------------------------------------------------------CHANGE PASSWORD-----------------------------------------------------------------------------------
app.get('/updatepassword',function(req, resp){
	var user = req.query.user;
	var pass = req.query.pass;
	var name = req.query.name;

	connection.query('UPDATE `employeedb` SET `PASSWORD`= "'+pass+'" WHERE USER_ID = "'+user+'" AND NAME = "'+name+'"', function (error, rows, fields){
		//callback
		if (!!error) {
			console.log('Error in query');
		}else{
			connection.query('SELECT * FROM employeedb WHERE USER_ID = "'+user+'"', function (error, rows, fields){
		//callback
		if (!!error) {
			console.log('Error in query');
		}else{
			resp.json(rows);
		}
	});
		}
	});
})

//--------------------------------------------------------UPDATE--LATITUDE--LONGITUDE-------------------------------------------------------------------------------
app.get('/updatelocation',function(req, resp){
    var user_id = req.query.user_id;
	var lat = req.query.lat;
	var lng = req.query.lng;
	var result = { 
		success:false,
	};
	connection.query("INSERT INTO `"+user_id+"`(`LATITUDE`, `LONGITUDE`) VALUES ("+lat+","+lng+")", function (error, rows, fields){
		//callback
		if (!!error) {
			console.log('Error in query');
			//resp.send(JSON.stringify(result));

		}else{
			result.success = true;
			//resp.send(JSON.stringify(result));

		}
		resp.send(JSON.stringify(result));

	});
})


//--------------------------------------------------------EXTRACT--LATITUDE--LONGITUDE--------------------------------------------------------------------------------

app.get('/recentlocation',function(req, resp){
	var user_id = req.query.user_id;
	var user_id2 = req.query.user_id;

	connection.query("SELECT `LATITUDE`, `LONGITUDE` FROM `"+user_id+"`ORDER BY ID DESC LIMIT 1", function (error, rows, fields){
		//callback
		if (!!error) {
			console.log('Error in query');
		}else{
			resp.json(rows);
		}
	});
})

//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
app.listen(1337);