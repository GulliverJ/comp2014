
var express = require("express");
var cassandra = require("cassandra-driver");

var client = new cassandra.Client( { contactPoints : [ '128.16.80.125' ], keyspace : 'orangesensors'} );
client.connect(function(err, result) {
    console.log('Connected.');
});

var api = express();

api.get("/api",function(request, response){

	client.execute("SELECT * FROM rawdata;", function(err, result){
		console.log("Executed Query");
		if (!err)
			response.json(result.rows);
		else
			response.json(err);
	}); 


});



api.set('port', process.env.PORT || 1995 );

var server = api.listen(api.get('port') , function(){
	console.log('Listening on port %d', server.address().port)
});


process.on("SIGTERM", function()
{
	client.shutdown(); 
});
